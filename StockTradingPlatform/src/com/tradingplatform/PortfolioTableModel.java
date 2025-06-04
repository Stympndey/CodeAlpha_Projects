package com.tradingplatform;

import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class PortfolioTableModel extends AbstractTableModel {
    private final String[] columnNames = {"Symbol", "Quantity", "Current Price", "Total Value"};
    private User user;
    private Market market;
    private List<Map.Entry<String, Integer>> portfolioData; // To store sorted entries

    public PortfolioTableModel(User user, Market market) {
        this.user = user;
        this.market = market;
        updatePortfolioData();
    }

    private void updatePortfolioData() {
        portfolioData = new ArrayList<>(user.getPortfolio().entrySet());
        portfolioData.sort((e1, e2) -> e1.getKey().compareTo(e2.getKey()));
    }

    @Override
    public String getColumnName(int col) {
        return columnNames[col];
    }

    @Override
    public int getRowCount() {
        updatePortfolioData(); // Ensure data is fresh before counting rows
        return portfolioData.size();
    }

    @Override
    public int getColumnCount() {
        return columnNames.length;
    }

    @Override
    public Object getValueAt(int row, int col) {
        Map.Entry<String, Integer> entry = portfolioData.get(row);
        String symbol = entry.getKey();
        int quantity = entry.getValue();
        Stock stock = market.getStock(symbol); // Get current price from market

        switch (col) {
            case 0: return symbol;
            case 1: return quantity;
            case 2: return (stock != null) ? String.format("$%.2f", stock.getPrice()) : "N/A";
            case 3: return (stock != null) ? String.format("$%.2f", stock.getPrice() * quantity) : "N/A";
            default: return null;
        }
    }

    @Override
    public void fireTableDataChanged() {
        updatePortfolioData(); // Re-fetch current portfolio data
        super.fireTableDataChanged();
    }
}