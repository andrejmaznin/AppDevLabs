package adapters.out.persistence;

import domain.models.*;
import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Entity
@Table(name = "missions")
public class MissionEntity {
    @Id
    private String missionId;
    private String date;
    private String location;
    private String outcome;
    private long damageCost;
    private String comment;

    @Embedded
    private CurseEntity curse;

    @ElementCollection
    @CollectionTable(name = "mission_sorcerers", joinColumns = @JoinColumn(name = "mission_id"))
    private List<SorcererEntity> sorcerers;

    @ElementCollection
    @CollectionTable(name = "mission_techniques", joinColumns = @JoinColumn(name = "mission_id"))
    private List<TechniqueEntity> techniques;

    @Embedded
    private EconomicAssessmentEntity economicAssessment;

    @Embedded
    private CivilianImpactEntity civilianImpact;

    @Embedded
    private EnemyActivityEntity enemyActivity;

    @Embedded
    private EnvironmentConditionsEntity environmentConditions;

    @ElementCollection
    @CollectionTable(name = "mission_timeline", joinColumns = @JoinColumn(name = "mission_id"))
    private List<OperationEventEntity> operationTimeline;

    @ElementCollection
    private List<String> operationTags;
    @ElementCollection
    private List<String> supportUnits;
    @ElementCollection
    private List<String> recommendations;
    @ElementCollection
    private List<String> artifactsRecovered;
    @ElementCollection
    private List<String> evacuationZones;
    @ElementCollection
    private List<String> statusEffects;

