package parsing;

import builder.StandardMissionBuilder;
import models.Mission;
import models.Sorcerer;
import models.Technique;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TxtMissionParser implements MissionParser {

    @Override
    public Mission parse(String data) {
        StandardMissionBuilder builder = new StandardMissionBuilder();

        String[] lines = data.split("\\r?\\n");
        Map<Integer, Sorcerer> sorcererMap = new HashMap<>();
        Map<Integer, Technique> techniqueMap = new HashMap<>();

        for (String line : lines) {
            if (line.trim().isEmpty() || !line.contains(": ")) continue;

            String[] parts = line.split(": ", 2);
            String key = parts[0].trim();
            String value = parts[1].trim();

            if (key.equals("missionId")) builder.setMissionId(value);
            else if (key.equals("date")) builder.setDate(value);
            else if (key.equals("location")) builder.setLocation(value);
            else if (key.equals("outcome")) builder.setOutcome(value);
            else if (key.equals("damageCost")) builder.setDamageCost(Long.parseLong(value));
            else if (key.equals("note")) builder.setComment(value);
            else if (key.startsWith("curse.")) {
                if (key.endsWith("name")) builder.setCurseDetails(value, null);
                else if (key.endsWith("threatLevel")) builder.setCurseDetails(null, value);
            } else if (key.startsWith("sorcerer[")) {
                handleSorcerer(sorcererMap, key, value);
            } else if (key.startsWith("technique[")) {
                handleTechnique(techniqueMap, key, value);
            }
        }

        sorcererMap.values().forEach(builder::addSorcerer);
        techniqueMap.values().forEach(builder::addTechnique);

        return builder.build();
    }

    private void handleSorcerer(Map<Integer, Sorcerer> map, String key, String value) {
        int index = extractIndex(key);
        Sorcerer s = map.computeIfAbsent(index, k -> new Sorcerer());
        if (key.endsWith(".name")) s.setName(value);
        else if (key.endsWith(".rank")) s.setRank(value);
    }

    private void handleTechnique(Map<Integer, Technique> map, String key, String value) {
        int index = extractIndex(key);
        Technique t = map.computeIfAbsent(index, k -> new Technique());
        if (key.endsWith(".name")) t.setName(value);
        else if (key.endsWith(".type")) t.setType(value);
        else if (key.endsWith(".owner")) t.setOwner(value);
        else if (key.endsWith(".damage")) t.setDamage(Integer.parseInt(value));
    }

    private int extractIndex(String key) {
        Matcher m = Pattern.compile("\\[(\\d+)\\]").matcher(key);
        return m.find() ? Integer.parseInt(m.group(1)) : 0;
    }
}