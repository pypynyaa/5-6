package command;

import manager.HumanBeingCollection;
import model.HumanBeing;
import utility.Printer;

import java.util.Optional;

/**
 * Команда для удаления элемента из коллекции по его ID
 */
public class RemoveByIdCommand implements Command {
    private final HumanBeingCollection collection;

    public RemoveByIdCommand(HumanBeingCollection collection) {
        this.collection = collection;
    }

    @Override
    public void execute(String[] args, Printer printer) {
        if (collection.size() == 0) {
            printer.printError("Коллекция пуста");
            return;
        }

        if (args.length == 0) {
            printer.printError("Не указан ID элемента");
            return;
        }

        try {
            int id = Integer.parseInt(args[0]);
            Optional<HumanBeing> humanBeing = collection.getHumanBeingSet().stream()
                    .filter(h -> h.getId() == id)
                    .findFirst();

            if (humanBeing.isPresent()) {
                collection.removeFromCollection(humanBeing.get());
                printer.println("Элемент с ID " + id + " успешно удален");
            } else {
                printer.printError("Элемент с ID " + id + " не найден");
            }
        } catch (NumberFormatException e) {
            printer.printError("ID должен быть числом");
        }
    }

    @Override
    public String getDescription() {
        return "удалить элемент из коллекции по его ID";
    }

    @Override
    public String getUsage() {
        return "remove_by_id <id>";
    }
}
