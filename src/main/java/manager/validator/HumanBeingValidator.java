package manager.validator;

import model.HumanBeing;

public class HumanBeingValidator implements Validatable<HumanBeing> {

    private final NameValidator nameValidator = new NameValidator();
    private final CoordinatesValidator coordinatesValidator = new CoordinatesValidator();
    private final BooleanValidator booleanValidator = new BooleanValidator(); // Для realHero
    private final ImpactSpeedValidator impactSpeedValidator = new ImpactSpeedValidator();
    private final SoundtrackNameValidator soundtrackNameValidator = new SoundtrackNameValidator();
    private final MinutesOfWaitingValidator minutesOfWaitingValidator = new MinutesOfWaitingValidator(); // Для minutesOfWaiting
    private final WeaponTypeValidator weaponTypeValidator = new WeaponTypeValidator();
    private final CarValidator carValidator = new CarValidator();
    // Поле creationDate валидируется проверкой на null
    // Поле hasToothpick (boolean примитив) не требует валидации

    @Override
    public boolean validate(HumanBeing humanBeing) {
        if (humanBeing == null) {
            System.err.println("Ошибка: объект HumanBeing не может быть null");
            return false;
        }

        boolean isValid = true;

        if (!nameValidator.validate(humanBeing.getName())) {
            isValid = false;
        }

        if (!coordinatesValidator.validate(humanBeing.getCoordinates())) {
            isValid = false;
        }

        if (humanBeing.getCreationDate() == null) { // Проверка на null для Date
            isValid = false;
        }

        if (!booleanValidator.validate(humanBeing.isRealHero())) {
            isValid = false;
        }

        if (!booleanValidator.validate(humanBeing.isHasToothpick())) {
            isValid = false;
        }

        if (!impactSpeedValidator.validate(humanBeing.getImpactSpeed())) {
            isValid = false;
        }

        if (!soundtrackNameValidator.validate(humanBeing.getSoundtrackName())) {
            isValid = false;
        }

        if (!minutesOfWaitingValidator.validate(humanBeing.getMinutesOfWaiting())) {
            isValid = false;
        }

        if (!weaponTypeValidator.validate(humanBeing.getWeaponType())) {
            isValid = false;
        }

        if (!carValidator.validate(humanBeing.getCar())) {
            isValid = false;
        }

        return isValid;
    }
} 