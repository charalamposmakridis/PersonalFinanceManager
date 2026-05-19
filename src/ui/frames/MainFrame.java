package ui.frames;

import models.User;
import ui.panels.BudgetsPanel;
import ui.panels.CategoriesPanel;
import ui.panels.DashboardPanel;
import ui.panels.ReportsPanel;
import ui.panels.TransactionsPanel;

import javax.swing.*;
import java.awt.*;

public class MainFrame extends JFrame {

    private final User loggedInUser;
    private JPanel contentPanel;

    public MainFrame(User loggedInUser) {
        this.loggedInUser = loggedInUser;
        initializeFrame();
        initializeComponents();
    }

    private void initializeFrame() {
        setTitle("Personal Finance Manager");
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(true);
    }

    private void initializeComponents() {
        setLayout(new BorderLayout());

        add(createHeaderPanel(), BorderLayout.NORTH);
        add(createSideBar(), BorderLayout.WEST);

        contentPanel = new JPanel(new BorderLayout());
        add(contentPanel, BorderLayout.CENTER);

        showDashboardPanel();
    }

    private JPanel createHeaderPanel() {
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));

        JLabel welcomeLabel = new JLabel("Welcome, " + loggedInUser.getUsername());
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 18));

        JButton logoutButton = new JButton("Logout");
        logoutButton.addActionListener(e -> handleLogout());

        headerPanel.add(welcomeLabel, BorderLayout.WEST);
        headerPanel.add(logoutButton, BorderLayout.EAST);

        return headerPanel;
    }

    private JPanel createSideBar() {
        JPanel sidebarPanel = new JPanel(new GridLayout(5, 1, 10, 10));
        sidebarPanel.setBorder(BorderFactory.createEmptyBorder(20, 15, 20, 15));
        sidebarPanel.setPreferredSize(new Dimension(170, 0));

        JButton dashboardButton = new JButton("Dashboard");
        JButton transactionsButton = new JButton("Transactions");
        JButton categoriesButton = new JButton("Categories");
        JButton budgetsButton = new JButton("Budgets");
        JButton reportsButton = new JButton("Reports");

        dashboardButton.addActionListener(e -> showDashboardPanel());
        transactionsButton.addActionListener(e -> showTransactionsPanel());
        categoriesButton.addActionListener(e -> showCategoriesPanel());
        budgetsButton.addActionListener(e -> showBudgetsPanel());
        reportsButton.addActionListener(e -> showReportsPanel());

        sidebarPanel.add(dashboardButton);
        sidebarPanel.add(transactionsButton);
        sidebarPanel.add(categoriesButton);
        sidebarPanel.add(budgetsButton);
        sidebarPanel.add(reportsButton);

        return sidebarPanel;
    }

    private void showDashboardPanel() {
        switchPanel(new DashboardPanel(loggedInUser));
    }

    private void showTransactionsPanel() {
        switchPanel(new TransactionsPanel(loggedInUser));
    }

    private void showCategoriesPanel() {
        switchPanel(new CategoriesPanel(loggedInUser));
    }

    private void showBudgetsPanel() {
        switchPanel(new BudgetsPanel(loggedInUser));
    }

    private void showReportsPanel() {
        switchPanel(new ReportsPanel(loggedInUser));
    }

    private void switchPanel(JPanel panel) {
        contentPanel.removeAll();
        contentPanel.add(panel, BorderLayout.CENTER);
        contentPanel.revalidate();
        contentPanel.repaint();
    }

    private void handleLogout() {
        int choice = JOptionPane.showConfirmDialog(
                this,
                "Are you sure you want to logout?",
                "Logout",
                JOptionPane.YES_NO_OPTION
        );

        if (choice == JOptionPane.YES_OPTION) {
            new LoginFrame().setVisible(true);
            dispose();
        }
    }
}