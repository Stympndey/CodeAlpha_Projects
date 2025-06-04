// src/com/tradingplatform/Transaction.java
package com.tradingplatform;

import java.io.Serializable;
import java.time.LocalDateTime;

public class Transaction implements Serializable {
    private static final long serialVersionUID = 1L;
    private String userId;
    private String symbol;
    private int quantity;
    private double price; // Price at the time of transaction
    private String type;  // "BUY" or "SELL"
    private LocalDateTime timestamp;

    // This constructor needs to be exactly as defined here
    public Transaction(String userId, String symbol, int quantity, double price, String type, LocalDateTime timestamp) {
        this.userId = userId;
        this.symbol = symbol;
        this.quantity = quantity;
        this.price = price;
        this.type = type;
        this.timestamp = timestamp;
    }

    // These getter methods must be present and correctly named
    public String getUserId() {
        return userId;
    }

    public String getSymbol() {
        return symbol;
    }

    public int getQuantity() {
        return quantity;
    }

    public double getPrice() { // Ensure this getter exists
        return price;
    }

    public String getType() {
        return type;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    @Override
    public String toString() {
        return "Transaction{" +
                "userId='" + userId + '\'' +
                ", symbol='" + symbol + '\'' +
                ", quantity=" + quantity +
                ", price=" + price +
                ", type='" + type + '\'' +
                ", timestamp=" + timestamp +
                '}';
    }
}