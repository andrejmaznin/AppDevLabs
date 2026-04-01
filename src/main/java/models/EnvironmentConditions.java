package models;

public class EnvironmentConditions {
    private String weather;
    private String timeOfDay;
    private String visibility;
    private Double cursedEnergyDensity;

    public String getWeather() {
        return weather;
    }

    public void setWeather(String weather) {
        this.weather = weather;
    }

    public String getTimeOfDay() {
        return timeOfDay;
    }

    public void setTimeOfDay(String timeOfDay) {
        this.timeOfDay = timeOfDay;
    }

    public String getVisibility() {
        return visibility;
    }

    public void setVisibility(String visibility) {
        this.visibility = visibility;
    }

    public Double getCursedEnergyDensity() {
        return cursedEnergyDensity;
    }

    public void setCursedEnergyDensity(Double cursedEnergyDensity) {
        this.cursedEnergyDensity = cursedEnergyDensity;
    }
}