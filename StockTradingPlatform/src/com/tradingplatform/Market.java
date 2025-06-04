package com.tradingplatform;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class Market {
    private Map<String, Stock> stocks;
    private Random random;

    public Market() {
        stocks = new HashMap<>();
        random = new Random();
        stocks.put("AAPL", new Stock("AAPL", "Apple Inc.", 170.00));
        stocks.put("GOOGL", new Stock("GOOGL", "Alphabet Inc.", 1500.00));
        stocks.put("MSFT", new Stock("MSFT", "Microsoft Corp.", 280.00));
        stocks.put("AMZN", new Stock("AMZN", "Amazon.com Inc.", 140.00));
    }

    public Stock getStock(String symbol) {
        return stocks.get(symbol);
    }

    public Collection<Stock> getAllStocks() {
        return stocks.values();
    }

    public void displayMarketData() {
        System.out.println("\n--- Current Market Data ---");
        System.out.printf("%-10s %-20s %-10s\n", "Symbol", "Company", "Price");
        System.out.println("----------------------------------------");
        for (Stock stock : stocks.values()) {
            System.out.printf("%-10s %-20s $%-9.2f\n", stock.getSymbol(), stock.getCompanyName(), stock.getPrice());
        }
        System.out.println("----------------------------------------");
    }

    public void updateMarketPrices() {
        for (Stock stock : stocks.values()) {
            double currentPrice = stock.getPrice();
            double change = (random.nextDouble() * 0.10 - 0.05) * currentPrice;
            double newPrice = currentPrice + change;
            if (newPrice < 1.0) newPrice = 1.0;
            stock.setPrice(newPrice);
        }
    }
}