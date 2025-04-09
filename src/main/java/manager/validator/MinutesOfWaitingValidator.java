package manager.validator;

public class MinutesOfWaitingValidator implements Validatable<Float> {
    @Override
    public boolean validate(Float value) {
        // Поле не может быть null (исходя из использования Float, а не float)
        // Конкретных ограничений в старом коде не было, кроме проверки на NumberFormatException
        return value != null;
    }
} 