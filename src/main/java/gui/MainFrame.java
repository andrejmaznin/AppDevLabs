package gui;

import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import com.intellij.uiDesigner.core.Spacer;
import logic.GUIEngine; // Ваш движок
import models.Mission;
import reports.BasicMissionReport;
import reports.BasicReportGenerator;
import reports.SmartTreeReport;
import reports.UniversalTreeGenerator;

import javax.swing.*;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;

public class MainFrame {
    private JTable missionsTable;
    private JPanel Main;
    private JButton importButton;

    private final GUIEngine engine;
    private final MissionTableModel tableModel;

    public MainFrame() {
        this.engine = new GUIEngine();
        this.tableModel = new MissionTableModel(engine.getStore());

        missionsTable.setModel(tableModel);

        missionsTable.setRowHeight(28);

        missionsTable.getColumnModel().getColumn(6).setCellRenderer(new ButtonRenderer());
        missionsTable.getColumnModel().getColumn(7).setCellRenderer(new ButtonRenderer());

        int buttonColWidth = 90;
        if (missionsTable.getColumnModel().getColumnCount() > 7) {
            missionsTable.getColumnModel().getColumn(6).setPreferredWidth(buttonColWidth);
            missionsTable.getColumnModel().getColumn(6).setMaxWidth(buttonColWidth);
            missionsTable.getColumnModel().getColumn(6).setMinWidth(buttonColWidth);
            missionsTable.getColumnModel().getColumn(6).setResizable(false);

            missionsTable.getColumnModel().getColumn(7).setPreferredWidth(buttonColWidth);
            missionsTable.getColumnModel().getColumn(7).setMaxWidth(buttonColWidth);
            missionsTable.getColumnModel().getColumn(7).setMinWidth(buttonColWidth);
            missionsTable.getColumnModel().getColumn(7).setResizable(false);
        }

        missionsTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int viewRow = missionsTable.rowAtPoint(e.getPoint());
                int viewCol = missionsTable.columnAtPoint(e.getPoint());

                if (viewRow == -1 || viewCol == -1) return;

                int modelRow = missionsTable.convertRowIndexToModel(viewRow);
                int modelCol = missionsTable.convertColumnIndexToModel(viewCol);

                if (e.getClickCount() == 1 && SwingUtilities.isLeftMouseButton(e)) {
                    if (modelCol == 6 || modelCol == 7) {
                        Mission mission = tableModel.getMissionAt(modelRow);

                        if (modelCol == 6) {
                            BasicMissionReport report = new BasicReportGenerator().generate(mission);
                            SwingUtilities.invokeLater(() -> showMissionDetails(report));
                        } else {
                            SmartTreeReport report = new UniversalTreeGenerator().generate(mission);
                            SwingUtilities.invokeLater(() -> SmartMissionFrame.showInFrame(report));
                        }
                        return;
                    }
                }

                if (e.getClickCount() == 2 && SwingUtilities.isLeftMouseButton(e)) {
                    int row = missionsTable.getSelectedRow();
                    if (row != -1) {
                        int modelRow2 = missionsTable.convertRowIndexToModel(row);
                        String missionId = (String) tableModel.getValueAt(modelRow2, 0);

                        engine.findMissionById(missionId).ifPresent(mission -> {
                            BasicMissionReport report = new BasicReportGenerator().generate(mission);
                            showMissionDetails(report);
                        });
                    }
                }
            }
        });

        importButton.addActionListener(e -> {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setFileFilter(new FileNameExtensionFilter("Mission files (JSON, XML, TXT)", "json", "xml", "txt"));

            int returnValue = fileChooser.showOpenDialog(Main);
            if (returnValue == JFileChooser.APPROVE_OPTION) {
                File selectedFile = fileChooser.getSelectedFile();
                try {
                    engine.importMission(selectedFile);

                    tableModel.fireTableDataChanged();

                    JOptionPane.showMessageDialog(Main, "Миссия успешно добавлена!");
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(Main, "Ошибка при импорте: " + ex.getMessage(), "Ошибка", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
    }

    private void showMissionDetails(BasicMissionReport report) {
        JFrame detailFrame = new JFrame("Детали миссии: " + report.getMissionId());
        detailFrame.setContentPane(new MissionFrame(report).getMainPanel());
        detailFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        detailFrame.pack();
        detailFrame.setLocationRelativeTo(Main);
        detailFrame.setVisible(true);
    }

    private static class ButtonRenderer extends JButton implements TableCellRenderer {
        public ButtonRenderer() {
            setOpaque(true);
        }

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            setText(value == null ? "" : value.toString());
            return this;
        }
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("Управление магическими миссиями");
        frame.setContentPane(new MainFrame().Main);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 600);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    {
// GUI initializer generated by IntelliJ IDEA GUI Designer
// >>> IMPORTANT!! <<<
// DO NOT EDIT OR ADD ANY CODE HERE!
        $$$setupUI$$$();
    }

    /**
     * Method generated by IntelliJ IDEA GUI Designer
     * >>> IMPORTANT!! <<<
     * DO NOT edit this method OR call it in your code!
     *
     * @noinspection ALL
     */
    private void $$$setupUI$$$() {
        Main = new JPanel();
        Main.setLayout(new GridLayoutManager(2, 1, new Insets(0, 0, 0, 0), -1, -1));
        final JPanel panel1 = new JPanel();
        panel1.setLayout(new GridLayoutManager(1, 3, new Insets(0, 0, 0, 0), -1, -1));
        Main.add(panel1, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        importButton = new JButton();
        importButton.setLabel("Импорт");
        importButton.setText("Импорт");
        panel1.add(importButton, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final Spacer spacer1 = new Spacer();
        panel1.add(spacer1, new GridConstraints(0, 2, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, 1, null, null, null, 0, false));
        final JScrollPane scrollPane1 = new JScrollPane();
        Main.add(scrollPane1, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        missionsTable = new JTable();
        scrollPane1.setViewportView(missionsTable);
    }

    /**
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$() {
        return Main;
    }

}
