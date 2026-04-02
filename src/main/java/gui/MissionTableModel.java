package gui;

import logic.MissionStore;
import logic.MissionStoreListener;
import models.Mission;
import specifications.MissionSpecification;

import javax.swing.SwingUtilities;
import javax.swing.table.AbstractTableModel;
import java.util.List;

public class MissionTableModel extends AbstractTableModel implements MissionStoreListener {
    private final String[] columnNames = {"ID", "Дата", "Локация", "Результат", "Ущерб", "Комментарий", "Панель", "Дерево"};
    private final MissionStore store;

    private List<Mission> displayList;
    private MissionSpecification currentFilter = null;

    public MissionTableModel(MissionStore store) {
        this.store = store;
        this.displayList = store.getAll();
        this.store.addListener(this);
    }

    public void setFilter(MissionSpecification spec) {
        this.currentFilter = spec;
        updateData();
    }

    private void updateData() {
        if (currentFilter == null) {
            displayList = store.getAll();
        } else {
            displayList = store.find(currentFilter);
        }
        fireTableDataChanged();
    }

    @Override
    public int getRowCount() {
        return displayList.size();
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
        Mission mission = displayList.get(rowIndex);
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
        return displayList.get(row);
    }

    @Override
    public void onStoreChanged() {
        Runnable updateTask = this::updateData;
        if (SwingUtilities.isEventDispatchThread()) {
            updateTask.run();
        } else {
            SwingUtilities.invokeLater(updateTask);
        }
    }
}