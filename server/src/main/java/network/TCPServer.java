
package network;

import commands.Command;
import exceptions.ScriptRecursionException;
import managers.CollectionManager;
import managers.CommandManager;
import managers.DatabaseManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import shit.Request;
import shit.Request.RequestType;
import shit.Response;
import shit.User;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.util.*;
import java.util.concurrent.*;


public class TCPServer {

    private static final Logger logger = LogManager.getLogger(TCPServer.class);

    private ServerSocket serverSocket;

    private volatile boolean isRunning = true;

    private final CollectionManager collectionManager;

    private final CommandManager commandManager;

    private final Scanner scanner = new Scanner(System.in);

    private final List<Socket> activeSockets = new CopyOnWriteArrayList<>();

    private int clientCount = 0;

    private final Set<String> activeScripts = Collections.synchronizedSet(new HashSet<>());

    private final ExecutorService readPool = Executors.newCachedThreadPool();
    private final ExecutorService processPool = Executors.newCachedThreadPool();
    private final ExecutorService sendPool = Executors.newFixedThreadPool(10);

    private final ConcurrentHashMap<Socket, Request> pendingDataRequests = new ConcurrentHashMap<>();

    public TCPServer(CollectionManager collectionManager, CommandManager commandManager) {
        this.collectionManager = collectionManager;
        this.commandManager = commandManager;
    }


    public void start(int port) throws IOException {
        serverSocket = new ServerSocket(port);
        logger.info("Сервер запущен на порту {}", port);


        new Thread(this::adminInput, "AdminInputThread").start();

        while (isRunning) {
            try {
                Socket clientSocket = serverSocket.accept();
                activeSockets.add(clientSocket);
                logger.info("Подключение от клиента: {} (текущее количество клиентов: {})",
                        clientSocket.getInetAddress().getHostAddress(), ++clientCount);

                readPool.submit(() -> handleClient(clientSocket));
            } catch (IOException e) {
                if (!isRunning) {
                    logger.info("Сервер остановлен!");
                    break;
                }
                logger.error("Ошибка при приеме нового соединения: {}", e.getMessage(), e);
            }
        }
    }


    private void handleClient(Socket clientSocket) {
        ObjectInputStream in = null;
        ObjectOutputStream out = null;
        try {
            out = new ObjectOutputStream(clientSocket.getOutputStream());
            in = new ObjectInputStream(clientSocket.getInputStream());

            while (!clientSocket.isClosed()) {
                Request request = (Request) in.readObject();
                logger.info("Получен запрос от {}: {}", clientSocket.getInetAddress().getHostAddress(), request.toString());


                CompletableFuture<Response> processingFuture = CompletableFuture.supplyAsync(() ->
                        processRequest(request, clientSocket), processPool);


                ObjectOutputStream finalOut = out;
                processingFuture.thenAcceptAsync(response -> {
                    try {
                        sendResponse(response, finalOut);
                    } catch (Exception e) {
                        logger.error("Ошибка при отправке ответа клиенту {}: {}", clientSocket.getInetAddress().getHostAddress(), e.getMessage(), e);
                    }
                }, sendPool);
            }
        } catch (EOFException e) {
            logger.warn("Клиент {} закрыл соединение некорректно (EOFException).", clientSocket.getInetAddress().getHostAddress());
        } catch (StreamCorruptedException e) {
            logger.error("Ошибка при чтении потока от {}: {}", clientSocket.getInetAddress().getHostAddress(), e.getMessage(), e);
        } catch (IOException e) {
            logger.warn("Соединение с клиентом {} разорвано: {}", clientSocket.getInetAddress().getHostAddress(), e.getMessage());
        } catch (ClassNotFoundException e) {
            logger.error("Ошибка десериализации объекта запроса от {}: {}", clientSocket.getInetAddress().getHostAddress(), e.getMessage());
        } catch (Exception e) {
            logger.error("Неожиданная ошибка в handleClient для клиента {}: {}", clientSocket.getInetAddress().getHostAddress(), e.getMessage(), e);
        } finally {
            pendingDataRequests.remove(clientSocket);
            try {
                if (out != null) out.close();
                if (in != null) in.close();
            } catch (IOException e) {
                logger.error("Ошибка при закрытии потоков клиента: {}", e.getMessage(), e);
            }
            closeSocket(clientSocket);
            activeSockets.remove(clientSocket);
            logger.info("Клиент отключен. Текущее количество клиентов: {}", --clientCount);
        }
    }


