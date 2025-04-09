package manager.validator;

public class SoundtrackNameValidator implements Validatable<String> {
    @Override
    public boolean validate(String value) {
        // Поле soundtrackName не может быть null согласно HumanBeing.java
        return value != null;
    }
} 