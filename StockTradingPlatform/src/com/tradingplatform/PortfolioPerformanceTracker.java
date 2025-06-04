// src/com/tradingplatform/PortfolioPerformanceTracker.java
package com.tradingplatform;

import java.util.Map;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Collections; // This import was previously missing and caused errors

public class PortfolioPerformanceTracker implements Serializable {
    private static final long serialVersionUID = 1L;
    private List<PerformanceSnapshot> performanceSnapshots;

    public PortfolioPerformanceTracker() {
        this.performanceSnapshots = new ArrayList<>();
    }

    // This method is required by App.java
    public void recordPerformance(User user, Market market) {
        double totalPortfolioValue = user.getCashBalance();
        for (Map.Entry<String, Integer> entry : user.getPortfolio().entrySet()) {
            String symbol = entry.getKey();
            int quantity = entry.getValue();
            Stock stock = market.getStock(symbol);
            if (stock != null) {
                totalPortfolioValue += stock.getPrice() * quantity;
            }
        }
        this.performanceSnapshots.add(new PerformanceSnapshot(totalPortfolioValue, LocalDateTime.now()));
    }

    // This method is required by App.java and PerformanceTableModel
    public List<PerformanceSnapshot> getSnapshots() {
        return Collections.unmodifiableList(performanceSnapshots);
    }

    // This is the static nested class that represents a snapshot
    // It must be public static to be accessible by DataManager and other classes
    public static class PerformanceSnapshot implements Serializable { // Ensure 'public static'
        private static final long serialVersionUID = 1L;
        private double totalPortfolioValue;
        private LocalDateTime timestamp;

        public PerformanceSnapshot(double totalPortfolioValue, LocalDateTime timestamp) {
            this.totalPortfolioValue = totalPortfolioValue;
            this.timestamp = timestamp;
        }

        public double getTotalPortfolioValue() { // Ensure this getter exists and is correctly named
            return totalPortfolioValue;
        }

        public LocalDateTime getTimestamp() {
            return timestamp;
        }

        @Override
        public String toString() {
            return "PerformanceSnapshot{" +
                   "totalPortfolioValue=" + String.format("%.2f", totalPortfolioValue) +
                   ", timestamp=" + timestamp +
                   '}';
        }
    }
}