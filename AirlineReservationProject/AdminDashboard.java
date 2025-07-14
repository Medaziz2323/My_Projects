import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.border.*;
import java.util.List;
import javax.swing.table.*;
import java.text.SimpleDateFormat;

/**
 * AdminDashboard class for administrative functions.
 * This class provides interfaces for managing users, tickets, and reservations.
 */
public class AdminDashboard extends JFrame {
    // Main panels
    private JPanel mainPanel;
    private JPanel menuPanel;
    private JPanel contentPanel;
    private JPanel userInfoPanel;
    
    // Card layout for switching between views
    private CardLayout cardLayout;
    
    // Menu buttons
    private JButton usersButton;
    private JButton ticketsButton;
    private JButton reservationsButton;
    private JButton logoutButton;
    
    // User management
    private JTable usersTable;
    private JScrollPane usersScrollPane;
    private JButton addUserButton;
    private JButton editUserButton;
    private JButton deleteUserButton;
    
    // Ticket management
    private JTable ticketsTable;
    private JScrollPane ticketsScrollPane;
    private JButton addTicketButton;
    private JButton editTicketButton;
    private JButton deleteTicketButton;
    
    // Reservation management
    private JTable reservationsTable;
    private JScrollPane reservationsScrollPane;
    private JButton viewReservationButton;
    private JButton updateStatusButton;
    
    // Admin info
    private JLabel welcomeLabel;
    private JLabel adminNameLabel;
    
    // References to other screens
    private LoginPage loginPage;
    
    // Currently logged in admin
    private User admin;
    
    /**
     * Constructor - creates the admin dashboard
     */
    public AdminDashboard(LoginPage loginPage, User admin) {
        // Set the window title
        super("Tunisia Airways - Admin Dashboard");
        
        // Validate admin role
        if (admin.getRole() != User.Role.ADMIN) {
            JOptionPane.showMessageDialog(null,
                "Access denied. Administrator privileges required.",
                "Access Denied",
                JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        // Save references
        this.loginPage = loginPage;
        this.admin = admin;
        
        // Create the main panel
        mainPanel = new JPanel(new BorderLayout(20, 20));
        mainPanel.setBackground(AppStyle.BACKGROUND);
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        // Create card layout for content
        cardLayout = new CardLayout();
        contentPanel = new JPanel(cardLayout);
        contentPanel.setBackground(AppStyle.BACKGROUND);
        
        // Create the menu panel
        createMenuPanel();
        
        // Create the user info panel
        createUserInfoPanel();
        
        // Create management panels
        createUsersPanel();
        createTicketsPanel();
        createReservationsPanel();
        
        // Add panels to the main panel
        mainPanel.add(userInfoPanel, BorderLayout.NORTH);
        mainPanel.add(menuPanel, BorderLayout.WEST);
        mainPanel.add(contentPanel, BorderLayout.CENTER);
        
        // Set content pane
        setContentPane(mainPanel);
        
        // Show users panel by default
        cardLayout.show(contentPanel, "users");
        
        // Display in fullscreen and set default close operation
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        AppStyle.setFullScreen(this);
        setVisible(true);
    }
    
    /**
     * Creates the menu panel with navigation options
     */
    private void createMenuPanel() {
        menuPanel = new JPanel();
        menuPanel.setLayout(new BoxLayout(menuPanel, BoxLayout.Y_AXIS));
        menuPanel.setBackground(AppStyle.DARK_NAVY);
        menuPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        menuPanel.setPreferredSize(new Dimension(200, 0));
        
        // Title
        JLabel titleLabel = new JLabel("ADMIN MENU");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 16));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        // User Management Button
        usersButton = new JButton("Users");
        AppStyle.styleMenuButton(usersButton, true);
        usersButton.addActionListener(e -> {
            cardLayout.show(contentPanel, "users");
            setActiveButton(usersButton);
        });
        
        // Ticket Management Button
        ticketsButton = new JButton("Tickets");
        AppStyle.styleMenuButton(ticketsButton, false);
        ticketsButton.addActionListener(e -> {
            cardLayout.show(contentPanel, "tickets");
            refreshTickets();
            setActiveButton(ticketsButton);
        });
        
        // Reservation Management Button
        reservationsButton = new JButton("Reservations");
        AppStyle.styleMenuButton(reservationsButton, false);
        reservationsButton.addActionListener(e -> {
            cardLayout.show(contentPanel, "reservations");
            refreshReservations();
            setActiveButton(reservationsButton);
        });
        
        // Logout Button
        logoutButton = new JButton("Logout");
        AppStyle.styleMenuButton(logoutButton, false);
        logoutButton.addActionListener(e -> logout());
        
        // Add spacing
        menuPanel.add(titleLabel);
        menuPanel.add(Box.createRigidArea(new Dimension(0, 30)));
        menuPanel.add(usersButton);
        menuPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        menuPanel.add(ticketsButton);
        menuPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        menuPanel.add(reservationsButton);
        menuPanel.add(Box.createVerticalGlue());
        menuPanel.add(logoutButton);
    }
    
