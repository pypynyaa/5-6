package mainClasses;

import interfaces.Validatable;

import java.io.Serializable;
import java.time.format.DateTimeFormatter;
import java.util.Date;

/**
 * Класс, представляющий информацию о человеке
 * Содержит основные характеристики: дату рождения, рост и вес
 */
public class Car implements Validatable , Serializable {
    private String name; //Поле не может быть null


    /**
     * Конструктор без параметров
     */
    public Car() {}


    public Car(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }


    @Override
    public String toString() {
        String result = "";
        if (name != null) {
            result += "Название машины : " + name + "\n";
        }
        return result;
    }

    /**
     * Вычисляет хеш-код объекта
     * @return хеш-код
     */
    @Override
    public int hashCode() {
        return name.hashCode();
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
        Car car = (Car) o;
        return name.equals(car.name);
    }

    @Override
    public void validate() throws IllegalArgumentException {
        if (name == null) {
            throw new IllegalArgumentException("Название не может быть null");
        }

    }
}