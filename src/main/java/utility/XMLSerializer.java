package utility;

import model.HumanBeing;
import model.Coordinates;
import model.Car;
import model.WeaponType;
import manager.HumanBeingCollection;
import parse.XMLReader;
import parse.XMLWriter;

import java.io.File;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Date;
import java.text.SimpleDateFormat;
import java.text.ParseException;
import java.util.Map;

/**
 * Класс для сериализации и десериализации коллекции в XML
 */
public class XMLSerializer {
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public static void saveToFile(HumanBeingCollection collection, String filename) {
        try {
            XMLWriter writer = new XMLWriter(filename);
            LinkedHashMap<String[], String> values = new LinkedHashMap<>();
            
            // Преобразуем коллекцию в формат для XMLWriter
            int index = 0;
            for (HumanBeing human : collection.getHumanBeingSet()) {
                String prefix = "humanBeing" + index;
                values.put(new String[]{prefix, "id"}, String.valueOf(human.getId()));
                values.put(new String[]{prefix, "name"}, human.getName());
                values.put(new String[]{prefix, "coordinates", "x"}, String.valueOf(human.getCoordinates().getX()));
                values.put(new String[]{prefix, "coordinates", "y"}, String.valueOf(human.getCoordinates().getY()));
                values.put(new String[]{prefix, "creationDate"}, dateFormat.format(human.getCreationDate()));
                values.put(new String[]{prefix, "realHero"}, String.valueOf(human.isRealHero()));
                values.put(new String[]{prefix, "hasToothpick"}, String.valueOf(human.isHasToothpick()));
                values.put(new String[]{prefix, "impactSpeed"}, String.valueOf(human.getImpactSpeed()));
                values.put(new String[]{prefix, "soundtrackName"}, human.getSoundtrackName());
                values.put(new String[]{prefix, "minutesOfWaiting"}, String.valueOf(human.getMinutesOfWaiting()));
                values.put(new String[]{prefix, "weaponType"}, human.getWeaponType().name());
                if (human.getCar() != null) {
                    values.put(new String[]{prefix, "car", "name"}, human.getCar().getName());
                }
                index++;
            }
            
            writer.writeToFile(values);
        } catch (Exception e) {
            throw new RuntimeException("Ошибка при сохранении в файл: " + e.getMessage());
        }
    }

    private static boolean arrayEquals(String[] arr1, String[] arr2) {
        if (arr1.length != arr2.length) return false;
        for (int i = 0; i < arr1.length; i++) {
            if (!arr1[i].equals(arr2[i])) return false;
        }
        return true;
    }

    private static String getValue(LinkedHashMap<String[], String> values, String... key) {
        for (Map.Entry<String[], String> entry : values.entrySet()) {
            if (arrayEquals(entry.getKey(), key)) {
                return entry.getValue();
            }
        }
        return null;
    }

    public static HumanBeingCollection loadFromFile(String filename) {
        try {
            File file = new File(filename);
            if (!file.exists()) {
                return new HumanBeingCollection();
            }

            XMLReader reader = new XMLReader(filename);
            LinkedHashMap<String[], String> values = reader.readFromFile();
            HumanBeingCollection collection = new HumanBeingCollection();
            
            // Преобразуем данные из XMLReader в коллекцию HumanBeing
            int index = 0;
            while (true) {
                String prefix = "humanBeing" + index;
                String id = getValue(values, prefix, "id");
                if (id == null) {
                    break;
                }
                
                HumanBeing human = new HumanBeing();
                human.setId(Integer.parseInt(id));
                human.setName(getValue(values, prefix, "name"));
                human.setCoordinates(new Coordinates(
                    Double.parseDouble(getValue(values, prefix, "coordinates", "x")),
                    Integer.parseInt(getValue(values, prefix, "coordinates", "y"))
                ));
                try {
                    human.setCreationDate(dateFormat.parse(getValue(values, prefix, "creationDate")));
                } catch (ParseException e) {
                    throw new RuntimeException("Ошибка при парсинге даты: " + e.getMessage());
                }
                human.setRealHero(Boolean.parseBoolean(getValue(values, prefix, "realHero")));
                human.setHasToothpick(Boolean.parseBoolean(getValue(values, prefix, "hasToothpick")));
                human.setImpactSpeed(Double.parseDouble(getValue(values, prefix, "impactSpeed")));
                human.setSoundtrackName(getValue(values, prefix, "soundtrackName"));
                human.setMinutesOfWaiting(Float.parseFloat(getValue(values, prefix, "minutesOfWaiting")));
                human.setWeaponType(WeaponType.valueOf(getValue(values, prefix, "weaponType")));
                
                String carName = getValue(values, prefix, "car", "name");
                if (carName != null) {
                    human.setCar(new Car(carName));
                }
                
                collection.addToCollection(human);
                index++;
            }
            
            return collection;
        } catch (Exception e) {
            throw new RuntimeException("Ошибка при загрузке из файла: " + e.getMessage());
        }
    }
} 