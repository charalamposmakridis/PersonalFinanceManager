package ui.dialogs;

import models.Category;
import models.TransactionType;
import models.User;
import services.CategoryService;
import services.TransactionService;

import javax.swing.*;
import java.awt.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public class AddTransactionsDialog extends JDialog {

    private final User loggedInUser;
    private final TransactionService transactionService;
    private final CategoryService categoryService;

    private JComboBox<Category> categoryComboBox;
    private JTextField amountField;
    private JTextField descriptionField;
    private JTextField dateField;

    private boolean transactionAdded = false;

    public AddTransactionsDialog(JFrame parent, User loggedInUser) {
        super(parent, "Add Transaction", true);

        this.loggedInUser = loggedInUser;
        this.transactionService = new TransactionService();
        this.categoryService = new CategoryService();

        initializeDialog();
        initializeComponents();
        loadCategories();
    }

    private void initializeDialog() {
        setSize(420, 300);
        setLocationRelativeTo(getParent());
        setResizable(false);
    }

    private void initializeComponents() {
        setLayout(new BorderLayout());
        JPanel formPanel = new JPanel(new GridLayout(4, 2, 10, 10));
        formPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        categoryComboBox = new JComboBox<>();
        amountField = new JTextField();
        descriptionField = new JTextField();
        dateField = new JTextField(LocalDate.now().toString());

        formPanel.add(new JLabel("Category:"));
        formPanel.add(categoryComboBox);

        formPanel.add(new JLabel("Amount:"));
        formPanel.add(amountField);

        formPanel.add(new JLabel("Description:"));
        formPanel.add(descriptionField);

        formPanel.add(new JLabel("Date (YYYY-MM-DD):"));
        formPanel.add(dateField);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton addButton = new JButton("Add");
        JButton cancelButton = new JButton("Cancel");

        addButton.addActionListener(e -> addTransaction());
        cancelButton.addActionListener(e -> dispose());

        buttonPanel.add(addButton);
        buttonPanel.add(cancelButton);

        add(formPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    private void loadCategories() {
        try {
            List<Category> categories =
                    categoryService.getAllCategoriesByUserId(loggedInUser.getId());

            for (Category category : categories) {
                categoryComboBox.addItem(category);
            }

            if (categories.isEmpty()) {
                JOptionPane.showMessageDialog(
                        this,
                        "You need to create a category first.",
                        "No Categories",
                        JOptionPane.WARNING_MESSAGE
                );
                dispose();
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(
                    this,
                    "Could not load categories.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );
            dispose();
        }
    }

    private void addTransaction() {
        try {
            Category selectedCategory = (Category) categoryComboBox.getSelectedItem();

            if (selectedCategory == null) {
                throw new IllegalArgumentException("Please select a category.");
            }

            BigDecimal amount = new BigDecimal(amountField.getText().trim());
            String description = descriptionField.getText();
            LocalDate date = LocalDate.parse(dateField.getText().trim());
            TransactionType type = selectedCategory.getType();

            transactionService.createTransaction(
                    loggedInUser.getId(),
                    selectedCategory.getId(),
                    amount,
                    type,
                    description,
                    date
            );

            transactionAdded = true;

            JOptionPane.showMessageDialog(
                    this,
                    "Transaction added successfully.",
                    "Success",
                    JOptionPane.INFORMATION_MESSAGE
            );

            dispose();

        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(
                    this,
                    "Amount must be a valid number.",
                    "Validation Error",
                    JOptionPane.WARNING_MESSAGE
            );
        } catch (Exception e) {
            JOptionPane.showMessageDialog(
                    this,
                    e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    public boolean isTransactionAdded() {
        return transactionAdded;
    }
}