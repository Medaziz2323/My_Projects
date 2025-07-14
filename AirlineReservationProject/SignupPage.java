import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.border.*;

/**
 * SignupPage class for user registration in the Tunisia Airways system.
 * This class allows new users to create accounts.
 */
public class SignupPage extends JFrame {
    // Main panels
    private JPanel mainPanel;
    private JPanel formPanel;
    
    // Form components
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JPasswordField confirmPasswordField;
    private JTextField fullNameField;
    private JTextField emailField;
    private JButton signupButton;
    private JButton cancelButton;
    private JLabel statusLabel;
    
    // Reference to login page
    private LoginPage loginPage;
    
    /**
     * Constructor - creates the signup window
     */
    public SignupPage(LoginPage loginPage) {
        // Set the window title
        super("Sign Up - Tunisia Airways");
        
        // Save reference to login page
        this.loginPage = loginPage;
        
        // Create the main panel
        mainPanel = new JPanel(new BorderLayout(20, 20));
        mainPanel.setBackground(AppStyle.BACKGROUND);
        mainPanel.setBorder(BorderFactory.createEmptyBorder(40, 40, 40, 40));
        
        // Create the form panel
        formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(AppStyle.WHITE);
        formPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(AppStyle.TEAL, 1),
            BorderFactory.createEmptyBorder(30, 30, 30, 30)
        ));
        
        // Create signup form
        createSignupForm();
        
        // Add form panel to main panel
        mainPanel.add(formPanel, BorderLayout.CENTER);
        
        // Set content pane
        setContentPane(mainPanel);
        
        // Display in fullscreen and set default close operation
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        AppStyle.setFullScreen(this);
        setVisible(true);
    }
    
    /**
     * Creates the signup form
     */
    private void createSignupForm() {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        // Title
        JLabel titleLabel = AppStyle.createTitleLabel("Create New Account");
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(0, 0, 20, 0);
        formPanel.add(titleLabel, gbc);
        
        // Username field
        JLabel usernameLabel = AppStyle.createLabel("Username");
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(0, 0, 5, 0);
        formPanel.add(usernameLabel, gbc);
        
        usernameField = new JTextField(20);
        AppStyle.styleTextField(usernameField);
        gbc.gridy = 2;
        gbc.insets = new Insets(0, 0, 15, 0);
        formPanel.add(usernameField, gbc);
        
        // Full Name field
        JLabel fullNameLabel = AppStyle.createLabel("Full Name");
        gbc.gridy = 3;
        gbc.insets = new Insets(0, 0, 5, 0);
        formPanel.add(fullNameLabel, gbc);
        
        fullNameField = new JTextField(20);
        AppStyle.styleTextField(fullNameField);
        gbc.gridy = 4;
        gbc.insets = new Insets(0, 0, 15, 0);
        formPanel.add(fullNameField, gbc);
        
        // Email field
        JLabel emailLabel = AppStyle.createLabel("Email");
        gbc.gridy = 5;
        gbc.insets = new Insets(0, 0, 5, 0);
        formPanel.add(emailLabel, gbc);
        
        emailField = new JTextField(20);
        AppStyle.styleTextField(emailField);
        gbc.gridy = 6;
        gbc.insets = new Insets(0, 0, 15, 0);
        formPanel.add(emailField, gbc);
        
        // Password field
        JLabel passwordLabel = AppStyle.createLabel("Password");
        gbc.gridy = 7;
        gbc.insets = new Insets(0, 0, 5, 0);
        formPanel.add(passwordLabel, gbc);
        
        passwordField = new JPasswordField(20);
        AppStyle.styleTextField(passwordField);
        gbc.gridy = 8;
        gbc.insets = new Insets(0, 0, 15, 0);
        formPanel.add(passwordField, gbc);
        
        // Confirm Password field
        JLabel confirmPasswordLabel = AppStyle.createLabel("Confirm Password");
        gbc.gridy = 9;
        gbc.insets = new Insets(0, 0, 5, 0);
        formPanel.add(confirmPasswordLabel, gbc);
        
        confirmPasswordField = new JPasswordField(20);
        AppStyle.styleTextField(confirmPasswordField);
        gbc.gridy = 10;
        gbc.insets = new Insets(0, 0, 20, 0);
        formPanel.add(confirmPasswordField, gbc);
        
        // Status label
        statusLabel = new JLabel("");
        statusLabel.setFont(AppStyle.FONT_REGULAR);
        gbc.gridy = 11;
        gbc.insets = new Insets(0, 0, 20, 0);
        formPanel.add(statusLabel, gbc);
        
        // Button panel for signup and cancel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 0));
        buttonPanel.setBackground(AppStyle.WHITE);
        
        // Sign Up button
        signupButton = new JButton("Sign Up");
        AppStyle.stylePrimaryButton(signupButton);
        signupButton.addActionListener(e -> signup());
        
        // Cancel button
        cancelButton = new JButton("Cancel");
        AppStyle.styleNeutralButton(cancelButton);
        cancelButton.addActionListener(e -> cancel());
        
        // Add buttons to panel
        buttonPanel.add(signupButton);
        buttonPanel.add(cancelButton);
        
        // Add button panel to form
        gbc.gridy = 12;
        gbc.insets = new Insets(0, 0, 0, 0);
        formPanel.add(buttonPanel, gbc);
    }
    
    /**
     * Signup action - validates form and creates user account
     */
    private void signup() {
        // Get form data
        String username = usernameField.getText().trim();
        char[] passwordChars = passwordField.getPassword();
        String password = new String(passwordChars);
        char[] confirmChars = confirmPasswordField.getPassword();
        String confirmPassword = new String(confirmChars);
        String fullName = fullNameField.getText().trim();
        String email = emailField.getText().trim();
        
        // Validate fields
        if (username.isEmpty() || password.isEmpty() || 
            confirmPassword.isEmpty() || fullName.isEmpty() || email.isEmpty()) {
            statusLabel.setText("All fields are required");
            statusLabel.setForeground(AppStyle.ERROR);
            return;
        }
        
        // Validate password match
        if (!password.equals(confirmPassword)) {
            statusLabel.setText("Passwords do not match");
            statusLabel.setForeground(AppStyle.ERROR);
            return;
        }
        
        // Validate email format
        if (!isValidEmail(email)) {
            statusLabel.setText("Invalid email format");
            statusLabel.setForeground(AppStyle.ERROR);
            return;
        }
        
        // Check if username already exists
        if (isUsernameTaken(username)) {
            statusLabel.setText("Username already exists");
            statusLabel.setForeground(AppStyle.ERROR);
            return;
        }
        
        try {
            // Create new user with USER role
            User newUser = new User(
                0, // ID will be set by database
                username,
                password,
                fullName,
                email,
                User.Role.USER // Regular user role
            );
            
            // Save user to database
            boolean success = UserDAO.insertUser(newUser);
            
            if (success) {
                // Show success dialog
                JOptionPane.showMessageDialog(this,
                    "Account created successfully!\nYou can now log in with your credentials.",
                    "Registration Successful",
                    JOptionPane.INFORMATION_MESSAGE);
                
                // Return to login page
                navigateToLogin();
            } else {
                statusLabel.setText("Error creating account");
                statusLabel.setForeground(AppStyle.ERROR);
            }
        } catch (Exception ex) {
            statusLabel.setText("Error: " + ex.getMessage());
            statusLabel.setForeground(AppStyle.ERROR);
            ex.printStackTrace();
        }
    }
    
    /**
     * Check if username already exists
     */
    private boolean isUsernameTaken(String username) {
        return UserDAO.getUserByUsername(username) != null;
    }
    
    /**
     * Validate email format
     */
    private boolean isValidEmail(String email) {
        // Simple email validation
        return email.matches("^[A-Za-z0-9+_.-]+@(.+)$");
    }
    
    /**
     * Cancel signup and return to login page
     */
    private void cancel() {
        // Navigate back to login page
        navigateToLogin();
    }
    
    /**
     * Navigate back to login page
     */
    private void navigateToLogin() {
        // Show login page
        loginPage.setVisible(true);
        
        // Close this window
        dispose();
    }
} 