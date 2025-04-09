package command;

import manager.UserManager;
import utility.Printer;

/**
 * Команда для завершения программы
 */
public class ExitCommand implements Command {
    @Override
    public void execute(String[] args, Printer printer) {
        UserManager.stop();
        printer.println("Программа завершена");
    }

    @Override
    public String getDescription() {
        return "завершить программу (без сохранения в файл)";
    }

    @Override
    public String getUsage() {
        return "exit";
    }
}
