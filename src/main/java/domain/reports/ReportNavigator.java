package domain.reports;

public class ReportNavigator {
    private ReportStrategy strategy;

    public ReportNavigator() {}

    public ReportNavigator(ReportStrategy strategy) {
        this.strategy = strategy;
    }

    public void setStrategy(ReportStrategy strategy) {
        this.strategy = strategy;
    }

    public MissionReport execute(Object data) {
        if (strategy == null) {
            throw new IllegalStateException("Report strategy is not set");
        }
        return strategy.generate(data);
    }
}
