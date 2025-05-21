package mainClasses;

import java.io.Serializable;

/**
 * Перечисление, представляющее должности работников
 */
public enum WeaponType implements Serializable {
    HUMMER("Хаммер"), PISTOL("Пистолет"), SHOTGUN("Дробовик"), RIFLE("Винтовка");

    private final String value;

    /**
     * Конструктор перечисления
     * @param value строковое представление должности
     */
    WeaponType(String value) {
        this.value = value;
    }

    /**
     * Получает строковое представление должности
     * @return строковое представление должности на русском языке
     */
    public String getValue() {
        return value;
    }
}