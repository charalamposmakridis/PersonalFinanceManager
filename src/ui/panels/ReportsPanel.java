package ui.panels;

import models.Transaction;
import models.User;
import services.TransactionService;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public class ReportsPanel extends JPanel {

    private final User loggedInUser;
    private final TransactionService transactionService;

    private JTextField startDateField;
    private JTextField endDateField;

    private JLabel incomeValueLabel;
    private JLabel expensesValueLabel;
    private JLabel balanceValueLabel;

    private JTable reportTable;
    private DefaultTableModel tableModel;

    private List<Transaction> currentReportTransactions;

    public ReportsPanel(User loggedInUser) {
        this.loggedInUser = loggedInUser;
        this.transactionService = new TransactionService();

        initializeComponents();
    }

    private void initializeComponents() {
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        JLabel titleLabel = new JLabel("Reports");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));

        JPanel filterPanel = createFilterPanel();
        JPanel summaryPanel = createSummaryPanel();

        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.add(titleLabel, BorderLayout.NORTH);
        topPanel.add(filterPanel, BorderLayout.CENTER);
        topPanel.add(summaryPanel, BorderLayout.SOUTH);

        tableModel = new DefaultTableModel(
                new Object[]{"ID", "Type", "Amount", "Description", "Date"},
                0
        ) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        reportTable = new JTable(tableModel);

        add(topPanel, BorderLayout.NORTH);
        add(new JScrollPane(reportTable), BorderLayout.CENTER);
    }

    private JPanel createFilterPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT));

        startDateField = new JTextField(10);
        endDateField = new JTextField(10);

        startDateField.setText(LocalDate.now().withDayOfMonth(1).toString());
        endDateField.setText(LocalDate.now().toString());

        JButton generateButton = new JButton("Generate");
        JButton exportButton = new JButton("Export CSV");

        generateButton.addActionListener(e -> generateReport());
        exportButton.addActionListener(e -> exportToCsv());

        panel.add(new JLabel("Start Date:"));
        panel.add(startDateField);
        panel.add(new JLabel("End Date:"));
        panel.add(endDateField);
        panel.add(generateButton);
        panel.add(exportButton);

        return panel;
    }

    private JPanel createSummaryPanel() {
        JPanel panel = new JPanel(new GridLayout(1, 3, 15, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));

        incomeValueLabel = new JLabel("0.00", SwingConstants.CENTER);
        expensesValueLabel = new JLabel("0.00", SwingConstants.CENTER);
        balanceValueLabel = new JLabel("0.00", SwingConstants.CENTER);

        panel.add(createSummaryCard("Income", incomeValueLabel));
        panel.add(createSummaryCard("Expenses", expensesValueLabel));
        panel.add(createSummaryCard("Balance", balanceValueLabel));

        return panel;
    }

    private JPanel createSummaryCard(String title, JLabel valueLabel) {
        JPanel card = new JPanel(new BorderLayout());
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.LIGHT_GRAY),
                BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));

        JLabel titleLabel = new JLabel(title, SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 14));

        valueLabel.setFont(new Font("Arial", Font.BOLD, 18));

        card.add(titleLabel, BorderLayout.NORTH);
        card.add(valueLabel, BorderLayout.CENTER);

        return card;
    }

    private void generateReport() {
        try {
            LocalDate startDate = LocalDate.parse(startDateField.getText().trim());
            LocalDate endDate = LocalDate.parse(endDateField.getText().trim());

            BigDecimal income = transactionService.getIncomeByDateRange(
                    loggedInUser.getId(),
                    startDate,
                    endDate
            );

            BigDecimal expenses = transactionService.getExpensesByDateRange(
                    loggedInUser.getId(),
                    startDate,
                    endDate
            );

            BigDecimal balance = transactionService.getBalanceByDateRange(
                    loggedInUser.getId(),
                    startDate,
                    endDate
            );

            incomeValueLabel.setText(income.toString());
            expensesValueLabel.setText(expenses.toString());
            balanceValueLabel.setText(balance.toString());

            currentReportTransactions = transactionService.getTransactionsByDateRange(
                    loggedInUser.getId(),
                    startDate,
                    endDate
            );

            loadTable(currentReportTransactions);

        } catch (Exception e) {
            JOptionPane.showMessageDialog(
                    this,
                    "Could not generate report. Use date format YYYY-MM-DD.",
                    "Report Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    private void loadTable(List<Transaction> transactions) {
        tableModel.setRowCount(0);

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

    private void exportToCsv() {
        if (currentReportTransactions == null || currentReportTransactions.isEmpty()) {
            JOptionPane.showMessageDialog(
                    this,
                    "Generate a report first.",
                    "No Report",
                    JOptionPane.WARNING_MESSAGE
            );
            return;
        }

        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Save CSV Report");
        fileChooser.setSelectedFile(new File("finance_report.csv"));

        int result = fileChooser.showSaveDialog(this);

        if (result == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();

            if (!file.getName().toLowerCase().endsWith(".csv")) {
                file = new File(file.getAbsolutePath() + ".csv");
            }

            try {
                writeCsv(file);

                JOptionPane.showMessageDialog(
                        this,
                        "CSV exported successfully.",
                        "Export Successful",
                        JOptionPane.INFORMATION_MESSAGE
                );

            } catch (Exception e) {
                JOptionPane.showMessageDialog(
                        this,
                        "Could not export CSV.",
                        "Export Error",
                        JOptionPane.ERROR_MESSAGE
                );
            }
        }
    }

    private void writeCsv(File file) throws Exception {
        try (PrintWriter writer = new PrintWriter(new FileWriter(file))) {
            writer.println("ID,Type,Amount,Description,Date");

            for (Transaction transaction : currentReportTransactions) {
                writer.printf(
                        "%d,%s,%s,%s,%s%n",
                        transaction.getId(),
                        transaction.getType(),
                        transaction.getAmount(),
                        escapeCsv(transaction.getDescription()),
                        transaction.getTransactionDate()
                );
            }
        }
    }

    private String escapeCsv(String value) {
        if (value == null) {
            return "";
        }

        String escaped = value.replace("\"", "\"\"");

        if (escaped.contains(",") || escaped.contains("\"") || escaped.contains("\n")) {
            return "\"" + escaped + "\"";
        }

        return escaped;
    }
}