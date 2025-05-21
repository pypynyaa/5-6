package mainClasses;

import interfaces.Validatable;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.Date;
import java.util.Objects;

/**
 * Класс, представляющий работника
 * Содержит всю информацию о работнике: личные данные, должность, зарплату и даты работы
 */
public class HumanBeing implements Validatable, Serializable, Comparable<HumanBeing> {
    private Integer id; //Поле не может быть null, Значение поля должно быть больше 0, Значение этого поля должно быть уникальным, Значение этого поля должно генерироваться автоматически
    private String name; //Поле не может быть null, Строка не может быть пустой
    private Coordinates coordinates; //Поле не может быть null
    private LocalDate creationDate; //Поле не может быть null, Значение этого поля должно генерироваться автоматически
    private Boolean realHero; //Поле не может быть null
    private boolean hasToothpick;
    private float impactSpeed; //Значение поля должно быть больше -22
    private String soundtrackName; //Поле не может быть null
    private Float minutesOfWaiting; //Поле может быть null
    private WeaponType weaponType; //Поле может быть null
    private Car car; //Поле не может быть null

    private static final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
    private static final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm");
    private static final DateTimeFormatter zonedDateTimeFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm z");

    /**
     * Конструктор без параметров
     */
    public HumanBeing() {}

    /**
     * Конструктор с параметрами
     */
    public HumanBeing(Integer id, String name, Coordinates coordinates, LocalDate creationDate, Boolean realHero, boolean hasToothpick,
                      float impactSpeed, String soundtrackName, Float minutesOfWaiting, WeaponType weaponType, Car car) {
        this.id = id;
        this.name = name;
        this.coordinates = coordinates;
        this.creationDate = creationDate;
        this.realHero = realHero;
        this.hasToothpick = hasToothpick;
        this.impactSpeed = impactSpeed;
        this.soundtrackName = soundtrackName;
        this.minutesOfWaiting = minutesOfWaiting;
        this.weaponType = weaponType;
        this.car = car;
    }

    /**
     * Получает идентификатор работника
     * @return идентификатор
     */
    public Integer getId() {
        return id;
    }

    /**
     * Получает имя работника
     * @return имя
     */
    public String getName() {
        return name;
    }

    /**
     * Получает координаты работника
     * @return координаты
     */
    public Coordinates getCoordinates() {
        return coordinates;
    }

    /**
     * Получает дату создания записи
     * @return дата создания
     */
    public LocalDate getCreationDate() {
        return creationDate;
    }

    public Boolean getRealHero() {
        return realHero;
    }

    public void setRealHero(Boolean realHero) {
        this.realHero = realHero;
    }

    public boolean getHasToothpick() {
        return hasToothpick;
    }

    public void setHasToothpick(boolean hasToothpick) {
        this.hasToothpick = hasToothpick;
    }

    public float getImpactSpeed() {
        return impactSpeed;
    }
    public void setImpactSpeed(float impactSpeed) {
        this.impactSpeed = impactSpeed;
    }
    public String getSoundtrackName() {
        return soundtrackName;
    }

    public void setSoundtrackName(String soundtrackName) {
        this.soundtrackName = soundtrackName;
    }

    public Float getMinutesOfWaiting() {
        return minutesOfWaiting;
    }

    public void setMinutesOfWaiting(Float minutesOfWaiting) {
        this.minutesOfWaiting = minutesOfWaiting;
    }

    public WeaponType getWeaponType() {
        return weaponType;
    }

    public void setWeaponType(WeaponType weaponType) {
        this.weaponType = weaponType;
    }
    public Car getCar() {
        return car;
    }
    public void setCar(Car car) {
        this.car = car;
    }
    /**
     * Устанавливает идентификатор работника
     * @param id идентификатор
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * Устанавливает имя работника
     * @param name имя
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Устанавливает координаты работника
     * @param coordinates координаты
     */
    public void setCoordinates(Coordinates coordinates) {
        this.coordinates = coordinates;
    }

