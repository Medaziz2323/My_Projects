import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import javax.swing.border.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * This class handles domestic flight bookings from Tunisia to European destinations.
 * It provides a form for users to book flights.
 */
public class DomesticFlight extends JFrame {
    // Form components - these will store user inputs
    private JComboBox<String> fromBox, toBox, classBox;
    private JComboBox<String> adultBox, childrenBox, infantBox;
    private JTextField dateField, passengerNameField;
    private JButton reserveButton, clearButton, backButton;
    
    // The main page that opened this window
    private LoginPage loginPage;
    
    // The logged in user
    private User user;
    
    // Lists of options for dropdown menus
    private String[] departureCities = {
        "Tunis", "Monastir", "Sfax", "Djerba", "Tozeur"
    };
    
    private String[] destinationCities = {
        "Paris", "Rome", "Barcelona", "London", "Brussels", 
        "Berlin", "Madrid", "Amsterdam", "Athens", "Vienna"
    };
    
    private String[] classOptions = {
        "Economic", "Business", "First Class"
    };
    
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
     * Constructor - creates the flight booking window
     */
    public DomesticFlight(LoginPage loginPage, User user) {
        // Set the window title
        super("Book Flight");
        
        // Save references
        this.loginPage = loginPage;
        this.user = user;
        
        // Admins should not be able to book flights, redirect to admin dashboard
        if (user != null && user.isAdmin()) {
            JOptionPane.showMessageDialog(this,
                "Admins cannot book tickets. Please use admin dashboard for ticket management.",
                "Admin Access",
                JOptionPane.INFORMATION_MESSAGE);
            dispose();
            new AdminDashboard(loginPage, user);
            return;
        }
        
        // Set up all the components and layout
        setupWindow();
        
        // Display in fullscreen and set default close operation
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        AppStyle.setFullScreen(this);
        setVisible(true);
    }
    
    /**
     * Constructor for backward compatibility
     */
    public DomesticFlight(LoginPage loginPage) {
        this(loginPage, null);
    }
    
