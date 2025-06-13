package mainClasses;

import interfaces.Validatable; 

import java.io.Serializable; 
import java.time.LocalDate;
import java.time.format.DateTimeFormatter; 
import java.util.Comparator; 
import java.util.Locale; 
import java.util.Objects; 


public class HumanBeing implements Validatable, Serializable, Comparable<HumanBeing> {
    private Integer id; 
    private String name; 
    private Coordinates coordinates; 
    private LocalDate creationDate; 
    private Boolean realHero; 
    private boolean hasToothpick; 
    private float impactSpeed; 
    private String soundtrackName; 
    private Float minutesOfWaiting; 
    private WeaponType weaponType; 
    private Car car;
    private String user; 

    
    public HumanBeing() {}

    
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

    public Integer getId() { return id; }
    public String getName() { return name; }
    public Coordinates getCoordinates() { return coordinates; }
    public LocalDate getCreationDate() { return creationDate; }
    public Boolean getRealHero() { return realHero; }
    public boolean getHasToothpick() { return hasToothpick; }
    public float getImpactSpeed() { return impactSpeed; }
    public String getSoundtrackName() { return soundtrackName; }
    public Float getMinutesOfWaiting() { return minutesOfWaiting; }
    public WeaponType getWeaponType() { return weaponType; }
    public Car getCar() { return car; }
    public String getUser() { return user; }


