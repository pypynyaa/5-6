package commands;

import mainClasses.Car;
import mainClasses.HumanBeing;
import managers.CollectionManager;

public class PrintDescendingCommand extends Command {

    public PrintDescendingCommand() {
        super("print_descending", "вывести элементы коллекции в порядке убывания", CommandType.WITHOUT_DATA, false);
    }

    /**
     * Выполняет команду с заданными аргументами
     *
     * @param args              аргументы команды
     * @param collectionManager менеджер коллекции, над которой выполняется команда
     */
    @Override
    public String execute(String[] args, CollectionManager collectionManager) {
        StringBuilder result = null;
        if (collectionManager.getHumansCollection().isEmpty()) {
            result.append("Коллекция пуста!");
        }

        collectionManager.getHumansCollection().stream().sorted((h1, h2) -> h2.compareTo(h1)).forEach(humanBeing -> {result.append(humanBeing.toString()).append("\n");});
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
