import manager.HumanBeingCollection;
import manager.UserManager;
import utility.Printer;
import utility.XMLSerializer;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;

/**
 * Главный класс приложения
 */
public class Main {
    public static void main(String[] args) {
        String filePath;
        if (args.length == 0) {
            filePath = "data.xml";
            System.out.println("Используется файл по умолчанию: " + filePath);
        } else {
            filePath = args[0];
        }

        File file = new File(filePath);
        final HumanBeingCollection collection;

        try {
            if (file.exists()) {
                collection = XMLSerializer.loadFromFile(filePath);
                System.out.println("Коллекция успешно загружена из файла " + filePath);
            } else {
                collection = new HumanBeingCollection();
                System.out.println("Создана новая коллекция, так как файл " + filePath + " не существует");
            }
        } catch (Exception e) {
            System.err.println("Ошибка при загрузке коллекции: " + e.getMessage());
            System.exit(1);
            return;
        }

        Printer printer = new Printer();
        UserManager userManager = new UserManager(collection, printer);

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            try {
                XMLSerializer.saveToFile(collection, filePath);
                System.out.println("Коллекция успешно сохранена в файл " + filePath);
            } catch (Exception e) {
                System.err.println("Ошибка при сохранении коллекции: " + e.getMessage());
            }
        }));

        userManager.start();
    }
}
