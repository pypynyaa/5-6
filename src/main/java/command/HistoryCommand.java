package command;

import command.CommandManager;
import utility.Printer;

/**
 * Команда для вывода истории команд
 */
public class HistoryCommand implements Command {
    private final CommandManager commandManager;

    public HistoryCommand(CommandManager commandManager) {
        this.commandManager = commandManager;
    }

    @Override
    public void execute(String[] args, Printer printer) {
        printer.println("Последние 5 команд:");
        commandManager.getCommandHistory().forEach(printer::println);
    }

    @Override
    public String getDescription() {
        return "вывести последние 5 команд (без их аргументов)";
    }

    @Override
    public String getUsage() {
        return "history";
    }
} 