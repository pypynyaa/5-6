package mainClasses;

import java.io.Serializable;


public enum WeaponType implements Serializable {
    HUMMER("Хаммер"), PISTOL("Пистолет"), SHOTGUN("Дробовик"), RIFLE("Винтовка");

    private final String value;

    
    WeaponType(String value) {
        this.value = value;
    }

    
    public String getValue() {
        return value;
    }
}