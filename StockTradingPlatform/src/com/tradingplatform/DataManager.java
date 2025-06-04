package com.tradingplatform;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;

public class DataManager {
    private static final String USERS_FILE = "users.dat";
    private static final String TRANSACTIONS_FILE = "transactions.dat";
    private static final String PERFORMANCE_FILE = "performance.dat";

    public static void saveUsers(List<User> users) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(USERS_FILE))) {
            oos.writeObject(users);
            System.out.println("Users data saved successfully.");
        } catch (IOException e) {
            System.err.println("Error saving users data: " + e.getMessage());
        }
    }

    @SuppressWarnings("unchecked")
    public static List<User> loadUsers() {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(USERS_FILE))) {
            System.out.println("Users data loaded successfully.");
            return (List<User>) ois.readObject();
        } catch (FileNotFoundException e) {
            System.out.println("Users data file not found. Starting with no users.");
            return new ArrayList<>();
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Error loading users data: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    public static void saveTransactions(List<Transaction> transactions) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(TRANSACTIONS_FILE))) {
            oos.writeObject(transactions);
            System.out.println("Transaction history saved successfully.");
        } catch (IOException e) {
            System.err.println("Error saving transaction history: " + e.getMessage());
        }
    }

    @SuppressWarnings("unchecked")
    public static List<Transaction> loadTransactions() {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(TRANSACTIONS_FILE))) {
            System.out.println("Transaction history loaded successfully.");
            return (List<Transaction>) ois.readObject();
        } catch (FileNotFoundException e) {
            System.out.println("Transaction history file not found. Starting with no transactions.");
            return new ArrayList<>();
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Error loading transaction history: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    public static void savePerformanceTracker(PortfolioPerformanceTracker tracker) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(PERFORMANCE_FILE))) {
            oos.writeObject(tracker);
            System.out.println("Performance data saved successfully.");
        } catch (IOException e) {
            System.err.println("Error saving performance data: " + e.getMessage());
        }
    }

    public static PortfolioPerformanceTracker loadPerformanceTracker() {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(PERFORMANCE_FILE))) {
            System.out.println("Performance data loaded successfully.");
            return (PortfolioPerformanceTracker) ois.readObject();
        } catch (FileNotFoundException e) {
            System.out.println("Performance data file not found. Starting new performance tracker.");
            return new PortfolioPerformanceTracker();
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Error loading performance data: " + e.getMessage());
            return new PortfolioPerformanceTracker();
        }
    }
}