package parsing;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.dataformat.yaml.YAMLMapper;
import models.Mission;

public class YamlMissionParser implements MissionParser {
    private final YAMLMapper yamlMapper;

    public YamlMissionParser() {
        this.yamlMapper = new YAMLMapper();
        this.yamlMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

    @Override
    public Mission parse(String yamlData) {
        try {
            return yamlMapper.readValue(yamlData, Mission.class);
        } catch (Exception e) {
            throw new RuntimeException("Ошибка YAML: " + e.getMessage());
        }
    }
}