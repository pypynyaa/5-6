package command;

import manager.HumanBeingCollection;
import utility.Printer;

import java.text.SimpleDateFormat;

/**
 * Команда для вывода информации о коллекции
 */
public class InfoCommand implements Command {
    private final HumanBeingCollection collection;

    public InfoCommand(HumanBeingCollection collection) {
        this.collection = collection;
    }

    @Override
    public void execute(String[] args, Printer printer) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
        printer.println("Тип коллекции: " + collection.getHumanBeingSet().getClass().getName());
        printer.println("Дата инициализации: " + dateFormat.format(collection.getInitializationDate()));
        printer.println("Количество элементов: " + collection.size());
    }

    @Override
    public String getDescription() {
        return "вывести в стандартный поток вывода информацию о коллекции (тип, дата инициализации, количество элементов и т.д.)";
    }

    @Override
    public String getUsage() {
        return "info";
    }
}
