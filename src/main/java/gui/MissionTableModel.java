package gui;

import logic.MissionStore;
import logic.MissionStoreListener;
import models.Mission;

import javax.swing.SwingUtilities;
import javax.swing.table.AbstractTableModel;

public class MissionTableModel extends AbstractTableModel implements MissionStoreListener {
    private final String[] columnNames = {"ID", "Дата", "Локация", "Результат", "Ущерб", "Комментарий", "Панель", "Дерево"};
    private final MissionStore store;

    public MissionTableModel(MissionStore store) {
        this.store = store;
        this.store.addListener(this);
    }

    @Override
    public int getRowCount() {
        return store.size();
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
        Mission mission = store.get(rowIndex);
        return switch (columnIndex) {
            case 0 -> mission.getMissionId();
            case 1 -> mission.getDate();
            case 2 -> mission.getLocation();
            case 3 -> mission.getOutcome();
            case 4 -> String.format("%,d", mission.getDamageCost());
            case 5 -> mission.getComment();
            case 6 -> "Панель";
            case 7 -> "Дерево";
            default -> null;
        };
    }

    public Mission getMissionAt(int row) {
        return store.get(row);
    }

    @Override
    public void onStoreChanged() {
        if (SwingUtilities.isEventDispatchThread()) {
            fireTableDataChanged();
        } else {
            SwingUtilities.invokeLater(this::fireTableDataChanged);
        }
    }
}