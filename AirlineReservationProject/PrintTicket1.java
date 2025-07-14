import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.border.*;
import java.util.Date;
import java.text.SimpleDateFormat;

/**
 * This class displays a flight ticket with all booking details.
 * It creates a simple window showing the ticket in a nice format.
 */
public class PrintTicket1 extends JFrame {
    // The panels that make up the window
    private JPanel mainPanel;      // Contains everything
    private JPanel ticketPanel;    // Contains the ticket itself
    private JPanel buttonPanel;    // Contains the close button
    
    // Labels for key information
    private JLabel titleLabel;     // "E-TICKET"
    private JLabel airlineLabel;   // "Tunisia Airways"
    private JButton closeButton;   // Button to close the window
    
    // Ticket information
    private Ticket ticket;
    private Reservation reservation;
    private User user;
    
    /**
     * Creates a new ticket window with a Ticket and Reservation from database
     */
    public PrintTicket1(Ticket ticket, Reservation reservation, User user) {
        // Set the window title
        super("Flight Ticket");
        
        // Store the ticket and reservation
        this.ticket = ticket;
        this.reservation = reservation;
        this.user = user;
        
        // Set up the window and all components
        setupWindow();
        
        // Display in fullscreen and set default close operation
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        AppStyle.setFullScreen(this);
        setVisible(true);
    }
    
    /**
     * Constructor for backward compatibility
     */
    public PrintTicket1(String from, String to, String travelClass, String passengerName,
                      Integer adults, Integer children, Integer infants,
                      String bookingDate, Integer price, String time) {
        // Set the window title
        super("Flight Ticket");
        
        try {
            // Parse the booking date
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
            Date parsedDate = dateFormat.parse(bookingDate);
            
            // Create temporary objects for display
            this.ticket = new Ticket(
                "TK" + System.currentTimeMillis() % 10000,
                1,
                "TN" + from.substring(0, 1) + to.substring(0, 1) + (int)(Math.random() * 1000),
                from,
                to,
                parsedDate,
                time,
                travelClass,
                price
            );
            
            this.reservation = new Reservation(
                1,
                1,
                1,
                passengerName,
                adults,
                children,
                infants,
                parsedDate,
                Reservation.Status.CONFIRMED,
                "TN" + (10000 + (int)(Math.random() * 90000))
            );
            
            this.user = new User(
                1, 
                "guest", 
                "", 
                passengerName, 
                "guest@example.com", 
                User.Role.USER
            );
            
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        // Set up the window and all components
        setupWindow();
        
        // Display in fullscreen and set default close operation
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        AppStyle.setFullScreen(this);
        setVisible(true);
    }
    
    /**
     * Sets up all the components of the window
     */
    private void setupWindow() {
        // Create the main panel with some padding
        mainPanel = new JPanel(new BorderLayout(20, 20));
        mainPanel.setBackground(AppStyle.BACKGROUND);
        mainPanel.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));
        
        // Create the ticket and button panels
        ticketPanel = createTicketPanel();
        buttonPanel = createButtonPanel();
        
