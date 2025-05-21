package manager.validator;

import model.WeaponType;

public class WeaponTypeValidator implements Validatable<WeaponType> {
    @Override
    public boolean validate(WeaponType value) {
        // Поле не может быть null согласно HumanBeing.java
        return value != null;
    }
} 