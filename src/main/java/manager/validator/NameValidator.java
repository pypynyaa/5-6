package manager.validator;

public class NameValidator implements Validatable<String> {
    @Override
    public boolean validate(String value) {
        return value != null && !value.isEmpty();
    }
} 