        // Add the panels to the main panel
        mainPanel.add(ticketPanel, BorderLayout.CENTER);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);
        
        // Set the content of the window to our main panel
        setContentPane(mainPanel);
    }
    
    /**
     * Creates the panel that displays the actual ticket
     */
    private JPanel createTicketPanel() {
        // Create a panel with absolute positioning (null layout)
        JPanel panel = new JPanel(null);
        panel.setBackground(AppStyle.WHITE);
        
        // Add a nice border around the ticket
        panel.setBorder(
            BorderFactory.createCompoundBorder(
                BorderFactory.createEmptyBorder(20, 20, 20, 20),
                BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(AppStyle.TEAL, 1),
                    BorderFactory.createEmptyBorder(30, 30, 30, 30)
                )
            )
        );
        
        // Add all the sections of the ticket
        addTitleToTicket(panel);
        addBookingReferenceToTicket(panel);
        addFlightDetailsToTicket(panel);
        addPassengerDetailsToTicket(panel);
        addPriceDetailsToTicket(panel);
        
        return panel;
    }
    
    /**
     * Adds the title section to the ticket
     */
    private void addTitleToTicket(JPanel panel) {
        // Add "E-TICKET" title
        titleLabel = new JLabel("E-TICKET");
        titleLabel.setFont(AppStyle.FONT_TITLE);
        titleLabel.setForeground(AppStyle.TEXT);
        titleLabel.setBounds(320, 20, 160, 30);
        
        // Add airline name
        airlineLabel = new JLabel("Tunisia Airways");
        airlineLabel.setFont(AppStyle.FONT_SUBTITLE);
        airlineLabel.setForeground(AppStyle.ORANGE);
        airlineLabel.setBounds(320, 60, 160, 25);
        
        // Add horizontal line separator
        JSeparator separator = new JSeparator();
        separator.setBounds(40, 95, 680, 1);
        separator.setForeground(AppStyle.TEAL);
        
        // Add components to the panel
        panel.add(titleLabel);
        panel.add(airlineLabel);
        panel.add(separator);
    }
    
    /**
     * Adds the booking reference section to the ticket
     */
    private void addBookingReferenceToTicket(JPanel panel) {
        // Add the confirmation code info
        JLabel refLabel = createHeaderLabel("Booking Reference", 40, 110, 200, 25);
        
        JLabel confirmationLabel = createBoldLabel("Confirmation Code:", 40, 145, 150, 20);
        JLabel confirmationValue = createNormalLabel(reservation.getConfirmationCode(), 190, 145, 150, 20);
        
        JLabel bookingDateLabel = createBoldLabel("Booking Date:", 340, 145, 150, 20);
        
        // Format the booking date
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        String bookingDate = dateFormat.format(reservation.getBookingDate());
        JLabel bookingDateValue = createNormalLabel(bookingDate, 490, 145, 150, 20);
        
        // Add a separator line
        JSeparator separator = new JSeparator();
        separator.setBounds(40, 175, 680, 1);
        separator.setForeground(AppStyle.TEAL);
        
        // Add components to the panel
        panel.add(refLabel);
        panel.add(confirmationLabel);
        panel.add(confirmationValue);
        panel.add(bookingDateLabel);
        panel.add(bookingDateValue);
        panel.add(separator);
    }
    
    /**
     * Adds the flight details section to the ticket
     */
    private void addFlightDetailsToTicket(JPanel panel) {
        // Add the flight information section header
        JLabel flightLabel = createHeaderLabel("Flight Information", 40, 190, 200, 25);
        
        // Flight number
        JLabel flightNumberLabel = createBoldLabel("Flight Number:", 40, 225, 150, 20);
        JLabel flightNumberValue = createNormalLabel(ticket.getFlightNumber(), 190, 225, 150, 20);
        
        // From city
        JLabel fromLabel = createBoldLabel("From:", 340, 225, 150, 20);
        JLabel fromValue = createNormalLabel(ticket.getFromCity(), 490, 225, 150, 20);
        
        // To city
        JLabel toLabel = createBoldLabel("To:", 40, 255, 150, 20);
        JLabel toValue = createNormalLabel(ticket.getToCity(), 190, 255, 150, 20);
        
        // Date
        JLabel dateLabel = createBoldLabel("Date:", 340, 255, 150, 20);
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        String departureDate = dateFormat.format(ticket.getDepartureDate());
        JLabel dateValue = createNormalLabel(departureDate, 490, 255, 150, 20);
        
        // Time
        JLabel timeLabel = createBoldLabel("Time:", 40, 285, 150, 20);
        JLabel timeValue = createNormalLabel(ticket.getDepartureTime(), 190, 285, 150, 20);
        
        // Class
        JLabel classLabel = createBoldLabel("Class:", 340, 285, 150, 20);
        JLabel classValue = createNormalLabel(ticket.getTravelClass(), 490, 285, 150, 20);
        
        // Add a separator
        JSeparator separator = new JSeparator();
        separator.setBounds(40, 315, 680, 1);
        separator.setForeground(AppStyle.TEAL);
        
        // Add all components to the panel
        panel.add(flightLabel);
        panel.add(flightNumberLabel);
        panel.add(flightNumberValue);
        panel.add(fromLabel);
        panel.add(fromValue);
        panel.add(toLabel);
        panel.add(toValue);
        panel.add(dateLabel);
        panel.add(dateValue);
        panel.add(timeLabel);
        panel.add(timeValue);
        panel.add(classLabel);
        panel.add(classValue);
        panel.add(separator);
    }
    
    /**
     * Adds the passenger details section to the ticket
     */
    private void addPassengerDetailsToTicket(JPanel panel) {
        // Add the passenger information section header
        JLabel passengerLabel = createHeaderLabel("Passenger Information", 40, 330, 250, 25);
        
        // Passenger name
        JLabel nameLabel = createBoldLabel("Passenger Name:", 40, 365, 150, 20);
        JLabel nameValue = createNormalLabel(reservation.getPassengerName(), 190, 365, 250, 20);
        
        // Number of adults
        JLabel adultLabel = createBoldLabel("Adults:", 40, 395, 150, 20);
        JLabel adultValue = createNormalLabel(String.valueOf(reservation.getAdults()), 190, 395, 150, 20);
        
        // Number of children
        JLabel childrenLabel = createBoldLabel("Children:", 340, 395, 150, 20);
        JLabel childrenValue = createNormalLabel(String.valueOf(reservation.getChildren()), 490, 395, 150, 20);
        
        // Number of infants
        JLabel infantLabel = createBoldLabel("Infants:", 40, 425, 150, 20);
        JLabel infantValue = createNormalLabel(String.valueOf(reservation.getInfants()), 190, 425, 150, 20);
        
        // Status
        JLabel statusLabel = createBoldLabel("Status:", 340, 425, 150, 20);
        JLabel statusValue = new JLabel(reservation.getStatus().toString());
        statusValue.setFont(AppStyle.FONT_BOLD);
        if (reservation.getStatus() == Reservation.Status.CONFIRMED) {
            statusValue.setForeground(AppStyle.SUCCESS);
        } else if (reservation.getStatus() == Reservation.Status.CANCELLED) {
            statusValue.setForeground(AppStyle.ERROR);
        } else {
            statusValue.setForeground(AppStyle.TEXT);
        }
        statusValue.setBounds(490, 425, 150, 20);
        
        // Add a separator
        JSeparator separator = new JSeparator();
        separator.setBounds(40, 455, 680, 1);
        separator.setForeground(AppStyle.TEAL);
        
        // Add all components to the panel
        panel.add(passengerLabel);
        panel.add(nameLabel);
        panel.add(nameValue);
        panel.add(adultLabel);
        panel.add(adultValue);
        panel.add(childrenLabel);
        panel.add(childrenValue);
        panel.add(infantLabel);
        panel.add(infantValue);
        panel.add(statusLabel);
        panel.add(statusValue);
        panel.add(separator);
    }
    
    /**
     * Adds the price details section to the ticket
     */
    private void addPriceDetailsToTicket(JPanel panel) {
        // Add the price information section header
        JLabel priceLabel = createHeaderLabel("Price Details", 40, 470, 200, 25);
        
        // Price per person
        JLabel perPersonLabel = createBoldLabel("Price Per Person:", 40, 505, 150, 20);
        JLabel perPersonValue = createNormalLabel(ticket.getPrice() + " TND", 190, 505, 150, 20);
        
        // Calculate total price
        int adultPrice = ticket.getPrice();
        int childPrice = (int)(ticket.getPrice() * 0.5);
        int totalPrice = (adultPrice * reservation.getAdults()) + 
                          (childPrice * reservation.getChildren());
        
        // Total price
        JLabel totalLabel = createBoldLabel("Total Price:", 340, 505, 150, 20);
        JLabel totalValue = new JLabel(totalPrice + " TND");
        totalValue.setFont(AppStyle.FONT_BOLD);
        totalValue.setForeground(AppStyle.ORANGE);
        totalValue.setBounds(490, 505, 150, 20);
        
        // Add all components to the panel
        panel.add(priceLabel);
        panel.add(perPersonLabel);
        panel.add(perPersonValue);
        panel.add(totalLabel);
        panel.add(totalValue);
    }
    
    /**
     * Creates a bold label for field names
     */
    private JLabel createBoldLabel(String text, int x, int y, int width, int height) {
        JLabel label = AppStyle.createBoldLabel(text);
        label.setBounds(x, y, width, height);
        return label;
    }
    
    /**
     * Creates a normal (non-bold) label for field values
     */
    private JLabel createNormalLabel(String text, int x, int y, int width, int height) {
        JLabel label = AppStyle.createLabel(text);
        label.setBounds(x, y, width, height);
        return label;
    }
    
    /**
     * Creates a header label for section titles
     */
    private JLabel createHeaderLabel(String text, int x, int y, int width, int height) {
        JLabel label = new JLabel(text);
        label.setFont(AppStyle.FONT_SUBTITLE);
        label.setForeground(AppStyle.DARK_NAVY);
        label.setBounds(x, y, width, height);
        return label;
    }
    
    /**
     * Creates the button panel with a close button
     */
    private JPanel createButtonPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        panel.setBackground(AppStyle.BACKGROUND);
        
        // Create close button
        closeButton = new JButton("Close");
        AppStyle.styleSecondaryButton(closeButton);
        closeButton.addActionListener(e -> dispose());
        
        // Add the button to the panel
        panel.add(closeButton);
        
        return panel;
    }
    
    /**
     * Main method for standalone testing
     */
    public static void main(String[] args) {
        // Set look and feel
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        // Create a sample ticket for testing
        SwingUtilities.invokeLater(() -> {
            new PrintTicket1(
                "Tunis",
                "Paris",
                "Business",
                "John Doe",
                2,
                1,
                0,
                "15/05/2023",
                1200,
                "10:30 AM"
            );
        });
    }
} 