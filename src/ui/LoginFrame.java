package ui.frames;

import models.User;
import services.AuthService;
import ui.RegisterFrame;

import javax.swing.*;
import java.awt.*;

public class LoginFrame extends JFrame {

    private final AuthService authService;

    private JTextField usernameField;
    private JPasswordField passwordField;
    private JLabel messageLabel;

    public LoginFrame() {
        this.authService = new AuthService();

        initializeFrame();
        initializeComponents();
    }

    private void initializeFrame() {
        setTitle("Personal Finance Manager - Login");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
    }

    private void initializeComponents() {
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));

        JLabel titleLabel = new JLabel("Login", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));

        JPanel formPanel = new JPanel(new GridLayout(3, 2, 10, 10));

        JLabel usernameLabel = new JLabel("Username:");
        usernameField = new JTextField();

        JLabel passwordLabel = new JLabel("Password:");
        passwordField = new JPasswordField();

        JButton loginButton = new JButton("Login");
        JButton registerButton = new JButton("Register");

        formPanel.add(usernameLabel);
        formPanel.add(usernameField);
        formPanel.add(passwordLabel);
        formPanel.add(passwordField);
        formPanel.add(loginButton);
        formPanel.add(registerButton);

        messageLabel = new JLabel("", SwingConstants.CENTER);
        messageLabel.setForeground(Color.RED);

        mainPanel.add(titleLabel, BorderLayout.NORTH);
        mainPanel.add(formPanel, BorderLayout.CENTER);
        mainPanel.add(messageLabel, BorderLayout.SOUTH);

        add(mainPanel);

        loginButton.addActionListener(e -> handleLogin());
        registerButton.addActionListener(e -> openRegisterFrame());
    }

    private void handleLogin() {
        String username = usernameField.getText();
        String password = new String(passwordField.getPassword());

        try {
            User loggedInUser = authService.login(username, password);

            JOptionPane.showMessageDialog(
                    this,
                    "Welcome, " + loggedInUser.getUsername() + "!",
                    "Login Successful",
                    JOptionPane.INFORMATION_MESSAGE
            );
            dispose();

        } catch (IllegalArgumentException ex) {
            messageLabel.setText(ex.getMessage());
        } catch (Exception ex) {
            messageLabel.setText("Something went wrong. Please try again.");
        }
    }

    private void openRegisterFrame() {
        new RegisterFrame().setVisible(true);
    }
}