package gui;

import models.Mission;

import javax.swing.table.AbstractTableModel;
import java.util.List;

public class MissionTableModel extends AbstractTableModel {
    private final String[] columnNames = {"ID", "Дата", "Локация", "Результат", "Ущерб", "Комментарий"};
    private List<Mission> missions;

    public MissionTableModel(List<Mission> missions) {
        this.missions = missions;
    }

    public void setMissions(List<Mission> missions) {
        this.missions = missions;
        fireTableDataChanged();
    }

    @Override
    public int getRowCount() {
        return missions.size();
    }

    @Override
    public int getColumnCount() {
        return columnNames.length;
    }

    @Override
    public String getColumnName(int col) {
        return columnNames[col];
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        Mission mission = missions.get(rowIndex);
        return switch (columnIndex) {
            case 0 -> mission.getMissionId();
            case 1 -> mission.getDate();
            case 2 -> mission.getLocation();
            case 3 -> mission.getOutcome();
            case 4 -> String.format("%,d", mission.getDamageCost());
            case 5 -> mission.getComment();
            default -> null;
        };
    }
}