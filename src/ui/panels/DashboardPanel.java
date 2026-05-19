package ui.panels;

import models.Transaction;
import models.User;
import services.TransactionService;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.math.BigDecimal;
import java.util.List;

public class DashboardPanel extends JPanel {

    private final User loggedInUser;
    private final TransactionService transactionService;

    private JLabel incomeValueLabel;
    private JLabel expensesValueLabel;
    private JLabel balanceValueLabel;

    private JTable recentTransactionsTable;
    private DefaultTableModel tableModel;

    public DashboardPanel(User loggedInUser) {
        this.loggedInUser = loggedInUser;
        this.transactionService = new TransactionService();

        initializeComponents();
        loadDashboardData();
    }

    private void initializeComponents() {
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        JLabel titleLabel = new JLabel("Dashboard");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));

        JPanel statsPanel = new JPanel(new GridLayout(1, 3, 15, 15));

        incomeValueLabel = new JLabel("0.00", SwingConstants.CENTER);
        expensesValueLabel = new JLabel("0.00", SwingConstants.CENTER);
        balanceValueLabel = new JLabel("0.00", SwingConstants.CENTER);

        statsPanel.add(createStatCard("Total Income", incomeValueLabel));
        statsPanel.add(createStatCard("Total Expenses", expensesValueLabel));
        statsPanel.add(createStatCard("Balance", balanceValueLabel));

        tableModel = new DefaultTableModel(
                new Object[]{"ID", "Type", "Amount", "Description", "Date"},
                0
        ) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        recentTransactionsTable = new JTable(tableModel);
        recentTransactionsTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.setBorder(BorderFactory.createEmptyBorder(20, 0, 0, 0));

        JLabel recentLabel = new JLabel("Recent Transactions");
        recentLabel.setFont(new Font("Arial", Font.BOLD, 18));

        centerPanel.add(recentLabel, BorderLayout.NORTH);
        centerPanel.add(new JScrollPane(recentTransactionsTable), BorderLayout.CENTER);

        add(titleLabel, BorderLayout.NORTH);
        add(statsPanel, BorderLayout.CENTER);
        add(centerPanel, BorderLayout.SOUTH);
    }

    private JPanel createStatCard(String title, JLabel valueLabel) {
        JPanel card = new JPanel(new BorderLayout());
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.LIGHT_GRAY),
                BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));

        JLabel titleLabel = new JLabel(title, SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 16));

        valueLabel.setFont(new Font("Arial", Font.BOLD, 22));

        card.add(titleLabel, BorderLayout.NORTH);
        card.add(valueLabel, BorderLayout.CENTER);

        return card;
    }

    private void loadDashboardData() {
        try {
            BigDecimal income = transactionService.getTotalIncomeByUserId(loggedInUser.getId());
            BigDecimal expenses = transactionService.getTotalExpensesByUserId(loggedInUser.getId());
            BigDecimal balance = transactionService.getBalanceByUserId(loggedInUser.getId());

            incomeValueLabel.setText(income.toString());
            expensesValueLabel.setText(expenses.toString());
            balanceValueLabel.setText(balance.toString());

            loadRecentTransactions();

        } catch (Exception e) {
            JOptionPane.showMessageDialog(
                    this,
                    "Could not load dashboard data.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    private void loadRecentTransactions() {
        tableModel.setRowCount(0);

        List<Transaction> transactions =
                transactionService.getRecentTransactionsByUserId(loggedInUser.getId(), 5);

        for (Transaction transaction : transactions) {
            tableModel.addRow(new Object[]{
                    transaction.getId(),
                    transaction.getType(),
                    transaction.getAmount(),
                    transaction.getDescription(),
                    transaction.getTransactionDate()
            });
        }
    }
}