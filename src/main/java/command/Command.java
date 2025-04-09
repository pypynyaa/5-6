package command;

import utility.Printer;

/**
 * Интерфейс для всех команд программы
 */
public interface Command {
    /**
     * Выполняет команду
     * @param args аргументы команды
     * @param printer объект для вывода сообщений
     */
    void execute(String[] args, Printer printer);

    /**
     * Возвращает описание команды
     * @return описание команды
     */
    String getDescription();

    /**
     * Возвращает использование команды
     * @return использование команды
     */
    String getUsage();
}
