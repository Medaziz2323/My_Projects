import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.border.*;

/**
 * UserForm class for adding and editing users.
 * This form is used in the admin dashboard.
 */
public class UserForm extends JDialog {
    // Colors and fonts
    private Color mainColor = new Color(40, 40, 40);
    private Color accentColor = new Color(0, 80, 180);
    private Color backgroundColor = new Color(245, 245, 245);
    private Color textColor = new Color(33, 33, 33);
    private Font mainFont = new Font("Arial", Font.PLAIN, 14);
    private Font boldFont = new Font("Arial", Font.BOLD, 14);
    private Font titleFont = new Font("Arial", Font.BOLD, 22);
    
    // Form fields
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JTextField fullNameField;
    private JTextField emailField;
    private JComboBox<String> roleComboBox;
    
    // Buttons
    private JButton saveButton;
    private JButton cancelButton;
    
    // User being edited (null if adding a new user)
    private User user;
    
    // Parent admin dashboard
    private AdminDashboard dashboard;
    
    /**
     * Constructor - creates a form for adding or editing a user
     */
    public UserForm(AdminDashboard dashboard, User user) {
        // Set dialog properties
        super(dashboard, user == null ? "Add User" : "Edit User", true);
        
        // Save references
        this.dashboard = dashboard;
        this.user = user;
        
        // Create the form
        setupForm();
        
        // If editing, populate fields with user data
        if (user != null) {
            populateFields();
        }
        
        // Display the dialog
        setSize(600, 500);
        setLocationRelativeTo(dashboard);
        setResizable(true);
        setVisible(true);
    }
    
