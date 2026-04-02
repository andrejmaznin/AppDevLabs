package reports;

import java.util.List;

public class BasicMissionReport implements MissionReport {
    private final String missionId;
    private final String date;
    private final String location;
    private final String outcome;
    private final String formattedDamage;
    private final String comment;

    private final String curseName;
    private final String curseThreatLevel;
    private final String environmentSummary;
    private final String civilianImpactSummary;
    private final String enemyActivitySummary;

    private final List<String> sorcerersDetails;
    private final List<String> techniquesDetails;
    private final List<String> timelineDetails;

    public BasicMissionReport(String missionId, String date, String location, String outcome,
                              String formattedDamage, String comment, String curseName,
                              String curseThreatLevel, String environmentSummary,
                              String civilianImpactSummary, String enemyActivitySummary,
                              List<String> sorcerersDetails, List<String> techniquesDetails,
                              List<String> timelineDetails) {
        this.missionId = missionId;
        this.date = date;
        this.location = location;
        this.outcome = outcome;
        this.formattedDamage = formattedDamage;
        this.comment = comment;
        this.curseName = curseName;
        this.curseThreatLevel = curseThreatLevel;
        this.environmentSummary = environmentSummary;
        this.civilianImpactSummary = civilianImpactSummary;
        this.enemyActivitySummary = enemyActivitySummary;
        this.sorcerersDetails = sorcerersDetails;
        this.techniquesDetails = techniquesDetails;
        this.timelineDetails = timelineDetails;
    }

    public String getMissionId() {
        return missionId;
    }

    public String getDate() {
        return date;
    }

    public String getLocation() {
        return location;
    }

    public String getOutcome() {
        return outcome;
    }

    public String getFormattedDamage() {
        return formattedDamage;
    }

    public String getComment() {
        return comment;
    }

    public String getCurseName() {
        return curseName;
    }

    public String getCurseThreatLevel() {
        return curseThreatLevel;
    }

    public String getEnvironmentSummary() {
        return environmentSummary;
    }

    public String getCivilianImpactSummary() {
        return civilianImpactSummary;
    }

    public String getEnemyActivitySummary() {
        return enemyActivitySummary;
    }

    public List<String> getSorcerersDetails() {
        return sorcerersDetails;
    }

    public List<String> getTechniquesDetails() {
        return techniquesDetails;
    }

    public List<String> getTimelineDetails() {
        return timelineDetails;
    }
}