package command;

import model.Car;
import model.HumanBeing;
import manager.HumanBeingCollection;
import manager.UserManager;
import utility.Printer;
import java.io.IOException;

/**
 * Команда для вывода элементов, значение поля car которых больше заданного
 */
public class FilterGreaterThanCarCommand implements Command {
    private final HumanBeingCollection collection;
    private final UserManager userManager;

    public FilterGreaterThanCarCommand(HumanBeingCollection collection, UserManager userManager) {
        this.collection = collection;
        this.userManager = userManager;
    }

    @Override
    public void execute(String[] args, Printer printer) {
        try {
            Car car = userManager.readCar(printer);
            if (car == null) {
                printer.println("Элементы с пустым значением car:");
                collection.getHumanBeingSet().stream()
                        .filter(h -> h.getCar() == null)
                        .forEach(h -> printer.println(h.toString()));
            } else {
                printer.println("Элементы с car больше " + car.getName() + ":");
                collection.getHumanBeingSet().stream()
                        .filter(h -> h.getCar() != null && h.getCar().getName().compareTo(car.getName()) > 0)
                        .forEach(h -> printer.println(h.toString()));
            }
        } catch (IOException e) {
            printer.printError("Ошибка при чтении данных: " + e.getMessage());
        }
    }

    @Override
    public String getDescription() {
        return "вывести элементы, значение поля car которых больше заданного";
    }

    @Override
    public String getUsage() {
        return "filter_greater_than_car";
    }
} 