package managers;

import commands.Command;
import commands.LoginCommand;
import commands.RegisterCommand;
import commands.AddCommand;
import commands.ShowCommand;
import commands.InfoCommand;
import commands.RemoveByIdCommand;
import commands.ClearCommand;
import commands.ExecuteScriptCommand;
import commands.HistoryCommand;
import commands.HelpCommand;


import shit.Request;
import shit.Response;
import shit.Request.RequestType;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.sql.SQLException;


public class CommandManager {
    private final Map<String, Command> commands = new HashMap<>();
    private final LinkedList<String> commandHistory = new LinkedList<>();
    private static final int HISTORY_SIZE = 5;

    private final LoginCommand loginCommand;
    private final RegisterCommand registerCommand;

    public CommandManager() {

        this.loginCommand = new LoginCommand();
        this.registerCommand = new RegisterCommand();

        registerCommand(new AddCommand());
        registerCommand(new InfoCommand());
        registerCommand(new ShowCommand());
        
        registerCommand(new RemoveByIdCommand());
        registerCommand(new ClearCommand());
        registerCommand(new ExecuteScriptCommand(this));
        registerCommand(new HistoryCommand(this));
        registerCommand(new HelpCommand(this));
    }

    
    public void registerCommand(Command command) {
        commands.put(command.getName(), command);
    }

    
    public Response executeCommand(Request request, CollectionManager collectionManager) {
        if (request.getType() == RequestType.AUTHORIZE_USER) {
            String loginMessage = loginCommand.execute(request, collectionManager);
            return new Response(Response.ResponseType.USER_AUTHORIZATION, loginMessage.contains("successfully logged in"), loginMessage);
        }
        if (request.getType() == RequestType.REGISTER_NEW_USER) {
            String registerMessage = registerCommand.execute(request, collectionManager);
            return new Response(Response.ResponseType.INFO, registerMessage.contains("successfully registered"), registerMessage);
        }

        String commandName = request.getCommandName();

        if (commandName == null || commandName.isEmpty()) {
            return new Response(Response.ResponseType.ERROR, false, "Ошибка: Имя команды не указано для запроса типа " + request.getType());
        }

        Command command = commands.get(commandName);

        if (command == null) {
            return new Response(Response.ResponseType.ERROR, false, "Неизвестная команда: " + commandName);
        }

        if (!commandName.equals("help") && !commandName.equals("info") && !commandName.equals("history")) { 
            String username = request.getUserName();
            String password = request.getPassword();

            if (username == null || password == null || username.isEmpty() || password.isEmpty()) {
                return new Response(Response.ResponseType.ERROR, false, "Команда '" + commandName + "' требует аутентификации. Пожалуйста, войдите или зарегистрируйтесь.");
            }

            try {
                if (!DatabaseManager.verifyUser(username, password)) {
                    return new Response(Response.ResponseType.ERROR, false, "Неверное имя пользователя или пароль для команды '" + commandName + "'.");
                }
            } catch (SQLException e) {
                System.err.println("Ошибка базы данных при проверке пользователя для команды " + commandName + ": " + e.getMessage());
                return new Response(Response.ResponseType.ERROR, false, "Ошибка сервера при проверке пользователя.");
            }
        }

        try {
            
            String message = command.execute(request, collectionManager);
            if (!commandName.equals("execute_script") && !commandName.equals("help") && !commandName.equals("info")) {
                addToHistory(commandName);
            }
            return new Response(Response.ResponseType.INFO, true, message);
        } catch (Exception e) {
            System.err.println("Ошибка при выполнении команды " + commandName + ": " + e.getMessage());
            e.printStackTrace();
            return new Response(Response.ResponseType.ERROR, false, "Ошибка при выполнении команды: " + e.getMessage());
        }
    }

    
    private void addToHistory(String command) {
        commandHistory.addFirst(command);
        if (commandHistory.size() > HISTORY_SIZE) {
            commandHistory.removeLast();
        }
    }

    
    public LinkedList<String> getCommandHistory() {
        return commandHistory;
    }

    
    public Map<String, Command> getCommands() {
        return commands;
    }
}