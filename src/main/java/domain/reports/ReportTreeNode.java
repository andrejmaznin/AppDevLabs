package domain.reports;

import java.util.ArrayList;
import java.util.List;

public class ReportTreeNode {
    private String label;
    private List<ReportTreeNode> children = new ArrayList<>();

    public ReportTreeNode(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }

    public List<ReportTreeNode> getChildren() {
        return children;
    }

    public void addChild(ReportTreeNode child) {
        children.add(child);
    }
}
