package reports;

import javax.swing.tree.DefaultMutableTreeNode;

public class SmartTreeReport implements MissionReport {
    private final DefaultMutableTreeNode rootNode;

    public SmartTreeReport(DefaultMutableTreeNode rootNode) {
        this.rootNode = rootNode;
    }

    public DefaultMutableTreeNode getRootNode() {
        return rootNode;
    }
}