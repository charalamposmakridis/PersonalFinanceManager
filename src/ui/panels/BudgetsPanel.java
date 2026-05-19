package ui.panels;

import models.Budget;
import models.Category;
import models.User;
import services.BudgetService;
import services.CategoryService;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.math.BigDecimal;
import java.time.Year;
import java.util.List;

public class BudgetsPanel extends JPanel {

    private final User loggedInUser;
    private final BudgetService budgetService;
    private final CategoryService categoryService;

    private JTable budgetsTable;
    private DefaultTableModel tableModel;

    public BudgetsPanel(User loggedInUser) {
        this.loggedInUser = loggedInUser;
        this.budgetService = new BudgetService();
        this.categoryService = new CategoryService();

        initializeComponents();
        loadBudgets();
    }

    private void initializeComponents() {
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        JLabel titleLabel = new JLabel("Budgets");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));

        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.add(titleLabel, BorderLayout.WEST);
        topPanel.add(createButtonPanel(), BorderLayout.EAST);

        tableModel = new DefaultTableModel(
                new Object[]{"ID", "Category ID", "Month", "Year", "Limit Amount"},
                0
        ) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        budgetsTable = new JTable(tableModel);
        budgetsTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        add(topPanel, BorderLayout.NORTH);
        add(new JScrollPane(budgetsTable), BorderLayout.CENTER);
    }

    private JPanel createButtonPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.RIGHT));

        JButton addButton = new JButton("Add");
        JButton deleteButton = new JButton("Delete");
        JButton refreshButton = new JButton("Refresh");

        addButton.addActionListener(e -> addBudget());
        deleteButton.addActionListener(e -> deleteSelectedBudget());
        refreshButton.addActionListener(e -> loadBudgets());

        panel.add(addButton);
        panel.add(deleteButton);
        panel.add(refreshButton);

        return panel;
    }

    private void loadBudgets() {
        tableModel.setRowCount(0);

        try {
            List<Budget> budgets = budgetService.getBudgetsByUserId(loggedInUser.getId());

            for (Budget budget : budgets) {
                tableModel.addRow(new Object[]{
                        budget.getId(),
                        budget.getCategoryId(),
                        budget.getMonth(),
                        budget.getYear(),
                        budget.getLimitAmount()
                });
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Could not load budgets.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void addBudget() {
        try {
            List<Category> categories = categoryService.getAllCategoriesByUserId(loggedInUser.getId());

            if (categories.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Create a category first.", "No Categories", JOptionPane.WARNING_MESSAGE);
                return;
            }

            JComboBox<Category> categoryBox = new JComboBox<>(categories.toArray(new Category[0]));
            JTextField monthField = new JTextField();
            JTextField yearField = new JTextField(String.valueOf(Year.now().getValue()));
            JTextField limitField = new JTextField();

            JPanel panel = new JPanel(new GridLayout(4, 2, 10, 10));
            panel.add(new JLabel("Category:"));
            panel.add(categoryBox);
            panel.add(new JLabel("Month:"));
            panel.add(monthField);
            panel.add(new JLabel("Year:"));
            panel.add(yearField);
            panel.add(new JLabel("Limit Amount:"));
            panel.add(limitField);

            int result = JOptionPane.showConfirmDialog(
                    this,
                    panel,
                    "Add Budget",
                    JOptionPane.OK_CANCEL_OPTION,
                    JOptionPane.PLAIN_MESSAGE
            );

            if (result == JOptionPane.OK_OPTION) {
                Category selectedCategory = (Category) categoryBox.getSelectedItem();

                int month = Integer.parseInt(monthField.getText().trim());
                int year = Integer.parseInt(yearField.getText().trim());
                BigDecimal limitAmount = new BigDecimal(limitField.getText().trim());

                budgetService.createBudget(
                        loggedInUser.getId(),
                        selectedCategory.getId(),
                        month,
                        year,
                        limitAmount
                );

                loadBudgets();
            }

        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Month, year and amount must be valid numbers.", "Validation Error", JOptionPane.WARNING_MESSAGE);
        } catch (IllegalArgumentException e) {
            JOptionPane.showMessageDialog(this, e.getMessage(), "Validation Error", JOptionPane.WARNING_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Could not add budget.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void deleteSelectedBudget() {
        int selectedRow = budgetsTable.getSelectedRow();

        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a budget first.", "No Selection", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int budgetId = (int) tableModel.getValueAt(selectedRow, 0);

        int choice = JOptionPane.showConfirmDialog(
                this,
                "Are you sure you want to delete this budget?",
                "Delete Budget",
                JOptionPane.YES_NO_OPTION
        );

        if (choice == JOptionPane.YES_OPTION) {
            budgetService.deleteBudget(budgetId);
            loadBudgets();
        }
    }
}