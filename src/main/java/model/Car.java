package model;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import java.util.Objects;

/**
 * Класс, представляющий автомобиль
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class Car implements Comparable<Car> {
    @XmlElement
    private String name; //Поле не может быть null

    public Car() {
        // Конструктор по умолчанию для JAXB
    }

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
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Car car = (Car) o;
        return Objects.equals(name, car.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }

    @Override
    public String toString() {
        return "Car{" +
                "name='" + name + '\'' +
                '}';
    }

    @Override
    public int compareTo(Car o) {
        return this.name.compareTo(o.name);
    }
} 