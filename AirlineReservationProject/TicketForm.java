import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.border.*;
import java.util.Date;
import java.text.SimpleDateFormat;
import java.text.ParseException;
import java.util.Calendar;
import com.toedter.calendar.JDateChooser; // Import JDateChooser from JCalendar library

/**
 * TicketForm class for adding and editing tickets.
 * This form is used in the admin dashboard.
 */
public class TicketForm extends JDialog {
    // Colors and fonts
    private Color mainColor = new Color(40, 40, 40);
    private Color accentColor = new Color(0, 80, 180);
    private Color backgroundColor = new Color(245, 245, 245);
    private Color textColor = new Color(33, 33, 33);
    private Font mainFont = new Font("Arial", Font.PLAIN, 14);
    private Font boldFont = new Font("Arial", Font.BOLD, 14);
    private Font titleFont = new Font("Arial", Font.BOLD, 22);
    
    // Form fields
    private JTextField ticketNumberField;
    private JTextField flightIdField;
    private JTextField flightNumberField;
    private JComboBox<String> fromCityComboBox;
    private JComboBox<String> toCityComboBox;
    private JDateChooser departureDateChooser;
    private TimeSpinner departureTimeSpinner;
    private JComboBox<String> travelClassComboBox;
    private JTextField priceField;
    private JCheckBox isActiveCheckBox;
    
    // Buttons
    private JButton saveButton;
    private JButton cancelButton;
    
    // Ticket being edited (null if adding a new ticket)
    private Ticket ticket;
    
    // Parent admin dashboard
    private AdminDashboard dashboard;
    
    // Lists of cities
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
    
    /**
     * Constructor - creates a form for adding or editing a ticket
     */
    public TicketForm(AdminDashboard dashboard, Ticket ticket) {
        // Set dialog properties
        super(dashboard, ticket == null ? "Add Ticket" : "Edit Ticket", true);
        
        // Save references
        this.dashboard = dashboard;
        this.ticket = ticket;
        
        // Create the form
        setupForm();
        
        // If editing, populate fields with ticket data
        if (ticket != null) {
            populateFields();
        }
        
        // Display the dialog
        setSize(650, 650);
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
        JLabel titleLabel = AppStyle.createTitleLabel(ticket == null ? "Add New Ticket" : "Edit Ticket");
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(0, 0, 20, 0);
        panel.add(titleLabel, gbc);
        
        // Reset insets and gridwidth
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.gridwidth = 1;
        
        // Ticket number field
        gbc.gridx = 0;
        gbc.gridy = 1;
        JLabel ticketNumberLabel = AppStyle.createLabel("Ticket Number:");
        panel.add(ticketNumberLabel, gbc);
        
        gbc.gridx = 1;
        ticketNumberField = new JTextField(20);
        AppStyle.styleTextField(ticketNumberField);
        panel.add(ticketNumberField, gbc);
        
        // Flight ID field
        gbc.gridx = 0;
        gbc.gridy = 2;
        JLabel flightIdLabel = AppStyle.createLabel("Flight ID:");
        panel.add(flightIdLabel, gbc);
        
        gbc.gridx = 1;
        flightIdField = new JTextField(20);
        AppStyle.styleTextField(flightIdField);
        panel.add(flightIdField, gbc);
        
        // Flight number field
        gbc.gridx = 0;
        gbc.gridy = 3;
        JLabel flightNumberLabel = AppStyle.createLabel("Flight Number:");
        panel.add(flightNumberLabel, gbc);
        
        gbc.gridx = 1;
        flightNumberField = new JTextField(20);
        AppStyle.styleTextField(flightNumberField);
        panel.add(flightNumberField, gbc);
        
        // From city dropdown
        gbc.gridx = 0;
        gbc.gridy = 4;
        JLabel fromCityLabel = AppStyle.createLabel("From City:");
        panel.add(fromCityLabel, gbc);
        
        gbc.gridx = 1;
        fromCityComboBox = new JComboBox<>(departureCities);
        AppStyle.styleComboBox(fromCityComboBox);
        panel.add(fromCityComboBox, gbc);
        
        // To city dropdown
        gbc.gridx = 0;
        gbc.gridy = 5;
        JLabel toCityLabel = AppStyle.createLabel("To City:");
        panel.add(toCityLabel, gbc);
        
        gbc.gridx = 1;
        toCityComboBox = new JComboBox<>(destinationCities);
        AppStyle.styleComboBox(toCityComboBox);
        panel.add(toCityComboBox, gbc);
        
        // Departure date field
        gbc.gridx = 0;
        gbc.gridy = 6;
        JLabel departureDateLabel = AppStyle.createLabel("Departure Date:");
        panel.add(departureDateLabel, gbc);
        
        gbc.gridx = 1;
        departureDateChooser = new JDateChooser();
        departureDateChooser.setDate(new Date());
        departureDateChooser.setDateFormatString("dd/MM/yyyy");
        panel.add(departureDateChooser, gbc);
        
        // Departure time field
        gbc.gridx = 0;
        gbc.gridy = 7;
        JLabel departureTimeLabel = AppStyle.createLabel("Departure Time:");
        panel.add(departureTimeLabel, gbc);
        
        gbc.gridx = 1;
        departureTimeSpinner = new TimeSpinner();
        panel.add(departureTimeSpinner, gbc);
        
        // Travel class dropdown
        gbc.gridx = 0;
        gbc.gridy = 8;
        JLabel travelClassLabel = AppStyle.createLabel("Travel Class:");
        panel.add(travelClassLabel, gbc);
        
        gbc.gridx = 1;
        travelClassComboBox = new JComboBox<>(classOptions);
        AppStyle.styleComboBox(travelClassComboBox);
        panel.add(travelClassComboBox, gbc);
        
        // Price field
        gbc.gridx = 0;
        gbc.gridy = 9;
        JLabel priceLabel = AppStyle.createLabel("Price:");
        panel.add(priceLabel, gbc);
        
        gbc.gridx = 1;
        priceField = new JTextField(20);
        AppStyle.styleTextField(priceField);
        panel.add(priceField, gbc);
        
        // Is active checkbox
        gbc.gridx = 0;
        gbc.gridy = 10;
        JLabel isActiveLabel = AppStyle.createLabel("Is Active:");
        panel.add(isActiveLabel, gbc);
        
        gbc.gridx = 1;
        isActiveCheckBox = new JCheckBox("Available for booking");
        isActiveCheckBox.setFont(mainFont);
        isActiveCheckBox.setBackground(Color.WHITE);
        isActiveCheckBox.setSelected(true);
        panel.add(isActiveCheckBox, gbc);
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
        saveButton = new JButton(ticket == null ? "Add Ticket" : "Save Changes");
        AppStyle.stylePrimaryButton(saveButton);
        saveButton.addActionListener(e -> saveTicket());
        
        // Add buttons to panel
        panel.add(cancelButton);
        panel.add(Box.createHorizontalStrut(10));
        panel.add(saveButton);
    }
    
