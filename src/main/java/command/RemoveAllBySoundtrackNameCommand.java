package command;

import manager.HumanBeingCollection;
import utility.Printer;

/**
 * Команда для удаления всех элементов с заданным названием саундтрека
 */
public class RemoveAllBySoundtrackNameCommand implements Command {
    private final HumanBeingCollection collection;

    public RemoveAllBySoundtrackNameCommand(HumanBeingCollection collection) {
        this.collection = collection;
    }

    @Override
    public void execute(String[] args, Printer printer) {
        if (args.length == 0) {
            printer.printError("Не указано название саундтрека");
            return;
        }

        String soundtrackName = args[0];
        int initialSize = collection.size();
        collection.removeAllBySoundtrackName(soundtrackName);
        int removedCount = initialSize - collection.size();
        printer.println("Удалено элементов: " + removedCount);
    }

    @Override
    public String getDescription() {
        return "удалить из коллекции все элементы, значение поля soundtrackName которого эквивалентно заданному";
    }

    @Override
    public String getUsage() {
        return "remove_all_by_soundtrack_name <soundtrackName>";
    }
} 