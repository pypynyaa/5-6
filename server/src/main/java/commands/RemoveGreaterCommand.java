package commands;

import mainClasses.Car;
import mainClasses.HumanBeing;
import managers.CollectionManager;

public class RemoveGreaterCommand extends Command {

    public RemoveGreaterCommand() {
        super("remove_greater", "удалить из коллекции все элементы, превышающие заданный",
                CommandType.WITH_HUMAN_DATA, false);
    }

    /**
     * Выполняет команду с заданными аргументами
     *
     * @param args              аргументы команды
     * @param collectionManager менеджер коллекции, над которой выполняется команда
     */
    @Override
    public String execute(String[] args, CollectionManager collectionManager) {
        return "";
    }

    @Override
    public String execute(String[] args, CollectionManager collectionManager, HumanBeing humanBeing) {
        if (args.length > 0) {
            return "Данная команда не принимает аргументы!";
        } else {
            collectionManager.getHumansCollection().removeIf(h -> h.getId() > humanBeing.getId());
            return "Элементы удалены";
        }
    }

    @Override
    public String execute(String[] args, CollectionManager collectionManager, Car car) {
        return "";
    }
}
