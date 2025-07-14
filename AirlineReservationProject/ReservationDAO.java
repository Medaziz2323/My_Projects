import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Data Access Object for Reservation entities
 */
public class ReservationDAO {
    /**
     * Insert a new reservation into the database
     */
    public static boolean insertReservation(Reservation reservation) {
        String sql = "INSERT INTO reservations (user_id, ticket_id, passenger_name, adults, children, infants, " +
                     "booking_date, status, confirmation_code) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            // Format the booking date
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            String formattedDate = dateFormat.format(reservation.getBookingDate());
            
            stmt.setInt(1, reservation.getUserId());
            stmt.setInt(2, reservation.getTicketId());
            stmt.setString(3, reservation.getPassengerName());
            stmt.setInt(4, reservation.getAdults());
            stmt.setInt(5, reservation.getChildren());
            stmt.setInt(6, reservation.getInfants());
            stmt.setString(7, formattedDate);
            stmt.setString(8, reservation.getStatus().toString());
            stmt.setString(9, reservation.getConfirmationCode());
            
            int rowsAffected = stmt.executeUpdate();
            
            if (rowsAffected > 0) {
                // Get the generated ID
                try (ResultSet rs = stmt.getGeneratedKeys()) {
                    if (rs.next()) {
                        reservation.setReservationId(rs.getInt(1));
                        return true;
                    }
                }
            }
            
            return false;
            
        } catch (SQLException e) {
            System.err.println("Error inserting reservation: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Update an existing reservation in the database
     */
    public static boolean updateReservation(Reservation reservation) {
        String sql = "UPDATE reservations SET user_id = ?, ticket_id = ?, passenger_name = ?, " +
                     "adults = ?, children = ?, infants = ?, booking_date = ?, status = ?, " +
                     "confirmation_code = ? WHERE reservation_id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            // Format the booking date
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            String formattedDate = dateFormat.format(reservation.getBookingDate());
            
            stmt.setInt(1, reservation.getUserId());
            stmt.setInt(2, reservation.getTicketId());
            stmt.setString(3, reservation.getPassengerName());
            stmt.setInt(4, reservation.getAdults());
            stmt.setInt(5, reservation.getChildren());
            stmt.setInt(6, reservation.getInfants());
            stmt.setString(7, formattedDate);
            stmt.setString(8, reservation.getStatus().toString());
            stmt.setString(9, reservation.getConfirmationCode());
            stmt.setInt(10, reservation.getReservationId());
            
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
            
        } catch (SQLException e) {
            System.err.println("Error updating reservation: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Update the status of a reservation
     */
    public static boolean updateReservationStatus(int reservationId, Reservation.Status status) {
        String sql = "UPDATE reservations SET status = ? WHERE reservation_id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, status.toString());
            stmt.setInt(2, reservationId);
            
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
            
        } catch (SQLException e) {
            System.err.println("Error updating reservation status: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Delete a reservation from the database
     */
    public static boolean deleteReservation(int reservationId) {
        String sql = "DELETE FROM reservations WHERE reservation_id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, reservationId);
            
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
            
        } catch (SQLException e) {
            System.err.println("Error deleting reservation: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Get a reservation by its ID
     */
    public static Reservation getReservationById(int reservationId) {
        String sql = "SELECT * FROM reservations WHERE reservation_id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, reservationId);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToReservation(rs);
                }
            }
            
        } catch (SQLException e) {
            System.err.println("Error getting reservation by ID: " + e.getMessage());
        }
        
        return null;
    }
    
    /**
     * Get a reservation by its confirmation code
     */
    public static Reservation getReservationByConfirmationCode(String confirmationCode) {
        String sql = "SELECT * FROM reservations WHERE confirmation_code = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, confirmationCode);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToReservation(rs);
                }
            }
            
        } catch (SQLException e) {
            System.err.println("Error getting reservation by confirmation code: " + e.getMessage());
        }
        
        return null;
    }
    
    /**
     * Get all reservations for a specific user
     */
    public static List<Reservation> getReservationsByUserId(int userId) {
        String sql = "SELECT * FROM reservations WHERE user_id = ?";
        
        List<Reservation> reservations = new ArrayList<>();
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, userId);
            
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    reservations.add(mapResultSetToReservation(rs));
                }
            }
            
        } catch (SQLException e) {
            System.err.println("Error getting reservations by user ID: " + e.getMessage());
        }
        
        return reservations;
    }
    
    /**
     * Get all reservations for a specific ticket
     */
    public static List<Reservation> getReservationsByTicketId(int ticketId) {
        String sql = "SELECT * FROM reservations WHERE ticket_id = ?";
        
        List<Reservation> reservations = new ArrayList<>();
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, ticketId);
            
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    reservations.add(mapResultSetToReservation(rs));
                }
            }
            
        } catch (SQLException e) {
            System.err.println("Error getting reservations by ticket ID: " + e.getMessage());
        }
        
        return reservations;
    }
    
    /**
     * Get all reservations with a specific status
     */
    public static List<Reservation> getReservationsByStatus(Reservation.Status status) {
        String sql = "SELECT * FROM reservations WHERE status = ?";
        
        List<Reservation> reservations = new ArrayList<>();
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, status.toString());
            
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    reservations.add(mapResultSetToReservation(rs));
                }
            }
            
        } catch (SQLException e) {
            System.err.println("Error getting reservations by status: " + e.getMessage());
        }
        
        return reservations;
    }
    
    /**
     * Get all reservations from the database
     */
    public static List<Reservation> getAllReservations() {
        String sql = "SELECT * FROM reservations";
        
        List<Reservation> reservations = new ArrayList<>();
        
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                reservations.add(mapResultSetToReservation(rs));
            }
            
        } catch (SQLException e) {
            System.err.println("Error getting all reservations: " + e.getMessage());
        }
        
        return reservations;
    }
    
    /**
     * Helper method to map a ResultSet row to a Reservation object
     */
    private static Reservation mapResultSetToReservation(ResultSet rs) throws SQLException {
        int reservationId = rs.getInt("reservation_id");
        int userId = rs.getInt("user_id");
        int ticketId = rs.getInt("ticket_id");
        String passengerName = rs.getString("passenger_name");
        int adults = rs.getInt("adults");
        int children = rs.getInt("children");
        int infants = rs.getInt("infants");
        java.sql.Date sqlDate = rs.getDate("booking_date");
        java.util.Date bookingDate = new java.util.Date(sqlDate.getTime());
        Reservation.Status status = Reservation.Status.valueOf(rs.getString("status"));
        String confirmationCode = rs.getString("confirmation_code");
        
        return new Reservation(reservationId, userId, ticketId, passengerName, 
                              adults, children, infants, bookingDate, status, confirmationCode);
    }
} 