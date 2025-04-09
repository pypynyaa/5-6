package manager.validator;

import model.Car;

public class CarValidator implements Validatable<Car> {
    @Override
    public boolean validate(Car value) {
        // Объект Car может быть null
        if (value == null) {
            return true; 
        }
        // Если Car не null, его имя не может быть null (согласно Car.java)
        return value.getName() != null;
    }
} 