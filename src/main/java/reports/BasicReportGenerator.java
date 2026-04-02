package reports;

import models.CivilianImpact;
import models.EnemyActivity;
import models.EnvironmentConditions;
import models.Mission;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class BasicReportGenerator implements ReportGenerator<BasicMissionReport, Mission> {

    @Override
    public BasicMissionReport generate(Mission mission) {
        String curseName = mission.getCurse() != null && mission.getCurse().getName() != null ? mission.getCurse().getName() : "—";
        String curseLevel = mission.getCurse() != null && mission.getCurse().getThreatLevel() != null ? mission.getCurse().getThreatLevel() : "—";
        String comment = mission.getComment() != null ? mission.getComment() : "—";
        String formattedDamage = String.format("%,d", mission.getDamageCost());

        List<String> sorcerers = new ArrayList<>();
        if (mission.getSorcerers() != null) {
            sorcerers = mission.getSorcerers().stream()
                .map(s -> s.getName() + " (" + s.getRank() + ")")
                .collect(Collectors.toList());
        }

        List<String> techniques = new ArrayList<>();
        if (mission.getTechniques() != null) {
            techniques = mission.getTechniques().stream()
                .map(t -> t.getName() + " — " + t.getOwner())
                .collect(Collectors.toList());
        }

        String envSummary = "—";
        if (mission.getEnvironmentConditions() != null) {
            EnvironmentConditions ec = mission.getEnvironmentConditions();
            envSummary = String.format("%s, %s, вид. %s",
                ec.getWeather() != null ? ec.getWeather() : "Н/Д",
                ec.getTimeOfDay() != null ? ec.getTimeOfDay() : "Н/Д",
                ec.getVisibility() != null ? ec.getVisibility() : "Н/Д");
        }

        String civSummary = "—";
        if (mission.getCivilianImpact() != null) {
            CivilianImpact ci = mission.getCivilianImpact();
            civSummary = String.format("Эвакуировано: %s, Пострадало: %s",
                ci.getEvacuated() != null ? ci.getEvacuated() : "0",
                ci.getInjured() != null ? ci.getInjured() : "0");
        }

        String enemySummary = "—";
        if (mission.getEnemyActivity() != null) {
            EnemyActivity ea = mission.getEnemyActivity();
            enemySummary = String.format("Тип: %s, Мобильность: %s",
                ea.getBehaviorType() != null ? ea.getBehaviorType() : "Н/Д",
                ea.getMobility() != null ? ea.getMobility() : "Н/Д");
        }

        List<String> timeline = new ArrayList<>();
        if (mission.getOperationTimeline() != null) {
            timeline = mission.getOperationTimeline().stream()
                .map(e -> e.getTimestamp() + " [" + e.getType() + "] " + e.getDescription())
                .collect(Collectors.toList());
        }

        return new BasicMissionReport(
            mission.getMissionId(), mission.getDate(), mission.getLocation(),
            mission.getOutcome(), formattedDamage, comment,
            curseName, curseLevel, envSummary, civSummary, enemySummary,
            sorcerers, techniques, timeline
        );
    }
}