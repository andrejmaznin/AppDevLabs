package domain.reports;

public class TreeReport implements MissionReport {
    private final ReportTreeNode rootNode;

    public TreeReport(ReportTreeNode rootNode) {
        this.rootNode = rootNode;
    }

    public ReportTreeNode getRootNode() {
        return rootNode;
    }
}
