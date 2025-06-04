// src/com/tradingplatform/User.java
package com.tradingplatform;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class User {
    private String userId;
    private double cashBalance;
    private Map<String, Integer> portfolio; // Stock Symbol -> Quantity

    public User(String userId, double initialCash) {
        this.userId = userId;
        this.cashBalance = initialCash;
        this.portfolio = new HashMap<>();
    }

    public String getUserId() {
        return userId;
    }

    public double getCashBalance() {
        return cashBalance;
    }

    public Map<String, Integer> getPortfolio() {
        return Collections.unmodifiableMap(portfolio); // Return an unmodifiable map for safety
    }

    public void addCash(double amount) {
        this.cashBalance += amount;
    }

    public void deductCash(double amount) {
        this.cashBalance -= amount;
    }

    public void addStock(String symbol, int quantity) {
        portfolio.put(symbol, portfolio.getOrDefault(symbol, 0) + quantity);
    }

    public void removeStock(String symbol, int quantity) {
        int currentQuantity = portfolio.getOrDefault(symbol, 0);
        if (currentQuantity >= quantity) {
            portfolio.put(symbol, currentQuantity - quantity);
            if (portfolio.get(symbol) == 0) {
                portfolio.remove(symbol); // Remove if quantity becomes zero
            }
        }
    }

    public boolean hasStock(String symbol, int quantity) {
        return portfolio.getOrDefault(symbol, 0) >= quantity;
    }

    public void displayPortfolio(Market market) {
        System.out.println("\n--- " + userId + "'s Portfolio ---");
        System.out.printf("Cash Balance: $%.2f\n", cashBalance);
        System.out.println("\n--- Stock Holdings ---");
        if (portfolio.isEmpty()) {
            System.out.println("No stocks in portfolio.");
            return;
        }

        System.out.printf("%-10s %-10s %-10s %-15s\n", "Symbol", "Quantity", "Current Price", "Total Value");
        System.out.println("--------------------------------------------------");
        double totalPortfolioValue = 0;
        for (Map.Entry<String, Integer> entry : portfolio.entrySet()) {
            String symbol = entry.getKey();
            int quantity = entry.getValue();
            Stock stock = market.getStock(symbol); // Uses market.getStock(), NOT getStockBySymbol()
            if (stock != null) {
                double currentPrice = stock.getPrice();
                double stockValue = currentPrice * quantity;
                totalPortfolioValue += stockValue;
                System.out.printf("%-10s %-10d $%-9.2f $%-14.2f\n", symbol, quantity, currentPrice, stockValue);
            } else {
                System.out.printf("%-10s %-10d Price N/A    $%-14.2f (Stock not found in market)\n", symbol, quantity, 0.0);
            }
        }
        System.out.println("--------------------------------------------------");
        System.out.printf("Total Stock Value: $%.2f\n", totalPortfolioValue);
        System.out.printf("Overall Portfolio Value (Cash + Stocks): $%.2f\n", cashBalance + totalPortfolioValue);
        System.out.println("--------------------------------------------------");
    }
}