package commands;

import mainClasses.Car;
import mainClasses.HumanBeing;
import managers.CollectionManager;

public class RemoveAllBySoundtrackNameCommand extends Command {

    public RemoveAllBySoundtrackNameCommand() {
        super("remove_all_by_soundtrack_name", "удалить из коллекции все элементы, значение поля soundtrackName которого эквивалентно заданному",
                CommandType.WITHOUT_DATA, true);
    }

    /**
     * Выполняет команду с заданными аргументами
     *
     * @param args              аргументы команды
     * @param collectionManager менеджер коллекции, над которой выполняется команда
     */
    @Override
    public String execute(String[] args, CollectionManager collectionManager) {
        try {
            if (args.length != 1) throw new IllegalArgumentException();
            String name = String.valueOf(args[0]);
            collectionManager.removeElementBySoundtrackName(name);
            return "Элемент успешно удален";
        } catch (IllegalArgumentException e) {
            return "Использование: remove_all_by_soundtrack_name {soundtrackName}";
        }
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
