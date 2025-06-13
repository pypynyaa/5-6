package userManagers;

import mainClasses.Car;
import mainClasses.HumanBeing;
import network.TCPClient;
import shit.Request;
import shit.Response;
import shit.Request.RequestType; 


import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;


public class UserInputScanner {
    
    private final TCPClient client;
    
    private final Scanner scanner;
    
    private final HumanInputHelper helper;
    
    private static final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");

    private String username;
    private String password;

    
    public UserInputScanner(TCPClient client) {
        this.client = client;
        this.scanner = new Scanner(System.in).useDelimiter("\n");
        this.helper = new HumanInputHelper(this.scanner); 
    }

    
    public void setUsername(String username) {
        this.username = username;
    }

    
    public void setPassword(String password) {
        this.password = password;
    }

    
    public void startInteractiveMode() {
        System.out.println("Клиент запущен в интерактивном режиме!");

        while (true) {
            try {
                System.out.print("> ");
                
                if (!scanner.hasNextLine()) {
                    System.out.println("Нет данных для чтения. Завершение работы.");
                    break;
                }

                String input = scanner.nextLine().trim(); 
                if (input.isEmpty()) continue;

                if ("exit".equalsIgnoreCase(input)) {
                    System.out.println("Завершение работы клиента");
                    break;
                } else if ("save".equalsIgnoreCase(input)) {
                    System.out.println("Ошибка: Команда 'save' должна быть отправлена на сервер, а не выполнена локально.");
                    continue;
                }

                
                Request request = createRequest(input);

                Response response = client.sendRequest(request);

                if (response.getType() == Response.ResponseType.NEED_HUMAN_DATA) {
                    System.out.println("Сервер запрашивает данные о человеке!");
                    HumanBeing newHumanBeing = helper.inputHuman();
                    Request newRequest = new Request(
                            RequestType.HUMAN_DATA,
                            request.getCommandName(), 
                            request.getArgs(),        
                            request.getScriptContent(), 
                            newHumanBeing,
                            request.getCar(),
                            this.username,
                            this.password
                    );
                    Response newResponse = client.sendRequest(newRequest);
                    System.out.println("\n" + newResponse.getMessage());
                } else if (response.getType() == Response.ResponseType.NEED_CAR_DATA) {
                    System.out.println("Сервер запрашивает данные о машине!");
                    Car car = helper.inputCar();
                    Request newRequest = new Request(
                            RequestType.CAR_DATA,
                            request.getCommandName(),
                            request.getArgs(),
                            request.getScriptContent(),
                            request.getHumanBeing(),
                            car,
                            this.username,
                            this.password
                    );
                    Response newResponse = client.sendRequest(newRequest);
                    System.out.println("\n" + newResponse.getMessage());
                } else if (response.getType() == Response.ResponseType.ERROR) {
                    System.out.println("Ошибка: " + response.getMessage());
                } else { 
                    System.out.println("\n" + response.getMessage());
                }

            } catch (IOException e) {
                System.out.println("Ошибка ввода/вывода или сети: " + e.getMessage());
                
                
                try {
                    System.out.println("Пытаемся восстановить подключение...");
                    client.disconnect();
                    client.connect("localhost", 5556);
                    System.out.println("Подключение успешно восстановлено.");
                } catch (IOException ex) {
                    System.out.println("Не удалось восстановить соединение: " + ex.getMessage());
                    break;
                }
            } catch (ClassNotFoundException e) {
                System.out.println("Ошибка десериализации ответа сервера: " + e.getMessage());
            } catch (Exception e) {
                System.out.println("Произошла непредвиденная ошибка: " + e.getMessage());
                
            }
        }
        scanner.close();
    }

    
    private Request createRequest(String input) throws IOException {
        String[] parts = input.split(" ", 2);
        String commandName = parts[0];
        String[] args = parts.length > 1 ? parts[1].split(" ") : new String[0];

        if (commandName.equalsIgnoreCase("execute_script")) {
            String scriptPath = args[0];
            String scriptContent = readScriptContent(scriptPath);
            return new Request(
                    RequestType.SCRIPT_TRANSFER,
                    commandName,
                    args,
                    scriptContent,
                    null, null,
                    this.username,
                    this.password
            );
        }
        
        return new Request(
                RequestType.COMMAND,
                commandName,
                args,
                null, null, null,
                this.username,
                this.password
        );
    }

    
    private String readScriptContent(String scriptPath) throws IOException {
        return new String(Files.readAllBytes(Paths.get(scriptPath)));
    }

    

    
    public boolean loginUser() {
        System.out.print("Введите логин: ");
        String login = scanner.nextLine().trim();
        System.out.print("Введите пароль: ");
        String pass = scanner.nextLine().trim();

        if (login.isEmpty() || pass.isEmpty()) {
            System.out.println("Логин и пароль не могут быть пустыми.");
            return false;
        }

        try {
            Request loginRequest = new Request(login, pass, RequestType.AUTHORIZE_USER); 
            Response response = client.sendRequest(loginRequest);

            if (response.isSuccess()) {
                System.out.println("Вход успешно выполнен: " + response.getMessage());
                this.setUsername(login); 
                this.setPassword(pass);
                return true;
            } else {
                System.out.println("Ошибка входа: " + response.getMessage());
                return false;
            }
        } catch (IOException e) {
            System.out.println("Ошибка сети при попытке входа: " + e.getMessage());
            return false;
        } catch (ClassNotFoundException e) {
            System.out.println("Ошибка десериализации ответа сервера: " + e.getMessage());
            return false;
        } catch (Exception e) {
            System.out.println("Непредвиденная ошибка при входе: " + e.getMessage());
            e.printStackTrace(); 
            return false;
        }
    }

    
    public boolean registerUser() {
        System.out.print("Введите логин для регистрации: ");
        String login = scanner.nextLine().trim();
        if (login.isEmpty()) {
            System.out.println("Логин не может быть пустым.");
            return false;
        }

        String pass;
        while (true) {
            System.out.print("Введите пароль: ");
            String passwordInput1 = scanner.nextLine().trim();
            if (passwordInput1.isEmpty()) {
                System.out.println("Пароль не может быть пустым. Пожалуйста, введите пароль.");
                continue;
            }
            System.out.print("Подтвердите пароль: ");
            String passwordInput2 = scanner.nextLine().trim();

            if (passwordInput1.equals(passwordInput2)) {
                pass = passwordInput1;
                break;
            } else {
                System.out.println("Пароли не совпадают. Повторите ввод!");
            }
        }

        try {
            Request registerRequest = new Request(login, pass, RequestType.REGISTER_NEW_USER); 
            Response response = client.sendRequest(registerRequest);

            if (response.isSuccess()) {
                System.out.println("Регистрация успешно выполнена: " + response.getMessage());
                
                
                
                return false; 
            } else {
                System.out.println("Ошибка регистрации: " + response.getMessage());
                return false;
            }
        } catch (IOException e) {
            System.out.println("Ошибка сети при попытке регистрации: " + e.getMessage());
            return false;
        } catch (ClassNotFoundException e) {
            System.out.println("Ошибка десериализации ответа сервера: " + e.getMessage());
            return false;
        } catch (Exception e) {
            System.out.println("Непредвиденная ошибка при регистрации: " + e.getMessage());
            e.printStackTrace(); 
            return false;
        }
    }
}