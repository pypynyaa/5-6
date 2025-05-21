package command;

import utility.Printer;

/**
 * Команда для перезапуска программы
 */
public class RestartCommand implements Command {
    @Override
    public void execute(String[] args, Printer printer) {
        printer.println("Перезапуск программы...");
        System.exit(0);
    }

    @Override
    public String getDescription() {
        return "перезапустить программу";
    }

    @Override
    public String getUsage() {
        return "restart";
    }
} 