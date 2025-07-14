import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.border.*;
import javax.swing.table.*;
import java.util.List;
import java.text.SimpleDateFormat;

/**
 * AvailableTicketsPage class to display available flight tickets.
 */
public class AvailableTicketsPage extends JFrame {
    // Main panels
    private JPanel mainPanel;
    private JPanel tablePanel;
    private JPanel buttonPanel;
    private JPanel filterPanel;
    
    // Table components
    private JTable ticketsTable;
    private JScrollPane scrollPane;
    
    // Filter components
    private JComboBox<String> fromCityFilter;
    private JComboBox<String> toCityFilter;
    private JComboBox<String> classFilter;
    private JButton applyFilterButton;
    private JButton clearFilterButton;
    
    // Action buttons
    private JButton backButton;
    private JButton bookTicketButton;
    
    // Reference to previous screen and user
    private MainMenuPage mainMenuPage;
    private User user;
    
    // Data
    private List<Ticket> tickets;
    private List<Ticket> filteredTickets;
    
    /**
     * Constructor - creates the available tickets page
     */
    public AvailableTicketsPage(MainMenuPage mainMenuPage, User user, List<Ticket> tickets) {
        // Set the window title
        super("Available Tickets - Tunisia Airways");
        
        // Save references
        this.mainMenuPage = mainMenuPage;
        this.user = user;
        this.tickets = tickets;
        this.filteredTickets = tickets; // Initially show all tickets
        
        // Create the main panel
        mainPanel = new JPanel(new BorderLayout(20, 20));
        mainPanel.setBackground(AppStyle.BACKGROUND);
        mainPanel.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));
        
        // Create filter panel
        createFilterPanel();
        
        // Create the table panel
        createTablePanel();
        
        // Create the button panel
        createButtonPanel();
        
        // Add panels to the main panel
        mainPanel.add(filterPanel, BorderLayout.NORTH);
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
     * Creates the filter panel with filter options
     */
    private void createFilterPanel() {
        filterPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 10));
        filterPanel.setBackground(AppStyle.WHITE);
        filterPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(AppStyle.TEAL, 1),
            BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));
        
        // Create filter components
        JLabel filterLabel = AppStyle.createBoldLabel("Filter Tickets:");
        
        JLabel fromLabel = AppStyle.createLabel("From:");
        
        JLabel toLabel = AppStyle.createLabel("To:");
        
        JLabel classLabel = AppStyle.createLabel("Class:");
        
        // Create filter dropdowns with unique values from tickets
        fromCityFilter = createFilterComboBox(tickets, ticket -> ticket.getFromCity());
        toCityFilter = createFilterComboBox(tickets, ticket -> ticket.getToCity());
        classFilter = createFilterComboBox(tickets, ticket -> ticket.getTravelClass());
        
        // Create filter buttons
        applyFilterButton = new JButton("Apply Filter");
        applyFilterButton.setFont(AppStyle.FONT_REGULAR);
        applyFilterButton.addActionListener(e -> applyFilters());
        AppStyle.styleSecondaryButton(applyFilterButton);
        
        clearFilterButton = new JButton("Clear Filters");
        clearFilterButton.setFont(AppStyle.FONT_REGULAR);
        clearFilterButton.addActionListener(e -> clearFilters());
        AppStyle.styleSecondaryButton(clearFilterButton);
        
        // Add components to panel
        filterPanel.add(filterLabel);
        filterPanel.add(fromLabel);
        filterPanel.add(fromCityFilter);
        filterPanel.add(toLabel);
        filterPanel.add(toCityFilter);
        filterPanel.add(classLabel);
        filterPanel.add(classFilter);
        filterPanel.add(applyFilterButton);
        filterPanel.add(clearFilterButton);
    }
    
    /**
     * Interface for extracting values from tickets
     */
    private interface ValueExtractor {
        String extract(Ticket ticket);
    }
    
    /**
     * Creates a combo box with unique values from tickets
     */
    private JComboBox<String> createFilterComboBox(List<Ticket> tickets, ValueExtractor extractor) {
        // Add "All" option as first item
        JComboBox<String> comboBox = new JComboBox<>();
        comboBox.addItem("All");
        
        // Extract unique values
        java.util.Set<String> uniqueValues = new java.util.HashSet<>();
        for (Ticket ticket : tickets) {
            uniqueValues.add(extractor.extract(ticket));
        }
        
        // Add unique values to combo box
        for (String value : uniqueValues) {
            comboBox.addItem(value);
        }
        
        // Style the combo box
        AppStyle.styleComboBox(comboBox);
        
        return comboBox;
    }
    
    /**
     * Apply selected filters to the table
     */
    private void applyFilters() {
        String fromFilter = (String) fromCityFilter.getSelectedItem();
        String toFilter = (String) toCityFilter.getSelectedItem();
        String classFilter = (String) this.classFilter.getSelectedItem();
        
        // Filter tickets based on selected criteria
        filteredTickets = new java.util.ArrayList<>();
        
        for (Ticket ticket : tickets) {
            boolean includeTicket = true;
            
            // Check From filter
            if (!"All".equals(fromFilter) && !ticket.getFromCity().equals(fromFilter)) {
                includeTicket = false;
            }
            
            // Check To filter
            if (includeTicket && !"All".equals(toFilter) && !ticket.getToCity().equals(toFilter)) {
                includeTicket = false;
            }
            
            // Check Class filter
            if (includeTicket && !"All".equals(classFilter) && !ticket.getTravelClass().equals(classFilter)) {
                includeTicket = false;
            }
            
            // Add to filtered list if all criteria match
            if (includeTicket) {
                filteredTickets.add(ticket);
            }
        }
        
        // Update the table with filtered results
        updateTable();
    }
    
    /**
     * Clear all filters
     */
    private void clearFilters() {
        // Reset combo boxes to "All"
        fromCityFilter.setSelectedItem("All");
        toCityFilter.setSelectedItem("All");
        this.classFilter.setSelectedItem("All");
        
        // Show all tickets
        filteredTickets = tickets;
        
        // Update table
        updateTable();
    }
    
    /**
     * Creates the table panel with ticket data
     */
    private void createTablePanel() {
        tablePanel = new JPanel(new BorderLayout());
        tablePanel.setBackground(AppStyle.WHITE);
        tablePanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(AppStyle.TEAL, 1),
            BorderFactory.createEmptyBorder(20, 20, 20, 20)
        ));
        
        // Create title label
        JLabel titleLabel = AppStyle.createTitleLabel("Available Flight Tickets");
        
        // Create table with ticket data
        updateTable();
        
        // Add components to panel
        tablePanel.add(titleLabel, BorderLayout.NORTH);
        tablePanel.add(scrollPane, BorderLayout.CENTER);
    }
    
    /**
     * Updates the table with current filtered tickets
     */
    private void updateTable() {
        // Create table model
        String[] columnNames = {
            "Flight Number", "From", "To", "Date", "Time", "Class", "Price"
        };
        
        Object[][] data = new Object[filteredTickets.size()][7];
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        
        for (int i = 0; i < filteredTickets.size(); i++) {
            Ticket ticket = filteredTickets.get(i);
            
            data[i][0] = ticket.getFlightNumber();
            data[i][1] = ticket.getFromCity();
            data[i][2] = ticket.getToCity();
            data[i][3] = dateFormat.format(ticket.getDepartureDate());
            data[i][4] = ticket.getDepartureTime();
            data[i][5] = ticket.getTravelClass();
            data[i][6] = ticket.getPrice() + " TND";
        }
        
        // Create table with custom model
        DefaultTableModel model = new DefaultTableModel(data, columnNames) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Make table non-editable
            }
        };
        
        // Create or update table
        if (ticketsTable == null) {
            ticketsTable = new JTable(model);
            
            // Style the table
            AppStyle.styleTable(ticketsTable);
            
            // Center align the text in cells
            DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
            centerRenderer.setHorizontalAlignment(JLabel.CENTER);
            
            for (int i = 0; i < ticketsTable.getColumnCount(); i++) {
                ticketsTable.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
            }
            
            // Set preferred widths
            ticketsTable.getColumnModel().getColumn(0).setPreferredWidth(120); // Flight Number
            ticketsTable.getColumnModel().getColumn(1).setPreferredWidth(100); // From
            ticketsTable.getColumnModel().getColumn(2).setPreferredWidth(100); // To
            ticketsTable.getColumnModel().getColumn(3).setPreferredWidth(100); // Date
            ticketsTable.getColumnModel().getColumn(4).setPreferredWidth(80);  // Time
            ticketsTable.getColumnModel().getColumn(5).setPreferredWidth(100); // Class
            ticketsTable.getColumnModel().getColumn(6).setPreferredWidth(80);  // Price
            
            // Add table to a scroll pane
            scrollPane = new JScrollPane(ticketsTable);
            scrollPane.setBorder(BorderFactory.createEmptyBorder(20, 0, 0, 0));
        } else {
            ticketsTable.setModel(model);
        }
    }
    
    /**
     * Creates the button panel with action buttons
     */
    private void createButtonPanel() {
        buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 0));
        buttonPanel.setBackground(AppStyle.BACKGROUND);
        
        // Book Ticket button
        bookTicketButton = new JButton("Book Selected Ticket");
        AppStyle.stylePrimaryButton(bookTicketButton);
        bookTicketButton.addActionListener(e -> bookSelectedTicket());
        
        // Back button
        backButton = new JButton("Back to Main Menu");
        AppStyle.styleSecondaryButton(backButton);
        backButton.addActionListener(e -> backToMainMenu());
        
        // Add buttons to panel
        buttonPanel.add(bookTicketButton);
        buttonPanel.add(backButton);
    }
    
    /**
     * Book the selected ticket
     */
    private void bookSelectedTicket() {
        int selectedRow = ticketsTable.getSelectedRow();
        
        if (selectedRow >= 0) {
            Ticket ticket = filteredTickets.get(selectedRow);
            
            // Regular users can book tickets, but admins should use the admin dashboard for management
            if (user.isAdmin()) {
                JOptionPane.showMessageDialog(this,
                    "Admins cannot book tickets. Please use admin dashboard for ticket management.",
                    "Admin Access",
                    JOptionPane.INFORMATION_MESSAGE);
                return;
            }
            
            // Open booking form with selected ticket
            new BookingForm(this, user, ticket);
        } else {
            JOptionPane.showMessageDialog(this,
                "Please select a ticket to book.",
                "No Selection",
                JOptionPane.INFORMATION_MESSAGE);
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