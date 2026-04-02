package specifications;

import models.Mission;
import models.OperationEvent;
import models.Sorcerer;
import models.Technique;

public class FullTextSpecification implements MissionSpecification {
    private final String[] tokens;

    public FullTextSpecification(String query) {
        if (query == null || query.trim().isEmpty()) {
            this.tokens = new String[0];
        } else {
            String normalizedQuery = query.toLowerCase().replaceAll("[\\p{Punct}]", " ");
            this.tokens = normalizedQuery.split("\\s+");
        }
    }

    @Override
    public boolean isSatisfiedBy(Mission mission) {
        if (tokens.length == 0) return true;

        StringBuilder sb = new StringBuilder();

        if (mission.getMissionId() != null) sb.append(mission.getMissionId()).append(" ");
        if (mission.getDate() != null) sb.append(mission.getDate()).append(" ");
        if (mission.getLocation() != null) sb.append(mission.getLocation()).append(" ");
        if (mission.getOutcome() != null) sb.append(mission.getOutcome()).append(" ");
        if (mission.getComment() != null) sb.append(mission.getComment()).append(" ");
        sb.append(mission.getDamageCost()).append(" ");

        if (mission.getCurse() != null) {
            if (mission.getCurse().getName() != null) sb.append(mission.getCurse().getName()).append(" ");
            if (mission.getCurse().getThreatLevel() != null) sb.append(mission.getCurse().getThreatLevel()).append(" ");
        }

        if (mission.getSorcerers() != null) {
            for (Sorcerer s : mission.getSorcerers()) {
                if (s.getName() != null) sb.append(s.getName()).append(" ");
                if (s.getRank() != null) sb.append(s.getRank()).append(" ");
            }
        }

        if (mission.getTechniques() != null) {
            for (Technique t : mission.getTechniques()) {
                if (t.getName() != null) sb.append(t.getName()).append(" ");
                if (t.getType() != null) sb.append(t.getType()).append(" ");
                if (t.getOwner() != null) sb.append(t.getOwner()).append(" ");
            }
        }

        if (mission.getOperationTimeline() != null) {
            for (OperationEvent e : mission.getOperationTimeline()) {
                if (e.getTimestamp() != null) sb.append(e.getTimestamp()).append(" ");
                if (e.getType() != null) sb.append(e.getType()).append(" ");
                if (e.getDescription() != null) sb.append(e.getDescription()).append(" ");
            }
        }

        String allText = sb.toString().toLowerCase().replaceAll("[\\p{Punct}]", " ");

        for (String token : tokens) {
            if (!allText.contains(token)) {
                return false;
            }
        }
        return true;
    }
}