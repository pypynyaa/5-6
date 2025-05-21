package parse;

import java.io.IOException;
import java.util.LinkedHashMap;
import javax.xml.stream.XMLStreamException;

/**
 * Интерфейс для чтения файлов
 */
public interface BaseReader {
    /**
     * Читает данные из файла
     * @return карта значений, где ключ - массив строк (путь), значение - строка
     * @throws IOException если произошла ошибка при чтении
     * @throws XMLStreamException если произошла ошибка при парсинге XML
     */
    LinkedHashMap<String[], String> readFromFile() throws IOException, XMLStreamException;
} 