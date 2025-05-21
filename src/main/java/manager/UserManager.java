package manager;

import command.*;
import model.*;
import manager.validator.*;
import utility.Printer;

import java.awt.Desktop;
import java.net.URI;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.NoSuchElementException;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Класс для работы с пользовательским вводом
 */
public class UserManager {
    private final HumanBeingCollection collection;
    private final Printer printer;
    private final BufferedReader reader;
    private static boolean running = true;
    private static boolean ctrlDPressed = false;

    public UserManager(HumanBeingCollection collection, Printer printer) {
        this.collection = collection;
        this.printer = printer;
        this.reader = new BufferedReader(new InputStreamReader(System.in));
    }

    public String readLine() throws IOException {
        String input = reader.readLine();
        if (input == null) {
            if (!ctrlDPressed) {
            ctrlDPressed = true;
            printer.println("\nНе надо так делать :)");
            try {
                    Desktop.getDesktop().browse(new URI("https://vk.com/wall-1959_7709426?z=video-1959_456249981%2Fb9d5c37a5c14750ca2%2Fpl_post_-1959_7709426"));
            } catch (Exception e) {
                    printer.printError("Не удалось открыть ссылку: " + e.getMessage());
                }
                printer.println("Для выхода используйте команду 'exit'");
            }
            return "exit";
        }
        return input;
    }


    public void close() throws IOException {
        reader.close();
    }

    public static void stop() {
        running = false;
    }

    public static boolean isRunning() {
        return running;
    }

    public HumanBeing readHumanBeing(Printer printer) throws IOException {
        printer.println("Введите данные для создания HumanBeing:");

        String name = readField("имя", printer, s -> s != null && !s.isEmpty(), String.class);
        Coordinates coordinates = readCoordinates(printer);
        Boolean realHero = readField("является ли героем (true/false)", printer, s -> s.equals("true") || s.equals("false"), Boolean.class);
        boolean hasToothpick = readField("имеет ли зубочистку (true/false)", printer, s -> s.equals("true") || s.equals("false"), Boolean.class);
        double impactSpeed = readField("скорость удара (больше -22)", printer, s -> {
            try {
                double value = Double.parseDouble(s);
                return value > -22;
            } catch (NumberFormatException e) {
                return false;
            }
        }, Double.class);
        String soundtrackName = readField("название саундтрека", printer, s -> s != null && !s.isEmpty(), String.class);
        float minutesOfWaiting = readField("минуты ожидания", printer, s -> {
            try {
                Float.parseFloat(s);
                return true;
            } catch (NumberFormatException e) {
                return false;
            }
        }, Float.class);
        WeaponType weaponType = readWeaponType(printer);
        Car car = readCar(printer);

        return new HumanBeing(
                collection.size() + 1,
                name,
                coordinates,
                new Date(),
                realHero,
                hasToothpick,
                impactSpeed,
                soundtrackName,
                minutesOfWaiting,
                weaponType,
                car
        );
    }

    private Coordinates readCoordinates(Printer printer) throws IOException {
        printer.println("Введите координаты:");
        double x = readField("координата X (не больше 25)", printer, s -> {
            try {
                double value = Double.parseDouble(s);
                return value <= 25;
            } catch (NumberFormatException e) {
                return false;
            }
        }, Double.class);
        Integer y = readField("координата Y (больше -337)", printer, s -> {
            try {
                int value = Integer.parseInt(s);
                return value > -337;
            } catch (NumberFormatException e) {
                return false;
            }
        }, Integer.class);
        return new Coordinates(x, y);
    }

    private WeaponType readWeaponType(Printer printer) throws IOException {
        printer.println("Доступные типы оружия:");
        for (WeaponType type : WeaponType.values()) {
            printer.println(type.name());
        }
        return readField("тип оружия", printer, s -> {
            try {
                WeaponType.valueOf(s);
                return true;
            } catch (IllegalArgumentException e) {
                return false;
            }
        }, WeaponType.class);
    }

