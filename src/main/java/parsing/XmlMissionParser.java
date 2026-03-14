package parsing;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import models.Mission;

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
            mission.validate();
            return mission;
        } catch (Exception e) {
            throw new RuntimeException("Ошибка XML: " + e.getMessage());
        }
    }
}