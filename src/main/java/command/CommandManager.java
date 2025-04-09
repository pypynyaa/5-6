package command;

import utility.Printer;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

/**
 * Класс для управления командами
 */
public class CommandManager {
    private final Map<String, Command> commands = new HashMap<>();
    private final LinkedList<String> commandHistory = new LinkedList<>();
    private static final int HISTORY_SIZE = 5;
    private final Printer printer;

    /**
     * Конструктор класса CommandManager
     * @param printer объект для вывода сообщений
     */
    public CommandManager(Printer printer) {
        this.printer = printer;
    }

    /**
     * Регистрирует новую команду
     * @param name имя команды
     * @param command объект команды
     */
    public void registerCommand(String name, Command command) {
        commands.put(name, command);
    }

    /**
     * Выполняет команду
     * @param name имя команды
     * @param args аргументы команды
     * @param printer объект для вывода сообщений
     */
    public void executeCommand(String name, String args, Printer printer) {
        Command command = commands.get(name);
        if (command == null) {
            printer.printError("Неизвестная команда: " + name);
            return;
        }
        String[] argsArray = args != null ? args.split("\\s+") : new String[0];
        command.execute(argsArray, printer);
        addToHistory(name);
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

    /**
     * Выводит список всех доступных команд
     */
    public void printHelp() {
        printer.println("Доступные команды:");
        commands.forEach((name, command) -> {
            printer.println(name + " - " + command.getDescription());
            printer.println("Использование: " + command.getUsage());
        });
    }
} 