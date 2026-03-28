package validation;

import models.Mission;

public class MissionBasicValidator extends AbstractValidator<Mission> {
    @Override
    public void validate(Mission mission) throws ValidationException {
        if (mission == null) throw new ValidationException("Mission is null");
        if (mission.getMissionId() == null || mission.getMissionId().trim().isEmpty())
            throw new ValidationException("ID миссии не может быть пустым");
        if (mission.getDate() == null || mission.getDate().trim().isEmpty())
            throw new ValidationException("Дата не может быть пустой");
        if (mission.getLocation() == null || mission.getLocation().trim().isEmpty())
            throw new ValidationException("Локация не может быть пустой");
        if (mission.getOutcome() == null || mission.getOutcome().trim().isEmpty())
            throw new ValidationException("Результат миссии не может быть пустым");

        callNext(mission);
    }
}