    /**
     * Устанавливает дату создания записи
     * @param creationDate дата создания
     */
    public void setCreationDate(LocalDate creationDate) {
        this.creationDate = creationDate;
    }




    /**
     * Преобразует объект в строковое представление
     * @return строковое представление работника со всеми его данными
     */
    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        result.append("Человек:\n");
        result.append("  ID: ").append(id != null ? id : "не установлен").append("\n");
        result.append("  Имя: ").append(name != null ? name : "не указано").append("\n");
        result.append("  Координаты: ").append(coordinates != null ? coordinates : "не указаны").append("\n");
        result.append("  Дата создания: ").append(creationDate != null ? creationDate : "не указана").append("\n");
        result.append("  Настоящий герой?: ").append(realHero).append("\n");
        result.append("  Есть зубочистка?: ").append(hasToothpick).append("\n");
        result.append("  Скорость удара: ").append(impactSpeed <= -22 ? impactSpeed : "не указана").append("\n");
        result.append("  Название саундтрека: ").append(soundtrackName != null ? soundtrackName : "не указана").append("\n");
        result.append("  Минут ожидания: ").append(minutesOfWaiting != null ? minutesOfWaiting : "не указано").append("\n");
        result.append(" Тип оружия: ").append(weaponType != null ? weaponType.getValue() : "не указан").append("\n");
        if (car != null) {
            result.append("Информация о машине: ");
            result.append("     ").append(car.toString()).append("\n");
        } else {
            result.append("Нет данных о машине.");
        }


        return result.toString();
    }

    /**
     * Вычисляет хеш-код объекта
     * @return хеш-код
     */
    @Override
    public int hashCode() {
        return id.hashCode() + name.hashCode() + coordinates.hashCode() + creationDate.hashCode()
                + realHero.hashCode() + soundtrackName.hashCode() + minutesOfWaiting.hashCode() + weaponType.hashCode() + car.hashCode();
    }



    /**
     * Сравнивает текущий объект с другим объектом
     * @param o объект для сравнения
     * @return true, если объекты равны, false в противном случае
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        HumanBeing humanBeing = (HumanBeing) o;
        return name == humanBeing.name &&
                id.equals(humanBeing.id) &&
                coordinates.equals(humanBeing.coordinates) &&
                creationDate.equals(humanBeing.creationDate) &&
                realHero.equals(humanBeing.realHero) &&
                hasToothpick == humanBeing.hasToothpick &&
                impactSpeed == humanBeing.impactSpeed &&
                soundtrackName.equals(humanBeing.soundtrackName) &&
                minutesOfWaiting.equals(humanBeing.minutesOfWaiting) &&
                weaponType.equals(humanBeing.weaponType) &&
                car.equals(humanBeing.car);
    }

    @Override
    public void validate() throws IllegalArgumentException {
        if (id == null || id <= 0) {
            throw new IllegalArgumentException("ID должен быть положительным числом");
        }
        if (name == null || name.isEmpty()) {
            throw new IllegalArgumentException("Имя не может быть пустым");
        }
        if (coordinates == null) {
            throw new IllegalArgumentException("Координаты не могут быть null");
        }
        coordinates.validate();
        
        if (creationDate == null) {
            throw new IllegalArgumentException("Дата создания не может быть null");
        }
        if (realHero == null) {
            throw new IllegalArgumentException("Поле не может быть null");
        }
        if (impactSpeed <= -22) {
            throw new IllegalArgumentException("Значение поля должно быть больше -22");
        }
        if (soundtrackName == null) {
            throw new IllegalArgumentException("Поле не может быть null");
        }
        if (car == null) {
            throw new IllegalArgumentException("Поле не может быть null");
        }
    }

    @Override
    public int compareTo(HumanBeing o) {
        return Integer.compare(this.id, o.id);
    }
}