package command;

import model.HumanBeing;
import manager.HumanBeingCollection;
import manager.UserManager;
import utility.Printer;
import java.io.IOException;

/**
 * Команда для добавления нового элемента в коллекцию
 */
public class AddCommand implements Command {
    private final HumanBeingCollection collection;
    private final UserManager userManager;

    public AddCommand(HumanBeingCollection collection, UserManager userManager) {
        this.collection = collection;
        this.userManager = userManager;
    }

    @Override
    public void execute(String[] args, Printer printer) {
        try {
            HumanBeing humanBeing = userManager.readHumanBeing(printer);
            if (humanBeing == null) {
                printer.printError("Не удалось создать объект HumanBeing.");
                return;
            }
            collection.addToCollection(humanBeing);
            printer.println("Объект успешно добавлен в коллекцию.");
        } catch (IOException e) {
            printer.printError("Ошибка при чтении данных: " + e.getMessage());
        }
    }

    @Override
    public String getDescription() {
        return "добавить новый элемент в коллекцию";
    }

    @Override
    public String getUsage() {
        return "add";
    }
}
