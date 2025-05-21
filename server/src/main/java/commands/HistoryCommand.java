package commands;

import mainClasses.Car;
import mainClasses.HumanBeing;
import managers.CollectionManager;
import managers.CommandManager;

public class HistoryCommand extends Command {
    private final CommandManager commandManager;
    public HistoryCommand(CommandManager commandManager) {
        super("history", "вывести последние 5 команд (без их аргументов)", CommandType.WITHOUT_DATA, false);
        this.commandManager = commandManager;
    }

    /**
     * Выполняет команду с заданными аргументами
     *
     * @param args              аргументы команды
     * @param collectionManager менеджер коллекции, над которой выполняется команда
     */
    @Override
    public String execute(String[] args, CollectionManager collectionManager) {
        StringBuilder result = new StringBuilder();
        commandManager.getCommandHistory().forEach(command -> {result.append(command).append("\n");});
        return result.toString();
    }

    @Override
    public String execute(String[] args, CollectionManager collectionManager, HumanBeing humanBeing) {
        return "";
    }

    @Override
    public String execute(String[] args, CollectionManager collectionManager, Car car) {
        return "";
    }
}
