package command;

import model.HumanBeing;
import manager.HumanBeingCollection;
import manager.UserManager;
import utility.Printer;
import java.io.IOException;
import java.util.Optional;

/**
 * Команда для обновления элемента коллекции по ID
 */
public class UpdateCommand implements Command {
    private final HumanBeingCollection collection;
    private final UserManager userManager;

    public UpdateCommand(HumanBeingCollection collection, UserManager userManager) {
        this.collection = collection;
        this.userManager = userManager;
    }

    @Override
    public void execute(String[] args, Printer printer) {
        if (args.length == 0) {
            printer.printError("Не указан ID элемента");
            return;
        }

        try {
            int id = Integer.parseInt(args[0]);
            Optional<HumanBeing> existingHuman = collection.getHumanBeingSet().stream()
                    .filter(h -> h.getId() == id)
                    .findFirst();

            if (existingHuman.isPresent()) {
                HumanBeing newHuman = userManager.readHumanBeing(printer);
                if (newHuman != null) {
                    collection.removeFromCollection(existingHuman.get());
                    newHuman.setId(id); // Сохраняем тот же ID
                    collection.addToCollection(newHuman);
                    printer.println("Элемент с ID " + id + " успешно обновлен");
                } else {
                    printer.printError("Не удалось создать новый объект HumanBeing");
                }
            } else {
                printer.printError("Элемент с ID " + id + " не найден");
            }
        } catch (NumberFormatException e) {
            printer.printError("ID должен быть числом");
        } catch (IOException e) {
            printer.printError("Ошибка при чтении данных: " + e.getMessage());
        }
    }

    @Override
    public String getDescription() {
        return "обновить значение элемента коллекции, id которого равен заданному";
    }

    @Override
    public String getUsage() {
        return "update <id>";
    }
} 