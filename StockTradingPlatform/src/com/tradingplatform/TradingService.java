// src/com/tradingplatform/TradingService.java
package com.tradingplatform;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class TradingService {
    private Market market;
    private List<Transaction> transactionHistory;

    public TradingService(Market market) {
        this.market = market;
        this.transactionHistory = new ArrayList<>();
    }

    public void setTransactionHistory(List<Transaction> transactions) {
        if (transactions != null) {
            this.transactionHistory = new ArrayList<>(transactions);
        }
    }

    public List<Transaction> getTransactionHistory() {
        return Collections.unmodifiableList(transactionHistory);
    }

    public void buyStock(User user, String symbol, int quantity) {
        Stock stock = market.getStock(symbol);
        if (stock == null) {
            System.out.println("Error: Stock with symbol " + symbol + " not found.");
            return;
        }

        if (quantity <= 0) {
            System.out.println("Error: Quantity must be positive.");
            return;
        }

        double totalPrice = stock.getPrice() * quantity;

        if (user.getCashBalance() >= totalPrice) {
            user.deductCash(totalPrice);
            user.addStock(symbol, quantity);
            transactionHistory.add(new Transaction(user.getUserId(), symbol, quantity, stock.getPrice(), "BUY", LocalDateTime.now()));
            System.out.printf("Successfully bought %d shares of %s for $%.2f. New cash balance: $%.2f\n",
                    quantity, symbol, totalPrice, user.getCashBalance());
        } else {
            System.out.println("Insufficient cash to buy " + quantity + " shares of " + symbol + ".");
        }
    }

    public void sellStock(User user, String symbol, int quantity) {
        Stock stock = market.getStock(symbol);
        if (stock == null) {
            System.out.println("Error: Stock with symbol " + symbol + " not found.");
            return;
        }

        if (quantity <= 0) {
            System.out.println("Error: Quantity must be positive.");
            return;
        }

        if (user.hasStock(symbol, quantity)) {
            double revenue = stock.getPrice() * quantity;
            user.addCash(revenue);
            user.removeStock(symbol, quantity);
            transactionHistory.add(new Transaction(user.getUserId(), symbol, quantity, stock.getPrice(), "SELL", LocalDateTime.now()));
            System.out.printf("Successfully sold %d shares of %s for $%.2f. New cash balance: $%.2f\n",
                    quantity, symbol, revenue, user.getCashBalance());
        } else {
            System.out.println("You do not have enough shares of " + symbol + " to sell.");
        }
    }

    public void displayTransactionHistory() {
        if (transactionHistory.isEmpty()) {
            System.out.println("\nNo transactions recorded yet.");
            return;
        }
        System.out.println("\n--- Transaction History ---");
        System.out.printf("%-15s %-10s %-10s %-10s %-10s %-20s\n",
                "User ID", "Type", "Symbol", "Quantity", "Price", "Timestamp");
        System.out.println("----------------------------------------------------------------------");
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        for (Transaction transaction : transactionHistory) {
            System.out.printf("%-15s %-10s %-10s %-10d $%-9.2f %-20s\n",
                    transaction.getUserId(),
                    transaction.getType(),
                    transaction.getSymbol(),
                    transaction.getQuantity(),
                    transaction.getPrice(),
                    transaction.getTimestamp().format(formatter));
        }
        System.out.println("----------------------------------------------------------------------");
    }
}