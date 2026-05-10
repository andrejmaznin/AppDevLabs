package domain.builder;

import domain.models.Mission;
import domain.models.Sorcerer;
import domain.models.Technique;

public class MissionBuildDirector {
    private final MissionBuilder builder;

    public MissionBuildDirector(MissionBuilder builder) {
        this.builder = builder;
    }

    public Mission construct(MissionData data) {
        if (data.missionId != null) builder.setMissionId(data.missionId);
        if (data.date != null) builder.setDate(data.date);
        if (data.location != null) builder.setLocation(data.location);

        if (data.curse != null) {
            builder.setCurse(data.curse);
        } else if (data.curseName != null || data.curseThreatLevel != null) {
            builder.setCurseDetails(data.curseName, data.curseThreatLevel);
        }

        if (data.environmentConditions != null) {
            builder.setEnvironmentConditions(data.environmentConditions);
        }

        if (data.sorcerers != null && !data.sorcerers.isEmpty()) {
            for (Sorcerer s : data.sorcerers) {
                builder.addSorcerer(s);
            }
        }
        
        if (data.techniques != null && !data.techniques.isEmpty()) {
            for (Technique t : data.techniques) {
                builder.addTechnique(t);
            }
        }

        if (data.enemyActivity != null) builder.setEnemyActivity(data.enemyActivity);
        if (data.operationTimeline != null && !data.operationTimeline.isEmpty()) {
            builder.setOperationTimeline(data.operationTimeline);
        }

        if (data.civilianImpact != null) builder.setCivilianImpact(data.civilianImpact);
        if (data.economicAssessment != null) builder.setEconomicAssessment(data.economicAssessment);
        if (data.outcome != null) builder.setOutcome(data.outcome);
        if (data.damageCost > 0) builder.setDamageCost(data.damageCost);
        if (data.comment != null) builder.setComment(data.comment);

        return builder.build();
    }
}
