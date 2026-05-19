package ui.panels;

import models.Category;
import models.TransactionType;
import models.User;
import services.CategoryService;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class CategoriesPanel extends JPanel {

    private final User loggedInUser;
    private final CategoryService categoryService;

    private JTable categoriesTable;
    private DefaultTableModel tableModel;

    public CategoriesPanel(User loggedInUser) {
        this.loggedInUser = loggedInUser;
        this.categoryService = new CategoryService();

        initializeComponents();
        loadCategories();
    }

    private void initializeComponents() {
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        JLabel titleLabel = new JLabel("Categories");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));

        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.add(titleLabel, BorderLayout.WEST);
        topPanel.add(createButtonPanel(), BorderLayout.EAST);

        tableModel = new DefaultTableModel(
                new Object[]{"ID", "Name", "Type"},
                0
        ) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        categoriesTable = new JTable(tableModel);
        categoriesTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        JScrollPane scrollPane = new JScrollPane(categoriesTable);

        add(topPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
    }

    private JPanel createButtonPanel() {
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));

        JButton addButton = new JButton("Add");
        JButton deleteButton = new JButton("Delete");
        JButton refreshButton = new JButton("Refresh");

        addButton.addActionListener(e -> addCategory());
        deleteButton.addActionListener(e -> deleteSelectedCategory());
        refreshButton.addActionListener(e -> loadCategories());

        buttonPanel.add(addButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(refreshButton);

        return buttonPanel;
    }

    private void loadCategories() {
        tableModel.setRowCount(0);

        try {
            List<Category> categories =
                    categoryService.getAllCategoriesByUserId(loggedInUser.getId());

            for (Category category : categories) {
                tableModel.addRow(new Object[]{
                        category.getId(),
                        category.getName(),
                        category.getType()
                });
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(
                    this,
                    "Could not load categories.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    private void addCategory() {
        JTextField nameField = new JTextField();

        JComboBox<TransactionType> typeComboBox =
                new JComboBox<>(TransactionType.values());

        JPanel panel = new JPanel(new GridLayout(2, 2, 10, 10));
        panel.add(new JLabel("Name:"));
        panel.add(nameField);
        panel.add(new JLabel("Type:"));
        panel.add(typeComboBox);

        int result = JOptionPane.showConfirmDialog(
                this,
                panel,
                "Add Category",
                JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.PLAIN_MESSAGE
        );

        if (result == JOptionPane.OK_OPTION) {
            try {
                String name = nameField.getText();
                TransactionType type = (TransactionType) typeComboBox.getSelectedItem();

                categoryService.createCategory(
                        loggedInUser.getId(),
                        name,
                        type
                );

                loadCategories();

            } catch (IllegalArgumentException ex) {
                JOptionPane.showMessageDialog(
                        this,
                        ex.getMessage(),
                        "Validation Error",
                        JOptionPane.WARNING_MESSAGE
                );
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(
                        this,
                        "Could not add category.",
                        "Error",
                        JOptionPane.ERROR_MESSAGE
                );
            }
        }
    }

    private void deleteSelectedCategory() {
        int selectedRow = categoriesTable.getSelectedRow();

        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(
                    this,
                    "Please select a category first.",
                    "No Selection",
                    JOptionPane.WARNING_MESSAGE
            );
            return;
        }

        int categoryId = (int) tableModel.getValueAt(selectedRow, 0);

        int choice = JOptionPane.showConfirmDialog(
                this,
                "Are you sure you want to delete this category?",
                "Delete Category",
                JOptionPane.YES_NO_OPTION
        );

        if (choice == JOptionPane.YES_OPTION) {
            try {
                categoryService.deleteCategory(
                        categoryId,
                        loggedInUser.getId()
                );

                loadCategories();

            } catch (Exception e) {
                JOptionPane.showMessageDialog(
                        this,
                        "Could not delete category. It may be used by transactions.",
                        "Error",
                        JOptionPane.ERROR_MESSAGE
                );
            }
        }
    }
}