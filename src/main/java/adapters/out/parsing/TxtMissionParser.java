package adapters.out.parsing;

import domain.builder.MissionBuildDirector;
import domain.builder.MissionData;
import domain.builder.StandardMissionBuilder;
import domain.models.EnvironmentConditions;
import domain.models.Mission;
import domain.models.Sorcerer;
import domain.models.Technique;

public class TxtMissionParser implements MissionParser {

    @Override
    public Mission parse(String data) {
        MissionData missionData = new MissionData();
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
                    missionData.sorcerers.add(currentSorcerer);
                } else if (currentSection.equals("TECHNIQUE")) {
                    currentTechnique = new Technique();
                    missionData.techniques.add(currentTechnique);
                } else if (currentSection.equals("ENVIRONMENT")) {
                    env = new EnvironmentConditions();
                    missionData.environmentConditions = env;
                }
                continue;
            }

            String[] parts = line.split("=", 2);
            if (parts.length < 2) continue;
            String key = parts[0].trim();
            String value = parts[1].trim();

            switch (currentSection) {
                case "MISSION":
                    if (key.equals("missionId")) missionData.missionId = value;
                    else if (key.equals("date")) missionData.date = value;
                    else if (key.equals("location")) missionData.location = value;
                    else if (key.equals("outcome")) missionData.outcome = value;
                    else if (key.equals("damageCost")) missionData.damageCost = Long.parseLong(value);
                    break;
                case "CURSE":
                    if (key.equals("name")) missionData.curseName = value;
                    else if (key.equals("threatLevel")) missionData.curseThreatLevel = value;
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

        MissionBuildDirector director = new MissionBuildDirector(new StandardMissionBuilder());
        return director.construct(missionData);
    }
}