import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Data Access Object for Ticket entities
 */
public class TicketDAO {
    /**
     * Insert a new ticket into the database
     */
    public static boolean insertTicket(Ticket ticket) {
        String sql = "INSERT INTO tickets (ticket_number, flight_id, flight_number, from_city, to_city, " +
                     "departure_date, departure_time, travel_class, price, is_active) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            // Format the departure date
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            String formattedDate = dateFormat.format(ticket.getDepartureDate());
            
            stmt.setString(1, ticket.getTicketNumber());
            stmt.setInt(2, ticket.getFlightId());
            stmt.setString(3, ticket.getFlightNumber());
            stmt.setString(4, ticket.getFromCity());
            stmt.setString(5, ticket.getToCity());
            stmt.setString(6, formattedDate);
            stmt.setString(7, ticket.getDepartureTime());
            stmt.setString(8, ticket.getTravelClass());
            stmt.setInt(9, ticket.getPrice());
            stmt.setBoolean(10, ticket.isActive());
            
            int rowsAffected = stmt.executeUpdate();
            
            if (rowsAffected > 0) {
                // Get the generated ID
                try (ResultSet rs = stmt.getGeneratedKeys()) {
                    if (rs.next()) {
                        ticket.setTicketId(rs.getInt(1));
                        return true;
                    }
                }
            }
            
            return false;
            
        } catch (SQLException e) {
            System.err.println("Error inserting ticket: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Update an existing ticket in the database
     */
    public static boolean updateTicket(Ticket ticket) {
        String sql = "UPDATE tickets SET ticket_number = ?, flight_id = ?, flight_number = ?, from_city = ?, to_city = ?, " +
                     "departure_date = ?, departure_time = ?, travel_class = ?, price = ?, is_active = ? WHERE ticket_id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            // Format the departure date
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            String formattedDate = dateFormat.format(ticket.getDepartureDate());
            
            stmt.setString(1, ticket.getTicketNumber());
            stmt.setInt(2, ticket.getFlightId());
            stmt.setString(3, ticket.getFlightNumber());
            stmt.setString(4, ticket.getFromCity());
            stmt.setString(5, ticket.getToCity());
            stmt.setString(6, formattedDate);
            stmt.setString(7, ticket.getDepartureTime());
            stmt.setString(8, ticket.getTravelClass());
            stmt.setInt(9, ticket.getPrice());
            stmt.setBoolean(10, ticket.isActive());
            stmt.setInt(11, ticket.getTicketId());
            
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
            
        } catch (SQLException e) {
            System.err.println("Error updating ticket: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Delete a ticket from the database
     */
    public static boolean deleteTicket(int ticketId) {
        String sql = "DELETE FROM tickets WHERE ticket_id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, ticketId);
            
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
            
        } catch (SQLException e) {
            System.err.println("Error deleting ticket: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Get a ticket by its ID
     */
    public static Ticket getTicketById(int ticketId) {
        String sql = "SELECT * FROM tickets WHERE ticket_id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, ticketId);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToTicket(rs);
                }
            }
            
        } catch (SQLException e) {
            System.err.println("Error getting ticket by ID: " + e.getMessage());
        }
        
        return null;
    }
    
    /**
     * Get a ticket by its ticket number
     */
    public static Ticket getTicketByNumber(String ticketNumber) {
        String sql = "SELECT * FROM tickets WHERE ticket_number = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, ticketNumber);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToTicket(rs);
                }
            }
            
        } catch (SQLException e) {
            System.err.println("Error getting ticket by number: " + e.getMessage());
        }
        
        return null;
    }
    
    /**
     * Get all available tickets from the database
     */
    public static List<Ticket> getAllTickets() {
        return getTicketsByQuery("SELECT * FROM tickets", null);
    }
    
    /**
     * Get all active tickets
     */
    public static List<Ticket> getActiveTickets() {
        return getTicketsByQuery("SELECT * FROM tickets WHERE is_active = TRUE", null);
    }
    
    /**
     * Get available tickets (active tickets with future departure dates)
     */
    public static List<Ticket> getAvailableTickets() {
        String sql = "SELECT * FROM tickets WHERE is_active = TRUE AND departure_date >= CURRENT_DATE ORDER BY departure_date";
        return getTicketsByQuery(sql, null);
    }
    
    /**
     * Search for tickets by departure and destination cities
     */
    public static List<Ticket> searchTickets(String fromCity, String toCity) {
        String sql = "SELECT * FROM tickets WHERE from_city = ? AND to_city = ? AND is_active = TRUE";
        
        String[] params = new String[2];
        params[0] = fromCity;
        params[1] = toCity;
        
        return getTicketsByQuery(sql, params);
    }
    
    /**
     * Helper method to execute a query and return a list of tickets
     */
    private static List<Ticket> getTicketsByQuery(String sql, String[] params) {
        List<Ticket> tickets = new ArrayList<>();
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            // Set parameters if provided
            if (params != null) {
                for (int i = 0; i < params.length; i++) {
                    stmt.setString(i + 1, params[i]);
                }
            }
            
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    tickets.add(mapResultSetToTicket(rs));
                }
            }
            
        } catch (SQLException e) {
            System.err.println("Error executing query: " + e.getMessage());
        }
        
        return tickets;
    }
    
    /**
     * Helper method to map a ResultSet row to a Ticket object
     */
    private static Ticket mapResultSetToTicket(ResultSet rs) throws SQLException {
        int ticketId = rs.getInt("ticket_id");
        String ticketNumber = rs.getString("ticket_number");
        int flightId = rs.getInt("flight_id");
        String flightNumber = rs.getString("flight_number");
        String fromCity = rs.getString("from_city");
        String toCity = rs.getString("to_city");
        java.sql.Date sqlDate = rs.getDate("departure_date");
        java.util.Date departureDate = new java.util.Date(sqlDate.getTime());
        String departureTime = rs.getString("departure_time");
        String travelClass = rs.getString("travel_class");
        int price = rs.getInt("price");
        boolean isActive = rs.getBoolean("is_active");
        
        return new Ticket(ticketId, ticketNumber, flightId, flightNumber, fromCity, toCity,
                         departureDate, departureTime, travelClass, price, isActive);
    }
} 