package parse;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Stack;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

/**
 * Класс для чтения XML файлов
 */
public class XMLReader implements BaseReader {
    private final String filePath;

    /**
     * Конструктор
     * @param filePath путь к файлу
     */
    public XMLReader(String filePath) {
        this.filePath = filePath;
    }

    @Override
    public LinkedHashMap<String[], String> readFromFile() throws IOException, XMLStreamException {
        LinkedHashMap<String[], String> values = new LinkedHashMap<>();
        Stack<String> path = new Stack<>();
        XMLInputFactory factory = XMLInputFactory.newInstance();
        XMLStreamReader reader = factory.createXMLStreamReader(new FileReader(new File(filePath)));

        String currentObject = null;
        while (reader.hasNext()) {
            int event = reader.next();
            switch (event) {
                case XMLStreamReader.START_ELEMENT:
                    String elementName = reader.getLocalName();
                    if (elementName.startsWith("humanBeing")) {
                        currentObject = elementName;
                    } else if (!elementName.equals("collection")) {
                        path.push(elementName);
                    }
                    break;
                case XMLStreamReader.CHARACTERS:
                    if (!reader.getText().trim().isEmpty()) {
                        String[] key;
                        if (currentObject != null) {
                            key = new String[path.size() + 1];
                            key[0] = currentObject;
                            for (int i = 0; i < path.size(); i++) {
                                key[i + 1] = path.get(i);
                            }
                        } else {
                            key = new String[path.size()];
                            for (int i = 0; i < path.size(); i++) {
                                key[i] = path.get(i);
                            }
                        }
                        String value = reader.getText().trim();
                        values.put(key, value);
                    }
                    break;
                case XMLStreamReader.END_ELEMENT:
                    if (reader.getLocalName().startsWith("humanBeing")) {
                        currentObject = null;
                    } else if (!reader.getLocalName().equals("collection")) {
                        path.pop();
                    }
                    break;
            }
        }
        reader.close();
        return values;
    }
} 