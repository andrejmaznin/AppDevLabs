package domain.builder;

import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import domain.models.Curse;
import domain.models.Mission;
import domain.models.Sorcerer;
import domain.models.Technique;

import java.util.ArrayList;
import java.util.List;

@JsonPOJOBuilder(withPrefix = "set")
public class StandardMissionBuilder implements MissionBuilder {
    private String missionId;
    private String date;
    private String location;
    private String outcome;
    private long damageCost;
    private Curse curse;
    private String comment;
    private final List<Sorcerer> sorcerers = new ArrayList<>();
    private final List<Technique> techniques = new ArrayList<>();
    private domain.models.EconomicAssessment economicAssessment;
    private domain.models.CivilianImpact civilianImpact;
    private domain.models.EnemyActivity enemyActivity;
    private domain.models.EnvironmentConditions environmentConditions;
    private List<domain.models.OperationEvent> operationTimeline;
    private List<String> operationTags;
    private List<String> supportUnits;
    private List<String> recommendations;
    private List<String> artifactsRecovered;
    private List<String> evacuationZones;
    private List<String> statusEffects;


    @Override
    public MissionBuilder setMissionId(String id) {
        this.missionId = id;
        return this;
    }

    @Override
    public MissionBuilder setDate(String date) {
        this.date = date;
        return this;
    }

    @Override
    public MissionBuilder setLocation(String location) {
        this.location = location;
        return this;
    }

    @Override
    public MissionBuilder setOutcome(String outcome) {
        this.outcome = outcome;
        return this;
    }

    @Override
    public MissionBuilder setDamageCost(long cost) {
        this.damageCost = cost;
        return this;
    }

    @Override
    public MissionBuilder setComment(String comment) {
        this.comment = comment;
        return this;
    }

    @Override
    public MissionBuilder setCurse(Curse curse) {
        this.curse = curse;
        return this;
    }

    @Override
    @JacksonXmlElementWrapper(localName = "sorcerers")
    @JacksonXmlProperty(localName = "sorcerer")
    public MissionBuilder setSorcerers(List<Sorcerer> sorcerers) {
        if (sorcerers != null) {
            this.sorcerers.addAll(sorcerers);
        }
        return this;
    }

    @Override
    @JacksonXmlElementWrapper(localName = "techniques")
    @JacksonXmlProperty(localName = "technique")
    public MissionBuilder setTechniques(List<Technique> techniques) {
        if (techniques != null) {
            this.techniques.addAll(techniques);
        }
        return this;
    }

    @Override
    public MissionBuilder setCurseDetails(String name, String threatLevel) {
        if (this.curse == null) this.curse = new Curse();
        if (name != null) this.curse.setName(name);
        if (threatLevel != null) this.curse.setThreatLevel(threatLevel);
        return this;
    }

    @Override
    public MissionBuilder addSorcerer(Sorcerer sorcerer) {
        this.sorcerers.add(sorcerer);
        return this;
    }

    @Override
    public MissionBuilder addTechnique(Technique technique) {
        this.techniques.add(technique);
        return this;
    }

    @Override
    public MissionBuilder setEconomicAssessment(domain.models.EconomicAssessment economicAssessment) {
        this.economicAssessment = economicAssessment;
        return this;
    }

    @Override
    public MissionBuilder setCivilianImpact(domain.models.CivilianImpact civilianImpact) {
        this.civilianImpact = civilianImpact;
        return this;
    }

    @Override
    public MissionBuilder setEnemyActivity(domain.models.EnemyActivity enemyActivity) {
        this.enemyActivity = enemyActivity;
        return this;
    }

    @Override
    public MissionBuilder setEnvironmentConditions(domain.models.EnvironmentConditions environmentConditions) {
        this.environmentConditions = environmentConditions;
        return this;
    }

    @Override
    public MissionBuilder setOperationTimeline(List<domain.models.OperationEvent> operationTimeline) {
        this.operationTimeline = operationTimeline;
        return this;
    }

    @Override
    @JacksonXmlElementWrapper(localName = "operationTags")
    @JacksonXmlProperty(localName = "tag")
    public MissionBuilder setOperationTags(List<String> tags) {
        this.operationTags = tags;
        return this;
    }

    @Override
    @JacksonXmlElementWrapper(localName = "supportUnits")
    @JacksonXmlProperty(localName = "unit")
    public MissionBuilder setSupportUnits(List<String> units) {
        this.supportUnits = units;
        return this;
    }

    @Override
    @JacksonXmlElementWrapper(localName = "recommendations")
    @JacksonXmlProperty(localName = "recommendation")
    public MissionBuilder setRecommendations(List<String> recommendations) {
        this.recommendations = recommendations;
        return this;
    }

    @Override
    @JacksonXmlElementWrapper(localName = "artifactsRecovered")
    @JacksonXmlProperty(localName = "artifact")
    public MissionBuilder setArtifactsRecovered(List<String> artifacts) {
        this.artifactsRecovered = artifacts;
        return this;
    }

    @Override
    @JacksonXmlElementWrapper(localName = "evacuationZones")
    @JacksonXmlProperty(localName = "zone")
    public MissionBuilder setEvacuationZones(List<String> zones) {
        this.evacuationZones = zones;
        return this;
    }

    @Override
    @JacksonXmlElementWrapper(localName = "statusEffects")
    @JacksonXmlProperty(localName = "effect")
    public MissionBuilder setStatusEffects(List<String> effects) {
        this.statusEffects = effects;
        return this;
    }

    @Override
    public Mission build() {
        if (!this.techniques.isEmpty()) {
            for (Technique tech : this.techniques) {
                String ownerName = tech.getOwner();
                if (ownerName != null && !ownerName.trim().isEmpty()) {
                    boolean isSorcererPresent = this.sorcerers.stream()
                        .anyMatch(s -> s.getName() != null && s.getName().trim().equalsIgnoreCase(ownerName.trim()));

                    if (!isSorcererPresent) {
                        Sorcerer autoAddedSorcerer = new Sorcerer();
                        autoAddedSorcerer.setName(ownerName.trim());
                        autoAddedSorcerer.setRank("НЕИЗВЕСТНО");
                        this.sorcerers.add(autoAddedSorcerer);
                    }
                }
            }
        }

        Mission mission = new Mission();
        mission.setMissionId(this.missionId);
        mission.setDate(this.date);
        mission.setLocation(this.location);
        mission.setOutcome(this.outcome);
        mission.setDamageCost(this.damageCost);
        mission.setComment(this.comment);
        mission.setCurse(this.curse);
        mission.setSorcerers(this.sorcerers);
        mission.setTechniques(this.techniques);
        mission.setEconomicAssessment(this.economicAssessment);
        mission.setCivilianImpact(this.civilianImpact);
        mission.setEnemyActivity(this.enemyActivity);
        mission.setEnvironmentConditions(this.environmentConditions);
        mission.setOperationTimeline(this.operationTimeline);
        mission.setOperationTags(this.operationTags);
        mission.setSupportUnits(this.supportUnits);
        mission.setRecommendations(this.recommendations);
        mission.setArtifactsRecovered(this.artifactsRecovered);
        mission.setEvacuationZones(this.evacuationZones);
        mission.setStatusEffects(this.statusEffects);

        mission.validate();

        return mission;
    }
}