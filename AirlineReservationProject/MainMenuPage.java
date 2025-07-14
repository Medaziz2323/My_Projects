import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.border.*;
import java.util.List;

/**
 * MainMenuPage class that serves as the central hub for the Tunisia Airways application.
 * This class provides access to various features like flight booking, viewing reservations, etc.
 */
public class MainMenuPage extends JFrame {
    // Main panels
    private JPanel mainPanel;
    private JPanel menuPanel;
    private JPanel userInfoPanel;
    
    // Menu buttons
    private JButton bookFlightButton;
    private JButton viewReservationsButton;
    private JButton availableTicketsButton;
    private JButton logoutButton;
    
    // User info
    private JLabel welcomeLabel;
    private JLabel userNameLabel;
    private JLabel userEmailLabel;
    
    // References to other screens
    private LoginPage loginPage;
    
    // Currently logged in user
    private User user;
    
    /**
     * Constructor - creates the main menu page
     */
    public MainMenuPage(LoginPage loginPage, User user) {
        // Set the window title
        super("Tunisia Airways - Main Menu");
        
        // Save references
        this.loginPage = loginPage;
        this.user = user;
        
        // Redirect admins to the admin dashboard
        if (user != null && user.isAdmin()) {
            dispose();
            new AdminDashboard(loginPage, user);
            return;
        }
        
        // Create the main panel
        mainPanel = new JPanel(new BorderLayout(20, 20));
        mainPanel.setBackground(AppStyle.BACKGROUND);
        mainPanel.setBorder(BorderFactory.createEmptyBorder(40, 40, 40, 40));
        
        // Create the menu panel
        createMenuPanel();
        
        // Create the user info panel
        createUserInfoPanel();
        
        // Add panels to the main panel
        mainPanel.add(userInfoPanel, BorderLayout.NORTH);
        mainPanel.add(menuPanel, BorderLayout.CENTER);
        
        // Set content pane
        setContentPane(mainPanel);
        
        // Display in fullscreen and set default close operation
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        AppStyle.setFullScreen(this);
        setVisible(true);
    }
    
    /**
     * Creates the menu panel with main options
     */
    private void createMenuPanel() {
        menuPanel = new JPanel(new GridLayout(2, 2, 20, 20));
        menuPanel.setBackground(AppStyle.BACKGROUND);
        
        // Book Flight Button with icon
        bookFlightButton = createMenuButton(
            "Book a Flight", 
            "Book domestic flights from Tunisia to European destinations"
        );
        bookFlightButton.addActionListener(e -> openBookFlight());
        
        // View Reservations Button with icon
        viewReservationsButton = createMenuButton(
            "My Reservations", 
            "View and manage your current flight reservations"
        );
        viewReservationsButton.addActionListener(e -> viewReservations());
        
        // Available Tickets Button with icon - only shown to admins
        availableTicketsButton = createMenuButton(
            "Available Tickets", 
            "Browse currently available flight tickets"
        );
        availableTicketsButton.addActionListener(e -> viewAvailableTickets());
        
        // Logout Button with icon
        logoutButton = createMenuButton(
            "Logout", 
            "Log out from your account"
        );
        logoutButton.addActionListener(e -> logout());
        
        // Add buttons to the menu panel
        menuPanel.add(bookFlightButton);
        menuPanel.add(viewReservationsButton);
        
        // Only show the Available Tickets button to admins
        if (user != null && user.isAdmin()) {
            menuPanel.add(availableTicketsButton);
        } else {
            // Add an empty panel to maintain the grid layout
            JPanel emptyPanel = new JPanel();
            emptyPanel.setBackground(AppStyle.BACKGROUND);
            menuPanel.add(emptyPanel);
        }
        
        menuPanel.add(logoutButton);
    }
    
