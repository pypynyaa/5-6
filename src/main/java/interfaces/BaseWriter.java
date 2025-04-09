package interfaces;

import java.io.IOException;
import java.util.LinkedHashMap;

/**
 * Интерфейс для записи данных в файл.
 */
public interface BaseWriter {
    /**
     * Записывает данные в файл.
     * @param values Данные для записи (путь к элементу -> значение).
     * @throws IOException Если произошла ошибка записи.
     */
    void writeToFile(LinkedHashMap<String[], String> values) throws IOException;
}
