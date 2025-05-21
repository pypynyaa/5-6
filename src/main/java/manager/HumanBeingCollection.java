package manager;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import model.HumanBeing;

import java.util.LinkedHashSet;
import java.util.Date;
import java.util.Set;

/**
 * Класс для управления коллекцией HumanBeing
 */
@XmlRootElement(name = "humanBeings")
@XmlAccessorType(XmlAccessType.FIELD)
public class HumanBeingCollection {
    @XmlElement(name = "humanBeing")
    private final LinkedHashSet<HumanBeing> humanBeingSet;
    private final Date initializationDate;

    public HumanBeingCollection() {
        this.humanBeingSet = new LinkedHashSet<>();
        this.initializationDate = new Date();
    }

    /**
     * Добавляет новый элемент в коллекцию
     * @param humanBeing элемент для добавления
     */
    public void addToCollection(HumanBeing humanBeing) {
        humanBeingSet.add(humanBeing);
    }

    /**
     * Удаляет элемент из коллекции
     * @param humanBeing элемент для удаления
     */
    public void removeFromCollection(HumanBeing humanBeing) {
        humanBeingSet.remove(humanBeing);
    }

    /**
     * Очищает коллекцию
     */
    public void clear() {
        humanBeingSet.clear();
    }

    /**
     * Возвращает коллекцию
     * @return коллекция HumanBeing
     */
    public LinkedHashSet<HumanBeing> getHumanBeingSet() {
        return humanBeingSet;
    }

    /**
     * Возвращает дату инициализации коллекции
     * @return дата инициализации
     */
    public Date getInitializationDate() {
        return initializationDate;
    }

    /**
     * Возвращает размер коллекции
     * @return размер коллекции
     */
    public int size() {
        return humanBeingSet.size();
    }

    /**
     * Возвращает элемент по ID
     * @param id ID элемента
     * @return элемент или null, если не найден
     */
    public HumanBeing getById(int id) {
        for (HumanBeing humanBeing : humanBeingSet) {
            if (humanBeing.getId() == id) {
                return humanBeing;
            }
        }
        return null;
    }

    /**
     * Удаляет элемент по ID
     * @param id ID элемента
     */
    public void removeById(int id) {
        humanBeingSet.removeIf(humanBeing -> humanBeing.getId() == id);
    }

    /**
     * Удаляет элементы, большие заданного
     * @param humanBeing элемент для сравнения
     * @return количество удаленных элементов
     */
    public int removeGreater(HumanBeing humanBeing) {
        final int[] count = {0};
        humanBeingSet.removeIf(h -> {
            if (h.compareTo(humanBeing) > 0) {
                count[0]++;
                return true;
            }
            return false;
        });
        return count[0];
    }

    /**
     * Удаляет элементы, меньшие заданного
     * @param humanBeing элемент для сравнения
     * @return количество удаленных элементов
     */
    public int removeLower(HumanBeing humanBeing) {
        final int[] count = {0};
        humanBeingSet.removeIf(h -> {
            if (h.compareTo(humanBeing) < 0) {
                count[0]++;
                return true;
            }
            return false;
        });
        return count[0];
    }

    /**
     * Удаляет все элементы с заданным названием саундтрека
     * @param soundtrackName название саундтрека
     */
    public void removeAllBySoundtrackName(String soundtrackName) {
        humanBeingSet.removeIf(humanBeing -> humanBeing.getSoundtrackName().equals(soundtrackName));
    }
} 