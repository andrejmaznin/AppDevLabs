package parsing;

import builder.StandardMissionBuilder;
import models.*;

import java.util.ArrayList;
import java.util.List;

public class BinMissionParser implements MissionParser {

    @Override
    public Mission parse(String data) {
        StandardMissionBuilder builder = new StandardMissionBuilder();
        String[] lines = data.split("\\r?\\n");

        CivilianImpact civilianImpact = new CivilianImpact();
        EnemyActivity enemyActivity = new EnemyActivity();
        List<OperationEvent> timeline = new ArrayList<>();
        List<String> attackPatterns = new ArrayList<>();

        for (String line : lines) {
            if (line.trim().isEmpty()) continue;
            String[] parts = line.split("\\|");
            String command = parts[0];

            switch (command) {
                case "MISSION_CREATED":
                    builder.setMissionId(parts[1]);
                    builder.setDate(parts[2]);
                    builder.setLocation(parts[3]);
                    break;
                case "CURSE_DETECTED":
                    builder.setCurseDetails(parts[1], parts[2]);
                    break;
                case "SORCERER_ASSIGNED":
                    Sorcerer s = new Sorcerer();
                    s.setName(parts[1]);
                    s.setRank(parts[2]);
                    builder.addSorcerer(s);
                    break;
                case "TECHNIQUE_USED":
                    Technique t = new Technique();
                    t.setName(parts[1]);
                    t.setType(parts[2]);
                    t.setOwner(parts[3]);
                    t.setDamage(Integer.parseInt(parts[4]));
                    builder.addTechnique(t);
                    break;
                case "TIMELINE_EVENT":
                    OperationEvent ev = new OperationEvent();
                    ev.setTimestamp(parts[1]);
                    ev.setType(parts[2]);
                    ev.setDescription(parts[3]);
                    timeline.add(ev);
                    break;
                case "ENEMY_ACTION":
                    if (enemyActivity.getBehaviorType() == null) {
                        enemyActivity.setBehaviorType(parts[1]);
                    }
                    attackPatterns.add(parts[2]);
                    break;
                case "CIVILIAN_IMPACT":
                    for (int i = 1; i < parts.length; i++) {
                        String[] kv = parts[i].split("=");
                        if (kv[0].equals("evacuated")) civilianImpact.setEvacuated(Integer.parseInt(kv[1]));
                        else if (kv[0].equals("injured")) civilianImpact.setInjured(Integer.parseInt(kv[1]));
                        else if (kv[0].equals("missing")) civilianImpact.setMissing(Integer.parseInt(kv[1]));
                    }
                    builder.setCivilianImpact(civilianImpact);
                    break;
                case "MISSION_RESULT":
                    builder.setOutcome(parts[1]);
                    String[] dmg = parts[2].split("=");
                    builder.setDamageCost(Long.parseLong(dmg[1]));
                    break;
            }
        }

        if (!attackPatterns.isEmpty()) {
            enemyActivity.setAttackPatterns(attackPatterns);
            builder.setEnemyActivity(enemyActivity);
        }
        if (!timeline.isEmpty()) {
            builder.setOperationTimeline(timeline);
        }

        return builder.build();
    }
}