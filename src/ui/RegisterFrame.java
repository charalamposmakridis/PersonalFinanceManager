package ui;

import models.User;
import services.AuthService;

import javax.swing.*;
import java.awt.*;

public class RegisterFrame extends JFrame {
    private final AuthService authService;

    private JTextField nameField;
    private JPasswordField passwordField;
    private JPasswordField confirmPasswordField;
    private JTextField fullNameField;
    private JLabel messageLabel;

    public  RegisterFrame(){
        this.authService=new AuthService();

        initializeFrame();
        initializeComponents();
    }

    private void initializeFrame(){
        setTitle("Personal Finance Manager - Register");
        setSize(450,600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
    }

    private void initializeComponents(){
        JPanel mainPanel=new JPanel(new BorderLayout());
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20,30,20,30));

        JLabel titleLabel=new JLabel("Create Account", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial",Font.BOLD,24));

        JPanel formPanel=new JPanel(new GridLayout(5,2,10,10));

        nameField=new JTextField();
        passwordField=new JPasswordField();
        confirmPasswordField=new JPasswordField();
        fullNameField=new JTextField();

        JButton registerButton=new JButton("Register");
        JButton backButton=new JButton("Back");

        formPanel.add(new JLabel("Full Name: "));
        formPanel.add(fullNameField);

        formPanel.add(new JLabel("Username: "));
        formPanel.add(nameField);

        formPanel.add(new JLabel("Password: "));
        formPanel.add(passwordField);

        formPanel.add(new JLabel("Confirm Password: "));
        formPanel.add(confirmPasswordField);

        formPanel.add(registerButton);
        formPanel.add(backButton);

        messageLabel=new JLabel("", SwingConstants.CENTER);
        messageLabel.setForeground(Color.RED);

        mainPanel.add(titleLabel,BorderLayout.NORTH);
        mainPanel.add(formPanel,BorderLayout.CENTER);
        mainPanel.add(messageLabel,BorderLayout.SOUTH);

        add(mainPanel);

        registerButton.addActionListener(e->handleRegister());
        backButton.addActionListener(e->dispose());
    }

    private void handleRegister(){
        String fullName=fullNameField.getText();
        String username=nameField.getText();
        String password=new String(passwordField.getPassword());
        String confirmPassword=new String(confirmPasswordField.getPassword());

        if(!password.equals(confirmPassword)){
            messageLabel.setText("Passwords do not match.");
            return;
        }

        try{
            User registeredUser=authService.register(username,password,fullName);
            JOptionPane.showMessageDialog(this,"Account created successfully for"+registeredUser.getFullName()+" !",
                    "Registered Successfully!",JOptionPane.INFORMATION_MESSAGE);
            dispose();
        }catch (IllegalArgumentException e){
            messageLabel.setText(e.getMessage());
        }catch (Exception e){
            messageLabel.setText("Something went wrong. Please try again.");
        }
    }
}
