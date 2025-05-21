package manager.validator;

import model.Coordinates;

public class CoordinatesValidator implements Validatable<Coordinates> {
    // Максимальное значение X согласно старой реализации в Coordinates.java
    private static final double MAX_X = 25;
    // Минимальное значение Y согласно старой реализации в Coordinates.java
    private static final int MIN_Y_EXCLUSIVE = -337;

    @Override
    public boolean validate(Coordinates value) {
        if (value == null) {
            return false;
        }
        if (value.getX() > MAX_X) {
            // System.err.println("Координата X не может быть больше " + MAX_X);
            return false;
        }
        if (value.getY() == null) {
            // System.err.println("Координата Y не может быть null");
            return false;
        }
        if (value.getY() <= MIN_Y_EXCLUSIVE) {
            // System.err.println("Координата Y должна быть больше " + MIN_Y_EXCLUSIVE);
            return false;
        }
        return true;
    }
} 