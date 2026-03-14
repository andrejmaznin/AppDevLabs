package parsing;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import models.Mission;
import models.Sorcerer;
import models.Technique;

public class XmlMissionParser implements MissionParser {
    private final XmlMapper xmlMapper;

    public XmlMissionParser() {
        this.xmlMapper = new XmlMapper();
        this.xmlMapper.registerModule(new JavaTimeModule());
        this.xmlMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

    @Override
    public Mission parse(String xmlData) {
        try {
            Mission mission = xmlMapper.readValue(xmlData, Mission.class);
            linkSorcerers(mission);
            mission.validate();
            return mission;
        } catch (Exception e) {
            throw new RuntimeException("Ошибка XML: " + e.getMessage());
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