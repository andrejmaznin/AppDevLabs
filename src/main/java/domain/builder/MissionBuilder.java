package domain.builder;

import domain.models.Curse;
import domain.models.Mission;
import domain.models.Sorcerer;
import domain.models.Technique;

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

    MissionBuilder setEconomicAssessment(domain.models.EconomicAssessment economicAssessment);

    MissionBuilder setCivilianImpact(domain.models.CivilianImpact civilianImpact);

    MissionBuilder setEnemyActivity(domain.models.EnemyActivity enemyActivity);

    MissionBuilder setEnvironmentConditions(domain.models.EnvironmentConditions environmentConditions);

    MissionBuilder setOperationTimeline(List<domain.models.OperationEvent> operationTimeline);

    Mission build();
}