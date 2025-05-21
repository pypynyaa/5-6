package managers;

import commands.Command;
import mainClasses.Car;
import mainClasses.HumanBeing;
import shit.Response;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

/**
 * Менеджер команд
 * Управляет регистрацией и выполнением команд
 */
public class CommandManager {
    private final Map<String, Command> commands = new HashMap<>();
    private final LinkedList<String> commandHistory = new LinkedList<>();
    private static final int HISTORY_SIZE = 5;

    /**
     * Регистрирует новую команду
     * @param command команда для регистрации
     */
    public void registerCommand(Command command) {
        commands.put(command.getName(), command);
    }

    /**
     * Выполняет команду по её имени
     * @param commandName имя команды
     * @param args аргументы команды
     * @param collectionManager менеджер коллекции
     */
    public Response executeCommand(String commandName, String[] args, CollectionManager collectionManager) {
        Command command = commands.get(commandName);
        if (command != null) {
            String message = command.execute(args, collectionManager);
            addToHistory(commandName);
            return new Response(Response.ResponseType.INFO, true, message);
        } else {
            return new Response(Response.ResponseType.INFO, false, "Неизвестная команда");
        }
    }

    public Response executeCommand(String commandName, String[] args, CollectionManager collectionManager, HumanBeing humanBeing) {
        Command command = commands.get(commandName);
        if (command != null) {
            String message = command.execute(args, collectionManager, humanBeing);
            addToHistory(commandName);
            return new Response(true, message);
        } else {
            return new Response(false, "Неизвестная команда");
        }
    }

    public Response executeCommand(String commandName, String[] args, CollectionManager collectionManager, Car car) {
        Command command = commands.get(commandName);
        if (command != null) {
            String message = command.execute(args, collectionManager, car);
            addToHistory(commandName);
            return new Response(true, message);
        } else {
            return new Response(false, "Неизвестная команда");
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

    /**
     * Получает все зарегистрированные команды
     * @return карта команд, где ключ - имя команды
     */
    public Map<String, Command> getCommands() {
        return commands;
    }
}


