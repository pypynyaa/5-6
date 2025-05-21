package managers;

import mainClasses.HumanBeing;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Менеджер коллекции работников
 * Управляет коллекцией объектов Worker, обеспечивая операции добавления, удаления и модификации элементов
 */
public class CollectionManager {
    private LinkedHashSet<HumanBeing> humanCollection;
    private final LocalDate creationDate;
    private final CollectionWriter writer = new CollectionWriter();
    private final CollectionParser parser = new CollectionParser(this);
    private String filePath;

    /**
     * Создает новый менеджер коллекции
     * Инициализирует пустую коллекцию и устанавливает дату создания
     */
    public CollectionManager() {
        humanCollection = new LinkedHashSet<HumanBeing>();
        creationDate = LocalDate.now();
    }

    /**
     * Устанавливает путь к файлу для сохранения/загрузки коллекции
     * @param filePath путь к файлу
     */
    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    /**
     * Загружает коллекцию из файла
     *
     * @param filePath путь к файлу
     */
    public String loadCollectionFromFile(String filePath) {
        this.filePath = filePath;
        HashMap<String, LinkedHashSet<HumanBeing>> zalupa = new HashMap<>();
        zalupa = parser.parseFromFile(filePath);
        String message = zalupa.keySet().iterator().next();
        humanCollection = zalupa.get(message);
        return message;
    }

    /**
     * Сохраняет коллекцию в файл
     * @throws IllegalStateException если путь к файлу не установлен
     */
    public void saveCollectionToFile() {
        if (filePath == null) {
            throw new IllegalStateException("Путь к файлу не установлен");
        }
        writer.writeToFile(filePath, humanCollection);
    }

    /**
     * Возвращает дату создания коллекции
     * @return дата создания
     */
    public LocalDate getCreationDate() {
        return creationDate;
    }

    /**
     * Возвращает коллекцию работников
     * @return коллекция работников
     */
    public LinkedHashSet<HumanBeing> getHumansCollection() {
        return humanCollection;
    }

    /**
     * Устанавливает новую коллекцию работников
     * @param workersCollection новая коллекция
     */
    public void setWorkersCollection(LinkedHashSet<HumanBeing> workersCollection) {
        this.humanCollection = workersCollection;
    }

    /**
     * Возвращает информацию о коллекции
     * @return строка с информацией о типе, дате создания и размере коллекции
     */
    public String getCollectionInfo() {
        return ("Type - " + humanCollection.getClass().getName().substring(10,20) +
                "\nCreation date - " + getCreationDate() +
                "\nAmount of elements - " + humanCollection.size());
    }

    /**
     * Выводит все элементы коллекции
     */
    public String showCollectionElements() {
        String res;
        if (humanCollection.isEmpty()) {
            res = "Коллекция пуста";
        } else {
            res = humanCollection.stream()
                    .sorted(Comparator.comparing(HumanBeing::getName))
                    .map(HumanBeing::toString).collect(Collectors.joining("\n"));
        }
        return res;
    }

    /**
     * Добавляет нового работника в коллекцию
     * @param humanBeing новый работник
     */
    public void addElement(HumanBeing humanBeing) {
        if (humanBeing == null) {
            throw new IllegalArgumentException("Работник не может быть null");
        }
        humanBeing.setId(generateId());
        humanBeing.setCreationDate(LocalDate.now());
        if (humanBeing.getName() == null || humanBeing.getName().isEmpty()) {
            throw new IllegalArgumentException("Имя работника не может быть пустым");
        }
        if (humanBeing.getCoordinates() == null) {
            throw new IllegalArgumentException("Координаты работника не могут быть null");
        }

        humanCollection.add(humanBeing);
    }

    public void updateElement(int id, HumanBeing new_humanBeing) {
        humanCollection.stream().filter(human -> human.getId() == id).findFirst().
                ifPresent(human -> {
                    human.setId(new_humanBeing.getId());
                    human.setName(new_humanBeing.getName());
                    human.setCoordinates(new_humanBeing.getCoordinates());
                    human.setRealHero(new_humanBeing.getRealHero());
                    human.setHasToothpick(new_humanBeing.getHasToothpick());
                    human.setImpactSpeed(new_humanBeing.getImpactSpeed());
                    human.setSoundtrackName(new_humanBeing.getSoundtrackName());
                    human.setMinutesOfWaiting(new_humanBeing.getMinutesOfWaiting());
                    human.setWeaponType(new_humanBeing.getWeaponType());
                    human.setCar(new_humanBeing.getCar());
                });
    }

    /**
     * Удаляет работника по его id
     * @param id идентификатор работника
     */
    public void removeElement(int id) {
        humanCollection.removeIf(worker -> worker.getId() == id);
    }

    public void removeElementBySoundtrackName(String name) {
        humanCollection.removeIf(worker -> worker.getSoundtrackName().equals(name));
    }

    /**
     * Очищает коллекцию
     */
    public void clearCollection() {
        humanCollection.clear();
    }



    /**
     * Удаляет всех работников с зарплатой меньше заданной
     * @param humanBeing работник для сравнения
     * @return true если были удалены элементы, false если нет
     */
    public boolean removeLowerElement(HumanBeing humanBeing) {
        return humanCollection.removeIf(h -> h.getId() < humanBeing.getId());
    }


    /**
     * Генерирует уникальный идентификатор для нового работника
     * @return новый уникальный id, на единицу больше максимального в коллекции
     */
    public int generateId() {
        if (humanCollection.isEmpty()) {
            return 1;
        }
        return humanCollection.stream()
                .mapToInt(HumanBeing::getId)
                .max()
                .orElse(0) + 1;
    }
}