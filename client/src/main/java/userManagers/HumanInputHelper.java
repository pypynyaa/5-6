package userManagers;

import mainClasses.Car;
import mainClasses.Coordinates;
import mainClasses.HumanBeing;
import mainClasses.WeaponType;
import java.util.Scanner;


public class HumanInputHelper {
    private final Scanner scanner;

    public HumanInputHelper(Scanner scanner) {
        this.scanner = scanner;
    }


    public HumanBeing inputHuman() {
        HumanBeing humanBeing = new HumanBeing();

        humanBeing.setName(inputName());
        humanBeing.setCoordinates(inputCoordinates());
        humanBeing.setRealHero(inputRealHero());
        humanBeing.setHasToothpick(inputToothpick());
        humanBeing.setImpactSpeed(inputImpactSpeed());
        humanBeing.setSoundtrackName(inputSoundtrackName());
        humanBeing.setMinutesOfWaiting(inputMinutesOfWaiting());
        humanBeing.setWeaponType(inputWeaponType());
        humanBeing.setCar(inputCar());


        return humanBeing;
    }

    public Float inputMinutesOfWaiting() {
        while (true) {
            try {
                System.out.print("Введите кол-во минут ожидания: ");
                String x = scanner.nextLine();
                if (!x.isEmpty()) {
                    return Float.parseFloat(x);
                }
                System.out.println("Ошибка! Значения поля не может быть null!");
            } catch (NumberFormatException e) {
                System.out.println("Ошибка! Введено некорректное число!");
            }
        }
    }

    public String inputSoundtrackName() {
        while (true) {
            try {
                System.out.print("Введите название саундтрека: ");
                String name = scanner.nextLine().trim();

                if (!name.isEmpty()) {
                    return name;
                }

                System.out.println("Название не может быть пустым. Попробуйте снова.");
            } catch (IllegalStateException e) {
                System.out.println("Ошибка ввода. Сканер закрыт.");
                throw new RuntimeException(e);
            }
        }
    }

    public float inputImpactSpeed() {
        while (true) {
            try {
                System.out.print("Введите скорость ударов: ");
                float speed = Float.parseFloat(scanner.nextLine());
                if (speed > -22) {
                    return speed;
                }
                System.out.println("Ошибка! Значения поля ImpactSpeed должно быть больше -22!");
            } catch (NumberFormatException e) {
                System.out.println("Ошибка! Введено некорректное число!");
            }
        }
    }

    public Boolean inputToothpick() {
        while (true) {
            System.out.println("У человека есть зубочистка?: y/n");
            String input = scanner.nextLine().trim().toLowerCase();
            if (input.equals("y")) {
                return true;
            } else if (input.equals("n")) {
                return false;
            } else {
                System.out.println("Введите 'y' - если да, 'n' - если нет");
            }
        }
    }

    public Boolean inputRealHero() {
        while (true) {
            System.out.println("Человек является настоящим героем?: y/n");
            String realHero = scanner.nextLine().trim().toLowerCase();
            if (realHero.equalsIgnoreCase("y")) {
                return true;
            } else if (realHero.equalsIgnoreCase("n")) {
                return false;
            } else {
                System.out.println("Введите 'y' - если да, 'n' - если нет");
                
            }
        }
    }

    private String inputName() { 
        while (true) {
            System.out.print("Введите имя: ");
            String name = scanner.nextLine().trim(); 
            if (name.isEmpty()) {
                System.out.println("Ошибка: имя не может быть пустым.");
                continue;
            }

            if (name.matches("\\d+")) {
                System.out.println("Ошибка: имя не может состоять из цифр.");
                continue;
            }
            return name;
        }
    }

    private Coordinates inputCoordinates() { 
        return new Coordinates(inputXCoordinate(), inputYCoordinate());
    }

    private Float inputXCoordinate() { 
        while (true) {
            try {
                System.out.print("Введите координату x: ");
                String x = scanner.nextLine(); 
                if (!x.isEmpty() && Float.parseFloat(x) > -99) {
                    return Float.parseFloat(x);
                }
                System.out.println("Ошибка! Значения поля Х не может быть null и должно быть больше -99!");
            } catch (NumberFormatException e) {
                System.out.println("Ошибка! Введено некорректное число!");
            }
        }
    }

    private long inputYCoordinate() { 
        while (true) {
            try {
                System.out.print("Введите координату y: ");
                String y = scanner.nextLine();

                if (!y.isEmpty()) {
                    return Long.parseLong(y);
                }
            } catch (NumberFormatException e) {
                System.out.println("Ошибка! Введено некорректное число!");
            }
        }
    }


    private WeaponType inputWeaponType() {
        while (true) {
            try {
                System.out.print("Введите тип оружия (хаммер, пистолет, дробовик, винтовка): ");
                String input = scanner.nextLine().trim().toLowerCase();

                for (WeaponType weaponType : WeaponType.values()) {
                    if (weaponType.getValue().equalsIgnoreCase(input)) {
                        return weaponType;
                    }
                }

                System.out.println("Ошибка: некорректная должность. Доступные значения: хаммер, пистолет, дробовик, винтовка.");
            } catch (IllegalArgumentException e) {
                System.out.println("Ошибка: некорректная должность. Доступные значения: хаммер, пистолет, дробовик, винтовка.");
            }
        }
    }

    public Car inputCar() {
        while (true) {
            System.out.print("Введите название машины: ");
            String name = scanner.nextLine().trim();
            if (name.isEmpty() || name == null) {
                System.out.println("Ошибка! Не указано название машины!");
                continue;
            }
            return new Car(name);
        }
    }
}