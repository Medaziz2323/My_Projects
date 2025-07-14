import java.io.Serializable;
import java.util.Date;

/**
 * Reservation class to represent a flight booking in the system.
 * Links a user to a ticket with passenger details.
 */
public class Reservation implements Serializable {
    // Reservation statuses
    public enum Status {
        PENDING, CONFIRMED, CANCELLED, COMPLETED
    }
    
    // Reservation properties
    private int reservationId;
    private int userId;
    private int ticketId;
    private String passengerName;
    private int adults;
    private int children;
    private int infants;
    private Date bookingDate;
    private Status status;
    private String confirmationCode;
    
    /**
     * Constructor for creating a reservation with all details
     */
    public Reservation(int reservationId, int userId, int ticketId, String passengerName,
                      int adults, int children, int infants, Date bookingDate,
                      Status status, String confirmationCode) {
        this.reservationId = reservationId;
        this.userId = userId;
        this.ticketId = ticketId;
        this.passengerName = passengerName;
        this.adults = adults;
        this.children = children;
        this.infants = infants;
        this.bookingDate = bookingDate;
        this.status = status;
        this.confirmationCode = confirmationCode;
    }
    
    /**
     * Constructor for creating a new reservation without ID (ID will be assigned by database)
     */
    public Reservation(int userId, int ticketId, String passengerName,
                      int adults, int children, int infants, Date bookingDate,
                      String confirmationCode) {
        this(-1, userId, ticketId, passengerName, adults, children, infants,
             bookingDate, Status.PENDING, confirmationCode);
    }
    
    // Getters and setters
    
    public int getReservationId() {
        return reservationId;
    }
    
    public void setReservationId(int reservationId) {
        this.reservationId = reservationId;
    }
    
    public int getUserId() {
        return userId;
    }
    
    public void setUserId(int userId) {
        this.userId = userId;
    }
    
    public int getTicketId() {
        return ticketId;
    }
    
    public void setTicketId(int ticketId) {
        this.ticketId = ticketId;
    }
    
    public String getPassengerName() {
        return passengerName;
    }
    
    public void setPassengerName(String passengerName) {
        this.passengerName = passengerName;
    }
    
    public int getAdults() {
        return adults;
    }
    
    public void setAdults(int adults) {
        this.adults = adults;
    }
    
    public int getChildren() {
        return children;
    }
    
    public void setChildren(int children) {
        this.children = children;
    }
    
    public int getInfants() {
        return infants;
    }
    
    public void setInfants(int infants) {
        this.infants = infants;
    }
    
    public Date getBookingDate() {
        return bookingDate;
    }
    
    public void setBookingDate(Date bookingDate) {
        this.bookingDate = bookingDate;
    }
    
    public Status getStatus() {
        return status;
    }
    
    public void setStatus(Status status) {
        this.status = status;
    }
    
    public String getConfirmationCode() {
        return confirmationCode;
    }
    
    public void setConfirmationCode(String confirmationCode) {
        this.confirmationCode = confirmationCode;
    }
    
    /**
     * Get total number of passengers
     */
    public int getTotalPassengers() {
        return adults + children + infants;
    }
    
    /**
     * Check if this reservation is active (not cancelled)
     */
    public boolean isActive() {
        return status != Status.CANCELLED;
    }
    
    /**
     * To string method for debugging
     */
    @Override
    public String toString() {
        return "Reservation{" +
                "reservationId=" + reservationId +
                ", userId=" + userId +
                ", ticketId=" + ticketId +
                ", passengerName='" + passengerName + '\'' +
                ", adults=" + adults +
                ", children=" + children +
                ", infants=" + infants +
                ", bookingDate=" + bookingDate +
                ", status=" + status +
                ", confirmationCode='" + confirmationCode + '\'' +
                '}';
    }
} 