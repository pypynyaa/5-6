package manager.validator;

public class BooleanValidator implements Validatable<Boolean> {
    @Override
    public boolean validate(Boolean value) {
        return value != null;
    }
} 