    /**
     * Sets the active menu button
     */
    private void setActiveButton(JButton activeButton) {
        // Reset all buttons
        AppStyle.styleMenuButton(usersButton, false);
        AppStyle.styleMenuButton(ticketsButton, false);
        AppStyle.styleMenuButton(reservationsButton, false);
        
        // Set active button
        AppStyle.styleMenuButton(activeButton, true);
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
        welcomeLabel = AppStyle.createTitleLabel("Welcome to Tunisia Airways Admin Dashboard");
        welcomeLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        // Admin name
        adminNameLabel = new JLabel(admin.getFullName());
        adminNameLabel.setFont(AppStyle.FONT_SUBTITLE);
        adminNameLabel.setForeground(AppStyle.TEAL);
        adminNameLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        // Admin email
        JLabel adminEmailLabel = new JLabel(admin.getEmail());
        adminEmailLabel.setFont(AppStyle.FONT_REGULAR);
        adminEmailLabel.setForeground(new Color(100, 100, 100));
        adminEmailLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        // Admin role label
        JLabel adminRoleLabel = new JLabel("Administrator");
        adminRoleLabel.setFont(AppStyle.FONT_BOLD);
        adminRoleLabel.setForeground(AppStyle.SUCCESS);
        adminRoleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        // Add components to panel
        userDetailsPanel.add(welcomeLabel);
        userDetailsPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        userDetailsPanel.add(adminNameLabel);
        userDetailsPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        userDetailsPanel.add(adminEmailLabel);
        userDetailsPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        userDetailsPanel.add(adminRoleLabel);
        
        // Add user details panel to user info panel
        userInfoPanel.add(userDetailsPanel, BorderLayout.CENTER);
        
        // Add some spacing at the bottom
        userInfoPanel.add(Box.createRigidArea(new Dimension(0, 20)), BorderLayout.SOUTH);
    }
    