    private Response processRequest(Request request, Socket clientSocket) {
        try {
            Response responseToSend;


            if (request.getType() != RequestType.REGISTER_NEW_USER &&
                    request.getType() != RequestType.AUTHORIZE_USER) {


                if (request.getUserName() == null || request.getPassword() == null ||
                        !DatabaseManager.verifyUser(request.getUserName(), request.getPassword())) {
                    return new Response(Response.ResponseType.ERROR, false, "Необходима авторизация. Используйте register или login.");
                }
            }


            if (request.getType() == RequestType.REGISTER_NEW_USER) {
                logger.info("Попытка регистрации нового пользователя: {}", request.getUserName());
                User newUser = new User();
                newUser.setUserName(request.getUserName());
                newUser.setPassword(request.getPassword());

                if (DatabaseManager.saveUser(newUser)) {
                    responseToSend = new Response(Response.ResponseType.USER_AUTHORIZATION, true, "Новый аккаунт '" + request.getUserName() + "' успешно создан!");
                } else {
                    responseToSend = new Response(Response.ResponseType.USER_AUTHORIZATION, false, "Пользователь с именем '" + request.getUserName() + "' уже существует!");
                }
            } else if (request.getType() == RequestType.AUTHORIZE_USER) {
                logger.info("Попытка авторизации пользователя: {}", request.getUserName());
                try {
                    if (DatabaseManager.verifyUser(request.getUserName(), request.getPassword())) {
                        responseToSend = new Response(Response.ResponseType.USER_AUTHORIZATION, true, "Пользователь '" + request.getUserName() + "' успешно авторизован!");
                    } else {
                        responseToSend = new Response(Response.ResponseType.USER_AUTHORIZATION, false, "Неправильный логин или пароль!");
                    }
                } catch (SQLException e) {
                    logger.error("Ошибка БД при верификации пользователя '{}': {}", request.getUserName(), e.getMessage(), e);
                    responseToSend = new Response(Response.ResponseType.ERROR, false, "Ошибка базы данных при проверке пользователя: " + e.getMessage());
                }
            } else if (request.getType() == RequestType.HUMAN_DATA || request.getType() == RequestType.CAR_DATA) {
                if (pendingDataRequests.containsKey(clientSocket)) {
                    Request originalRequest = pendingDataRequests.remove(clientSocket);

                    Request finalRequest;
                    if (request.getType() == RequestType.HUMAN_DATA) {
                        finalRequest = new Request(
                                RequestType.COMMAND,
                                originalRequest.getCommandName(),
                                originalRequest.getArgs(),
                                originalRequest.getScriptContent(),
                                request.getHumanBeing(),
                                originalRequest.getCar(),
                                originalRequest.getUserName(),
                                originalRequest.getPassword()
                        );
                    } else {
                        finalRequest = new Request(
                                RequestType.COMMAND,
                                originalRequest.getCommandName(),
                                originalRequest.getArgs(),
                                originalRequest.getScriptContent(),
                                originalRequest.getHumanBeing(),
                                request.getCar(),
                                originalRequest.getUserName(),
                                originalRequest.getPassword()
                        );
                    }
                    responseToSend = commandManager.executeCommand(finalRequest, collectionManager);
                } else {
                    logger.warn("Получены неожиданные данные от клиента {}: {}", clientSocket.getInetAddress().getHostAddress(), request.toString());
                    responseToSend = new Response(Response.ResponseType.ERROR, false, "Получены неожиданные данные. Пожалуйста, сначала введите команду.");
                }
            } else if (request.getType() == RequestType.SCRIPT_TRANSFER) {
                responseToSend = processScriptRequest(request);
            } else {
                String commandName = request.getCommandName();
                Command command = commandManager.getCommands().get(commandName);

                if (command == null) {
                    responseToSend = new Response(Response.ResponseType.ERROR, false, "Неизвестная команда: " + commandName);
                } else if (command.isWithHumanData() && request.getHumanBeing() == null) {
                    pendingDataRequests.put(clientSocket, request);
                    responseToSend = new Response(Response.ResponseType.NEED_HUMAN_DATA, false, "Для команды '" + commandName + "' требуются данные о человеке.");
                } else if (command.isWithCarData() && request.getCar() == null) {
                    pendingDataRequests.put(clientSocket, request);
                    responseToSend = new Response(Response.ResponseType.NEED_CAR_DATA, false, "Для команды '" + commandName + "' требуются данные о машине.");
                } else {
                    responseToSend = commandManager.executeCommand(request, collectionManager);
                }
            }

            logger.info("Сформирован ответ клиенту {}: {}", clientSocket.getInetAddress().getHostAddress(), responseToSend.toString());
            return responseToSend;

        } catch (Exception e) {
            logger.error("Ошибка при обработке запроса {}: {}", request.toString(), e.getMessage(), e);
            return new Response(Response.ResponseType.ERROR, false, "Ошибка сервера при обработке запроса: " + e.getMessage());
        }
    }


