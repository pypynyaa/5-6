package utility;

/**
 * Класс для вывода сообщений в консоль
 */
public class Printer {
    /**
     * Выводит сообщение в консоль
     * @param message сообщение для вывода
     */
    public void print(String message) {
        System.out.print(message);
    }

    /**
     * Выводит сообщение в консоль с переносом строки
     * @param message сообщение для вывода
     */
    public void println(String message) {
        System.out.println(message);
    }

    /**
     * Выводит сообщение об ошибке в консоль
     * @param message сообщение об ошибке
     */
    public void printError(String message) {
        System.err.println(message);
    }
}