    /**
     * Creates the users management panel
     */
    private void createUsersPanel() {
        JPanel usersPanel = new JPanel(new BorderLayout(0, 20));
        usersPanel.setBackground(AppStyle.BACKGROUND);
        
        // Create title panel
        JPanel titlePanel = new JPanel(new BorderLayout());
        titlePanel.setBackground(AppStyle.WHITE);
        titlePanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(230, 230, 230), 1),
            BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));
        
        JLabel titleLabel = AppStyle.createTitleLabel("User Management");
        titlePanel.add(titleLabel, BorderLayout.WEST);
        
        // Create table
        String[] columnNames = {"ID", "Username", "Full Name", "Email", "Role", "Created At"};
        usersTable = createTable(columnNames);
        usersScrollPane = new JScrollPane(usersTable);
        usersScrollPane.setBorder(BorderFactory.createEmptyBorder());
        
        // Create button panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        buttonPanel.setBackground(AppStyle.BACKGROUND);
        
        addUserButton = new JButton("Add User");
        editUserButton = new JButton("Edit User");
        deleteUserButton = new JButton("Delete User");
        
        AppStyle.styleSecondaryButton(addUserButton);
        AppStyle.styleSecondaryButton(editUserButton);
        AppStyle.styleDangerButton(deleteUserButton);
        
        addUserButton.addActionListener(e -> addUser());
        editUserButton.addActionListener(e -> editUser());
        deleteUserButton.addActionListener(e -> deleteUser());
        
        buttonPanel.add(addUserButton);
        buttonPanel.add(editUserButton);
        buttonPanel.add(deleteUserButton);
        
        // Add components to panel
        usersPanel.add(titlePanel, BorderLayout.NORTH);
        usersPanel.add(usersScrollPane, BorderLayout.CENTER);
        usersPanel.add(buttonPanel, BorderLayout.SOUTH);
        
        // Add to content panel
        contentPanel.add(usersPanel, "users");
        
        // Load user data
        refreshUsers();
    }
    
    /**
     * Creates the tickets management panel
     */
    private void createTicketsPanel() {
        JPanel ticketsPanel = new JPanel(new BorderLayout(0, 20));
        ticketsPanel.setBackground(AppStyle.BACKGROUND);
        
        // Create title panel
        JPanel titlePanel = new JPanel(new BorderLayout());
        titlePanel.setBackground(AppStyle.WHITE);
        titlePanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(230, 230, 230), 1),
            BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));
        
        JLabel titleLabel = AppStyle.createTitleLabel("Ticket Management");
        titlePanel.add(titleLabel, BorderLayout.WEST);
        
        // Create table
        String[] columnNames = {"ID", "Ticket Number", "Flight Number", "From", "To", "Date", "Time", "Class", "Price", "Active"};
        ticketsTable = createTable(columnNames);
        ticketsScrollPane = new JScrollPane(ticketsTable);
        ticketsScrollPane.setBorder(BorderFactory.createEmptyBorder());
        
        // Create button panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        buttonPanel.setBackground(AppStyle.BACKGROUND);
        
        addTicketButton = new JButton("Add Ticket");
        editTicketButton = new JButton("Edit Ticket");
        deleteTicketButton = new JButton("Delete Ticket");
        
        AppStyle.styleSecondaryButton(addTicketButton);
        AppStyle.styleSecondaryButton(editTicketButton);
        AppStyle.styleDangerButton(deleteTicketButton);
        
        addTicketButton.addActionListener(e -> addTicket());
        editTicketButton.addActionListener(e -> editTicket());
        deleteTicketButton.addActionListener(e -> deleteTicket());
        
        buttonPanel.add(addTicketButton);
        buttonPanel.add(editTicketButton);
        buttonPanel.add(deleteTicketButton);
        
        // Add components to panel
        ticketsPanel.add(titlePanel, BorderLayout.NORTH);
        ticketsPanel.add(ticketsScrollPane, BorderLayout.CENTER);
        ticketsPanel.add(buttonPanel, BorderLayout.SOUTH);
        
        // Add to content panel
        contentPanel.add(ticketsPanel, "tickets");
    }
    
    /**
     * Creates the reservations management panel
     */
    private void createReservationsPanel() {
        JPanel reservationsPanel = new JPanel(new BorderLayout(0, 20));
        reservationsPanel.setBackground(AppStyle.BACKGROUND);
        
        // Create title panel
        JPanel titlePanel = new JPanel(new BorderLayout());
        titlePanel.setBackground(AppStyle.WHITE);
        titlePanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(230, 230, 230), 1),
            BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));
        
        JLabel titleLabel = AppStyle.createTitleLabel("Reservation Management");
        titlePanel.add(titleLabel, BorderLayout.WEST);
        
        // Create table
        String[] columnNames = {"ID", "User", "Ticket", "Passenger Name", "Passengers", "Booking Date", "Status", "Confirmation"};
        reservationsTable = createTable(columnNames);
        reservationsScrollPane = new JScrollPane(reservationsTable);
        reservationsScrollPane.setBorder(BorderFactory.createEmptyBorder());
        
        // Create button panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        buttonPanel.setBackground(AppStyle.BACKGROUND);
        
        viewReservationButton = new JButton("View Details");
        updateStatusButton = new JButton("Update Status");
        
        AppStyle.styleSecondaryButton(viewReservationButton);
        AppStyle.styleSecondaryButton(updateStatusButton);
        
        viewReservationButton.addActionListener(e -> viewReservation());
        updateStatusButton.addActionListener(e -> updateReservationStatus());
        
        buttonPanel.add(viewReservationButton);
        buttonPanel.add(updateStatusButton);
        
        // Add components to panel
        reservationsPanel.add(titlePanel, BorderLayout.NORTH);
        reservationsPanel.add(reservationsScrollPane, BorderLayout.CENTER);
        reservationsPanel.add(buttonPanel, BorderLayout.SOUTH);
        
        // Add to content panel
        contentPanel.add(reservationsPanel, "reservations");
    }
    
    /**
     * Creates a styled table
     */
    private JTable createTable(String[] columnNames) {
        // Create empty table model
        DefaultTableModel model = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Make cells non-editable
            }
        };
        
        JTable table = new JTable(model);
        AppStyle.styleTable(table);
        
        return table;
    }
    
    /**
     * Refreshes the users table with data from database
     */
    public void refreshUsers() {
        DefaultTableModel model = (DefaultTableModel) usersTable.getModel();
        model.setRowCount(0); // Clear existing data
        
        // Get users from database
        List<User> users = UserDAO.getAllUsers();
        
        // Add users to table
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        for (User user : users) {
            model.addRow(new Object[] {
                user.getUserId(),
                user.getUsername(),
                user.getFullName(),
                user.getEmail(),
                user.getRole(),
                user.getCreatedAt() != null ? dateFormat.format(user.getCreatedAt()) : "N/A"
            });
        }
    }
    
    /**
     * Refreshes the tickets table with data from database
     */
    public void refreshTickets() {
        DefaultTableModel model = (DefaultTableModel) ticketsTable.getModel();
        model.setRowCount(0); // Clear existing data
        
        // Get tickets from database
        List<Ticket> tickets = TicketDAO.getAllTickets();
        
        // Add tickets to table
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        for (Ticket ticket : tickets) {
            model.addRow(new Object[] {
                ticket.getTicketId(),
                ticket.getTicketNumber(),
                ticket.getFlightNumber(),
                ticket.getFromCity(),
                ticket.getToCity(),
                dateFormat.format(ticket.getDepartureDate()),
                ticket.getDepartureTime(),
                ticket.getTravelClass(),
                ticket.getPrice() + " TND",
                ticket.isActive() ? "Yes" : "No"
            });
        }
    }
    
    /**
     * Refreshes the reservations table with data from database
     */
    public void refreshReservations() {
        DefaultTableModel model = (DefaultTableModel) reservationsTable.getModel();
        model.setRowCount(0); // Clear existing data
        
        // Get reservations from database
        List<Reservation> reservations = ReservationDAO.getAllReservations();
        
        // Add reservations to table
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        for (Reservation reservation : reservations) {
            // Get username
            User user = UserDAO.getUserById(reservation.getUserId());
            String username = user != null ? user.getUsername() : "Unknown";
            
            // Get ticket number
            Ticket ticket = TicketDAO.getTicketById(reservation.getTicketId());
            String ticketNumber = ticket != null ? ticket.getTicketNumber() : "Unknown";
            
            model.addRow(new Object[] {
                reservation.getReservationId(),
                username,
                ticketNumber,
                reservation.getPassengerName(),
                reservation.getTotalPassengers(),
                dateFormat.format(reservation.getBookingDate()),
                reservation.getStatus(),
                reservation.getConfirmationCode()
            });
        }
    }
    
    /**
     * Add user action
     */
    private void addUser() {
        new UserForm(this, null);
    }
    
    /**
     * Edit user action
     */
    private void editUser() {
        int selectedRow = usersTable.getSelectedRow();
        
        if (selectedRow >= 0) {
            int userId = (int) usersTable.getValueAt(selectedRow, 0);
            User user = UserDAO.getUserById(userId);
            
            if (user != null) {
                new UserForm(this, user);
            }
        } else {
            JOptionPane.showMessageDialog(this,
                "Please select a user to edit.",
                "No Selection",
                JOptionPane.INFORMATION_MESSAGE);
        }
    }
    
    /**
     * Delete user action
     */
    private void deleteUser() {
        int selectedRow = usersTable.getSelectedRow();
        
        if (selectedRow >= 0) {
            int userId = (int) usersTable.getValueAt(selectedRow, 0);
            
            // Prevent deleting yourself
            if (userId == admin.getUserId()) {
                JOptionPane.showMessageDialog(this,
                    "You cannot delete your own admin account.",
                    "Operation Not Allowed",
                    JOptionPane.WARNING_MESSAGE);
                return;
            }
            
            // Confirm deletion
            int choice = JOptionPane.showConfirmDialog(this,
                "Are you sure you want to delete this user?\nThis action cannot be undone.",
                "Confirm Deletion",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE);
                
            if (choice == JOptionPane.YES_OPTION) {
                boolean success = UserDAO.deleteUser(userId);
                
                if (success) {
                    JOptionPane.showMessageDialog(this,
                        "User deleted successfully.",
                        "Success",
                        JOptionPane.INFORMATION_MESSAGE);
                    
                    // Refresh the users list
                    refreshUsers();
                } else {
                    JOptionPane.showMessageDialog(this,
                        "Failed to delete user. They may have active reservations.",
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
                }
            }
        } else {
            JOptionPane.showMessageDialog(this,
                "Please select a user to delete.",
                "No Selection",
                JOptionPane.INFORMATION_MESSAGE);
        }
    }
    
    /**
     * Add ticket action
     */
    private void addTicket() {
        new TicketForm(this, null);
    }
    
    /**
     * Edit ticket action
     */
    private void editTicket() {
        int selectedRow = ticketsTable.getSelectedRow();
        
        if (selectedRow >= 0) {
            int ticketId = (int) ticketsTable.getValueAt(selectedRow, 0);
            Ticket ticket = TicketDAO.getTicketById(ticketId);
            
            if (ticket != null) {
                new TicketForm(this, ticket);
            }
        } else {
            JOptionPane.showMessageDialog(this,
                "Please select a ticket to edit.",
                "No Selection",
                JOptionPane.INFORMATION_MESSAGE);
        }
    }
    
    /**
     * Delete ticket action
     */
    private void deleteTicket() {
        int selectedRow = ticketsTable.getSelectedRow();
        
        if (selectedRow >= 0) {
            int ticketId = (int) ticketsTable.getValueAt(selectedRow, 0);
            
            // Check if ticket has reservations
            List<Reservation> reservations = ReservationDAO.getReservationsByTicketId(ticketId);
            if (!reservations.isEmpty()) {
                JOptionPane.showMessageDialog(this,
                    "Cannot delete ticket. It has active reservations.",
                    "Operation Not Allowed",
                    JOptionPane.WARNING_MESSAGE);
                return;
            }
            
            // Confirm deletion
            int choice = JOptionPane.showConfirmDialog(this,
                "Are you sure you want to delete this ticket?\nThis action cannot be undone.",
                "Confirm Deletion",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE);
                
            if (choice == JOptionPane.YES_OPTION) {
                boolean success = TicketDAO.deleteTicket(ticketId);
                
                if (success) {
                    JOptionPane.showMessageDialog(this,
                        "Ticket deleted successfully.",
                        "Success",
                        JOptionPane.INFORMATION_MESSAGE);
                    
                    // Refresh the tickets list
                    refreshTickets();
                } else {
                    JOptionPane.showMessageDialog(this,
                        "Failed to delete ticket.",
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
                }
            }
        } else {
            JOptionPane.showMessageDialog(this,
                "Please select a ticket to delete.",
                "No Selection",
                JOptionPane.INFORMATION_MESSAGE);
        }
    }
    
    /**
     * View reservation details
     */
    private void viewReservation() {
        int selectedRow = reservationsTable.getSelectedRow();
        
        if (selectedRow >= 0) {
            int reservationId = (int) reservationsTable.getValueAt(selectedRow, 0);
            Reservation reservation = ReservationDAO.getReservationById(reservationId);
            
            if (reservation != null) {
                Ticket ticket = TicketDAO.getTicketById(reservation.getTicketId());
                User user = UserDAO.getUserById(reservation.getUserId());
                
                if (ticket != null && user != null) {
                    // Show the reservation details and ticket
                    new PrintTicket1(ticket, reservation, user);
                }
            }
        } else {
            JOptionPane.showMessageDialog(this,
                "Please select a reservation to view.",
                "No Selection",
                JOptionPane.INFORMATION_MESSAGE);
        }
    }
    
    /**
     * Update reservation status
     */
    private void updateReservationStatus() {
        int selectedRow = reservationsTable.getSelectedRow();
        
        if (selectedRow >= 0) {
            int reservationId = (int) reservationsTable.getValueAt(selectedRow, 0);
            Reservation reservation = ReservationDAO.getReservationById(reservationId);
            
            if (reservation != null) {
                String[] statusOptions = {
                    Reservation.Status.PENDING.toString(),
                    Reservation.Status.CONFIRMED.toString(),
                    Reservation.Status.CANCELLED.toString(),
                    Reservation.Status.COMPLETED.toString()
                };
                
                String currentStatus = reservation.getStatus().toString();
                
                String newStatus = (String) JOptionPane.showInputDialog(
                    this,
                    "Select new status for reservation " + reservation.getConfirmationCode(),
                    "Update Status",
                    JOptionPane.QUESTION_MESSAGE,
                    null,
                    statusOptions,
                    currentStatus
                );
                
                if (newStatus != null && !newStatus.equals(currentStatus)) {
                    reservation.setStatus(Reservation.Status.valueOf(newStatus));
                    boolean success = ReservationDAO.updateReservation(reservation);
                    
                    if (success) {
                        JOptionPane.showMessageDialog(this,
                            "Reservation status updated successfully.",
                            "Success",
                            JOptionPane.INFORMATION_MESSAGE);
                        
                        // Refresh the reservations list
                        refreshReservations();
                    } else {
                        JOptionPane.showMessageDialog(this,
                            "Failed to update reservation status.",
                            "Error",
                            JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        } else {
            JOptionPane.showMessageDialog(this,
                "Please select a reservation to update.",
                "No Selection",
                JOptionPane.INFORMATION_MESSAGE);
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