package reports;

import models.Mission;

import java.util.List;
import java.util.stream.Collectors;

public class BasicReportGenerator implements ReportGenerator<BasicMissionReport, Mission> {

    @Override
    public BasicMissionReport generate(Mission mission) {
        String curseName = mission.getCurse() != null ? mission.getCurse().getName() : "—";
        String curseLevel = mission.getCurse() != null ? mission.getCurse().getThreatLevel() : "—";
        String comment = mission.getComment() != null ? mission.getComment() : "—";
        String formattedDamage = String.format("%,d", mission.getDamageCost());

        List<String> sorcerers = mission.getSorcerers().stream()
            .map(s -> s.getName() + " (" + s.getRank() + ")")
            .collect(Collectors.toList());

        List<String> techniques = mission.getTechniques().stream()
            .map(t -> t.getName() + " — " + t.getOwner())
            .collect(Collectors.toList());

        return new BasicMissionReport(
            mission.getMissionId(), mission.getDate(), mission.getLocation(),
            mission.getOutcome(), formattedDamage, comment,
            curseName, curseLevel, sorcerers, techniques
        );
    }
}