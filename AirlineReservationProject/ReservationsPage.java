import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.border.*;
import javax.swing.table.*;
import java.util.List;
import java.text.SimpleDateFormat;

/**
 * ReservationsPage class to display a user's flight reservations.
 */
public class ReservationsPage extends JFrame {
    // Main panels
    private JPanel mainPanel;
    private JPanel tablePanel;
    private JPanel buttonPanel;
    
    // Table components
    private JTable reservationsTable;
    private JScrollPane scrollPane;
    
    // Buttons
    private JButton backButton;
    private JButton viewTicketButton;
    private JButton cancelReservationButton;
    
    // Reference to previous screen and user
    private MainMenuPage mainMenuPage;
    private User user;
    
    // Data
    private List<Reservation> reservations;
    private Ticket[] tickets;
    
    /**
     * Constructor - creates the reservations page
     */
    public ReservationsPage(MainMenuPage mainMenuPage, User user, List<Reservation> reservations) {
        // Set the window title
        super("My Reservations - Tunisia Airways");
        
        // Save references
        this.mainMenuPage = mainMenuPage;
        this.user = user;
        this.reservations = reservations;
        
        // Load ticket data for each reservation
        loadTicketData();
        
        // Create the main panel
        mainPanel = new JPanel(new BorderLayout(20, 20));
        mainPanel.setBackground(AppStyle.BACKGROUND);
        mainPanel.setBorder(BorderFactory.createEmptyBorder(40, 40, 40, 40));
        
        // Create the table panel
        createTablePanel();
        
        // Create the button panel
        createButtonPanel();
        
        // Add panels to the main panel
        mainPanel.add(tablePanel, BorderLayout.CENTER);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);
        
        // Set content pane
        setContentPane(mainPanel);
        
