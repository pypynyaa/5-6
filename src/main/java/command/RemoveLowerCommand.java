package command;

import manager.HumanBeingCollection;
import manager.UserManager;
import model.HumanBeing;
import utility.Printer;

import java.io.IOException;

/**
 * Команда для удаления элементов, меньших чем заданный
 */
public class RemoveLowerCommand implements Command {
    private final HumanBeingCollection collection;
    private final UserManager userManager;

    public RemoveLowerCommand(HumanBeingCollection collection, UserManager userManager) {
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
            collection.removeLower(humanBeing);
            int removedCount = initialSize - collection.size();
            printer.println("Удалено элементов: " + removedCount);
        } catch (IOException e) {
            printer.printError("Ошибка при чтении данных: " + e.getMessage());
        }
    }

    @Override
    public String getDescription() {
        return "удалить из коллекции все элементы, меньшие, чем заданный";
    }

    @Override
    public String getUsage() {
        return "remove_lower";
    }
} 