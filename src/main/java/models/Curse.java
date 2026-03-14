package models;

public class Curse {
    private String name;
    private String threatLevel;

    public void validate() throws IllegalArgumentException {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Имя проклятия не может быть пустым");
        }

        if (threatLevel == null || threatLevel.trim().isEmpty()) {
            throw new IllegalArgumentException("Уровень угрозы не может быть пустым");
        }
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getThreatLevel() {
        return threatLevel;
    }

    public void setThreatLevel(String threatLevel) {
        this.threatLevel = threatLevel;
    }
}