    public String getMissionId() { return missionId; }
    public void setMissionId(String missionId) { this.missionId = missionId; }
    public String getDate() { return date; }
    public void setDate(String date) { this.date = date; }
    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }
    public String getOutcome() { return outcome; }
    public void setOutcome(String outcome) { this.outcome = outcome; }
    public long getDamageCost() { return damageCost; }
    public void setDamageCost(long damageCost) { this.damageCost = damageCost; }
    public String getComment() { return comment; }
    public void setComment(String comment) { this.comment = comment; }
    public CurseEntity getCurse() { return curse; }
    public void setCurse(CurseEntity curse) { this.curse = curse; }
    public List<SorcererEntity> getSorcerers() { return sorcerers; }
    public void setSorcerers(List<SorcererEntity> sorcerers) { this.sorcerers = sorcerers; }
    public List<TechniqueEntity> getTechniques() { return techniques; }
    public void setTechniques(List<TechniqueEntity> techniques) { this.techniques = techniques; }
    public EconomicAssessmentEntity getEconomicAssessment() { return economicAssessment; }
    public void setEconomicAssessment(EconomicAssessmentEntity economicAssessment) { this.economicAssessment = economicAssessment; }
    public CivilianImpactEntity getCivilianImpact() { return civilianImpact; }
    public void setCivilianImpact(CivilianImpactEntity civilianImpact) { this.civilianImpact = civilianImpact; }
    public EnemyActivityEntity getEnemyActivity() { return enemyActivity; }
    public void setEnemyActivity(EnemyActivityEntity enemyActivity) { this.enemyActivity = enemyActivity; }
    public EnvironmentConditionsEntity getEnvironmentConditions() { return environmentConditions; }
    public void setEnvironmentConditions(EnvironmentConditionsEntity environmentConditions) { this.environmentConditions = environmentConditions; }
    public List<OperationEventEntity> getOperationTimeline() { return operationTimeline; }
    public void setOperationTimeline(List<OperationEventEntity> operationTimeline) { this.operationTimeline = operationTimeline; }
    public List<String> getOperationTags() { return operationTags; }
    public void setOperationTags(List<String> operationTags) { this.operationTags = operationTags; }
    public List<String> getSupportUnits() { return supportUnits; }
    public void setSupportUnits(List<String> supportUnits) { this.supportUnits = supportUnits; }
    public List<String> getRecommendations() { return recommendations; }
    public void setRecommendations(List<String> recommendations) { this.recommendations = recommendations; }
    public List<String> getArtifactsRecovered() { return artifactsRecovered; }
    public void setArtifactsRecovered(List<String> artifactsRecovered) { this.artifactsRecovered = artifactsRecovered; }
    public List<String> getEvacuationZones() { return evacuationZones; }
    public void setEvacuationZones(List<String> evacuationZones) { this.evacuationZones = evacuationZones; }
    public List<String> getStatusEffects() { return statusEffects; }
    public void setStatusEffects(List<String> statusEffects) { this.statusEffects = statusEffects; }

    public static MissionEntity fromDomain(Mission domain) {
        if (domain == null) return null;
        MissionEntity entity = new MissionEntity();
        entity.setMissionId(domain.getMissionId());
        entity.setDate(domain.getDate());
        entity.setLocation(domain.getLocation());
        entity.setOutcome(domain.getOutcome());
        entity.setDamageCost(domain.getDamageCost());
        entity.setComment(domain.getComment());
        entity.setCurse(CurseEntity.fromDomain(domain.getCurse()));
        if (domain.getSorcerers() != null) {
            entity.setSorcerers(domain.getSorcerers().stream().map(SorcererEntity::fromDomain).collect(Collectors.toList()));
        }
        if (domain.getTechniques() != null) {
            entity.setTechniques(domain.getTechniques().stream().map(TechniqueEntity::fromDomain).collect(Collectors.toList()));
        }
        entity.setEconomicAssessment(EconomicAssessmentEntity.fromDomain(domain.getEconomicAssessment()));
        entity.setCivilianImpact(CivilianImpactEntity.fromDomain(domain.getCivilianImpact()));
        entity.setEnemyActivity(EnemyActivityEntity.fromDomain(domain.getEnemyActivity()));
        entity.setEnvironmentConditions(EnvironmentConditionsEntity.fromDomain(domain.getEnvironmentConditions()));
        if (domain.getOperationTimeline() != null) {
            entity.setOperationTimeline(domain.getOperationTimeline().stream().map(OperationEventEntity::fromDomain).collect(Collectors.toList()));
        }
        entity.setOperationTags(domain.getOperationTags());
        entity.setSupportUnits(domain.getSupportUnits());
        entity.setRecommendations(domain.getRecommendations());
        entity.setArtifactsRecovered(domain.getArtifactsRecovered());
        entity.setEvacuationZones(domain.getEvacuationZones());
        entity.setStatusEffects(domain.getStatusEffects());
        return entity;
    }

    public Mission toDomain() {
        Mission domain = new Mission();
        domain.setMissionId(this.getMissionId());
        domain.setDate(this.getDate());
        domain.setLocation(this.getLocation());
        domain.setOutcome(this.getOutcome());
        domain.setDamageCost(this.getDamageCost());
        domain.setComment(this.getComment());
        if (this.getCurse() != null) domain.setCurse(this.getCurse().toDomain());
        if (this.getSorcerers() != null) {
            domain.setSorcerers(this.getSorcerers().stream().map(SorcererEntity::toDomain).collect(Collectors.toList()));
        }
        if (this.getTechniques() != null) {
            domain.setTechniques(this.getTechniques().stream().map(TechniqueEntity::toDomain).collect(Collectors.toList()));
        }
        if (this.getEconomicAssessment() != null) domain.setEconomicAssessment(this.getEconomicAssessment().toDomain());
        if (this.getCivilianImpact() != null) domain.setCivilianImpact(this.getCivilianImpact().toDomain());
        if (this.getEnemyActivity() != null) domain.setEnemyActivity(this.getEnemyActivity().toDomain());
        if (this.getEnvironmentConditions() != null) domain.setEnvironmentConditions(this.getEnvironmentConditions().toDomain());
        if (this.getOperationTimeline() != null) {
            domain.setOperationTimeline(this.getOperationTimeline().stream().map(OperationEventEntity::toDomain).collect(Collectors.toList()));
        }
        domain.setOperationTags(this.getOperationTags());
        domain.setSupportUnits(this.getSupportUnits());
        domain.setRecommendations(this.getRecommendations());
        domain.setArtifactsRecovered(this.getArtifactsRecovered());
        domain.setEvacuationZones(this.getEvacuationZones());
        domain.setStatusEffects(this.getStatusEffects());
        return domain;
    }
}

@Embeddable
class CurseEntity {
    private String name;
    private String threatLevel;
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getThreatLevel() { return threatLevel; }
    public void setThreatLevel(String threatLevel) { this.threatLevel = threatLevel; }
    public static CurseEntity fromDomain(Curse domain) {
        if (domain == null) return null;
        CurseEntity entity = new CurseEntity();
        entity.setName(domain.getName());
        entity.setThreatLevel(domain.getThreatLevel());
        return entity;
    }
    public Curse toDomain() {
        Curse domain = new Curse();
        domain.setName(this.name);
        domain.setThreatLevel(this.threatLevel);
        return domain;
    }
}

@Embeddable
class SorcererEntity {
    private String name;
    private String rank;
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getRank() { return rank; }
    public void setRank(String rank) { this.rank = rank; }
    public static SorcererEntity fromDomain(Sorcerer domain) {
        if (domain == null) return null;
        SorcererEntity entity = new SorcererEntity();
        entity.setName(domain.getName());
        entity.setRank(domain.getRank());
        return entity;
    }
    public Sorcerer toDomain() {
        Sorcerer domain = new Sorcerer();
        domain.setName(this.name);
        domain.setRank(this.rank);
        return domain;
    }
}