    /**
     * Читает данные автомобиля из пользовательского ввода
     * @param printer объект для вывода сообщений
     * @return объект Car или null, если пользователь ввел пустую строку
     * @throws IOException если произошла ошибка при чтении
     */
    public Car readCar(Printer printer) throws IOException {
        printer.println("Введите название автомобиля (или пустую строку для null):");
        String name = readLine();
        if (name == null || name.isEmpty()) {
            return null;
        }
        return new Car(name);
    }

    private <T> T readField(String fieldName, Printer printer, java.util.function.Predicate<String> validator, Class<T> type) throws IOException {
        while (true) {
            printer.print("Введите " + fieldName + ": ");
            String input = readLine();
            if (validator.test(input)) {
                try {
                    if (type == String.class) {
                        return type.cast(input);
                    } else if (type == Boolean.class) {
                        return type.cast(Boolean.valueOf(input));
                    } else if (type == Integer.class) {
                        return type.cast(Integer.valueOf(input));
                    } else if (type == Double.class) {
                        return type.cast(Double.valueOf(input));
                    } else if (type == Float.class) {
                        return type.cast(Float.valueOf(input));
                    } else if (type == WeaponType.class) {
                        return type.cast(WeaponType.valueOf(input));
                    }
                } catch (IllegalArgumentException e) {
                    printer.printError("Неверный формат ввода. Попробуйте еще раз.");
                    continue;
                }
            }
            printer.printError("Неверный ввод. Попробуйте еще раз.");
        }
    }

    public void start() {
        CommandManager commandManager = new CommandManager(printer);
        
        // Регистрация команд
        commandManager.registerCommand("help", new HelpCommand(commandManager));
        commandManager.registerCommand("info", new InfoCommand(collection));
        commandManager.registerCommand("show", new ShowCommand(collection));
        commandManager.registerCommand("add", new AddCommand(collection, this));
        commandManager.registerCommand("update", new UpdateCommand(collection, this));
        commandManager.registerCommand("remove_by_id", new RemoveByIdCommand(collection));
        commandManager.registerCommand("clear", new ClearCommand(collection));
        commandManager.registerCommand("execute_script", new ExecuteScriptCommand(collection, this, commandManager));
        commandManager.registerCommand("exit", new ExitCommand());
        commandManager.registerCommand("remove_greater", new RemoveGreaterCommand(collection, this));
        commandManager.registerCommand("remove_lower", new RemoveLowerCommand(collection, this));
        commandManager.registerCommand("history", new HistoryCommand(commandManager));
        commandManager.registerCommand("remove_all_by_soundtrack_name", new RemoveAllBySoundtrackNameCommand(collection));
        commandManager.registerCommand("filter_greater_than_car", new FilterGreaterThanCarCommand(collection, this));
        commandManager.registerCommand("print_descending", new PrintDescendingCommand(collection));
        commandManager.registerCommand("save", new SaveCommand(collection, "data.xml"));

        printer.println("Программа запущена. Введите 'help' для списка команд.");
        while (running) {
            try {
                printer.print("\nВведите команду: ");
                String input = readLine();
                if (input == null || input.equals("exit")) {
                    break;
                }
                if (!input.trim().isEmpty()) {
                    String[] parts = input.split(" ", 2);
                    String commandName = parts[0];
                    String args = parts.length > 1 ? parts[1] : "";
                    commandManager.executeCommand(commandName, args, printer);
                }
            } catch (IOException e) {
                printer.printError("Ошибка при чтении ввода: " + e.getMessage());
            }
        }
    }

    public static void main(String[] args) {
        try {
            HumanBeingCollection collection = new HumanBeingCollection();
            Printer printer = new Printer();
            UserManager userManager = new UserManager(collection, printer);
            userManager.start();
        } catch (Exception e) {
            System.err.println("Ошибка при запуске программы: " + e.getMessage());
            e.printStackTrace();
        }
    }
}

