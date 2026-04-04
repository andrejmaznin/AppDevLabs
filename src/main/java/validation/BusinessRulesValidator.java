package validation;

import models.Mission;
import models.Sorcerer;
import models.Technique;

import java.util.List;

public class BusinessRulesValidator extends AbstractValidator<Mission> {
    @Override
    public void validate(Mission mission) throws ValidationException {
        if (mission == null) throw new ValidationException("Mission is null");

        if (mission.getDamageCost() < 0) {
            throw new ValidationException("Ущерб не может быть отрицательным");
        }

        List<Sorcerer> sorcerers = mission.getSorcerers();

        List<Technique> techniques = mission.getTechniques();
        
        if (techniques != null) {
            for (Technique currentTech : techniques) {
                String techOwner = currentTech != null ? currentTech.getOwner() : null;
                boolean ownerFound = false;
                if (sorcerers != null && techOwner != null) {
                    for (Sorcerer s : sorcerers) {
                        if (s != null && s.getName() != null) {
                            if (s.getName().trim().equals(techOwner.trim())) {
                                ownerFound = true;
                                break;
                            }
                        }
                    }
                }
                if (!ownerFound) {
                    throw new ValidationException("Техника '" + (currentTech != null ? currentTech.getName() : "<null>") +
                        "' принадлежит магу '" + techOwner + "', которого нет в списке участников!");
                }
            }
        }
        callNext(mission);
    }
}
