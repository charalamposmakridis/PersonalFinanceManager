package ui.frames;

import models.User;

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
        setSize(900, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(true);
    }

    private void initializeComponents() {
        setLayout(new BorderLayout());

        JPanel headerPanel = createHeaderPanel();
        JPanel sideBar = createSideBar();

        contentPanel = new JPanel(new BorderLayout());
        showWelcomePanel();

        add(headerPanel, BorderLayout.NORTH);
        add(sideBar, BorderLayout.WEST);
        add(contentPanel, BorderLayout.CENTER);
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
        JButton budgetButton = new JButton("Budgets");
        JButton reportsButton = new JButton("Reports");

        dashboardButton.addActionListener(e -> showWelcomePanel());
        transactionsButton.addActionListener(e -> showPlaceholderPanel("Transactions"));
        categoriesButton.addActionListener(e -> showPlaceholderPanel("Categories"));
        budgetButton.addActionListener(e -> showPlaceholderPanel("Budgets"));
        reportsButton.addActionListener(e -> showPlaceholderPanel("Reports"));

        sidebarPanel.add(dashboardButton);
        sidebarPanel.add(transactionsButton);
        sidebarPanel.add(categoriesButton);
        sidebarPanel.add(budgetButton);
        sidebarPanel.add(reportsButton);

        return sidebarPanel;
    }

    private void showWelcomePanel() {
        contentPanel.removeAll();

        JPanel panel = new JPanel(new BorderLayout());

        JLabel titleLabel = new JLabel("Personal Finance Manager", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 28));

        JLabel subtitleLabel = new JLabel(
                "Use the menu on the left to manage your finances.",
                SwingConstants.CENTER
        );
        subtitleLabel.setFont(new Font("Arial", Font.PLAIN, 16));

        panel.add(titleLabel, BorderLayout.CENTER);
        panel.add(subtitleLabel, BorderLayout.SOUTH);

        contentPanel.add(panel, BorderLayout.CENTER);
        refreshContentPanel();
    }

    private void showPlaceholderPanel(String title) {
        contentPanel.removeAll();

        JLabel label = new JLabel(title + " panel will be implemented next.", SwingConstants.CENTER);
        label.setFont(new Font("Arial", Font.BOLD, 22));

        contentPanel.add(label, BorderLayout.CENTER);
        refreshContentPanel();
    }

    private void refreshContentPanel() {
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