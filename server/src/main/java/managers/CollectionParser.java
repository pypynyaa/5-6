package managers;

import mainClasses.Car;
import mainClasses.Coordinates;
import mainClasses.HumanBeing;
import mainClasses.WeaponType;
import org.w3c.dom.*;
import org.xml.sax.InputSource;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.*;

public class CollectionParser {
    private final CollectionManager collectionManager;

    public CollectionParser(CollectionManager collectionManager) {
        this.collectionManager = collectionManager;
    }

    /**
     * Читает и парсит XML файл в LinkedHashSet<HumanBeing>
     * @param filePath путь к XML файлу
     * @return LinkedHashSet<HumanBeing> или null в случае ошибки
     */
    public HashMap<String, LinkedHashSet<HumanBeing>> parseFromFile(String filePath) {
        LinkedHashSet<HumanBeing> humanBeings = new LinkedHashSet<>();
        Set<Integer> uniqueIds = new HashSet<>();
        String errorMessage = "";

        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();

            try (InputStreamReader reader = new InputStreamReader(
                    new FileInputStream(filePath), StandardCharsets.UTF_8)) {

                InputSource inputSource = new InputSource(reader);
                Document document = builder.parse(inputSource);
                NodeList humanNodes = document.getElementsByTagName("human");

                for (int i = 0; i < humanNodes.getLength(); i++) {
                    Node humanNode = humanNodes.item(i);
                    if (humanNode.getNodeType() == Node.ELEMENT_NODE) {
                        Element humanElement = (Element) humanNode;
                        HumanBeing human = parseHumanBeing(humanElement);

                        if (human != null) {
                            if (uniqueIds.add(human.getId())) {
                                humanBeings.add(human);
                            } else {
                                errorMessage = "Обнаружены дубликаты ID! Добавлен только первый HumanBeing с id="
                                        + human.getId();
                            }
                        }
                    }
                }

                HashMap<String, LinkedHashSet<HumanBeing>> result = new HashMap<>();
                result.put(errorMessage, humanBeings);
                return result;

            } catch (Exception e) {
                HashMap<String, LinkedHashSet<HumanBeing>> errorResult = new HashMap<>();
                errorResult.put("Ошибка парсинга: " + e.getMessage(), new LinkedHashSet<>());
                return errorResult;
            }
        } catch (Exception e) {
            HashMap<String, LinkedHashSet<HumanBeing>> errorResult = new HashMap<>();
            errorResult.put("Фатальная ошибка: " + e.getMessage(), new LinkedHashSet<>());
            return errorResult;
        }
    }

    /**
     * Парсит элемент XML в объект HumanBeing.
     */
    private HumanBeing parseHumanBeing(Element humanElement) {
        try {
            Integer id = Integer.parseInt(getElementText(humanElement, "id"));
            String name = getElementText(humanElement, "name");
            Coordinates coordinates = parseCoordinates(humanElement.getElementsByTagName("coordinates").item(0));
            LocalDate creationDate = LocalDate.parse(getElementText(humanElement, "creationDate"));
            Boolean realHero = Boolean.parseBoolean(getElementText(humanElement, "realHero"));
            boolean hasToothpick = Boolean.parseBoolean(getElementText(humanElement, "hasToothpick"));
            float impactSpeed = Float.parseFloat(getElementText(humanElement, "impactSpeed"));
            String soundtrackName = getElementText(humanElement, "soundtrackName");
            Float minutesOfWaiting = parseNullableFloat(humanElement, "minutesOfWaiting");
            WeaponType weaponType = parseWeaponType(humanElement);
            Car car = parseCar(humanElement.getElementsByTagName("car").item(0));

            HumanBeing human = new HumanBeing(
                    id, name, coordinates, creationDate, realHero, hasToothpick,
                    impactSpeed, soundtrackName, minutesOfWaiting, weaponType, car
            );
            human.validate(); // Валидация полей
            return human;

        } catch (Exception e) {
            System.err.println("Ошибка парсинга HumanBeing1: " + e.getMessage());
            return null;
        }
    }

    /**
     * Парсит Coordinates из XML узла
     */
    private Coordinates parseCoordinates(Node coordinatesNode) {
        if (coordinatesNode.getNodeType() == Node.ELEMENT_NODE) {
            Element coordinatesElement = (Element) coordinatesNode;
            Float x = Float.parseFloat(getElementText(coordinatesElement, "x"));
            long y = Long.parseLong(getElementText(coordinatesElement, "y"));
            Coordinates coordinates = new Coordinates(x, y);
            coordinates.validate();
            return coordinates;
        }
        return null;
    }

    /**
     * Парсит Car из XML узла
     */
    private Car parseCar(Node carNode) {
        if (carNode.getNodeType() == Node.ELEMENT_NODE) {
            Element carElement = (Element) carNode;
            String name = getElementText(carElement, "name");
            Car car = new Car(name);
            car.validate();
            return car;
        }
        return null;
    }

    /**
     * Парсит WeaponType (может быть null)
     */
    private WeaponType parseWeaponType(Element humanElement) {
        String weaponTypeStr = getElementText(humanElement, "weaponType");
        return weaponTypeStr != null ? WeaponType.valueOf(weaponTypeStr) : null;
    }

    /**
     * Парсит Float (может быть null)
     */
    private Float parseNullableFloat(Element element, String tagName) {
        String text = getElementText(element, tagName);
        return text != null ? Float.parseFloat(text) : null;
    }

    /**
     * Получает текст элемента по тегу
     */
    private String getElementText(Element parent, String tagName) {
        NodeList nodeList = parent.getElementsByTagName(tagName);
        return nodeList.getLength() > 0 ? nodeList.item(0).getTextContent() : null;
    }
}