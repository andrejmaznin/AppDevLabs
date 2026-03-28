package parsing;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import models.Mission;

public class XmlMissionParser implements MissionParser {
    private final XmlMapper xmlMapper;

    public XmlMissionParser() {
        this.xmlMapper = new XmlMapper();
        this.xmlMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

    @Override
    public Mission parse(String xmlData) {
        try {
            return xmlMapper.readValue(xmlData, Mission.class);
        } catch (Exception e) {
            throw new RuntimeException("Ошибка XML: " + e.getMessage());
        }
    }
}