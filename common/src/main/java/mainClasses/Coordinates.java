package mainClasses;

import interfaces.Validatable;

import java.io.Serializable;
import java.util.Objects; 


public class Coordinates implements Validatable, Serializable {
    private Float x; 
    private long y;

    
    public Coordinates() {}

    
    public Coordinates(Float x, long y) {
        this.x = x;
        this.y = y;
    }

    
    public Float getX() { 
        return x;
    }

    
    public long getY() {
        return y;
    }

    
    public void setX(Float x) {
        this.x = x;
    }

    
    public void setY(long y) {
        this.y = y;
    }

    
    @Override
    public String toString() {
        return "(x, y) : (" + x + ", " + y + ")";
    }

    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Coordinates that = (Coordinates) o;

        return y == that.y && Objects.equals(x, that.x);
    }

    
    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }

    @Override
    public void validate() throws IllegalArgumentException {
        if (x == null) {
            throw new IllegalArgumentException("Координата X не может быть null");
        }
        
        if (x <= -99) { 
            throw new IllegalArgumentException("Координата X должна быть больше -99");
        }
        
        if (y <= -415) {
            throw new IllegalArgumentException("Координата Y должна быть больше -415");
        }
    }
}