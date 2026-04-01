package models;

import java.util.List;

public class EnemyActivity {
    private String behaviorType;
    private List<String> targetPriority;
    private List<String> attackPatterns;
    private String mobility;
    private String escalationRisk;

    public String getBehaviorType() {
        return behaviorType;
    }

    public void setBehaviorType(String behaviorType) {
        this.behaviorType = behaviorType;
    }

    public List<String> getTargetPriority() {
        return targetPriority;
    }

    public void setTargetPriority(List<String> targetPriority) {
        this.targetPriority = targetPriority;
    }

    public List<String> getAttackPatterns() {
        return attackPatterns;
    }

    public void setAttackPatterns(List<String> attackPatterns) {
        this.attackPatterns = attackPatterns;
    }

    public String getMobility() {
        return mobility;
    }

    public void setMobility(String mobility) {
        this.mobility = mobility;
    }

    public String getEscalationRisk() {
        return escalationRisk;
    }

    public void setEscalationRisk(String escalationRisk) {
        this.escalationRisk = escalationRisk;
    }
}