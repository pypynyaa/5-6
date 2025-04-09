package command;

import manager.HumanBeingCollection;
import utility.Printer;

import java.util.stream.Collectors;

/**
 * Команда для вывода элементов в порядке убывания
 */
public class PrintDescendingCommand implements Command {
    private final HumanBeingCollection collection;

    public PrintDescendingCommand(HumanBeingCollection collection) {
        this.collection = collection;
    }

    @Override
    public void execute(String[] args, Printer printer) {
        if (collection.size() == 0) {
            printer.println("Коллекция пуста");
            return;
        }

        collection.getHumanBeingSet().stream()
                .sorted((h1, h2) -> h2.compareTo(h1))
                .forEach(h -> printer.println(h.toString()));
    }

    @Override
    public String getDescription() {
        return "вывести элементы коллекции в порядке убывания";
    }

    @Override
    public String getUsage() {
        return "print_descending";
    }
} 