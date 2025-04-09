package manager.validator;

/**
 * Common interface for all validators
 */
public interface Validatable<T> {
    /**
     * Validates the given value.
     * @param value the value to validate
     * @return true if the value is valid, false otherwise
     */
    boolean validate(T value);
} 