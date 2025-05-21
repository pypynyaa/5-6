package manager.validator;

public class ImpactSpeedValidator implements Validatable<Double> {
    // Минимальное значение согласно старой реализации в HumanBeing.java
    private static final double MIN_IMPACT_SPEED_EXCLUSIVE = -22.0;

    @Override
    public boolean validate(Double value) {
        return value != null && value > MIN_IMPACT_SPEED_EXCLUSIVE;
    }
} 