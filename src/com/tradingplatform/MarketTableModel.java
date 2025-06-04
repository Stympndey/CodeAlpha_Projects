// src/com/tradingplatform/MarketTableModel.java
package com.tradingplatform;

import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;
import java.util.List;
import java.util.Collection; // Keep this import as market.getAllStocks() returns Collection

public class MarketTableModel extends AbstractTableModel {
    private List<Stock> stocks;
    private final String[] columnNames = {"Symbol", "Company Name", "Price"};
    private Market market;

    public MarketTableModel(Market market) {
        this.market = market;
        this.stocks = new ArrayList<>(market.getAllStocks());
    }

    @Override
    public void fireTableDataChanged() {
        this.stocks = new ArrayList<>(market.getAllStocks());
        super.fireTableDataChanged();
    }

    @Override
    public int getRowCount() {
        return stocks.size();
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
        Stock stock = stocks.get(rowIndex);
        switch (columnIndex) {
            case 0: return stock.getSymbol();
            case 1: return stock.getCompanyName();
            case 2: return String.format("$%.2f", stock.getPrice());
            default: return null;
        }
    }
}