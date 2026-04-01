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
    private final List<String> sorcerersDetails;
    private final List<String> techniquesDetails;

    public BasicMissionReport(String missionId, String date, String location, String outcome,
                              String formattedDamage, String comment, String curseName,
                              String curseThreatLevel, List<String> sorcerersDetails,
                              List<String> techniquesDetails) {
        this.missionId = missionId;
        this.date = date;
        this.location = location;
        this.outcome = outcome;
        this.formattedDamage = formattedDamage;
        this.comment = comment;
        this.curseName = curseName;
        this.curseThreatLevel = curseThreatLevel;
        this.sorcerersDetails = sorcerersDetails;
        this.techniquesDetails = techniquesDetails;
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

    public List<String> getSorcerersDetails() {
        return sorcerersDetails;
    }

    public List<String> getTechniquesDetails() {
        return techniquesDetails;
    }
}