package parsing;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import models.Mission;
import models.Sorcerer;
import models.Technique;
import parsing.MissionParser;

public class JsonMissionParser implements MissionParser {
    private final ObjectMapper objectMapper;

    public JsonMissionParser() {
        this.objectMapper = new ObjectMapper();
        this.objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

    @Override
    public Mission parse(String jsonData) {
        try {
            Mission mission = objectMapper.readValue(jsonData, Mission.class);
            linkSorcerers(mission);
            mission.validate();
            return mission;
        } catch (Exception e) {
            throw new RuntimeException("Ошибка JSON: " + e.getMessage());
        }
    }

    private void linkSorcerers(Mission mission) {
        if (mission.getTechniques() != null && mission.getSorcerers() != null) {
            for (Technique t : mission.getTechniques()) {
                String ownerName = t.getOwnerName();
                if (ownerName != null) {
                    boolean found = false;
                    for (Sorcerer s : mission.getSorcerers()) {
                        if (s.getName() != null && s.getName().trim().equals(ownerName.trim())) {
                            t.setOwner(s);
                            found = true;
                            break;
                        }
                    }
                    if (!found) {
                        throw new IllegalArgumentException("Техника '" + t.getName() +
                            "' принадлежит магу '" + ownerName + "', которого нет в списке участников!");
                    }
                }
            }
        }
    }
}