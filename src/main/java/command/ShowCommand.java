package command;

import manager.HumanBeingCollection;
import model.HumanBeing;
import utility.Printer;

import java.text.SimpleDateFormat;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Команда для вывода всех элементов коллекции в виде таблицы.
 */
public class ShowCommand implements Command {
    private final HumanBeingCollection collection;
    private final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    private static final int ID_WIDTH = 5;
    private static final int NAME_WIDTH = 20;
    private static final int COORD_WIDTH = 25;
    private static final int DATE_WIDTH = 19;
    private static final int HERO_WIDTH = 5;
    private static final int TOOTHPICK_WIDTH = 10;
    private static final int SPEED_WIDTH = 10;
    private static final int SOUNDTRACK_WIDTH = 20;
    private static final int WAIT_WIDTH = 10;
    private static final int WEAPON_WIDTH = 10;
    private static final int CAR_WIDTH = 15;

    private static final String TABLE_FORMAT = buildFormatString();
    private static final String SEPARATOR = buildSeparator();

    public ShowCommand(HumanBeingCollection collection) {
        this.collection = collection;
    }

    @Override
    public void execute(String[] args, Printer printer) {
        if (collection.size() == 0) {
            printer.println("Коллекция пуста");
            return;
        }

        printer.println(SEPARATOR);
        printer.println(String.format(TABLE_FORMAT,
                "ID", "Имя", "Координаты", "Дата создания", "Герой", "Зубочистка",
                "Скорость", "Саундтрек", "Ожидание", "Оружие", "Машина"));
        printer.println(SEPARATOR);

        collection.getHumanBeingSet().stream()
                .sorted(Comparator.comparing(HumanBeing::getId))
                .forEach(h -> {
                    String carName = h.getCar() != null ? h.getCar().getName() : "null";
                    printer.println(String.format(TABLE_FORMAT,
                            h.getId(),
                            h.getName(),
                            h.getCoordinates(),
                            dateFormat.format(h.getCreationDate()),
                            h.isRealHero(),
                            h.isHasToothpick(),
                            h.getImpactSpeed(),
                            h.getSoundtrackName(),
                            h.getMinutesOfWaiting(),
                            h.getWeaponType(),
                            carName));
                });
        printer.println(SEPARATOR);
    }

    @Override
    public String getDescription() {
        return "вывести все элементы коллекции в строковом представлении";
    }

    @Override
    public String getUsage() {
        return "show";
    }

    private static String buildFormatString() {
        return "| %-" + ID_WIDTH + "s | %-" + NAME_WIDTH + "s | %-" + COORD_WIDTH + "s | %-" + DATE_WIDTH + "s | %-" +
                HERO_WIDTH + "s | %-" + TOOTHPICK_WIDTH + "s | %-" + SPEED_WIDTH + "s | %-" + SOUNDTRACK_WIDTH + "s | %-" +
                WAIT_WIDTH + "s | %-" + WEAPON_WIDTH + "s | %-" + CAR_WIDTH + "s |";
    }

    private static String buildSeparator() {
        return "+" + "-".repeat(ID_WIDTH + 2) + "+" + "-".repeat(NAME_WIDTH + 2) + "+" + "-".repeat(COORD_WIDTH + 2) +
                "+" + "-".repeat(DATE_WIDTH + 2) + "+" + "-".repeat(HERO_WIDTH + 2) + "+" + "-".repeat(TOOTHPICK_WIDTH + 2) +
                "+" + "-".repeat(SPEED_WIDTH + 2) + "+" + "-".repeat(SOUNDTRACK_WIDTH + 2) + "+" + "-".repeat(WAIT_WIDTH + 2) +
                "+" + "-".repeat(WEAPON_WIDTH + 2) + "+" + "-".repeat(CAR_WIDTH + 2) + "+";
    }
}
