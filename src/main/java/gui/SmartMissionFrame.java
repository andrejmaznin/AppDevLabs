package gui;

import reports.SmartTreeReport;

import javax.swing.*;
import javax.swing.tree.DefaultTreeModel;
import java.awt.BorderLayout;

public class SmartMissionFrame {
    private final JPanel mainPanel;
    private final JTree tree;

    public SmartMissionFrame(SmartTreeReport report) {
        mainPanel = new JPanel(new BorderLayout());
        tree = new JTree(new DefaultTreeModel(report.getRootNode()));
        tree.setCellRenderer(new SmartTreeRenderer());
        JScrollPane sp = new JScrollPane(tree);
        mainPanel.add(sp, BorderLayout.CENTER);
    }

    public JPanel getMainPanel() {
        return mainPanel;
    }

    public static void showInFrame(SmartTreeReport report) {
        JFrame f = new JFrame("Умное отображение отчетов");
        f.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        SmartMissionFrame panel = new SmartMissionFrame(report);
        f.setContentPane(panel.getMainPanel());
        f.setSize(700, 500);
        f.setLocationRelativeTo(null);
        f.setVisible(true);
    }
}