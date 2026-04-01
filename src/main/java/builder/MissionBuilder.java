package builder;

import models.Curse;
import models.Mission;
import models.Sorcerer;
import models.Technique;

import java.util.List;

public interface MissionBuilder {
    MissionBuilder setMissionId(String id);

    MissionBuilder setDate(String date);

    MissionBuilder setLocation(String location);

    MissionBuilder setOutcome(String outcome);

    MissionBuilder setDamageCost(long cost);

    MissionBuilder setComment(String comment);

    MissionBuilder setCurse(Curse curse);

    MissionBuilder setSorcerers(List<Sorcerer> sorcerers);

    MissionBuilder setTechniques(List<Technique> techniques);

    MissionBuilder setCurseDetails(String name, String threatLevel);

    MissionBuilder addSorcerer(Sorcerer sorcerer);

    MissionBuilder addTechnique(Technique technique);

    MissionBuilder setEconomicAssessment(models.EconomicAssessment economicAssessment);

    MissionBuilder setCivilianImpact(models.CivilianImpact civilianImpact);

    MissionBuilder setEnemyActivity(models.EnemyActivity enemyActivity);

    MissionBuilder setEnvironmentConditions(models.EnvironmentConditions environmentConditions);

    MissionBuilder setOperationTimeline(List<models.OperationEvent> operationTimeline);

    Mission build();
}