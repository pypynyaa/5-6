package manager.validator;

public class IdValidator implements Validatable<Long> {
    @Override
    public boolean validate(Long value) {
        return value != null && value > 0;
    }
} 