    /**
     * Set up the form layout and components
     */
    private void setupForm() {
        // Create main panel with a border layout
        JPanel mainPanel = new JPanel(new BorderLayout(20, 20));
        mainPanel.setBackground(backgroundColor);
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        // Create form panel
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(Color.WHITE);
        formPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(230, 230, 230), 1),
            BorderFactory.createEmptyBorder(20, 20, 20, 20)
        ));
        
        // Create button panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.setBackground(backgroundColor);
        
        // Set up the form fields
        setupFormFields(formPanel);
        
        // Set up the buttons
        setupButtons(buttonPanel);
        
        // Add panels to main panel
        mainPanel.add(formPanel, BorderLayout.CENTER);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);
        
        // Set content pane
        setContentPane(mainPanel);
    }
    
    /**
     * Set up the form fields
     */
    private void setupFormFields(JPanel panel) {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        // Title
        JLabel titleLabel = AppStyle.createTitleLabel(user == null ? "Add New User" : "Edit User");
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(0, 0, 20, 0);
        panel.add(titleLabel, gbc);
        
        // Reset insets and gridwidth
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.gridwidth = 1;
        
        // Username field
        gbc.gridx = 0;
        gbc.gridy = 1;
        JLabel usernameLabel = AppStyle.createLabel("Username:");
        panel.add(usernameLabel, gbc);
        
        gbc.gridx = 1;
        usernameField = new JTextField(20);
        AppStyle.styleTextField(usernameField);
        panel.add(usernameField, gbc);
        
        // Password field
        gbc.gridx = 0;
        gbc.gridy = 2;
        JLabel passwordLabel = AppStyle.createLabel("Password:");
        panel.add(passwordLabel, gbc);
        
        gbc.gridx = 1;
        passwordField = new JPasswordField(20);
        AppStyle.styleTextField(passwordField);
        panel.add(passwordField, gbc);
        
        // Full name field
        gbc.gridx = 0;
        gbc.gridy = 3;
        JLabel fullNameLabel = AppStyle.createLabel("Full Name:");
        panel.add(fullNameLabel, gbc);
        
        gbc.gridx = 1;
        fullNameField = new JTextField(20);
        AppStyle.styleTextField(fullNameField);
        panel.add(fullNameField, gbc);
        
        // Email field
        gbc.gridx = 0;
        gbc.gridy = 4;
        JLabel emailLabel = AppStyle.createLabel("Email:");
        panel.add(emailLabel, gbc);
        
        gbc.gridx = 1;
        emailField = new JTextField(20);
        AppStyle.styleTextField(emailField);
        panel.add(emailField, gbc);
        
        // Role dropdown
        gbc.gridx = 0;
        gbc.gridy = 5;
        JLabel roleLabel = AppStyle.createLabel("Role:");
        panel.add(roleLabel, gbc);
        
        gbc.gridx = 1;
        String[] roles = {"USER", "ADMIN"};
        roleComboBox = new JComboBox<>(roles);
        AppStyle.styleComboBox(roleComboBox);
        panel.add(roleComboBox, gbc);
    }
    
    /**
     * Set up the save and cancel buttons
     */
    private void setupButtons(JPanel panel) {
        // Cancel button
        cancelButton = new JButton("Cancel");
        AppStyle.styleNeutralButton(cancelButton);
        cancelButton.addActionListener(e -> dispose());
        
        // Save button
        saveButton = new JButton(user == null ? "Add User" : "Save Changes");
        AppStyle.stylePrimaryButton(saveButton);
        saveButton.addActionListener(e -> saveUser());
        
        // Add buttons to panel
        panel.add(cancelButton);
        panel.add(Box.createHorizontalStrut(10));
        panel.add(saveButton);
    }
    
    /**
     * Populate form fields with user data
     */
    private void populateFields() {
        usernameField.setText(user.getUsername());
        passwordField.setText(user.getPassword());
        fullNameField.setText(user.getFullName());
        emailField.setText(user.getEmail());
        roleComboBox.setSelectedItem(user.getRole().toString());
    }
    
    /**
     * Save the user data
     */
    private void saveUser() {
        // Validate input
        if (!validateInput()) {
            return;
        }
        
        // Get form values
        String username = usernameField.getText().trim();
        String password = new String(passwordField.getPassword());
        String fullName = fullNameField.getText().trim();
        String email = emailField.getText().trim();
        User.Role role = User.Role.valueOf((String) roleComboBox.getSelectedItem());
        
        boolean success;
        
        if (user == null) {
            // Create a new user
            User newUser = new User(username, password, fullName, email, role);
            success = UserDAO.insertUser(newUser);
        } else {
            // Update existing user
            user.setUsername(username);
            user.setPassword(password);
            user.setFullName(fullName);
            user.setEmail(email);
            user.setRole(role);
            success = UserDAO.updateUser(user);
        }
        
        if (success) {
            JOptionPane.showMessageDialog(this,
                user == null ? "User added successfully." : "User updated successfully.",
                "Success",
                JOptionPane.INFORMATION_MESSAGE);
            
            // Close the dialog
            dispose();
            
            // Refresh the users list in the dashboard
            // This assumes a method to refresh users exists in AdminDashboard
            try {
                dashboard.getClass().getMethod("refreshUsers").invoke(dashboard);
            } catch (Exception ex) {
                System.err.println("Could not refresh users: " + ex.getMessage());
            }
        } else {
            JOptionPane.showMessageDialog(this,
                "Failed to save user. Please try again.",
                "Error",
                JOptionPane.ERROR_MESSAGE);
        }
    }
    
    /**
     * Validate input fields
     */
    private boolean validateInput() {
        // Check username
        if (usernameField.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this,
                "Username cannot be empty.",
                "Validation Error",
                JOptionPane.ERROR_MESSAGE);
            usernameField.requestFocus();
            return false;
        }
        
        // Check password
        if (passwordField.getPassword().length == 0) {
            JOptionPane.showMessageDialog(this,
                "Password cannot be empty.",
                "Validation Error",
                JOptionPane.ERROR_MESSAGE);
            passwordField.requestFocus();
            return false;
        }
        
        // Check full name
        if (fullNameField.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this,
                "Full name cannot be empty.",
                "Validation Error",
                JOptionPane.ERROR_MESSAGE);
            fullNameField.requestFocus();
            return false;
        }
        
        // Check email
        String email = emailField.getText().trim();
        if (email.isEmpty() || !email.contains("@") || !email.contains(".")) {
            JOptionPane.showMessageDialog(this,
                "Please enter a valid email address.",
                "Validation Error",
                JOptionPane.ERROR_MESSAGE);
            emailField.requestFocus();
            return false;
        }
        
        return true;
    }
} 