@Embeddable
class TechniqueEntity {
    private String name;
    private String type;
    private String owner;
    private int damage;
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getType() { return type; }
    public void setType(String type) { this.type = type; }
    public String getOwner() { return owner; }
    public void setOwner(String owner) { this.owner = owner; }
    public int getDamage() { return damage; }
    public void setDamage(int damage) { this.damage = damage; }
    public static TechniqueEntity fromDomain(Technique domain) {
        if (domain == null) return null;
        TechniqueEntity entity = new TechniqueEntity();
        entity.setName(domain.getName());
        entity.setType(domain.getType());
        entity.setOwner(domain.getOwner());
        entity.setDamage(domain.getDamage());
        return entity;
    }
    public Technique toDomain() {
        Technique domain = new Technique();
        domain.setName(this.name);
        domain.setType(this.type);
        domain.setOwner(this.owner);
        domain.setDamage(this.damage);
        return domain;
    }
}

@Embeddable
class EconomicAssessmentEntity {
    private Long totalDamageCost;
    private Long infrastructureDamage;
    private Long commercialDamage;
    private Long transportDamage;
    private Integer recoveryEstimateDays;
    private Boolean insuranceCovered;
    public Long getTotalDamageCost() { return totalDamageCost; }
    public void setTotalDamageCost(Long totalDamageCost) { this.totalDamageCost = totalDamageCost; }
    public Long getInfrastructureDamage() { return infrastructureDamage; }
    public void setInfrastructureDamage(Long infrastructureDamage) { this.infrastructureDamage = infrastructureDamage; }
    public Long getCommercialDamage() { return commercialDamage; }
    public void setCommercialDamage(Long commercialDamage) { this.commercialDamage = commercialDamage; }
    public Long getTransportDamage() { return transportDamage; }
    public void setTransportDamage(Long transportDamage) { this.transportDamage = transportDamage; }
    public Integer getRecoveryEstimateDays() { return recoveryEstimateDays; }
    public void setRecoveryEstimateDays(Integer recoveryEstimateDays) { this.recoveryEstimateDays = recoveryEstimateDays; }
    public Boolean getInsuranceCovered() { return insuranceCovered; }
    public void setInsuranceCovered(Boolean insuranceCovered) { this.insuranceCovered = insuranceCovered; }

    public static EconomicAssessmentEntity fromDomain(EconomicAssessment domain) {
        if (domain == null) return null;
        EconomicAssessmentEntity entity = new EconomicAssessmentEntity();
        entity.setTotalDamageCost(domain.getTotalDamageCost());
        entity.setInfrastructureDamage(domain.getInfrastructureDamage());
        entity.setCommercialDamage(domain.getCommercialDamage());
        entity.setTransportDamage(domain.getTransportDamage());
        entity.setRecoveryEstimateDays(domain.getRecoveryEstimateDays());
        entity.setInsuranceCovered(domain.getInsuranceCovered());
        return entity;
    }
    public EconomicAssessment toDomain() {
        EconomicAssessment domain = new EconomicAssessment();
        domain.setTotalDamageCost(this.totalDamageCost);
        domain.setInfrastructureDamage(this.infrastructureDamage);
        domain.setCommercialDamage(this.commercialDamage);
        domain.setTransportDamage(this.transportDamage);
        domain.setRecoveryEstimateDays(this.recoveryEstimateDays);
        domain.setInsuranceCovered(this.insuranceCovered);
        return domain;
    }
}

@Embeddable
class CivilianImpactEntity {
    private Integer evacuated;
    private Integer injured;
    private Integer missing;
    private String publicExposureRisk;
    public Integer getEvacuated() { return evacuated; }
    public void setEvacuated(Integer evacuated) { this.evacuated = evacuated; }
    public Integer getInjured() { return injured; }
    public void setInjured(Integer injured) { this.injured = injured; }
    public Integer getMissing() { return missing; }
    public void setMissing(Integer missing) { this.missing = missing; }
    public String getPublicExposureRisk() { return publicExposureRisk; }
    public void setPublicExposureRisk(String publicExposureRisk) { this.publicExposureRisk = publicExposureRisk; }

    public static CivilianImpactEntity fromDomain(CivilianImpact domain) {
        if (domain == null) return null;
        CivilianImpactEntity entity = new CivilianImpactEntity();
        entity.setEvacuated(domain.getEvacuated());
        entity.setInjured(domain.getInjured());
        entity.setMissing(domain.getMissing());
        entity.setPublicExposureRisk(domain.getPublicExposureRisk());
        return entity;
    }
    public CivilianImpact toDomain() {
        CivilianImpact domain = new CivilianImpact();
        domain.setEvacuated(this.evacuated);
        domain.setInjured(this.injured);
        domain.setMissing(this.missing);
        domain.setPublicExposureRisk(this.publicExposureRisk);
        return domain;
    }
}