    /**
     * Populate form fields with ticket data
     */
    private void populateFields() {
        ticketNumberField.setText(ticket.getTicketNumber());
        flightIdField.setText(String.valueOf(ticket.getFlightId()));
        flightNumberField.setText(ticket.getFlightNumber());
        
        // Find and select the from city
        for (int i = 0; i < departureCities.length; i++) {
            if (departureCities[i].equals(ticket.getFromCity())) {
                fromCityComboBox.setSelectedIndex(i);
                break;
            }
        }
        
        // Find and select the to city
        for (int i = 0; i < destinationCities.length; i++) {
            if (destinationCities[i].equals(ticket.getToCity())) {
                toCityComboBox.setSelectedIndex(i);
                break;
            }
        }
        
        // Format and set the departure date
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        departureDateChooser.setDate(ticket.getDepartureDate());
        
        departureTimeSpinner.setTimeFromString(ticket.getDepartureTime());
        
        // Find and select the travel class
        for (int i = 0; i < classOptions.length; i++) {
            if (classOptions[i].equals(ticket.getTravelClass())) {
                travelClassComboBox.setSelectedIndex(i);
                break;
            }
        }
        
        priceField.setText(String.valueOf(ticket.getPrice()));
        isActiveCheckBox.setSelected(ticket.isActive());
    }
    
