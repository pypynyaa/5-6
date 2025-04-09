package parse;

import interfaces.BaseWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Arrays;

/**
 * Класс для записи XML файлов
 */
public class XMLWriter implements BaseWriter {
    private final String filePath;

    /**
     * Конструктор
     * @param filePath путь к файлу
     */
    public XMLWriter(String filePath) {
        this.filePath = filePath;
    }

    @Override
    public void writeToFile(LinkedHashMap<String[], String> values) throws IOException {
        try (FileWriter writer = new FileWriter(filePath)) {
            writer.write("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
            writer.write("<collection>\n");
            
            // Группируем данные для записи (адаптированная логика)
            Map<String, Map<String[], String>> objects = groupDataForXml(values);
            writeObjects(writer, objects, 1);
            
            writer.write("</collection>\n");
        }
    }
    
    /**
     * Группировка данных для корректной записи в XML.
     * Предполагает, что keys в values имеют вид: [objectType, fieldName] или [objectType, nestedObjectType, nestedFieldName]
     * Например: ["humanBeing", "name"] -> "John", ["humanBeing", "coordinates", "x"] -> "10.0"
     */
     private Map<String, Map<String[], String>> groupDataForXml(LinkedHashMap<String[], String> values) {
        Map<String, Map<String[], String>> result = new LinkedHashMap<>();
        int objectCounter = 0;

        for (Map.Entry<String[], String> entry : values.entrySet()) {
            String[] path = entry.getKey();
            if (path == null || path.length < 1) continue; // Игнорировать некорректные пути

            // Используем ID или первый элемент path как идентификатор объекта
            // Для группировки может потребоваться более сложная логика, 
            // зависящая от того, как вы генерируете ID и структуру path.
            // Здесь простой пример: группируем по первому элементу path (тип объекта)
            // и добавляем счетчик для различения однотипных объектов.
            String objectKey = path[0] + "_" + objectCounter; 
            if (path.length == 2 && path[1].equalsIgnoreCase("id")) {
                // Если есть ID, можно использовать его для группировки, но это усложнит логику
                // Пока оставляем группировку по типу + счетчик
            } else if (result.isEmpty()) {
                 // Если это первый элемент, начинаем новый объект
                 objectCounter++;
                 objectKey = path[0] + "_" + objectCounter;
            }
            // TODO: Улучшить логику определения начала нового объекта (например, по полю ID)

            Map<String[], String> objectFields = result.computeIfAbsent(objectKey, k -> new LinkedHashMap<>());
            
            // Сохраняем путь *относительно* объекта (убираем первый элемент)
            String[] relativePath = Arrays.copyOfRange(path, 1, path.length);
            if (relativePath.length > 0) { // Убедимся, что есть что добавлять
                 objectFields.put(relativePath, entry.getValue());
            }
        }
        return result;
    }

    /**
     * Запись объектов (адаптированная версия).
     */
    private void writeObjects(FileWriter writer, Map<String, Map<String[], String>> objects,
                            int depth) throws IOException {
        for (Map.Entry<String, Map<String[], String>> entry : objects.entrySet()) {
            String objectTagName = entry.getKey().split("_")[0]; // Имя тега из ключа
            Map<String[], String> fields = entry.getValue();

            writeIndent(writer, depth);
            writer.write("<" + objectTagName + ">\n"); 
            writeObjectFields(writer, fields, depth + 1);
            writeIndent(writer, depth);
            writer.write("</" + objectTagName + ">\n");
        }
    }

    /**
     * Запись полей объекта (рекурсивная).
     */
    private void writeObjectFields(FileWriter writer, Map<String[], String> fields,
                                 int depth) throws IOException {
        Map<String, Map<String[], String>> nestedObjects = new LinkedHashMap<>();
        Map<String, String> simpleFields = new LinkedHashMap<>();

        // Разделяем простые поля (path.length == 1) и вложенные (path.length > 1)
        for (Map.Entry<String[], String> entry : fields.entrySet()) {
            String[] path = entry.getKey();
            if (path.length == 1) {
                simpleFields.put(path[0], entry.getValue());
            } else if (path.length > 1) {
                String nestedObjectName = path[0];
                String[] remainingPath = Arrays.copyOfRange(path, 1, path.length);
                nestedObjects.computeIfAbsent(nestedObjectName, k -> new LinkedHashMap<>()).put(remainingPath, entry.getValue());
            }
        }

        // Записываем простые поля
        for (Map.Entry<String, String> simpleEntry : simpleFields.entrySet()) {
            writeSimpleField(writer, simpleEntry.getKey(), simpleEntry.getValue(), depth);
        }

        // Записываем вложенные объекты рекурсивно
        for (Map.Entry<String, Map<String[], String>> nestedEntry : nestedObjects.entrySet()) {
            writeIndent(writer, depth);
            writer.write("<" + nestedEntry.getKey() + ">\n");
            writeObjectFields(writer, nestedEntry.getValue(), depth + 1); // Рекурсивный вызов
            writeIndent(writer, depth);
            writer.write("</" + nestedEntry.getKey() + ">\n");
        }
    }
    
    /**
     * Запись простого поля (тег-значение).
     */
    private void writeSimpleField(FileWriter writer, String tagName, String value, int depth) throws IOException {
         writeIndent(writer, depth);
         writer.write("<" + tagName + ">");
         writer.write(escapeXml(value));
         writer.write("</" + tagName + ">\n");
    }

    /**
     * Запись отступа.
     */
    private void writeIndent(FileWriter writer, int depth) throws IOException {
        for (int i = 0; i < depth; i++) {
            writer.write("  ");
        }
    }
    
    /**
     * Экранирование спецсимволов XML.
     */
    private String escapeXml(String value) {
        if (value == null) {
            return "";
        }
        // Простая замена основных символов
        return value.replace("&", "&amp;")
                    .replace("<", "&lt;")
                    .replace(">", "&gt;")
                    .replace("\"", "&quot;")
                    .replace("'", "&apos;");
    }
} 