        // Display in fullscreen and set default close operation
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        AppStyle.setFullScreen(this);
        setVisible(true);
    }
    
    /**
     * Loads the ticket data for each reservation
     */
    private void loadTicketData() {
        tickets = new Ticket[reservations.size()];
        
        for (int i = 0; i < reservations.size(); i++) {
            Reservation reservation = reservations.get(i);
            tickets[i] = TicketDAO.getTicketById(reservation.getTicketId());
        }
    }
    
    /**
     * Creates the table panel with reservations data
     */
    private void createTablePanel() {
        tablePanel = new JPanel(new BorderLayout());
        tablePanel.setBackground(AppStyle.WHITE);
        tablePanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(AppStyle.TEAL, 1),
            BorderFactory.createEmptyBorder(20, 20, 20, 20)
        ));
        
        // Create title label
        JLabel titleLabel = AppStyle.createTitleLabel("My Reservations");
        
        // Create table model
        String[] columnNames = {
            "Confirmation", "From", "To", "Date", "Class", "Passengers", "Status", "Price"
        };
        
        Object[][] data = new Object[reservations.size()][8];
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        
        for (int i = 0; i < reservations.size(); i++) {
            Reservation reservation = reservations.get(i);
            Ticket ticket = tickets[i];
            
            if (ticket != null) {
                data[i][0] = reservation.getConfirmationCode();
                data[i][1] = ticket.getFromCity();
                data[i][2] = ticket.getToCity();
                data[i][3] = dateFormat.format(ticket.getDepartureDate());
                data[i][4] = ticket.getTravelClass();
                data[i][5] = reservation.getTotalPassengers() + " (" + 
                             reservation.getAdults() + "A, " + 
                             reservation.getChildren() + "C, " + 
                             reservation.getInfants() + "I)";
                data[i][6] = reservation.getStatus();
                data[i][7] = ticket.getPrice() + " TND";
            }
        }
        
        // Create table with custom model
        DefaultTableModel model = new DefaultTableModel(data, columnNames) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Make table non-editable
            }
        };
        
        reservationsTable = new JTable(model);
        
        // Style the table
        AppStyle.styleTable(reservationsTable);
        
        // Center align the text in cells
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        
        for (int i = 0; i < reservationsTable.getColumnCount(); i++) {
            reservationsTable.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }
        
        // Set preferred widths
        reservationsTable.getColumnModel().getColumn(0).setPreferredWidth(120); // Confirmation code
        reservationsTable.getColumnModel().getColumn(1).setPreferredWidth(80);  // From
        reservationsTable.getColumnModel().getColumn(2).setPreferredWidth(80);  // To
        reservationsTable.getColumnModel().getColumn(3).setPreferredWidth(100); // Date
        reservationsTable.getColumnModel().getColumn(4).setPreferredWidth(80);  // Class
        reservationsTable.getColumnModel().getColumn(5).setPreferredWidth(150); // Passengers
        reservationsTable.getColumnModel().getColumn(6).setPreferredWidth(100); // Status
        reservationsTable.getColumnModel().getColumn(7).setPreferredWidth(80);  // Price
        
        // Add table to a scroll pane
        scrollPane = new JScrollPane(reservationsTable);
        scrollPane.setBorder(BorderFactory.createEmptyBorder(20, 0, 0, 0));
        
        // Add components to the panel
        tablePanel.add(titleLabel, BorderLayout.NORTH);
        tablePanel.add(scrollPane, BorderLayout.CENTER);
    }
    
    /**
     * Creates the button panel with action buttons
     */
    private void createButtonPanel() {
        buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 0));
        buttonPanel.setBackground(AppStyle.BACKGROUND);
        
        // View Ticket button
        viewTicketButton = new JButton("View Ticket");
        AppStyle.styleSecondaryButton(viewTicketButton);
        viewTicketButton.addActionListener(e -> viewSelectedTicket());
        
        // Cancel Reservation button
        cancelReservationButton = new JButton("Cancel Reservation");
        AppStyle.styleDangerButton(cancelReservationButton);
        cancelReservationButton.addActionListener(e -> cancelSelectedReservation());
        
        // Back button
        backButton = new JButton("Back to Main Menu");
        AppStyle.styleNeutralButton(backButton);
        backButton.addActionListener(e -> backToMainMenu());
        
        // Add buttons to panel
        buttonPanel.add(viewTicketButton);
        buttonPanel.add(cancelReservationButton);
        buttonPanel.add(backButton);
    }
    
    /**
     * View the selected ticket
     */
    private void viewSelectedTicket() {
        int selectedRow = reservationsTable.getSelectedRow();
        
        if (selectedRow >= 0) {
            Reservation reservation = reservations.get(selectedRow);
            Ticket ticket = tickets[selectedRow];
            
            // Open ticket view
            new PrintTicket1(ticket, reservation, user);
        } else {
            JOptionPane.showMessageDialog(this,
                "Please select a reservation to view.",
                "No Selection",
                JOptionPane.INFORMATION_MESSAGE);
        }
    }
    
    /**
     * Cancel the selected reservation
     */
    private void cancelSelectedReservation() {
        int selectedRow = reservationsTable.getSelectedRow();
        
        if (selectedRow >= 0) {
            Reservation reservation = reservations.get(selectedRow);
            
            // Confirm cancellation
            int choice = JOptionPane.showConfirmDialog(this,
                "Are you sure you want to cancel this reservation?\n" +
                "Confirmation Code: " + reservation.getConfirmationCode(),
                "Confirm Cancellation",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE);
                
            if (choice == JOptionPane.YES_OPTION) {
                // Update reservation status to cancelled
                reservation.setStatus(Reservation.Status.CANCELLED);
                boolean success = ReservationDAO.updateReservation(reservation);
                
                if (success) {
                    JOptionPane.showMessageDialog(this,
                        "Reservation cancelled successfully.",
                        "Cancellation Successful",
                        JOptionPane.INFORMATION_MESSAGE);
                    
                    // Refresh the reservations list
                    refreshReservations();
                } else {
                    JOptionPane.showMessageDialog(this,
                        "Failed to cancel reservation. Please try again.",
                        "Cancellation Failed",
                        JOptionPane.ERROR_MESSAGE);
                }
            }
        } else {
            JOptionPane.showMessageDialog(this,
                "Please select a reservation to cancel.",
                "No Selection",
                JOptionPane.INFORMATION_MESSAGE);
        }
    }
    
    /**
     * Refresh the reservations data
     */
    private void refreshReservations() {
        // Get updated reservations from database
        reservations = ReservationDAO.getReservationsByUserId(user.getUserId());
        
        // Reload ticket data
        loadTicketData();
        
        // Update table data
        DefaultTableModel model = (DefaultTableModel) reservationsTable.getModel();
        model.setRowCount(0); // Clear existing data
        
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        
        for (int i = 0; i < reservations.size(); i++) {
            Reservation reservation = reservations.get(i);
            Ticket ticket = tickets[i];
            
            if (ticket != null) {
                model.addRow(new Object[] {
                    reservation.getConfirmationCode(),
                    ticket.getFromCity(),
                    ticket.getToCity(),
                    dateFormat.format(ticket.getDepartureDate()),
                    ticket.getTravelClass(),
                    reservation.getTotalPassengers() + " (" + 
                    reservation.getAdults() + "A, " + 
                    reservation.getChildren() + "C, " + 
                    reservation.getInfants() + "I)",
                    reservation.getStatus(),
                    ticket.getPrice() + " TND"
                });
            }
        }
    }
    
    /**
     * Navigate back to the main menu
     */
    private void backToMainMenu() {
        // Show main menu
        mainMenuPage.setVisible(true);
        
        // Close this window
        dispose();
    }
} 