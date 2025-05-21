package commands;

import mainClasses.Car;
import mainClasses.HumanBeing;
import managers.CollectionManager;

/**
 * Команда 'exit'
 * Завершает программу
 */
public class ExitCommand extends Command {

    /**
     * Создает команду exit
     */
    public ExitCommand() {
        super("exit", "завершить программу", CommandType.WITHOUT_DATA, false);
    }

    /**
     * Исполняет команду
     * @param args аргументы команды (не используются)
     * @param collectionManager менеджер коллекции (не используется)
     */
    @Override
    public String  execute(String[] args, CollectionManager collectionManager) {
        if (args.length > 0) {
            return "Данная команда не принимает аргументов!";
        } else {
            System.exit(0);
        }
        return "";
    }

    @Override
    public String execute(String[] args, CollectionManager collectionManager, HumanBeing humanBeing) {
        return "";
    }

    @Override
    public String execute(String[] args, CollectionManager collectionManager, Car car) {
        return "";
    }
}