    private Response processScriptRequest(Request request) {
        String scriptPath = request.getArgs() != null && request.getArgs().length > 0 ? request.getArgs()[0] : "unknown_script";
        String scriptContent = request.getScriptContent();
        String username = request.getUserName();
        String password = request.getPassword();
        StringBuilder result = new StringBuilder();

        try {
            if (activeScripts.contains(scriptPath)) {
                throw new ScriptRecursionException("Рекурсия! Скрипт " + scriptPath + " уже выполняется");
            }
            activeScripts.add(scriptPath);
            logger.info("Начало выполнения скрипта: {}", scriptPath);

            result.append("=== Начало выполнения скрипта ").append(scriptPath).append(" ===\n");
            result.append(processScriptContent(scriptContent, scriptPath, username, password));
            result.append("=== Завершение скрипта ").append(scriptPath).append(" ===\n");

        } catch (ScriptRecursionException e) {
            logger.error("Ошибка при выполнении скрипта: {}", e.getMessage());
            result.append("ОШИБКА: ").append(e.getMessage()).append("\n");
        } catch (Exception e) {
            logger.error("Непредвиденная ошибка при обработке скрипта {}: {}", scriptPath, e.getMessage());
            result.append("ОШИБКА: Непредвиденная ошибка при обработке скрипта: ").append(e.getMessage()).append("\n");
        } finally {
            activeScripts.remove(scriptPath);
        }
        return new Response(Response.ResponseType.INFO, true, result.toString());
    }


    private String processScriptContent(String content, String currentScriptPath, String username, String password) {
        StringBuilder output = new StringBuilder();
        List<String> lines = Arrays.asList(content.split("\n"));

        for (String line : lines) {
            line = line.trim();
            if (line.isEmpty() || line.startsWith("#")) continue;

            try {
                String[] parts = line.split("\\s+", 2);
                String commandName = parts[0];
                String[] args = parts.length > 1 ? parts[1].split(" ") : new String[0];

                if (commandName.equalsIgnoreCase("execute_script")) {
                    String nestedScriptPath = args[0];
                    output.append(handleNestedScript(nestedScriptPath, currentScriptPath, username, password));
                } else {
                    Request scriptCommandRequest = new Request(
                            RequestType.COMMAND,
                            commandName,
                            args,
                            null, null, null,
                            username,
                            password
                    );


                    Response response = commandManager.executeCommand(scriptCommandRequest, collectionManager);
                    output.append(response.getMessage()).append("\n");
                }
            } catch (Exception e) {
                output.append("ОШИБКА: ").append(e.getMessage()).append("\n");
                logger.error("Ошибка при обработке команды в скрипте {}: {} - {}", currentScriptPath, line, e.getMessage(), e);
            }
        }
        return output.toString();
    }


