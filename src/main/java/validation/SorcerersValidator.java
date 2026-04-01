package validation;

import models.Mission;
import models.Sorcerer;

import java.util.List;

public class SorcerersValidator extends AbstractValidator<Mission> {
    @Override
    public void validate(Mission mission) throws ValidationException {
        List<Sorcerer> list = mission.getSorcerers();
        if (list == null || list.isEmpty()) {
            throw new ValidationException("В миссии должен участвовать хотя бы один маг");
        }
        for (Sorcerer s : list) {
            if (s.getName() == null || s.getName().trim().isEmpty()) {
                throw new ValidationException("Имя мага не может быть пустым");
            }
            if (s.getRank() == null || s.getRank().trim().isEmpty()) {
                throw new ValidationException("Ранг мага не может быть пустым");
            }
        }
        callNext(mission);
    }
}
