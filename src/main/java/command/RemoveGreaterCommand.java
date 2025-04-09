package command;

import model.HumanBeing;
import manager.HumanBeingCollection;
import manager.UserManager;
import utility.Printer;
import java.io.IOException;

/**
 * Команда для удаления элементов, превышающих заданный
 */
public class RemoveGreaterCommand implements Command {
    private final HumanBeingCollection collection;
    private final UserManager userManager;

    public RemoveGreaterCommand(HumanBeingCollection collection, UserManager userManager) {
        this.collection = collection;
        this.userManager = userManager;
    }

    @Override
    public void execute(String[] args, Printer printer) {
        try {
            HumanBeing humanBeing = userManager.readHumanBeing(printer);
            if (humanBeing == null) {
                printer.printError("Не удалось создать объект HumanBeing для сравнения.");
                return;
            }

            int initialSize = collection.size();
            collection.removeGreater(humanBeing);
            int removedCount = initialSize - collection.size();
            printer.println("Удалено элементов: " + removedCount);
        } catch (IOException e) {
            printer.printError("Ошибка при чтении данных: " + e.getMessage());
        }
    }

    @Override
    public String getDescription() {
        return "удалить из коллекции все элементы, превышающие заданный";
    }

    @Override
    public String getUsage() {
        return "remove_greater";
    }
} 