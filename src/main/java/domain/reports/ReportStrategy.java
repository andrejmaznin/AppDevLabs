package domain.reports;

public interface ReportStrategy {
    MissionReport generate(Object data);
}
