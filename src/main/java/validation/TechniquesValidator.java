package validation;

import models.Mission;
import models.Technique;

import java.util.List;

public class TechniquesValidator extends AbstractValidator<Mission> {
    @Override
    public void validate(Mission mission) throws ValidationException {
        List<Technique> list = mission.getTechniques();
        if (list == null || list.isEmpty()) {
            throw new ValidationException("Список примененных техник не может быть пустым");
        }
        for (Technique t : list) {
            if (t.getName() == null || t.getName().trim().isEmpty()) {
                throw new ValidationException("Название техники не может быть пустым");
            }
            if (t.getType() == null || t.getType().trim().isEmpty()) {
                throw new ValidationException("Тип техники не может быть пустым");
            }
            if (t.getOwner() == null || t.getOwner().trim().isEmpty()) {
                throw new ValidationException("Владелец техники не может быть пустым");
            }
            if (t.getDamage() < 0) {
                throw new ValidationException("Урон не может быть отрицательным числом");
            }
        }
        callNext(mission);
    }
}