    /**
     * Save the ticket data
     */
    private void saveTicket() {
        // Validate input
        if (!validateInput()) {
            return;
        }
        
        try {
            // Get form values
            String ticketNumber = ticketNumberField.getText().trim();
            int flightId = Integer.parseInt(flightIdField.getText().trim());
            String flightNumber = flightNumberField.getText().trim();
            String fromCity = (String) fromCityComboBox.getSelectedItem();
            String toCity = (String) toCityComboBox.getSelectedItem();
            
            // Get departure date directly from the JDateChooser
            Date departureDate = departureDateChooser.getDate();
            
            // Get departure time from the TimeSpinner
            String departureTime = departureTimeSpinner.getTimeAsString();
            String travelClass = (String) travelClassComboBox.getSelectedItem();
            int price = Integer.parseInt(priceField.getText().trim());
            boolean isActive = isActiveCheckBox.isSelected();
            
            boolean success;
            
            if (ticket == null) {
                // Create a new ticket
                ticket = new Ticket(ticketNumber, flightId, flightNumber, fromCity, toCity,
                           departureDate, departureTime, travelClass, price);
                ticket.setActive(isActive);
                
                success = TicketDAO.insertTicket(ticket);
            } else {
                // Update existing ticket
                ticket.setTicketNumber(ticketNumber);
                ticket.setFlightId(flightId);
                ticket.setFlightNumber(flightNumber);
                ticket.setFromCity(fromCity);
                ticket.setToCity(toCity);
                ticket.setDepartureDate(departureDate);
                ticket.setDepartureTime(departureTime);
                ticket.setTravelClass(travelClass);
                ticket.setPrice(price);
                ticket.setActive(isActive);
                
                success = TicketDAO.updateTicket(ticket);
            }
            
            if (success) {
                JOptionPane.showMessageDialog(this,
                    ticket.getTicketId() == -1 ? "Ticket added successfully." : "Ticket updated successfully.",
                    "Success",
                    JOptionPane.INFORMATION_MESSAGE);
                
                // Close the dialog
                dispose();
                
                // Refresh the tickets list in the dashboard
                try {
                    dashboard.getClass().getMethod("refreshTickets").invoke(dashboard);
                } catch (Exception ex) {
                    System.err.println("Could not refresh tickets: " + ex.getMessage());
                }
            } else {
                JOptionPane.showMessageDialog(this,
                    "Failed to save ticket. Please try again.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this,
                "Invalid number format for flight ID or price.",
                "Validation Error",
                JOptionPane.ERROR_MESSAGE);
        }
    }
    
    /**
     * Validate input fields
     */
    private boolean validateInput() {
        // Check ticket number
        if (ticketNumberField.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this,
                "Ticket number cannot be empty.",
                "Validation Error",
                JOptionPane.ERROR_MESSAGE);
            ticketNumberField.requestFocus();
            return false;
        }
        
        // Check flight ID
        try {
            Integer.parseInt(flightIdField.getText().trim());
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this,
                "Flight ID must be a number.",
                "Validation Error",
                JOptionPane.ERROR_MESSAGE);
            flightIdField.requestFocus();
            return false;
        }
        
        // Check flight number
        if (flightNumberField.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this,
                "Flight number cannot be empty.",
                "Validation Error",
                JOptionPane.ERROR_MESSAGE);
            flightNumberField.requestFocus();
            return false;
        }
        
        // Check from and to cities are different
        if (fromCityComboBox.getSelectedItem().equals(toCityComboBox.getSelectedItem())) {
            JOptionPane.showMessageDialog(this,
                "Departure and destination cities must be different.",
                "Validation Error",
                JOptionPane.ERROR_MESSAGE);
            toCityComboBox.requestFocus();
            return false;
        }
        
        // Check departure date
        Date departureDate = departureDateChooser.getDate();
        if (departureDate == null) {
            JOptionPane.showMessageDialog(this,
                "Departure date cannot be empty.",
                "Validation Error",
                JOptionPane.ERROR_MESSAGE);
            departureDateChooser.requestFocus();
            return false;
        }
        
        // Check that departure date is not in the past
        Date today = new Date();
        Calendar cal = Calendar.getInstance();
        cal.setTime(today);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        today = cal.getTime();
        
        if (departureDate.before(today)) {
            JOptionPane.showMessageDialog(this,
                "Departure date cannot be in the past.",
                "Validation Error",
                JOptionPane.ERROR_MESSAGE);
            departureDateChooser.requestFocus();
            return false;
        }
        
        // No need to validate time format as TimeSpinner ensures correct format
        
        // Check price
        try {
            int price = Integer.parseInt(priceField.getText().trim());
            if (price <= 0) {
                throw new NumberFormatException();
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this,
                "Price must be a positive number.",
                "Validation Error",
                JOptionPane.ERROR_MESSAGE);
            priceField.requestFocus();
            return false;
        }
        
        return true;
    }
} 