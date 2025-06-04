package com.tradingplatform;

import javax.swing.*;
import java.awt.*;
import java.util.List;

import com.tradingplatform.MarketTableModel;
import com.tradingplatform.PortfolioTableModel;
import com.tradingplatform.TransactionTableModel;
import com.tradingplatform.PerformanceTableModel;

public class App extends JFrame {
    private Market market;
    private TradingService tradingService;
    private PortfolioPerformanceTracker performanceTracker;
    private DataManager dataManager;
    private User currentUser;
    private JTextArea outputArea;
    private JTextField buySymbolField, buyQuantityField;
    private JTextField sellSymbolField, sellQuantityField;
    private JLabel userLabel;
    private JLabel cashLabel;
    private JTable marketDataTable;
    private MarketTableModel marketTableModel;
    private JTable portfolioTable;
    private PortfolioTableModel portfolioTableModel;
    private JTable transactionTable;
    private TransactionTableModel transactionTableModel;
    private JTable performanceTable;
    private PerformanceTableModel performanceTableModel;

    public App() {
        super("Stock Trading Platform");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1280, 850);
        setLocationRelativeTo(null);
        setResizable(false);
        market = new Market();
        dataManager = new DataManager();
        List<User> loadedUsers = DataManager.loadUsers();
        if (loadedUsers.isEmpty()) {
            String initialUserId = JOptionPane.showInputDialog(this, "Enter your User ID:", "New User", JOptionPane.QUESTION_MESSAGE);
            if (initialUserId == null || initialUserId.trim().isEmpty()) {
                initialUserId = "defaultUser";
            }
            double initialCash = 10000;
            try {
                String cashStr = JOptionPane.showInputDialog(this, "Enter initial cash balance (e.g., 10000):", "New User", JOptionPane.QUESTION_MESSAGE);
                if (cashStr != null && !cashStr.isEmpty()) {
                    initialCash = Double.parseDouble(cashStr);
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Invalid cash amount, defaulting to $10000.", "Input Error", JOptionPane.WARNING_MESSAGE);
            }
            currentUser = new User(initialUserId, initialCash);
            DataManager.saveUsers(List.of(currentUser));
        } else {
            currentUser = loadedUsers.get(0);
            JOptionPane.showMessageDialog(this, "Welcome back, " + currentUser.getUserId() + "!", "Welcome", JOptionPane.INFORMATION_MESSAGE);
        }
        tradingService = new TradingService(market);
        tradingService.setTransactionHistory(DataManager.loadTransactions());
        performanceTracker = DataManager.loadPerformanceTracker();
        if (performanceTracker.getSnapshots().isEmpty()) {
            performanceTracker.recordPerformance(currentUser, market);
        }
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ignored) {}
        JPanel backgroundPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                GradientPaint gp = new GradientPaint(0, 0, new Color(36, 198, 220), getWidth(), getHeight(), new Color(81, 74, 157));
                g2d.setPaint(gp);
                g2d.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        backgroundPanel.setLayout(new BorderLayout());
        JPanel headerPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(new Color(255, 255, 255, 220));
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 40, 40);
                g2.dispose();
            }
        };
        headerPanel.setOpaque(false);
        headerPanel.setLayout(new BoxLayout(headerPanel, BoxLayout.Y_AXIS));
        headerPanel.setBorder(BorderFactory.createEmptyBorder(20, 40, 20, 40));
        JLabel titleLabel = new JLabel("Stock Trading Platform", JLabel.CENTER);
        titleLabel.setFont(new Font("Segoe UI Black", Font.BOLD, 44));
        titleLabel.setForeground(new Color(81, 74, 157));
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        headerPanel.add(titleLabel);
        headerPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        JPanel userInfoPanel = new JPanel();
        userInfoPanel.setOpaque(false);
        userInfoPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 30, 5));
        userInfoPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
        userLabel = new JLabel("User ID: " + currentUser.getUserId());
        userLabel.setFont(new Font("Segoe UI", Font.BOLD, 22));
        userLabel.setForeground(new Color(36, 198, 220));
        cashLabel = new JLabel("Cash: $" + String.format("%.2f", currentUser.getCashBalance()));
        cashLabel.setFont(new Font("Segoe UI", Font.BOLD, 22));
        cashLabel.setForeground(new Color(255, 94, 98));
        JButton saveButton = new JButton("💾 Save Data");
        saveButton.setFont(new Font("Segoe UI", Font.BOLD, 18));
        saveButton.setBackground(new Color(81, 74, 157));
        saveButton.setForeground(Color.WHITE);
        saveButton.setFocusPainted(false);
        saveButton.setBorder(BorderFactory.createEmptyBorder(8, 24, 8, 24));
        saveButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        saveButton.addActionListener(_ -> saveAllData());
        userInfoPanel.add(userLabel);
        userInfoPanel.add(cashLabel);
        userInfoPanel.add(saveButton);
        headerPanel.add(userInfoPanel);
        backgroundPanel.add(headerPanel, BorderLayout.NORTH);
        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.setFont(new Font("Segoe UI", Font.BOLD, 20));
        tabbedPane.setBackground(new Color(240, 248, 255));
        tabbedPane.setForeground(new Color(81, 74, 157));
        java.util.function.Supplier<JPanel> cardPanelSupplier = () -> {
            JPanel card = new JPanel() {
                @Override
                protected void paintComponent(Graphics g) {
                    Graphics2D g2 = (Graphics2D) g.create();
                    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                    g2.setColor(new Color(255, 255, 255, 230));
                    g2.fillRoundRect(0, 0, getWidth(), getHeight(), 30, 30);
                    g2.dispose();
                    super.paintComponent(g);
                }
            };
            card.setOpaque(false);
            card.setLayout(new BorderLayout(10, 10));
            card.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));
            return card;
        };
        JPanel marketTab = cardPanelSupplier.get();
        marketTableModel = new MarketTableModel(market);
        marketDataTable = new JTable(marketTableModel);
        marketDataTable.setFillsViewportHeight(true);
        marketDataTable.setFont(new Font("Segoe UI", Font.PLAIN, 18));
        marketDataTable.setRowHeight(28);
        marketDataTable.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 20));
        marketTab.add(new JScrollPane(marketDataTable), BorderLayout.CENTER);
        JPanel marketButtonsPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        marketButtonsPanel.setOpaque(false);
        JButton refreshMarketButton = new JButton("🔄 Refresh Market Data");
        refreshMarketButton.setFont(new Font("Segoe UI", Font.BOLD, 16));
        refreshMarketButton.setBackground(new Color(36, 198, 220));
        refreshMarketButton.setForeground(Color.WHITE);
        refreshMarketButton.setFocusPainted(false);
        refreshMarketButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        refreshMarketButton.addActionListener(_ -> updateMarketData());
        JButton updateMarketPricesButton = new JButton("💹 Simulate Price Update");
        updateMarketPricesButton.setFont(new Font("Segoe UI", Font.BOLD, 16));
        updateMarketPricesButton.setBackground(new Color(255, 94, 98));
        updateMarketPricesButton.setForeground(Color.WHITE);
        updateMarketPricesButton.setFocusPainted(false);
        updateMarketPricesButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        updateMarketPricesButton.addActionListener(_ -> simulateMarketUpdate());
        marketButtonsPanel.add(refreshMarketButton);
        marketButtonsPanel.add(updateMarketPricesButton);
        marketTab.add(marketButtonsPanel, BorderLayout.SOUTH);
        tabbedPane.addTab("Market Data", marketTab);
        JPanel tradingTab = cardPanelSupplier.get();
        tradingTab.setLayout(new GridLayout(1, 2, 30, 0));
        JPanel buyPanel = new JPanel();
        buyPanel.setOpaque(false);
        buyPanel.setLayout(new BoxLayout(buyPanel, BoxLayout.Y_AXIS));
        buyPanel.setBorder(BorderFactory.createTitledBorder("Buy Stock"));
        JLabel buyLabel = new JLabel("Buy Stock");
        buyLabel.setFont(new Font("Segoe UI", Font.BOLD, 22));
        buyLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        buyPanel.add(buyLabel);
        buyPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        buySymbolField = new JTextField();
        buySymbolField.setFont(new Font("Segoe UI", Font.PLAIN, 18));
        buyPanel.add(new JLabel("Symbol:"));
        buyPanel.add(buySymbolField);
        buyQuantityField = new JTextField();
        buyQuantityField.setFont(new Font("Segoe UI", Font.PLAIN, 18));
        buyPanel.add(new JLabel("Quantity:"));
        buyPanel.add(buyQuantityField);
        JButton buyButton = new JButton("Buy");
        buyButton.setFont(new Font("Segoe UI", Font.BOLD, 18));
        buyButton.setBackground(new Color(36, 198, 220));
        buyButton.setForeground(Color.WHITE);
        buyButton.setFocusPainted(false);
        buyButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        buyButton.addActionListener(_ -> executeBuy());
        buyPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        buyPanel.add(buyButton);
        JPanel sellPanel = new JPanel();
        sellPanel.setOpaque(false);
        sellPanel.setLayout(new BoxLayout(sellPanel, BoxLayout.Y_AXIS));
        sellPanel.setBorder(BorderFactory.createTitledBorder("Sell Stock"));
        JLabel sellLabel = new JLabel("Sell Stock");
        sellLabel.setFont(new Font("Segoe UI", Font.BOLD, 22));
        sellLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        sellPanel.add(sellLabel);
        sellPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        sellSymbolField = new JTextField();
        sellSymbolField.setFont(new Font("Segoe UI", Font.PLAIN, 18));
        sellPanel.add(new JLabel("Symbol:"));
        sellPanel.add(sellSymbolField);
        sellQuantityField = new JTextField();
        sellQuantityField.setFont(new Font("Segoe UI", Font.PLAIN, 18));
        sellPanel.add(new JLabel("Quantity:"));
        sellPanel.add(sellQuantityField);
        JButton sellButton = new JButton("Sell");
        sellButton.setFont(new Font("Segoe UI", Font.BOLD, 18));
        sellButton.setBackground(new Color(255, 94, 98));
        sellButton.setForeground(Color.WHITE);
        sellButton.setFocusPainted(false);
        sellButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        sellButton.addActionListener(_ -> executeSell());
        sellPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        sellPanel.add(sellButton);
        tradingTab.add(buyPanel);
        tradingTab.add(sellPanel);
        tabbedPane.addTab("Trade", tradingTab);
        JPanel portfolioTab = cardPanelSupplier.get();
        portfolioTableModel = new PortfolioTableModel(currentUser, market);
        portfolioTable = new JTable(portfolioTableModel);
        portfolioTable.setFillsViewportHeight(true);
        portfolioTable.setFont(new Font("Segoe UI", Font.PLAIN, 18));
        portfolioTable.setRowHeight(28);
        portfolioTable.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 20));
        portfolioTab.add(new JScrollPane(portfolioTable), BorderLayout.CENTER);
        JPanel portfolioButtonsPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        portfolioButtonsPanel.setOpaque(false);
        JButton refreshPortfolioButton = new JButton("🔄 Refresh Portfolio");
        refreshPortfolioButton.setFont(new Font("Segoe UI", Font.BOLD, 16));
        refreshPortfolioButton.setBackground(new Color(36, 198, 220));
        refreshPortfolioButton.setForeground(Color.WHITE);
        refreshPortfolioButton.setFocusPainted(false);
        refreshPortfolioButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        refreshPortfolioButton.addActionListener(_ -> updatePortfolioData());
        portfolioButtonsPanel.add(refreshPortfolioButton);
        portfolioTab.add(portfolioButtonsPanel, BorderLayout.SOUTH);
        tabbedPane.addTab("Portfolio", portfolioTab);
        JPanel transactionTab = cardPanelSupplier.get();
        transactionTableModel = new TransactionTableModel(tradingService);
        transactionTable = new JTable(transactionTableModel);
        transactionTable.setFillsViewportHeight(true);
        transactionTable.setFont(new Font("Segoe UI", Font.PLAIN, 18));
        transactionTable.setRowHeight(28);
        transactionTable.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 20));
        transactionTab.add(new JScrollPane(transactionTable), BorderLayout.CENTER);
        tabbedPane.addTab("Transactions", transactionTab);
        JPanel performanceTab = cardPanelSupplier.get();
        performanceTableModel = new PerformanceTableModel(performanceTracker);
        performanceTable = new JTable(performanceTableModel);
        performanceTable.setFillsViewportHeight(true);
        performanceTable.setFont(new Font("Segoe UI", Font.PLAIN, 18));
        performanceTable.setRowHeight(28);
        performanceTable.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 20));
        performanceTab.add(new JScrollPane(performanceTable), BorderLayout.CENTER);
        JPanel performanceButtonsPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        performanceButtonsPanel.setOpaque(false);
        JButton recordPerformanceButton = new JButton("📈 Record Performance Snapshot");
        recordPerformanceButton.setFont(new Font("Segoe UI", Font.BOLD, 16));
        recordPerformanceButton.setBackground(new Color(81, 74, 157));
        recordPerformanceButton.setForeground(Color.WHITE);
        recordPerformanceButton.setFocusPainted(false);
        recordPerformanceButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        recordPerformanceButton.addActionListener(_ -> recordPerformance());
        performanceButtonsPanel.add(recordPerformanceButton);
        performanceTab.add(performanceButtonsPanel, BorderLayout.SOUTH);
        tabbedPane.addTab("Performance", performanceTab);
        backgroundPanel.add(tabbedPane, BorderLayout.CENTER);
        outputArea = new JTextArea(5, 40);
        outputArea.setEditable(false);
        outputArea.setFont(new Font("Segoe UI", Font.PLAIN, 18));
        outputArea.setBackground(new Color(240, 248, 255));
        outputArea.setForeground(new Color(81, 74, 157));
        JScrollPane scrollPane = new JScrollPane(outputArea);
        scrollPane.setBorder(BorderFactory.createEmptyBorder(10, 30, 10, 30));
        backgroundPanel.add(scrollPane, BorderLayout.SOUTH);
        setContentPane(backgroundPanel);
        setVisible(true);
        updateMarketData();
        updatePortfolioData();
        transactionTableModel.fireTableDataChanged();
        performanceTableModel.fireTableDataChanged();
    }
    private void updateMarketData() {
        marketTableModel.fireTableDataChanged();
        appendOutput("Market data refreshed.");
    }
    private void simulateMarketUpdate() {
        market.updateMarketPrices();
        updateMarketData();
        updatePortfolioData();
        performanceTracker.recordPerformance(currentUser, market);
        performanceTableModel.fireTableDataChanged();
        appendOutput("Market prices simulated and updated.");
    }
    private void executeBuy() {
        String symbol = buySymbolField.getText().trim().toUpperCase();
        String quantityStr = buyQuantityField.getText().trim();
        if (symbol.isEmpty() || quantityStr.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter both symbol and quantity.", "Input Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        try {
            int quantity = Integer.parseInt(quantityStr);
            tradingService.buyStock(currentUser, symbol, quantity);
            appendOutput("Attempted to buy " + quantity + " of " + symbol + ".");
            updatePortfolioData();
            transactionTableModel.fireTableDataChanged();
            performanceTracker.recordPerformance(currentUser, market);
            performanceTableModel.fireTableDataChanged();
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Please enter a valid whole number for quantity.", "Input Error", JOptionPane.ERROR_MESSAGE);
        }
        buySymbolField.setText("");
        buyQuantityField.setText("");
    }
    private void executeSell() {
        String symbol = sellSymbolField.getText().trim().toUpperCase();
        String quantityStr = sellQuantityField.getText().trim();
        if (symbol.isEmpty() || quantityStr.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter both symbol and quantity.", "Input Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        try {
            int quantity = Integer.parseInt(quantityStr);
            tradingService.sellStock(currentUser, symbol, quantity);
            appendOutput("Attempted to sell " + quantity + " of " + symbol + ".");
            updatePortfolioData();
            transactionTableModel.fireTableDataChanged();
            performanceTracker.recordPerformance(currentUser, market);
            performanceTableModel.fireTableDataChanged();
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Please enter a valid whole number for quantity.", "Input Error", JOptionPane.ERROR_MESSAGE);
        }
        sellSymbolField.setText("");
        sellQuantityField.setText("");
    }
    private void updatePortfolioData() {
        portfolioTableModel.fireTableDataChanged();
        appendOutput("Portfolio data refreshed.");
        updateCashDisplay();
    }
    private void updateCashDisplay() {
        cashLabel.setText("Cash: $" + String.format("%.2f", currentUser.getCashBalance()));
    }
    private void recordPerformance() {
        performanceTracker.recordPerformance(currentUser, market);
        performanceTableModel.fireTableDataChanged();
        appendOutput("Portfolio performance snapshot recorded.");
    }
    private void saveAllData() {
        DataManager.saveUsers(List.of(currentUser));
        DataManager.saveTransactions(tradingService.getTransactionHistory());
        DataManager.savePerformanceTracker(performanceTracker);
        appendOutput("All data saved successfully.");
    }
    private void appendOutput(String message) {
        outputArea.append(message + "\n");
        outputArea.setCaretPosition(outputArea.getDocument().getLength());
    }
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new App());
    }
}