package ui.panels;

import ui.dialogs.AddTransactionsDialog;

import models.Transaction;
import models.User;
import services.TransactionService;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class TransactionsPanel extends JPanel {

    private final User loggedInUser;
    private final TransactionService transactionService;

    private JTable transactionsTable;
    private DefaultTableModel tableModel;

    public TransactionsPanel(User loggedInUser) {
        this.loggedInUser = loggedInUser;
        this.transactionService = new TransactionService();

        initializeComponents();
        loadTransactions();
    }

    private void initializeComponents() {
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        JLabel titleLabel = new JLabel("Transactions");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));

        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.add(titleLabel, BorderLayout.WEST);
        topPanel.add(createButtonPanel(), BorderLayout.EAST);

        tableModel = new DefaultTableModel(
                new Object[]{"ID", "Category ID", "Type", "Amount", "Description", "Date"},
                0
        ) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        transactionsTable = new JTable(tableModel);
        transactionsTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        JScrollPane scrollPane = new JScrollPane(transactionsTable);

        add(topPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
    }

    private JPanel createButtonPanel() {
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));

        JButton addButton = new JButton("Add");
        JButton deleteButton = new JButton("Delete");
        JButton refreshButton = new JButton("Refresh");

        addButton.addActionListener(e -> openAddTransactionsDialog());
        deleteButton.addActionListener(e -> deleteSelectedTransaction());
        refreshButton.addActionListener(e -> loadTransactions());

        buttonPanel.add(addButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(refreshButton);

        return buttonPanel;
    }

    private void loadTransactions() {
        tableModel.setRowCount(0);

        try {
            List<Transaction> transactions =
                    transactionService.getTransactionsByUserId(loggedInUser.getId());

            for (Transaction transaction : transactions) {
                tableModel.addRow(new Object[]{
                        transaction.getId(),
                        transaction.getCategoryId(),
                        transaction.getType(),
                        transaction.getAmount(),
                        transaction.getDescription(),
                        transaction.getTransactionDate()
                });
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(
                    this,
                    "Could not load transactions.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    private void deleteSelectedTransaction() {
        int selectedRow = transactionsTable.getSelectedRow();

        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(
                    this,
                    "Please select a transaction first.",
                    "No Selection",
                    JOptionPane.WARNING_MESSAGE
            );
            return;
        }

        int transactionId = (int) tableModel.getValueAt(selectedRow, 0);

        int choice = JOptionPane.showConfirmDialog(
                this,
                "Are you sure you want to delete this transaction?",
                "Delete Transaction",
                JOptionPane.YES_NO_OPTION
        );

        if (choice == JOptionPane.YES_OPTION) {
            try {
                transactionService.deleteTransaction(transactionId, loggedInUser.getId());
                loadTransactions();

            } catch (Exception e) {
                JOptionPane.showMessageDialog(
                        this,
                        "Could not delete transaction.",
                        "Error",
                        JOptionPane.ERROR_MESSAGE
                );
            }
        }
    }

    private void openAddTransactionsDialog() {

        AddTransactionsDialog dialog =
                new AddTransactionsDialog(
                        (JFrame) SwingUtilities.getWindowAncestor(this),
                        loggedInUser
                );

        dialog.setVisible(true);

        if (dialog.isTransactionAdded()) {
            loadTransactions();
        }
    }

    private void showAddTransactionMessage() {
        JOptionPane.showMessageDialog(
                this,
                "Add Transaction dialog will be implemented next.",
                "Add Transaction",
                JOptionPane.INFORMATION_MESSAGE
        );
    }
}