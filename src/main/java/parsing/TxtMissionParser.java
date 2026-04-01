package parsing;

import builder.StandardMissionBuilder;
import models.EnvironmentConditions;
import models.Mission;
import models.Sorcerer;
import models.Technique;

public class TxtMissionParser implements MissionParser {

    @Override
    public Mission parse(String data) {
        StandardMissionBuilder builder = new StandardMissionBuilder();
        String[] lines = data.split("\\r?\\n");

        String currentSection = "";
        Sorcerer currentSorcerer = null;
        Technique currentTechnique = null;
        EnvironmentConditions env = null;

        for (String line : lines) {
            line = line.trim();
            if (line.isEmpty()) continue;

            if (line.startsWith("[") && line.endsWith("]")) {
                currentSection = line.substring(1, line.length() - 1);

                if (currentSection.equals("SORCERER")) {
                    currentSorcerer = new Sorcerer();
                    builder.addSorcerer(currentSorcerer);
                } else if (currentSection.equals("TECHNIQUE")) {
                    currentTechnique = new Technique();
                    builder.addTechnique(currentTechnique);
                } else if (currentSection.equals("ENVIRONMENT")) {
                    env = new EnvironmentConditions();
                    builder.setEnvironmentConditions(env);
                }
                continue;
            }

            String[] parts = line.split("=", 2);
            if (parts.length < 2) continue;
            String key = parts[0].trim();
            String value = parts[1].trim();

            switch (currentSection) {
                case "MISSION":
                    if (key.equals("missionId")) builder.setMissionId(value);
                    else if (key.equals("date")) builder.setDate(value);
                    else if (key.equals("location")) builder.setLocation(value);
                    else if (key.equals("outcome")) builder.setOutcome(value);
                    else if (key.equals("damageCost")) builder.setDamageCost(Long.parseLong(value));
                    break;
                case "CURSE":
                    if (key.equals("name")) builder.setCurseDetails(value, null);
                    else if (key.equals("threatLevel")) builder.setCurseDetails(null, value);
                    break;
                case "SORCERER":
                    if (currentSorcerer != null) {
                        if (key.equals("name")) currentSorcerer.setName(value);
                        else if (key.equals("rank")) currentSorcerer.setRank(value);
                    }
                    break;
                case "TECHNIQUE":
                    if (currentTechnique != null) {
                        if (key.equals("name")) currentTechnique.setName(value);
                        else if (key.equals("type")) currentTechnique.setType(value);
                        else if (key.equals("owner")) currentTechnique.setOwner(value);
                        else if (key.equals("damage")) currentTechnique.setDamage(Integer.parseInt(value));
                    }
                    break;
                case "ENVIRONMENT":
                    if (env != null) {
                        if (key.equals("weather")) env.setWeather(value);
                        else if (key.equals("timeOfDay")) env.setTimeOfDay(value);
                        else if (key.equals("visibility")) env.setVisibility(value);
                        else if (key.equals("cursedEnergyDensity"))
                            env.setCursedEnergyDensity(Double.parseDouble(value));
                    }
                    break;
            }
        }

        return builder.build();
    }
}