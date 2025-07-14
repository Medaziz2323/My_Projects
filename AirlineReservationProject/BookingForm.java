import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.border.*;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * BookingForm class to handle ticket booking with passenger information.
 */
public class BookingForm extends JFrame {
    // Main panels
    private JPanel mainPanel;
    private JPanel formPanel;
    private JPanel ticketInfoPanel;
    private JPanel buttonPanel;
    
    // Form fields
    private JTextField passengerNameField;
    private JComboBox<String> adultBox;
    private JComboBox<String> childrenBox;
    private JComboBox<String> infantBox;
    
    // Info labels
    private JLabel flightNumberValue;
    private JLabel fromValue;
    private JLabel toValue;
    private JLabel dateValue;
    private JLabel timeValue;
    private JLabel classValue;
    private JLabel priceValue;
    private JLabel totalPriceValue;
    
    // Buttons
    private JButton bookButton;
    private JButton cancelButton;
    
    // Reference to previous screen and user
    private AvailableTicketsPage ticketsPage;
    private User user;
    
    // The ticket to book
    private Ticket ticket;
    
    // Options for dropdowns
    private String[] adultOptions = {
        "1", "2", "3", "4", "5", "6"
    };
    
    private String[] childOptions = {
        "0", "1", "2", "3", "4"
    };
    
    private String[] infantOptions = {
        "0", "1", "2", "3"
    };
    