    /**
     * Sets up the entire window and all components
     */
    private void setupWindow() {
        // Create the main panel with a border layout
        JPanel mainPanel = new JPanel(new BorderLayout(20, 20));
        mainPanel.setBackground(AppStyle.BACKGROUND);
        mainPanel.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));
        
        // Create a panel to hold the two form sections side by side
        JPanel formPanel = new JPanel(new GridLayout(1, 2, 20, 0));
        formPanel.setBackground(AppStyle.BACKGROUND);
        
        // Add the booking and passenger panels to the form panel
        formPanel.add(createBookingPanel());
        formPanel.add(createPassengerPanel());
        
        // Add the panels to the main panel
        mainPanel.add(formPanel, BorderLayout.CENTER);
        mainPanel.add(createButtonPanel(), BorderLayout.SOUTH);
        
        // Set the content of the window to the main panel
        setContentPane(mainPanel);
    }
    
    /**
     * Creates the panel for booking details (left side)
     */
    private JPanel createBookingPanel() {
        // Create a panel with border layout
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(AppStyle.WHITE);
        panel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(230, 230, 230), 1),
            BorderFactory.createEmptyBorder(25, 25, 25, 25)
        ));
        
        // Create a panel for the form fields using GridBagLayout for precise positioning
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(AppStyle.WHITE);
        
        // Set up positioning constraints
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        // Add heading
        JLabel headingLabel = AppStyle.createTitleLabel("Flight Details");
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(0, 0, 20, 0);
        formPanel.add(headingLabel, gbc);
        
        // Reset gridwidth to 1 for other components
        gbc.gridwidth = 1;
        
        // From field
        gbc.gridy++;
        gbc.gridx = 0;
        JLabel fromLabel = AppStyle.createLabel("From");
        formPanel.add(fromLabel, gbc);
        
        fromBox = new JComboBox<>(departureCities);
        AppStyle.styleComboBox(fromBox);
        gbc.gridx = 1;
        formPanel.add(fromBox, gbc);
        
        // To field
        gbc.gridx = 0;
        gbc.gridy++;
        JLabel toLabel = AppStyle.createLabel("To");
        formPanel.add(toLabel, gbc);
        
        toBox = new JComboBox<>(destinationCities);
        AppStyle.styleComboBox(toBox);
        gbc.gridx = 1;
        formPanel.add(toBox, gbc);
        
        // Date field
        gbc.gridx = 0;
        gbc.gridy++;
        JLabel dateLabel = AppStyle.createLabel("Date");
        formPanel.add(dateLabel, gbc);
        
        JPanel datePanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        datePanel.setBackground(AppStyle.WHITE);
        
        dateField = new JTextField(10);
        AppStyle.styleTextField(dateField);
        datePanel.add(dateField);
        
        JLabel formatLabel = new JLabel("(DD/MM/YYYY)");
        formatLabel.setFont(AppStyle.FONT_REGULAR);
        formatLabel.setForeground(new Color(150, 150, 150));
        datePanel.add(formatLabel);
        
        gbc.gridx = 1;
        formPanel.add(datePanel, gbc);
        
        // Class selection
        gbc.gridx = 0;
        gbc.gridy++;
        JLabel classLabel = AppStyle.createLabel("Class");
        formPanel.add(classLabel, gbc);
        
        classBox = new JComboBox<>(classOptions);
        AppStyle.styleComboBox(classBox);
        gbc.gridx = 1;
        formPanel.add(classBox, gbc);
        
        // Reserve button - to book the flight
        reserveButton = new JButton("Reserve Flight");
        AppStyle.stylePrimaryButton(reserveButton);
        reserveButton.addActionListener(e -> reserveFlight());
        
        gbc.gridx = 0;
        gbc.gridy++;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.insets = new Insets(15, 10, 5, 10);
        formPanel.add(reserveButton, gbc);
        
        // Try to add a map image
        try {
            ImageIcon mapIcon = new ImageIcon(getClass().getResource("/img/tunisia_europe_map.jpg"));
            if (mapIcon.getIconWidth() <= 0) {
                // Try another path if not found
                mapIcon = new ImageIcon("tunisia_europe_map.jpg");
            }
            if (mapIcon.getIconWidth() > 0) {
                // If we found the image, display it
                JLabel mapLabel = new JLabel(mapIcon);
                mapLabel.setPreferredSize(new Dimension(300, 200));
                panel.add(mapLabel, BorderLayout.SOUTH);
            }
        } catch (Exception e) {
            // Don't worry if image is not found
        }
        
        // Add the form panel to the main panel
        panel.add(formPanel, BorderLayout.NORTH);
        return panel;
    }
    
    /**
     * Creates the panel for passenger details (right side)
     */
    private JPanel createPassengerPanel() {
        // Create a panel with border layout
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(AppStyle.WHITE);
        panel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(230, 230, 230), 1),
            BorderFactory.createEmptyBorder(25, 25, 25, 25)
        ));
        
        // Create a panel for the form fields
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(AppStyle.WHITE);
        
        // Set up positioning constraints
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        // Add heading
        JLabel headingLabel = AppStyle.createTitleLabel("Passenger Details");
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(0, 0, 20, 0);
        formPanel.add(headingLabel, gbc);
        
        // Reset gridwidth to 1 for other components
        gbc.gridwidth = 1;
        
        // Passenger name field
        gbc.gridy++;
        gbc.gridx = 0;
        JLabel nameLabel = AppStyle.createLabel("Passenger Name");
        formPanel.add(nameLabel, gbc);
        
        passengerNameField = new JTextField(15);
        AppStyle.styleTextField(passengerNameField);
        gbc.gridx = 1;
        formPanel.add(passengerNameField, gbc);
        
        // Number of adults
        gbc.gridy++;
        gbc.gridx = 0;
        JLabel adultLabel = AppStyle.createLabel("Adults (12+)");
        formPanel.add(adultLabel, gbc);
        
        adultBox = new JComboBox<>(adultOptions);
        AppStyle.styleComboBox(adultBox);
        gbc.gridx = 1;
        formPanel.add(adultBox, gbc);
        
        // Number of children
        gbc.gridx = 0;
        gbc.gridy++;
        JLabel childLabel = AppStyle.createLabel("Children (2-11)");
        formPanel.add(childLabel, gbc);
        
        childrenBox = new JComboBox<>(childOptions);
        AppStyle.styleComboBox(childrenBox);
        gbc.gridx = 1;
        formPanel.add(childrenBox, gbc);
        
        // Number of infants
        gbc.gridx = 0;
        gbc.gridy++;
        JLabel infantLabel = AppStyle.createLabel("Infants (under 2)");
        formPanel.add(infantLabel, gbc);
        
        infantBox = new JComboBox<>(infantOptions);
        AppStyle.styleComboBox(infantBox);
        gbc.gridx = 1;
        formPanel.add(infantBox, gbc);
        
        // Add the form panel to the main panel
        panel.add(formPanel, BorderLayout.NORTH);
        
        // Add a notes section with travel information
        panel.add(createNotesPanel(), BorderLayout.CENTER);
        
        return panel;
    }
    
    /**
     * Creates a panel with travel notes and information
     */
    private JPanel createNotesPanel() {
        JPanel notesPanel = new JPanel(new BorderLayout());
        notesPanel.setBackground(AppStyle.WHITE);
        
        // Create a label with HTML formatting for multiple lines
        JLabel notesLabel = new JLabel(
            "<html><body><p><b>NOTE:</b> Tunisian nationals need a Schengen visa<br>" + 
            "for most European destinations. Please ensure<br>" + 
            "you have valid travel documents before booking.<br>" + 
            "All prices are shown in Tunisian Dinar (TND).</p></body></html>"
        );
        notesLabel.setForeground(new Color(128, 0, 0));
        notesLabel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        notesPanel.add(notesLabel, BorderLayout.CENTER);
        return notesPanel;
    }
    
    /**
     * Creates the button panel at the bottom
     */
    private JPanel createButtonPanel() {
        // Create a panel with centered components and some spacing
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        panel.setBackground(AppStyle.BACKGROUND);
        
        // Clear button - to reset the form
        clearButton = new JButton("Clear");
        AppStyle.styleSecondaryButton(clearButton);
        clearButton.addActionListener(e -> clearForm());
        
        // Back button - to return to the main menu
        backButton = new JButton("Back to Main Menu");
        AppStyle.styleSecondaryButton(backButton);
        backButton.addActionListener(e -> {
            // Go back to the main menu (not the login page)
            if (user != null) {
                // Create a new main menu with the current user
                new MainMenuPage(loginPage, user);
                dispose(); // Close this window
            } else {
                // If there's no user, go back to login
                if (loginPage != null) {
                    loginPage.setVisible(true);
                } else {
                    new LoginPage();
                }
                dispose();
            }
        });
        
        // Logout button - to logout and go back to login screen
        JButton logoutButton = new JButton("Logout");
        AppStyle.styleSecondaryButton(logoutButton);
        logoutButton.addActionListener(e -> logout());
        
        // Add buttons to the panel
        panel.add(clearButton);
        panel.add(backButton);
        panel.add(logoutButton);
        
        return panel;
    }
    
    /**
     * Logs out and returns to login screen
     */
    private void logout() {
        // Create a new login page instead of reusing the old one
        LoginPage newLoginPage = new LoginPage();
        newLoginPage.setVisible(true);
        
        // Close this window
        dispose();
    }
    
    /**
     * Clears all form fields
     */
    private void clearForm() {
        // Reset all selections to default (first item)
        fromBox.setSelectedIndex(0);
        toBox.setSelectedIndex(0);
        classBox.setSelectedIndex(0);
        dateField.setText("");
        passengerNameField.setText("");
        adultBox.setSelectedIndex(0);
        childrenBox.setSelectedIndex(0);
        infantBox.setSelectedIndex(0);
    }
    
    /**
     * Handles the flight reservation process
     */
    private void reserveFlight() {
        // First ensure user is logged in
        if (user == null) {
            JOptionPane.showMessageDialog(this, 
                "Error: You must be logged in to book a flight.",
                "Login Required",
                JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        // Check if all inputs are valid
        if (!checkInputs()) {
            return;
        }
        
        // Get values from the form
        String from = (String) fromBox.getSelectedItem();
        String to = (String) toBox.getSelectedItem();
        String travelClass = (String) classBox.getSelectedItem();
        String bookingDate = dateField.getText().trim();
        
        // Get passenger counts
        int adults = Integer.parseInt((String) adultBox.getSelectedItem());
        int children = Integer.parseInt((String) childrenBox.getSelectedItem());
        int infants = Integer.parseInt((String) infantBox.getSelectedItem());
        int totalPassengers = adults + children + infants;
        
        // Calculate flight price and get flight time
        FlightInfo flightInfo = calculateFlightInfo(from, to, travelClass, adults, children, infants);
        
        // Check if seats are available
        if (!checkSeatsAvailable(totalPassengers)) {
            JOptionPane.showMessageDialog(this, 
                "Sorry! This flight is fully booked on " + bookingDate,
                "No Seats Available",
                JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        // Show confirmation dialog and get user confirmation
        boolean confirmed = showConfirmationDialog(from, to, travelClass, 
                adults, children, infants, bookingDate, 
                flightInfo.price, flightInfo.time);
                
        // If user confirms, complete the booking
        if (confirmed) {
            completeBooking(from, to, travelClass, adults, children, infants, 
                    bookingDate, flightInfo.price, flightInfo.time);
        }
    }
    
    /**
     * Checks if all input fields are valid
     */
    private boolean checkInputs() {
        // Check date format
        String date = dateField.getText().trim();
        if (date.length() <= 0) {
            JOptionPane.showMessageDialog(this,
                "Please enter a booking date.",
                "Input Error",
                JOptionPane.ERROR_MESSAGE);
            return false;
        }
        
        // Check date format (simple check for XX/XX/XXXX)
        if (!date.matches("\\d{2}/\\d{2}/\\d{4}")) {
            JOptionPane.showMessageDialog(this,
                "Please enter a valid date in format DD/MM/YYYY.",
                "Input Error",
                JOptionPane.ERROR_MESSAGE);
            return false;
        }
        
        // Check passenger name
        String passengerName = passengerNameField.getText().trim();
        if (passengerName.length() <= 0) {
            JOptionPane.showMessageDialog(this,
                "Please enter passenger name.",
                "Input Error",
                JOptionPane.ERROR_MESSAGE);
            return false;
        }
        
        return true;
    }
    
    /**
     * Calculates the flight price and generates flight time
     */
    private FlightInfo calculateFlightInfo(String from, String to, String travelClass, int adults, int children, int infants) {
        // Base price depends on destination
        int basePrice = getBasePrice(to);
        
        // Small price adjustment based on departure city
        basePrice += getDeparturePriceAdjustment(from);
        
        // Apply multiplier based on class
        basePrice = applyClassPriceMultiplier(basePrice, travelClass);
        
        // Calculate per-passenger prices
        int adultPrice = basePrice;
        int childPrice = (int)(basePrice * 0.7); // Children pay 70% of adult price
        int infantPrice = (int)(basePrice * 0.2); // Infants pay 20% of adult price
        
        // Calculate total price for all passengers
        int totalPrice = (adultPrice * adults) + (childPrice * children) + (infantPrice * infants);
        
        // Generate a flight time (just for simulation)
        String time = generateRandomFlightTime();
        
        return new FlightInfo(totalPrice, time);
    }
    
    /**
     * Gets the base price for a destination
     */
    private int getBasePrice(String destination) {
        // Different prices for different destinations
        switch (destination) {
            case "Paris": return 780;
            case "Rome": return 650;
            case "Barcelona": return 720;
            case "London": return 900;
            case "Brussels": return 820;
            case "Berlin": return 850;
            case "Madrid": return 750;
            case "Amsterdam": return 830;
            case "Athens": return 600;
            case "Vienna": return 800;
            default: return 700; // Default price
        }
    }
    
    /**
     * Gets price adjustment based on departure city
     */
    private int getDeparturePriceAdjustment(String departureCity) {
        // Different prices for different departure cities
        switch (departureCity) {
            case "Monastir": return 20;
            case "Sfax": return 40;
            case "Djerba": return 60;
            case "Tozeur": return 80;
            default: return 0; // No adjustment for Tunis
        }
    }
    
    /**
     * Applies a multiplier based on travel class
     */
    private int applyClassPriceMultiplier(int price, String travelClass) {
        // Different multipliers for different classes
        switch (travelClass) {
            case "Business": return price * 2;      // Business is 2x economy
            case "First Class": return price * 3;   // First Class is 3x economy
            default: return price;                  // Economy (no multiplier)
        }
    }
    
    /**
     * Generates a random flight time for simulation
     */
    private String generateRandomFlightTime() {
        // Generate a random hour (8 AM to 7 PM)
        int hour = 8 + (int)(Math.random() * 12);
        
        // Generate a random minute (0, 15, 30, or 45)
        int minute = ((int)(Math.random() * 4)) * 15;
        
        // Format as HH:MM
        return String.format("%02d:%02d", hour, minute);
    }
    
    /**
     * Checks if enough seats are available
     */
    private boolean checkSeatsAvailable(int requestedSeats) {
        // This is just a simulation
        // In a real app, we would check a database
        
        // Randomly determine how many seats are already booked
        int bookedSeats = (int)(Math.random() * 40); // 0-39 seats already booked
        
        // Each flight has 60 seats total
        int availableSeats = 60 - bookedSeats;
        
        // Check if we have enough seats
        return requestedSeats <= availableSeats;
    }
    
    /**
     * Shows a confirmation dialog with all booking details
     */
    private boolean showConfirmationDialog(String from, String to, String travelClass, 
            int adults, int children, int infants, String bookingDate, 
            int totalPrice, String time) {
        // Create a nice looking confirmation message
        StringBuilder message = new StringBuilder();
        message.append("<html><body style='width: 300px'>");
        message.append("<h2>Confirm your booking</h2>");
        message.append("<table>");
        message.append("<tr><td><b>From:</b></td><td>").append(from).append("</td></tr>");
        message.append("<tr><td><b>To:</b></td><td>").append(to).append("</td></tr>");
        message.append("<tr><td><b>Class:</b></td><td>").append(travelClass).append("</td></tr>");
        message.append("<tr><td><b>Passenger:</b></td><td>").append(passengerNameField.getText().trim()).append("</td></tr>");
        message.append("<tr><td><b>Adults:</b></td><td>").append(adults).append("</td></tr>");
        
        if (children > 0) {
            message.append("<tr><td><b>Children:</b></td><td>").append(children).append("</td></tr>");
        }
        
        if (infants > 0) {
            message.append("<tr><td><b>Infants:</b></td><td>").append(infants).append("</td></tr>");
        }
        
        message.append("<tr><td><b>Date:</b></td><td>").append(bookingDate).append("</td></tr>");
        message.append("<tr><td><b>Time:</b></td><td>").append(time).append("</td></tr>");
        message.append("<tr><td><b>Total Price:</b></td><td>").append(totalPrice).append(" TND</td></tr>");
        message.append("</table>");
        message.append("</body></html>");
        
        // Show the confirmation dialog
        int choice = JOptionPane.showConfirmDialog(this,
            message.toString(),
            "Confirm Booking",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.QUESTION_MESSAGE);
        
        // Return true if user clicked Yes, false otherwise
        return choice == JOptionPane.YES_OPTION;
    }
    
    /**
     * Completes the booking process
     */
    private void completeBooking(String from, String to, String travelClass, 
            int adults, int children, int infants, 
            String bookingDate, int price, String time) {
        
        // Get passenger name
        String passengerName = passengerNameField.getText().trim();
        
        try {
            // Parse the booking date
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
            Date parsedDate = dateFormat.parse(bookingDate);
            
            // Find an existing available ticket instead of creating a new one
            List<Ticket> availableTickets = TicketDAO.getAvailableTickets();
            Ticket matchingTicket = null;
            
            // Find a matching ticket from available tickets
            for (Ticket ticket : availableTickets) {
                if (ticket.getFromCity().equals(from) && 
                    ticket.getToCity().equals(to) && 
                    ticket.getTravelClass().equals(travelClass) && 
                    dateFormat.format(ticket.getDepartureDate()).equals(bookingDate)) {
                    
                    // Check if this ticket has any existing reservations
                    List<Reservation> existingReservations = ReservationDAO.getReservationsByTicketId(ticket.getTicketId());
                    int totalReservedSeats = 0;
                    
                    for (Reservation res : existingReservations) {
                        totalReservedSeats += res.getTotalPassengers();
                    }
                    
                    // We'll assume each flight has 60 seats
                    int totalRequestedSeats = adults + children + infants;
                    if (totalReservedSeats + totalRequestedSeats <= 60) {
                        matchingTicket = ticket;
                        break;
                    }
                }
            }
            
            // If no matching ticket is found, inform the user to contact admin
            if (matchingTicket == null) {
                JOptionPane.showMessageDialog(this,
                    "Sorry, there are no available tickets matching your criteria.\n" +
                    "Please contact an administrator to add new flight tickets.",
                    "No Available Tickets",
                    JOptionPane.WARNING_MESSAGE);
                return;
            }
            
            // Generate confirmation code
            String confirmationCode = "TN" + (10000 + (int)(Math.random() * 90000));
            
            // Create and save the reservation to database using the existing ticket
            Reservation reservation = new Reservation(
                user.getUserId(),
                matchingTicket.getTicketId(),
                passengerName,
                adults,
                children,
                infants,
                parsedDate,
                confirmationCode
            );
            
            boolean reservationSaved = ReservationDAO.insertReservation(reservation);
            
            if (!reservationSaved) {
                throw new Exception("Failed to save reservation to database");
            }
            
            // Show success message
            JOptionPane.showMessageDialog(this,
                "Your booking has been confirmed and saved!\n" +
                "A ticket will now be generated.",
                "Booking Successful",
                JOptionPane.INFORMATION_MESSAGE);
                
            // Generate and show the ticket
            new PrintTicket1(matchingTicket, reservation, user);
            
            // Clear form for next booking
            clearForm();
            
        } catch (Exception ex) {
            // Show error message if saving fails
            JOptionPane.showMessageDialog(this,
                "Error saving booking: " + ex.getMessage(),
                "Save Error",
                JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }
    
    /**
     * Simple class to hold flight information
     */
    private static class FlightInfo {
        int price;
        String time;
        
        FlightInfo(int price, String time) {
            this.price = price;
            this.time = time;
        }
    }
    
    /**
     * Main method for testing this class separately
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new DomesticFlight(null, null));
    }
} 