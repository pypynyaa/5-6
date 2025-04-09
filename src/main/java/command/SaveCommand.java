package command;

import manager.HumanBeingCollection;
import utility.Printer;
import utility.XMLSerializer;

/**
 * Команда для сохранения коллекции в файл
 */
public class SaveCommand implements Command {
    private final HumanBeingCollection collection;
    private final String filename;

    public SaveCommand(HumanBeingCollection collection, String filename) {
        this.collection = collection;
        this.filename = filename;
    }

    @Override
    public void execute(String[] args, Printer printer) {
        try {
            XMLSerializer.saveToFile(collection, filename);
            printer.println("Коллекция успешно сохранена в файл");
        } catch (Exception e) {
            printer.printError("Ошибка при сохранении в файл: " + e.getMessage());
        }
    }

    @Override
    public String getDescription() {
        return "сохранить коллекцию в файл";
    }

    @Override
    public String getUsage() {
        return "save";
    }
}
