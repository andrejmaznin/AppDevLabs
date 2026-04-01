package validation;

import models.Mission;

public class CurseValidator extends AbstractValidator<Mission> {
    @Override
    public void validate(Mission mission) throws ValidationException {
        if (mission.getCurse() == null) {
            throw new ValidationException("Данные о проклятии отсутствуют");
        }
        if (mission.getCurse().getName() == null || mission.getCurse().getName().trim().isEmpty()) {
            throw new ValidationException("Название проклятия не может быть пустым");
        }
        if (mission.getCurse().getThreatLevel() == null || mission.getCurse().getThreatLevel().trim().isEmpty()) {
            throw new ValidationException("Уровень угрозы проклятия не может быть пустым");
        }
        callNext(mission);
    }
}