    public void setId(Integer id) { this.id = id; }
    public void setName(String name) { this.name = name; }
    public void setCoordinates(Coordinates coordinates) { this.coordinates = coordinates; }
    public void setCreationDate(LocalDate creationDate) { this.creationDate = creationDate; }
    public void setRealHero(Boolean realHero) { this.realHero = realHero; }
    public void setHasToothpick(boolean hasToothpick) { this.hasToothpick = hasToothpick; }
    public void setImpactSpeed(float impactSpeed) { this.impactSpeed = impactSpeed; }
    public void setSoundtrackName(String soundtrackName) { this.soundtrackName = soundtrackName; }
    public void setMinutesOfWaiting(Float minutesOfWaiting) { this.minutesOfWaiting = minutesOfWaiting; }
    public void setWeaponType(WeaponType weaponType) { this.weaponType = weaponType; }
    public void setCar(Car car) { this.car = car; }
    public void setUser(String user) { this.user = user; }


    
    public static String[] getTableHeaders() {
        return new String[] {
                "ID",
                "Имя",
                "Координаты",
                "Дата создания",
                "Герой",
                "Зубочистка",
                "Скорость удара",
                "Саундтрек",
                "Мин. ожидания",
                "Оружие",
                "Машина"
        };
    }

    
    public String[] toTableRow() {
        System.err.println("DEBUG (HumanBeing.toTableRow): ID=" + (id != null ? id : "null"));
        System.err.println("DEBUG:   impactSpeed = " + this.impactSpeed +
                " (isNaN: " + Float.isNaN(this.impactSpeed) +
                ", isInfinite: " + Float.isInfinite(this.impactSpeed) + ")");
        System.err.println("DEBUG:   minutesOfWaiting = " + (this.minutesOfWaiting != null ? this.minutesOfWaiting : "null") +
                " (isNaN: " + (this.minutesOfWaiting != null && Float.isNaN(this.minutesOfWaiting)) +
                ", isInfinite: " + (this.minutesOfWaiting != null && Float.isInfinite(this.minutesOfWaiting)) + ")");
        if (coordinates != null) {
            System.err.println("DEBUG:   coordinates.x = " + coordinates.getX() +
                    " (isNaN: " + Double.isNaN((double) coordinates.getX()) +
                    ", isInfinite: " + Double.isInfinite((double) coordinates.getX()) + ")");
            System.err.println("DEBUG:   coordinates.y = " + coordinates.getY() +
                    " (isNaN: " + Double.isNaN((double) coordinates.getY()) +
                    ", isInfinite: " + Double.isInfinite((double) coordinates.getY()) + ")");
        } else {
            System.err.println("DEBUG:   coordinates = null");
        }


        String formattedImpactSpeed;
        if (Float.isNaN(impactSpeed) || Float.isInfinite(impactSpeed)) {
            formattedImpactSpeed = "N/A";
        } else {
            formattedImpactSpeed = String.format(Locale.ROOT, impactSpeed == (int)impactSpeed ? "%.0f" : "%.1f", impactSpeed);
        }

        String formattedMinutesOfWaiting;
        if (minutesOfWaiting == null) {
            formattedMinutesOfWaiting = "-";
        } else if (Float.isNaN(minutesOfWaiting) || Float.isInfinite(minutesOfWaiting)) {
            formattedMinutesOfWaiting = "N/A";
        } else {
            formattedMinutesOfWaiting = String.format(Locale.ROOT, minutesOfWaiting == (int)minutesOfWaiting.floatValue() ? "%.0f" : "%.1f", minutesOfWaiting);
        }

        String formattedCoordinates;
        if (coordinates == null) {
            formattedCoordinates = "-";
        } else {
            double x = (double) coordinates.getX();
            double y = (double) coordinates.getY();

            String xStr = (Double.isNaN(x) || Double.isInfinite(x)) ? "N/A" : String.format(Locale.ROOT, "%.0f", x);
            String yStr = (Double.isNaN(y) || Double.isInfinite(y)) ? "N/A" : String.format(Locale.ROOT, "%.0f", y);

            if (xStr.equals("N/A") && yStr.equals("N/A")) {
                formattedCoordinates = "N/A";
            } else {
                formattedCoordinates = String.format(Locale.ROOT, "(%s,%s)", xStr, yStr);
            }
        }

        return new String[] {
                String.valueOf(id != null ? id : "-"),
                name != null ? (name.length() > 10 ? name.substring(0, 7) + "..." : name) : "-",
                formattedCoordinates,
                creationDate != null ? creationDate.format(DateTimeFormatter.ofPattern("dd.MM.yy")) : "-",
                realHero != null ? (realHero ? "✓" : "✗") : "?",
                hasToothpick ? "✓" : "✗",
                formattedImpactSpeed,
                soundtrackName != null ? (soundtrackName.length() > 10 ? soundtrackName.substring(0, 7) + "..." : soundtrackName) : "-",
                formattedMinutesOfWaiting,
                weaponType != null ? weaponType.name().substring(0, Math.min(weaponType.name().length(), 3)) : "-",
                car != null ? (car.getName() != null && car.getName().length() > 8 ? car.getName().substring(0, 5) + "..." : (car.getName() != null ? car.getName() : "-")) : "-"
        };
    }

    
    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        result.append("Человек:\n");
        result.append("  ID: ").append(id != null ? id : "не установлен").append("\n");
        result.append("  Имя: ").append(name != null ? name : "не указано").append("\n");
        result.append("  Координаты: ").append(coordinates != null ? coordinates : "не указаны").append("\n");
        result.append("  Дата создания: ").append(creationDate != null ? creationDate.format(DateTimeFormatter.ofPattern("dd-MM-yyyy")) : "не указана").append("\n");
        result.append("  Настоящий герой?: ").append(realHero).append("\n");
        result.append("  Есть зубочистка?: ").append(hasToothpick).append("\n");
        result.append("  Скорость удара: ").append(Float.isNaN(impactSpeed) || Float.isInfinite(impactSpeed) ? "N/A" : (impactSpeed > -22 ? String.valueOf(impactSpeed) : "не указана (должно быть > -22)")).append("\n");
        result.append("  Название саундтрека: ").append(soundtrackName != null ? soundtrackName : "не указана").append("\n");
        result.append("  Минут ожидания: ").append(minutesOfWaiting != null ? (Float.isNaN(minutesOfWaiting) || Float.isInfinite(minutesOfWaiting) ? "N/A" : String.valueOf(minutesOfWaiting)) : "не указано").append("\n");
        result.append(" Тип оружия: ").append(weaponType != null ? weaponType.getValue() : "не указан").append("\n");
        if (car != null) {
            result.append("  Информация о машине:\n");
            result.append("    ").append(car.toString().replace("\n", "\n    ")).append("\n");
        } else {
            result.append("  Нет данных о машине.\n");
        }
        return result.toString();
    }

    
    
    @Override
    public int hashCode() {
        
        
        if (id != null) {
            return Objects.hash(id);
        }
        
        
        
        
        
        return Objects.hash(name, coordinates, creationDate, realHero, hasToothpick,
                impactSpeed, soundtrackName, minutesOfWaiting, weaponType, car);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        HumanBeing that = (HumanBeing) o;
        
        if (this.id != null && that.id != null) {
            return Objects.equals(this.id, that.id);
        }
        
        return hasToothpick == that.hasToothpick &&
                Float.compare(that.impactSpeed, impactSpeed) == 0 &&
                Objects.equals(name, that.name) &&
                Objects.equals(coordinates, that.coordinates) &&
                Objects.equals(creationDate, that.creationDate) &&
                Objects.equals(realHero, that.realHero) &&
                Objects.equals(soundtrackName, that.soundtrackName) &&
                Objects.equals(minutesOfWaiting, that.minutesOfWaiting) &&
                Objects.equals(weaponType, that.weaponType) &&
                Objects.equals(car, that.car);
    }

    
    
    @Override
    public void validate() throws IllegalArgumentException {

        if (name == null || name.isEmpty()) {
            throw new IllegalArgumentException("Имя не может быть null или пустым.");
        }
        if (coordinates == null) {
            throw new IllegalArgumentException("Координаты не могут быть null.");
        }
        coordinates.validate(); 

        if (creationDate == null) {
            throw new IllegalArgumentException("Дата создания не может быть null.");
        }
        if (realHero == null) {
            throw new IllegalArgumentException("Поле 'Настоящий герой' не может быть null.");
        }

        if (Float.isNaN(impactSpeed) || Float.isInfinite(impactSpeed) || impactSpeed <= -22) {
            throw new IllegalArgumentException("Значение поля 'Скорость удара' должно быть числом (не NaN/Infinity) и больше -22.");
        }
        if (soundtrackName == null || soundtrackName.isEmpty()) {
            throw new IllegalArgumentException("Поле 'Название саундтрека' не может быть null или пустым.");
        }

        if (minutesOfWaiting != null && (Float.isNaN(minutesOfWaiting) || Float.isInfinite(minutesOfWaiting))) {
            throw new IllegalArgumentException("Значение поля 'Минут ожидания' должно быть числом (не NaN/Infinity), если оно установлено.");
        }
    }

    
    @Override
    public int compareTo(HumanBeing o) {
        if (this.id == null && o.id == null) return 0;
        if (this.id == null) return -1;
        if (o.id == null) return 1;
        return Integer.compare(this.id, o.id);
    }

    
}