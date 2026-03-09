package parsing;

import models.Curse;
import models.Mission;
import models.Sorcerer;
import models.Technique;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TxtMissionParser implements MissionParser {

    @Override
    public Mission parse(String data) {
        Mission mission = new Mission();
        mission.setCurse(new Curse());
        mission.setSorcerers(new ArrayList<>());
        mission.setTechniques(new ArrayList<>());

        String[] lines = data.split("\\r?\\n");

        for (String line : lines) {
            if (line.trim().isEmpty() || !line.contains(": ")) continue;

            String[] parts = line.split(": ", 2);
            String key = parts[0].trim();
            String value = parts[1].trim();

            if (key.equals("missionId")) mission.setMissionId(value);
            else if (key.equals("date")) mission.setDate(value);
            else if (key.equals("location")) mission.setLocation(value);
            else if (key.equals("outcome")) mission.setOutcome(value);
            else if (key.equals("damageCost")) mission.setDamageCost(Long.parseLong(value));
            else if (key.equals("note")) mission.setComment(value);
            else if (key.startsWith("curse.")) {
                if (key.endsWith("name")) mission.getCurse().setName(value);
                else if (key.endsWith("threatLevel")) mission.getCurse().setThreatLevel(value);
            } else if (key.startsWith("sorcerer[")) {
                handleSorcerer(mission, key, value);
            } else if (key.startsWith("technique[")) {
                handleTechnique(mission, key, value);
            }
        }
        return mission;
    }

    private void handleSorcerer(Mission mission, String key, String value) {
        int index = extractIndex(key);
        ensureSize(mission.getSorcerers(), index, Sorcerer.class);
        Sorcerer s = mission.getSorcerers().get(index);

        if (key.endsWith(".name")) s.setName(value);
        else if (key.endsWith(".rank")) s.setRank(value);
    }

    private void handleTechnique(Mission mission, String key, String value) {
        int index = extractIndex(key);
        ensureSize(mission.getTechniques(), index, Technique.class);
        Technique t = mission.getTechniques().get(index);

        if (key.endsWith(".name")) t.setName(value);
        else if (key.endsWith(".type")) t.setType(value);
        else if (key.endsWith(".owner")) t.setOwner(value);
        else if (key.endsWith(".damage")) t.setDamage(Integer.parseInt(value));
    }

    private int extractIndex(String key) {
        Matcher m = Pattern.compile("\\[(\\d+)\\]").matcher(key);
        return m.find() ? Integer.parseInt(m.group(1)) : 0;
    }

    private <T> void ensureSize(java.util.List<T> list, int index, Class<T> clazz) {
        while (list.size() <= index) {
            try {
                list.add(clazz.getDeclaredConstructor().newInstance());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}