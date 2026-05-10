package adapters.in.gui;

import domain.reports.ReportTreeNode;
import domain.reports.TreeReport;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import java.awt.BorderLayout;

public class SmartMissionFrame {
    private final JPanel mainPanel;
    private final JTree tree;

    public SmartMissionFrame(TreeReport report) {
        mainPanel = new JPanel(new BorderLayout());

        DefaultMutableTreeNode swingRoot = convertToSwingNode(report.getRootNode());
        tree = new JTree(new DefaultTreeModel(swingRoot));

        tree.setCellRenderer(new SmartTreeRenderer());
        JScrollPane sp = new JScrollPane(tree);
        mainPanel.add(sp, BorderLayout.CENTER);
    }

    private DefaultMutableTreeNode convertToSwingNode(ReportTreeNode domainNode) {
        DefaultMutableTreeNode swingNode = new DefaultMutableTreeNode(domainNode.getLabel());
        for (ReportTreeNode child : domainNode.getChildren()) {
            swingNode.add(convertToSwingNode(child));
        }
        return swingNode;
    }

    public JPanel getMainPanel() {
        return mainPanel;
    }

    public static void showInFrame(TreeReport report) {
        JFrame f = new JFrame("Умное отображение отчетов");
        f.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        SmartMissionFrame panel = new SmartMissionFrame(report);
        f.setContentPane(panel.getMainPanel());
        f.setSize(700, 500);
        f.setLocationRelativeTo(null);
        f.setVisible(true);
    }
}