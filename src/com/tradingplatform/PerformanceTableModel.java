// src/com/tradingplatform/PerformanceTableModel.java
package com.tradingplatform;

import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;
import java.util.List;
import java.time.format.DateTimeFormatter;

public class PerformanceTableModel extends AbstractTableModel {
    private List<PortfolioPerformanceTracker.PerformanceSnapshot> snapshots;
    private final String[] columnNames = {"Timestamp", "Total Portfolio Value"};
    private PortfolioPerformanceTracker performanceTracker;

    public PerformanceTableModel(PortfolioPerformanceTracker tracker) {
        this.performanceTracker = tracker;
        this.snapshots = new ArrayList<>(performanceTracker.getSnapshots());
    }

    @Override
    public void fireTableDataChanged() {
        this.snapshots = new ArrayList<>(performanceTracker.getSnapshots());
        super.fireTableDataChanged();
    }

    @Override
    public int getRowCount() {
        return snapshots.size();
    }

    @Override
    public int getColumnCount() {
        return columnNames.length;
    }

    @Override
    public String getColumnName(int column) {
        return columnNames[column];
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        PortfolioPerformanceTracker.PerformanceSnapshot snapshot = snapshots.get(rowIndex);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        switch (columnIndex) {
            case 0: return snapshot.getTimestamp().format(formatter);
            case 1: return String.format("$%.2f", snapshot.getTotalPortfolioValue()); // <--- ENSURE THIS IS getTotalPortfolioValue()
            default: return null;
        }
    }
}