    /**
     * Creates a styled menu button with text and description
     */
    private JButton createMenuButton(String text, String description) {
        JButton button = new JButton();
        
        // Create a layered panel for the button content
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.Y_AXIS));
        buttonPanel.setBackground(AppStyle.WHITE);
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        // Create the main label (title)
        JLabel titleLabel = new JLabel(text);
        titleLabel.setFont(AppStyle.FONT_SUBTITLE);
        titleLabel.setForeground(AppStyle.TEXT);
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        // Create the description label
        JLabel descLabel = new JLabel("<html><center>" + description + "</center></html>");
        descLabel.setFont(AppStyle.FONT_REGULAR);
        descLabel.setForeground(new Color(100, 100, 100));
        descLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        // Add some spacing
        buttonPanel.add(Box.createVerticalGlue());
        buttonPanel.add(titleLabel);
        buttonPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        buttonPanel.add(descLabel);
        buttonPanel.add(Box.createVerticalGlue());
        
        // Add the panel to the button
        button.setLayout(new BorderLayout());
        button.add(buttonPanel, BorderLayout.CENTER);
        
        // Style the button
        button.setBackground(AppStyle.WHITE);
        button.setBorder(BorderFactory.createLineBorder(new Color(230, 230, 230), 1));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setFocusPainted(false);
        
        // Add hover effect
        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                buttonPanel.setBackground(new Color(240, 248, 255)); // Light blue background on hover
            }
            
            @Override
            public void mouseExited(MouseEvent e) {
                buttonPanel.setBackground(AppStyle.WHITE);
            }
        });
        
        return button;
    }
    
    /**
     * Creates the user info panel
     */
    private void createUserInfoPanel() {
        userInfoPanel = new JPanel(new BorderLayout());
        userInfoPanel.setBackground(AppStyle.BACKGROUND);
        
        // Create a panel for user details
        JPanel userDetailsPanel = new JPanel();
        userDetailsPanel.setLayout(new BoxLayout(userDetailsPanel, BoxLayout.Y_AXIS));
        userDetailsPanel.setBackground(AppStyle.WHITE);
        userDetailsPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(230, 230, 230), 1),
            BorderFactory.createEmptyBorder(20, 20, 20, 20)
        ));
        
        // Welcome message
        welcomeLabel = AppStyle.createTitleLabel("Welcome to Tunisia Airways");
        welcomeLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        // User name
        userNameLabel = new JLabel(user.getFullName());
        userNameLabel.setFont(AppStyle.FONT_SUBTITLE);
        userNameLabel.setForeground(AppStyle.TEAL);
        userNameLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        // User email
        userEmailLabel = new JLabel(user.getEmail());
        userEmailLabel.setFont(AppStyle.FONT_REGULAR);
        userEmailLabel.setForeground(new Color(100, 100, 100));
        userEmailLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        // Add components to panel
        userDetailsPanel.add(welcomeLabel);
        userDetailsPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        userDetailsPanel.add(userNameLabel);
        userDetailsPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        userDetailsPanel.add(userEmailLabel);
        
        // Add user details panel to user info panel
        userInfoPanel.add(userDetailsPanel, BorderLayout.CENTER);
        
        // Add some spacing at the bottom
        userInfoPanel.add(Box.createRigidArea(new Dimension(0, 20)), BorderLayout.SOUTH);
    }
    
    /**
     * Opens the flight booking window
     */
    private void openBookFlight() {
        // For regular users, check if there are available tickets before letting them proceed
        if (!user.isAdmin()) {
            List<Ticket> availableTickets = TicketDAO.getAvailableTickets();
            
            if (availableTickets.isEmpty()) {
                JOptionPane.showMessageDialog(this,
                    "Sorry, there are no available tickets at the moment.\n" +
                    "Please try again later or contact an administrator.",
                    "No Available Tickets",
                    JOptionPane.INFORMATION_MESSAGE);
                return;
            }
            
            // Open the available tickets page for booking instead of DomesticFlight
            new AvailableTicketsPage(this, user, availableTickets);
            setVisible(false); // Hide this window
        } else {
            // Admins should be directed to the admin dashboard
            JOptionPane.showMessageDialog(this,
                "Admins cannot book tickets. Please use admin dashboard for ticket management.",
                "Admin Access",
                JOptionPane.INFORMATION_MESSAGE);
        }
    }
    
    /**
     * Shows the user's reservations
     */
    private void viewReservations() {
        // Get user's reservations from database
        List<Reservation> reservations = ReservationDAO.getReservationsByUserId(user.getUserId());
        
        if (reservations.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                "You don't have any reservations yet.",
                "No Reservations",
                JOptionPane.INFORMATION_MESSAGE);
        } else {
            // Open the reservations view
            new ReservationsPage(this, user, reservations);
            setVisible(false); // Hide this window
        }
    }
    
    /**
     * Shows available tickets
     */
    private void viewAvailableTickets() {
        // Get available tickets from database
        List<Ticket> tickets = TicketDAO.getAvailableTickets();
        
        if (tickets.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                "There are no available tickets at the moment.",
                "No Available Tickets",
                JOptionPane.INFORMATION_MESSAGE);
        } else {
            // Open the available tickets view
            new AvailableTicketsPage(this, user, tickets);
            setVisible(false); // Hide this window
        }
    }
    
    /**
     * Logout action
     */
    private void logout() {
        // Confirm logout
        int choice = JOptionPane.showConfirmDialog(this,
            "Are you sure you want to logout?",
            "Confirm Logout",
            JOptionPane.YES_NO_OPTION);
            
        if (choice == JOptionPane.YES_OPTION) {
            // Create a new login page instead of reusing the old one
            LoginPage newLoginPage = new LoginPage();
            newLoginPage.setVisible(true);
            
            // Close this window
            dispose();
        }
    }
} 