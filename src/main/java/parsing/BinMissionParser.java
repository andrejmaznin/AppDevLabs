package parsing;

import builder.StandardMissionBuilder;
import models.*;

import java.util.ArrayList;
import java.util.List;

public class BinMissionParser implements MissionParser {

    @Override
    public Mission parse(String data) {
        StandardMissionBuilder builder = new StandardMissionBuilder();

        if (data.startsWith("\uFEFF")) {
            data = data.substring(1);
        }

        String[] lines = data.split("\\r?\\n");

        CivilianImpact civilianImpact = new CivilianImpact();
        EnemyActivity enemyActivity = new EnemyActivity();
        List<OperationEvent> timeline = new ArrayList<>();
        List<String> attackPatterns = new ArrayList<>();

        for (String line : lines) {
            if (line.trim().isEmpty()) continue;

            String[] parts = line.split("\\|");
            String command = parts[0].trim();

            try {
                switch (command) {
                    case "MISSION_CREATED":
                        builder.setMissionId(parts[1].trim());
                        builder.setDate(parts[2].trim());
                        builder.setLocation(parts[3].trim());
                        break;
                    case "CURSE_DETECTED":
                        builder.setCurseDetails(parts[1].trim(), parts[2].trim());
                        break;
                    case "SORCERER_ASSIGNED":
                        Sorcerer s = new Sorcerer();
                        s.setName(parts[1].trim());
                        s.setRank(parts[2].trim());
                        builder.addSorcerer(s);
                        break;
                    case "TECHNIQUE_USED":
                        Technique t = new Technique();
                        t.setName(parts[1].trim());
                        t.setType(parts[2].trim());
                        t.setOwner(parts[3].trim());
                        t.setDamage(Integer.parseInt(parts[4].trim()));
                        builder.addTechnique(t);
                        break;
                    case "TIMELINE_EVENT":
                        OperationEvent ev = new OperationEvent();
                        ev.setTimestamp(parts[1].trim());
                        ev.setType(parts[2].trim());
                        ev.setDescription(parts[3].trim());
                        timeline.add(ev);
                        break;
                    case "ENEMY_ACTION":
                        if (enemyActivity.getBehaviorType() == null) {
                            enemyActivity.setBehaviorType(parts[1].trim());
                        }
                        attackPatterns.add(parts[2].trim());
                        break;
                    case "CIVILIAN_IMPACT":
                        for (int i = 1; i < parts.length; i++) {
                            String[] kv = parts[i].split("=");
                            if (kv.length == 2) {
                                String key = kv[0].trim();
                                int val = Integer.parseInt(kv[1].trim());
                                if (key.equals("evacuated")) civilianImpact.setEvacuated(val);
                                else if (key.equals("injured")) civilianImpact.setInjured(val);
                                else if (key.equals("missing")) civilianImpact.setMissing(val);
                            }
                        }
                        builder.setCivilianImpact(civilianImpact);
                        break;
                    case "MISSION_RESULT":
                        builder.setOutcome(parts[1].trim());
                        if (parts.length > 2) {
                            String[] dmg = parts[2].split("=");
                            if (dmg.length == 2 && dmg[0].trim().equals("damageCost")) {
                                builder.setDamageCost(Long.parseLong(dmg[1].trim()));
                            }
                        }
                        break;
                }
            } catch (Exception e) {
                throw new RuntimeException("Ошибка при парсинге строки: [" + line + "]", e);
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