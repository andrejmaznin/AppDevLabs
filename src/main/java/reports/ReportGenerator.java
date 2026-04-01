package reports;

public interface ReportGenerator<R extends MissionReport, T> {
    R generate(T data);
}