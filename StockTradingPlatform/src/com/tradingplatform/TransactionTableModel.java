package com.tradingplatform;

import javax.swing.table.AbstractTableModel;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class TransactionTableModel extends AbstractTableModel {
    private final String[] columnNames = {"User ID", "Type", "Symbol", "Quantity", "Price", "Timestamp"};
    private TradingService tradingService;
    private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public TransactionTableModel(TradingService tradingService) {
        this.tradingService = tradingService;
    }

    @Override
    public String getColumnName(int col) {
        return columnNames[col];
    }

    @Override
    public int getRowCount() {
        return tradingService.getTransactionHistory().size();
    }

    @Override
    public int getColumnCount() {
        return columnNames.length;
    }

    @Override
    public Object getValueAt(int row, int col) {
        List<Transaction> transactions = tradingService.getTransactionHistory();
        Transaction transaction = transactions.get(row);

        switch (col) {
            case 0: return transaction.getUserId();
            case 1: return transaction.getType();
            case 2: return transaction.getSymbol();
            case 3: return transaction.getQuantity();
            case 4: return String.format("$%.2f", transaction.getPrice());
            case 5: return transaction.getTimestamp().format(formatter);
            default: return null;
        }
    }
}