    /**
     * Constructor - creates the booking form
     */
    public BookingForm(AvailableTicketsPage ticketsPage, User user, Ticket ticket) {
        // Set the window title
        super("Book Flight - Tunisia Airways");
        
        // Save references
        this.ticketsPage = ticketsPage;
        this.user = user;
        this.ticket = ticket;
        
        // Create the main panel
        mainPanel = new JPanel(new BorderLayout(20, 20));
        mainPanel.setBackground(AppStyle.BACKGROUND);
        mainPanel.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));
        
        // Create ticket info panel
        createTicketInfoPanel();
        
        // Create form panel
        createFormPanel();
        
        // Create button panel
        createButtonPanel();
        
        // Add panels to main panel
        mainPanel.add(ticketInfoPanel, BorderLayout.NORTH);
        mainPanel.add(formPanel, BorderLayout.CENTER);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);
        
        // Set content pane
        setContentPane(mainPanel);
        
        // Display in fullscreen and set default close operation
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        AppStyle.setFullScreen(this);
        setVisible(true);
    }
    
    /**
     * Creates the ticket info panel
     */
    private void createTicketInfoPanel() {
        ticketInfoPanel = new JPanel(new BorderLayout());
        ticketInfoPanel.setBackground(AppStyle.WHITE);
        ticketInfoPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(AppStyle.TEAL, 1),
            BorderFactory.createEmptyBorder(20, 20, 20, 20)
        ));
        
        // Panel title
        JLabel titleLabel = AppStyle.createTitleLabel("Ticket Information");
        
        // Create grid for ticket details
        JPanel detailsPanel = new JPanel(new GridLayout(0, 4, 10, 10));
        detailsPanel.setBackground(AppStyle.WHITE);
        
        // Format date
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        String formattedDate = dateFormat.format(ticket.getDepartureDate());
        
        // Flight number
        JLabel flightNumberLabel = AppStyle.createBoldLabel("Flight Number:");
        flightNumberValue = AppStyle.createLabel(ticket.getFlightNumber());
        
        // From city
        JLabel fromLabel = AppStyle.createBoldLabel("From:");
        fromValue = AppStyle.createLabel(ticket.getFromCity());
        
        // To city
        JLabel toLabel = AppStyle.createBoldLabel("To:");
        toValue = AppStyle.createLabel(ticket.getToCity());
        
        // Date
        JLabel dateLabel = AppStyle.createBoldLabel("Date:");
        dateValue = AppStyle.createLabel(formattedDate);
        
        // Time
        JLabel timeLabel = AppStyle.createBoldLabel("Time:");
        timeValue = AppStyle.createLabel(ticket.getDepartureTime());
        
        // Class
        JLabel classLabel = AppStyle.createBoldLabel("Class:");
        classValue = AppStyle.createLabel(ticket.getTravelClass());
        
        // Price
        JLabel priceLabel = AppStyle.createBoldLabel("Price Per Person:");
        priceValue = AppStyle.createLabel(ticket.getPrice() + " TND");
        
        // Add all components to grid
        detailsPanel.add(flightNumberLabel);
        detailsPanel.add(flightNumberValue);
        detailsPanel.add(fromLabel);
        detailsPanel.add(fromValue);
        detailsPanel.add(toLabel);
        detailsPanel.add(toValue);
        detailsPanel.add(dateLabel);
        detailsPanel.add(dateValue);
        detailsPanel.add(timeLabel);
        detailsPanel.add(timeValue);
        detailsPanel.add(classLabel);
        detailsPanel.add(classValue);
        detailsPanel.add(priceLabel);
        detailsPanel.add(priceValue);
        
        // Add title and details to the panel
        ticketInfoPanel.add(titleLabel, BorderLayout.NORTH);
        ticketInfoPanel.add(detailsPanel, BorderLayout.CENTER);
    }
    
    /**
     * Creates the form panel
     */
    private void createFormPanel() {
        formPanel = new JPanel(new BorderLayout());
        formPanel.setBackground(AppStyle.WHITE);
        formPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(AppStyle.TEAL, 1),
            BorderFactory.createEmptyBorder(20, 20, 20, 20)
        ));
        
        // Form title
        JLabel formTitle = AppStyle.createTitleLabel("Passenger Information");
        
        // Create grid for form fields
        JPanel fieldsPanel = new JPanel(new GridBagLayout());
        fieldsPanel.setBackground(AppStyle.WHITE);
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);
        
        // Passenger name field
        JLabel nameLabel = AppStyle.createBoldLabel("Passenger Name:");
        gbc.gridx = 0;
        gbc.gridy = 0;
        fieldsPanel.add(nameLabel, gbc);
        
        passengerNameField = new JTextField(20);
        AppStyle.styleTextField(passengerNameField);
        passengerNameField.setText(user.getFullName()); // Pre-fill with user's name
        gbc.gridx = 1;
        gbc.gridy = 0;
        fieldsPanel.add(passengerNameField, gbc);
        
        // Number of adults
        JLabel adultLabel = AppStyle.createBoldLabel("Adults:");
        gbc.gridx = 0;
        gbc.gridy = 1;
        fieldsPanel.add(adultLabel, gbc);
        
        adultBox = new JComboBox<>(adultOptions);
        AppStyle.styleComboBox(adultBox);
        adultBox.addActionListener(e -> updateTotalPrice());
        gbc.gridx = 1;
        gbc.gridy = 1;
        fieldsPanel.add(adultBox, gbc);
        
        // Number of children
        JLabel childrenLabel = AppStyle.createBoldLabel("Children:");
        gbc.gridx = 0;
        gbc.gridy = 2;
        fieldsPanel.add(childrenLabel, gbc);
        
        childrenBox = new JComboBox<>(childOptions);
        AppStyle.styleComboBox(childrenBox);
        childrenBox.addActionListener(e -> updateTotalPrice());
        gbc.gridx = 1;
        gbc.gridy = 2;
        fieldsPanel.add(childrenBox, gbc);
        
        // Number of infants
        JLabel infantLabel = AppStyle.createBoldLabel("Infants:");
        gbc.gridx = 0;
        gbc.gridy = 3;
        fieldsPanel.add(infantLabel, gbc);
        
        infantBox = new JComboBox<>(infantOptions);
        AppStyle.styleComboBox(infantBox);
        infantBox.addActionListener(e -> updateTotalPrice());
        gbc.gridx = 1;
        gbc.gridy = 3;
        fieldsPanel.add(infantBox, gbc);
        
        // Total price
        JLabel totalPriceLabel = AppStyle.createBoldLabel("Total Price:");
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.insets = new Insets(20, 5, 5, 5);
        fieldsPanel.add(totalPriceLabel, gbc);
        
        totalPriceValue = AppStyle.createLabel(ticket.getPrice() + " TND"); // Initial price
        gbc.gridx = 1;
        gbc.gridy = 4;
        fieldsPanel.add(totalPriceValue, gbc);
        
        // Add components to form panel
        formPanel.add(formTitle, BorderLayout.NORTH);
        formPanel.add(fieldsPanel, BorderLayout.CENTER);
    }
    
    /**
     * Updates the total price based on passenger numbers
     */
    private void updateTotalPrice() {
        int adults = Integer.parseInt((String) adultBox.getSelectedItem());
        int children = Integer.parseInt((String) childrenBox.getSelectedItem());
        int infants = Integer.parseInt((String) infantBox.getSelectedItem());
        
        // Calculate total price (infants are free, children at 50% price)
        double totalPrice = (adults * ticket.getPrice()) + (children * ticket.getPrice() * 0.5);
        
        // Update the total price label
        totalPriceValue.setText(totalPrice + " TND");
    }
    
    /**
     * Creates the button panel
     */
    private void createButtonPanel() {
        buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 0));
        buttonPanel.setBackground(AppStyle.BACKGROUND);
        
        // Book button
        bookButton = new JButton("Book Now");
        AppStyle.stylePrimaryButton(bookButton);
        bookButton.addActionListener(e -> bookTicket());
        
        // Cancel button
        cancelButton = new JButton("Cancel");
        AppStyle.styleDangerButton(cancelButton);
        cancelButton.addActionListener(e -> cancel());
        
        // Add buttons to panel
        buttonPanel.add(bookButton);
        buttonPanel.add(cancelButton);
    }
    
    /**
     * Book the ticket with entered passenger information
     */
    private void bookTicket() {
        // Get form data
        String passengerName = passengerNameField.getText().trim();
        int adults = Integer.parseInt((String) adultBox.getSelectedItem());
        int children = Integer.parseInt((String) childrenBox.getSelectedItem());
        int infants = Integer.parseInt((String) infantBox.getSelectedItem());
        
        // Validate passenger name
        if (passengerName.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                "Please enter passenger name.",
                "Missing Information",
                JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        // Generate confirmation code (simple implementation)
        String confirmationCode = "TN" + (10000 + (int)(Math.random() * 90000));
        
        // Create a new reservation
        Reservation reservation = new Reservation(
            0, // ID will be set by database
            user.getUserId(),
            ticket.getTicketId(),
            passengerName,
            adults,
            children,
            infants,
            new Date(), // Current date
            Reservation.Status.CONFIRMED,
            confirmationCode
        );
        
        // Save to database
        boolean success = ReservationDAO.insertReservation(reservation);
        
        if (success) {
            // Show success message
            JOptionPane.showMessageDialog(this,
                "Ticket booked successfully!\n" +
                "Confirmation Code: " + confirmationCode,
                "Booking Successful",
                JOptionPane.INFORMATION_MESSAGE);
            
            // Open the ticket view
            new PrintTicket1(ticket, reservation, user);
            
            // Close this window
            dispose();
        } else {
            // Show error message
            JOptionPane.showMessageDialog(this,
                "Failed to book ticket. Please try again.",
                "Booking Failed",
                JOptionPane.ERROR_MESSAGE);
        }
    }
    
    /**
     * Cancel booking and return to tickets page
     */
    private void cancel() {
        // Close this window
        dispose();
    }
} 