@Embeddable
class EnemyActivityEntity {
    private String behaviorType;
    @ElementCollection
    private List<String> targetPriority;
    @ElementCollection
    private List<String> attackPatterns;
    @ElementCollection
    private List<String> countermeasuresUsed;
    private String mobility;
    private String escalationRisk;
    public String getBehaviorType() { return behaviorType; }
    public void setBehaviorType(String behaviorType) { this.behaviorType = behaviorType; }
    public List<String> getTargetPriority() { return targetPriority; }
    public void setTargetPriority(List<String> targetPriority) { this.targetPriority = targetPriority; }
    public List<String> getAttackPatterns() { return attackPatterns; }
    public void setAttackPatterns(List<String> attackPatterns) { this.attackPatterns = attackPatterns; }
    public List<String> getCountermeasuresUsed() { return countermeasuresUsed; }
    public void setCountermeasuresUsed(List<String> countermeasuresUsed) { this.countermeasuresUsed = countermeasuresUsed; }
    public String getMobility() { return mobility; }
    public void setMobility(String mobility) { this.mobility = mobility; }
    public String getEscalationRisk() { return escalationRisk; }
    public void setEscalationRisk(String escalationRisk) { this.escalationRisk = escalationRisk; }

    public static EnemyActivityEntity fromDomain(EnemyActivity domain) {
        if (domain == null) return null;
        EnemyActivityEntity entity = new EnemyActivityEntity();
        entity.setBehaviorType(domain.getBehaviorType());
        entity.setTargetPriority(domain.getTargetPriority());
        entity.setAttackPatterns(domain.getAttackPatterns());
        entity.setCountermeasuresUsed(domain.getCountermeasuresUsed());
        entity.setMobility(domain.getMobility());
        entity.setEscalationRisk(domain.getEscalationRisk());
        return entity;
    }
    public EnemyActivity toDomain() {
        EnemyActivity domain = new EnemyActivity();
        domain.setBehaviorType(this.behaviorType);
        domain.setTargetPriority(this.targetPriority);
        domain.setAttackPatterns(this.attackPatterns);
        domain.setCountermeasuresUsed(this.countermeasuresUsed);
        domain.setMobility(this.mobility);
        domain.setEscalationRisk(this.escalationRisk);
        return domain;
    }
}

@Embeddable
class EnvironmentConditionsEntity {
    private String weather;
    private String timeOfDay;
    private String visibility;
    private Double cursedEnergyDensity;
    public String getWeather() { return weather; }
    public void setWeather(String weather) { this.weather = weather; }
    public String getTimeOfDay() { return timeOfDay; }
    public void setTimeOfDay(String timeOfDay) { this.timeOfDay = timeOfDay; }
    public String getVisibility() { return visibility; }
    public void setVisibility(String visibility) { this.visibility = visibility; }
    public Double getCursedEnergyDensity() { return cursedEnergyDensity; }
    public void setCursedEnergyDensity(Double cursedEnergyDensity) { this.cursedEnergyDensity = cursedEnergyDensity; }

    public static EnvironmentConditionsEntity fromDomain(EnvironmentConditions domain) {
        if (domain == null) return null;
        EnvironmentConditionsEntity entity = new EnvironmentConditionsEntity();
        entity.setWeather(domain.getWeather());
        entity.setTimeOfDay(domain.getTimeOfDay());
        entity.setVisibility(domain.getVisibility());
        entity.setCursedEnergyDensity(domain.getCursedEnergyDensity());
        return entity;
    }
    public EnvironmentConditions toDomain() {
        EnvironmentConditions domain = new EnvironmentConditions();
        domain.setWeather(this.weather);
        domain.setTimeOfDay(this.timeOfDay);
        domain.setVisibility(this.visibility);
        domain.setCursedEnergyDensity(this.cursedEnergyDensity);
        return domain;
    }
}

@Embeddable
class OperationEventEntity {
    private String timestamp;
    private String type;
    private String description;
    public String getTimestamp() { return timestamp; }
    public void setTimestamp(String timestamp) { this.timestamp = timestamp; }
    public String getType() { return type; }
    public void setType(String type) { this.type = type; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public static OperationEventEntity fromDomain(OperationEvent domain) {
        if (domain == null) return null;
        OperationEventEntity entity = new OperationEventEntity();
        entity.setTimestamp(domain.getTimestamp());
        entity.setType(domain.getType());
        entity.setDescription(domain.getDescription());
        return entity;
    }
    public OperationEvent toDomain() {
        OperationEvent domain = new OperationEvent();
        domain.setTimestamp(this.timestamp);
        domain.setType(this.type);
        domain.setDescription(this.description);
        return domain;
    }
}
