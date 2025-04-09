package command;

import utility.Printer;

/**
 * Команда для вывода справки по доступным командам
 */
public class HelpCommand implements Command {
    private final CommandManager commandManager;

    /**
     * Конструктор команды help
     * @param commandManager менеджер команд
     */
    public HelpCommand(CommandManager commandManager) {
        this.commandManager = commandManager;
    }

    @Override
    public void execute(String[] args, Printer printer) {
        commandManager.printHelp();
    }

    @Override
    public String getDescription() {
        return "вывести справку по доступным командам";
    }

    @Override
    public String getUsage() {
        return "help";
    }
}
