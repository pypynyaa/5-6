package managers;

import mainClasses.HumanBeing;
import org.w3c.dom.*;
import javax.xml.parsers.*;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.*;
import java.util.LinkedHashSet;

/**
 * Класс для сохранения коллекции HumanBeing в XML файл
 */
public class CollectionWriter {

    /**
     * Записывает коллекцию HumanBeing в XML файл
     * @param filePath путь к файлу для сохранения
     * @param collection коллекция HumanBeing для сохранения
     */
    public void writeToFile(String filePath, LinkedHashSet<HumanBeing> collection) {
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document = builder.newDocument();

            // Создаем корневой элемент
            Element rootElement = document.createElement("humans");
            document.appendChild(rootElement);

            // Добавляем все объекты HumanBeing
            for (HumanBeing human : collection) {
                Element humanElement = createHumanElement(document, human);
                rootElement.appendChild(humanElement);
            }

            // Настраиваем форматирование
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");

            // Записываем в файл
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
                DOMSource source = new DOMSource(document);
                StreamResult result = new StreamResult(writer);
                transformer.transform(source, result);
            }

        } catch (Exception e) {
            System.out.println("Ошибка при записи в файл: " + e.getMessage());
        }
    }

    /**
     * Создает XML элемент для HumanBeing
     */
    private Element createHumanElement(Document document, HumanBeing human) {
        Element humanElement = document.createElement("human");

        // Добавляем id
        addTextElement(document, humanElement, "id", String.valueOf(human.getId()));

        // Добавляем name
        addTextElement(document, humanElement, "name", human.getName());

        // Добавляем coordinates
        Element coordinatesElement = document.createElement("coordinates");
        addTextElement(document, coordinatesElement, "x", String.valueOf(human.getCoordinates().getX()));
        addTextElement(document, coordinatesElement, "y", String.valueOf(human.getCoordinates().getY()));
        humanElement.appendChild(coordinatesElement);

        // Добавляем creationDate
        addTextElement(document, humanElement, "creationDate", human.getCreationDate().toString());

        // Добавляем realHero
        addTextElement(document, humanElement, "realHero", String.valueOf(human.getRealHero()));

        // Добавляем hasToothpick
        addTextElement(document, humanElement, "hasToothpick", String.valueOf(human.getHasToothpick()));

        // Добавляем impactSpeed
        addTextElement(document, humanElement, "impactSpeed", String.valueOf(human.getImpactSpeed()));

        // Добавляем soundtrackName
        addTextElement(document, humanElement, "soundtrackName", human.getSoundtrackName());

        // Добавляем minutesOfWaiting (если есть)
        if (human.getMinutesOfWaiting() != null) {
            addTextElement(document, humanElement, "minutesOfWaiting", String.valueOf(human.getMinutesOfWaiting()));
        }

        // Добавляем weaponType (если есть)
        if (human.getWeaponType() != null) {
            addTextElement(document, humanElement, "weaponType", human.getWeaponType().name());
        }

        // Добавляем car
        Element carElement = document.createElement("car");
        addTextElement(document, carElement, "name", human.getCar().getName());
        humanElement.appendChild(carElement);

        return humanElement;
    }

    /**
     * Вспомогательный метод для создания текстового элемента
     */
    private void addTextElement(Document doc, Element parent, String tagName, String textContent) {
        Element element = doc.createElement(tagName);
        element.setTextContent(textContent);
        parent.appendChild(element);
    }
}