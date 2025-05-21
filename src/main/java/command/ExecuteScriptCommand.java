package command;

import manager.HumanBeingCollection;
import manager.UserManager;
import utility.Printer;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

/**
 * Команда для выполнения скрипта из файла
 */
public class ExecuteScriptCommand implements Command {
    private final HumanBeingCollection collection;
    private final UserManager userManager;
    private final CommandManager commandManager;

    /**
     * Конструктор команды execute_script
     * @param collection коллекция HumanBeing
     * @param userManager менеджер пользовательского ввода
     * @param commandManager менеджер команд
     */
    public ExecuteScriptCommand(HumanBeingCollection collection, UserManager userManager, CommandManager commandManager) {
        this.collection = collection;
        this.userManager = userManager;
        this.commandManager = commandManager;
    }

    @Override
    public void execute(String[] args, Printer printer) {
        if (args.length < 1) {
            printer.printError("Не указано имя файла");
            return;
        }

        String filename = args[0];
        File file = new File(filename);

        if (!file.exists()) {
            printer.printError("Файл не существует: " + filename);
            return;
        }

        if (!file.canRead()) {
            printer.printError("Нет прав на чтение файла: " + filename);
            return;
        }

        try (Scanner fileScanner = new Scanner(file)) {
            while (fileScanner.hasNextLine()) {
                String line = fileScanner.nextLine().trim();
                if (line.isEmpty()) continue;

                String[] parts = line.split("\\s+", 2);
                String commandName = parts[0];
                String commandArgs = parts.length > 1 ? parts[1] : null;

                commandManager.executeCommand(commandName, commandArgs, printer);
            }
        } catch (FileNotFoundException e) {
            printer.printError("Ошибка при чтении файла: " + e.getMessage());
        }
    }

    @Override
    public String getDescription() {
        return "выполняет скрипт из указанного файла";
    }

    @Override
    public String getUsage() {
        return "execute_script <имя_файла>";
    }
}