    private String handleNestedScript(String scriptPath, String parentScript, String username, String password) throws Exception {
        if (activeScripts.contains(scriptPath)) {
            throw new ScriptRecursionException("Рекурсивный вызов из " + parentScript + " в " + scriptPath);
        }

        activeScripts.add(scriptPath);
        StringBuilder output = new StringBuilder();

        try {
            Path path = Paths.get(scriptPath).normalize();
            if (!Files.exists(path) || !Files.isRegularFile(path)) {
                throw new FileNotFoundException("Файл скрипта не найден или не является обычным файлом: " + scriptPath);
            }
            String content = new String(Files.readAllBytes(path));
            output.append("=== Начало вложенного скрипта ").append(scriptPath).append(" ===\n");
            output.append(processScriptContent(content, scriptPath, username, password));
            output.append("=== Конец вложенного скрипта ").append(scriptPath).append(" ===\n");
        } finally {
            activeScripts.remove(scriptPath);
        }
        return output.toString();
    }


    public void sendResponse(Response response, ObjectOutputStream out) throws IOException {
        out.writeObject(response);
        out.flush();
    }


    public void adminInput() {
        logger.info("Доступен интерактивный режим админа");
        while (isRunning) {
            System.out.print("> ");
            try {
                String input = scanner.nextLine().trim();
                if (input.isEmpty()) {
                    continue;
                }

                String[] parts = input.split(" ", 2);
                String commandName = parts[0];
                String[] commandArgs = parts.length > 1 ? parts[1].split(" ") : new String[0];

                Request adminRequest = new Request(
                        RequestType.COMMAND,
                        commandName,
                        commandArgs,
                        null, null, null,
                        "admin",
                        "admin_pass"
                );

                if (commandName.equalsIgnoreCase("exit")) {
                    logger.info("Получена команда на завершение работы сервера");
                    disconnect();
                    break;
                } else {
                    logger.info("Выполнение команды администратора: {}", commandName);


                    try {
                        Response adminResponse = commandManager.executeCommand(adminRequest, collectionManager);
                        System.out.println(adminResponse.getMessage());
                    } catch (Exception e) {
                        System.err.println("Ошибка при выполнении админ-команды: " + e.getMessage());
                        logger.error("Ошибка при выполнении админ-команды '{}': {}", commandName, e.getMessage(), e);
                    }
                }
            } catch (NoSuchElementException e) {
                logger.info("Админский ввод завершен.");
                break;
            } catch (IllegalStateException e) {
                logger.warn("Сканер для админ-ввода уже закрыт.");
                break;
            }
        }
    }


    public synchronized void disconnect() {
        logger.info("Начало отключения сервера");
        if (!isRunning) return;
        isRunning = false;
        System.out.println("Завершение работы сервера...");


        activeSockets.forEach(this::closeSocket);


        shutdownAndAwaitTermination(readPool, "readPool");
        shutdownAndAwaitTermination(processPool, "processPool");
        shutdownAndAwaitTermination(sendPool, "sendPool");

        try {
            if (serverSocket != null && !serverSocket.isClosed()) {
                serverSocket.close();
            }
            DatabaseManager.closeConnection();
            logger.info("Сервер успешно остановлен");
        } catch (IOException e) {
            logger.error("Ошибка при остановке сервера: {}", e.getMessage(), e);
        }

        System.out.println("Сервер остановлен корректно. Всем пока!");
        System.exit(0);
    }


    private void shutdownAndAwaitTermination(ExecutorService pool, String poolName) {
        pool.shutdown();
        try {

            if (!pool.awaitTermination(60, TimeUnit.SECONDS)) {
                pool.shutdownNow();
                if (!pool.awaitTermination(60, TimeUnit.SECONDS))
                    logger.error("Пул {} не завершился.", poolName);
            }
        } catch (InterruptedException ie) {
            pool.shutdownNow();
            Thread.currentThread().interrupt();
        }
    }


    private void closeSocket(Socket socket) {
        try {
            if (socket != null && !socket.isClosed()) {
                socket.close();
                logger.info("Соединение с клиентом {} закрыто!", socket.getInetAddress().getHostAddress());
            }
        } catch (IOException e) {
            logger.error("Ошибка закрытия сокета {}: {}", socket.getInetAddress().getHostAddress(), e.getMessage(), e);
        }
    }
}