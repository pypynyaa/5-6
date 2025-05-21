package commands;

import mainClasses.Car;
import mainClasses.HumanBeing;
import managers.CollectionManager;

public class FilterGreaterThanCarCommand extends Command {
    public FilterGreaterThanCarCommand() {
        super("filter_greater_than_car", "вывести элементы, значение поля car которых больше заданного",
                Command.CommandType.WITH_CAR_DATA, false);
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
        return "";
    }

    @Override
    public String execute(String[] args, CollectionManager collectionManager, Car car) {
        StringBuilder result = new StringBuilder();
        if (car == null) {
            result.append("Элементы с пустым значением car: ");
            collectionManager.getHumansCollection().stream().filter(h -> h.getCar() == null).
                    forEach(h -> result.append(h.toString()).append("\n"));
        } else {
            result.append("Элементы с car больше ").append(car.getName()).append(": \n");
            collectionManager.getHumansCollection().stream().filter(h -> h.getCar() != null && h.getCar().getName().compareTo(car.getName()) > 0).
                    forEach(h -> result.append(h.toString()).append("\n"));
        }
        return result.toString();
    }
}
