package parsing;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import models.Mission;
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
            mission.validate();
            return mission;
        } catch (Exception e) {
            throw new RuntimeException("Ошибка JSON: " + e.getMessage());
        }
    }
}