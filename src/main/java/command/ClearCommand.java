package command;

import manager.HumanBeingCollection;
import utility.Printer;

/**
 * Команда для очистки коллекции
 */
public class ClearCommand implements Command {
    private final HumanBeingCollection collection;

    public ClearCommand(HumanBeingCollection collection) {
        this.collection = collection;
    }

    @Override
    public void execute(String[] args, Printer printer) {
        collection.clear();
        printer.println("Коллекция успешно очищена");
    }

    @Override
    public String getDescription() {
        return "очистить коллекцию";
    }

    @Override
    public String getUsage() {
        return "clear";
    }
}
