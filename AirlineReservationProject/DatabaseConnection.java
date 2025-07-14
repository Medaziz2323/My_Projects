import java.sql.*;

/**
 * DatabaseConnection class to handle database connections and provide utility methods.
 * This class uses MySQL with XAMPP.
 */
public class DatabaseConnection {
    // Database connection parameters
    private static final String DB_URL = "jdbc:mysql://localhost:3306/";
    private static final String DB_NAME = "tunisia_airways";
    private static final String FULL_DB_URL = DB_URL + DB_NAME;
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "";
    
    // Database connection
    private static Connection connection;
    
    /**
     * Get a connection to the database
     */
    public static Connection getConnection() throws SQLException {
        if (connection == null || connection.isClosed()) {
            try {
                // Load the MySQL JDBC driver
                Class.forName("com.mysql.cj.jdbc.Driver");
                
                try {
                    // Try to connect directly to the database
                    connection = DriverManager.getConnection(FULL_DB_URL, DB_USER, DB_PASSWORD);
                } catch (SQLException e) {
                    // Database might not exist, try to create it
                    if (e.getMessage().contains("Unknown database")) {
                        System.out.println("Database doesn't exist. Creating database " + DB_NAME);
                        createDatabase();
                        // Connect again after creating the database
                        connection = DriverManager.getConnection(FULL_DB_URL, DB_USER, DB_PASSWORD);
                    } else {
                        // Some other error, rethrow it
                        throw e;
                    }
                }
            } catch (ClassNotFoundException e) {
                throw new SQLException("MySQL JDBC Driver not found", e);
            }
        }
        
        return connection;
    }
    
    /**
     * Create the database if it doesn't exist
     */
    private static void createDatabase() throws SQLException {
        // Connect to MySQL server without specifying a database
        try (Connection rootConnection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             Statement stmt = rootConnection.createStatement()) {
            
            // Create the database
            stmt.executeUpdate("CREATE DATABASE IF NOT EXISTS " + DB_NAME);
            System.out.println("Database " + DB_NAME + " created successfully");
        }
    }
    
    /**
     * Close the database connection
     */
    public static void closeConnection() {
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                System.err.println("Error closing database connection: " + e.getMessage());
            }
        }
    }
    
    /**
     * Close database resources (ResultSet, Statement, etc.)
     */
    public static void closeResources(ResultSet rs, Statement stmt) {
        if (rs != null) {
            try {
                rs.close();
            } catch (SQLException e) {
                System.err.println("Error closing ResultSet: " + e.getMessage());
            }
        }
        
        if (stmt != null) {
            try {
                stmt.close();
            } catch (SQLException e) {
                System.err.println("Error closing Statement: " + e.getMessage());
            }
        }
    }
    
    /**
     * Create database tables if they don't exist
     */
    public static void initializeDatabase() {
        try {
            Connection conn = getConnection();
            Statement stmt = conn.createStatement();
            
            // Create Users table
            stmt.executeUpdate(
                "CREATE TABLE IF NOT EXISTS users (" +
                "user_id INT AUTO_INCREMENT PRIMARY KEY, " +
                "username VARCHAR(50) NOT NULL UNIQUE, " +
                "password VARCHAR(100) NOT NULL, " +
                "full_name VARCHAR(100) NOT NULL, " +
                "email VARCHAR(100) NOT NULL, " +
                "role ENUM('ADMIN', 'USER') NOT NULL, " +
                "created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP" +
                ")"
            );
            
            // Create Tickets table
            stmt.executeUpdate(
                "CREATE TABLE IF NOT EXISTS tickets (" +
                "ticket_id INT AUTO_INCREMENT PRIMARY KEY, " +
                "ticket_number VARCHAR(20) NOT NULL UNIQUE, " +
                "flight_id INT NOT NULL, " +
                "flight_number VARCHAR(20) NOT NULL, " +
                "from_city VARCHAR(50) NOT NULL, " +
                "to_city VARCHAR(50) NOT NULL, " +
                "departure_date DATE NOT NULL, " +
                "departure_time VARCHAR(10) NOT NULL, " +
                "travel_class VARCHAR(20) NOT NULL, " +
                "price INT NOT NULL, " +
                "is_active BOOLEAN DEFAULT TRUE, " +
                "created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP" +
                ")"
            );
            
            // Create Reservations table
            stmt.executeUpdate(
                "CREATE TABLE IF NOT EXISTS reservations (" +
                "reservation_id INT AUTO_INCREMENT PRIMARY KEY, " +
                "user_id INT NOT NULL, " +
                "ticket_id INT NOT NULL, " +
                "passenger_name VARCHAR(100) NOT NULL, " +
                "adults INT NOT NULL, " +
                "children INT NOT NULL, " +
                "infants INT NOT NULL, " +
                "booking_date DATE NOT NULL, " +
                "status ENUM('PENDING', 'CONFIRMED', 'CANCELLED', 'COMPLETED') NOT NULL, " +
                "confirmation_code VARCHAR(20) NOT NULL UNIQUE, " +
                "created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP, " +
                "FOREIGN KEY (user_id) REFERENCES users(user_id), " +
                "FOREIGN KEY (ticket_id) REFERENCES tickets(ticket_id)" +
                ")"
            );
            
            // Insert default admin user if not exists
            PreparedStatement checkAdmin = conn.prepareStatement(
                "SELECT COUNT(*) FROM users WHERE username = 'admin'"
            );
            ResultSet rs = checkAdmin.executeQuery();
            rs.next();
            int adminCount = rs.getInt(1);
            
            if (adminCount == 0) {
                PreparedStatement insertAdmin = conn.prepareStatement(
                    "INSERT INTO users (username, password, full_name, email, role) " +
                    "VALUES (?, ?, ?, ?, ?)"
                );
                insertAdmin.setString(1, "admin");
                insertAdmin.setString(2, "admin"); // In production, use password hashing
                insertAdmin.setString(3, "System Administrator");
                insertAdmin.setString(4, "admin@tunisiaairways.com");
                insertAdmin.setString(5, "ADMIN");
                insertAdmin.executeUpdate();
                insertAdmin.close();
                
                // Add sample tickets when initializing for the first time
                insertSampleTickets(conn);
            }
            
            checkAdmin.close();
            rs.close();
            stmt.close();
            
            System.out.println("Database initialized successfully");
            
        } catch (SQLException e) {
            System.err.println("Error initializing database: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    /**
     * Insert sample tickets to the database
     */
    private static void insertSampleTickets(Connection conn) throws SQLException {
        // Array of sample ticket data
        String[][] sampleTickets = {
            // ticket_number, flight_id, flight_number, from, to, departure_date, time, class, price
            {"TK1001", "1", "TN101", "Tunis", "Paris", "2023-12-15", "09:30", "Economic", "780"},
            {"TK1002", "1", "TN102", "Tunis", "Paris", "2023-12-15", "14:45", "Business", "1560"},
            {"TK1003", "1", "TN103", "Tunis", "Paris", "2023-12-16", "10:15", "First Class", "2340"},
            {"TK1004", "2", "TN201", "Tunis", "Rome", "2023-12-17", "08:00", "Economic", "650"},
            {"TK1005", "2", "TN202", "Tunis", "Rome", "2023-12-18", "15:30", "Business", "1300"},
            {"TK1006", "3", "TN301", "Tunis", "London", "2023-12-19", "07:45", "Economic", "900"},
            {"TK1007", "3", "TN302", "Tunis", "London", "2023-12-20", "13:00", "Business", "1800"},
            {"TK1008", "4", "TN401", "Monastir", "Barcelona", "2023-12-21", "09:30", "Economic", "740"},
            {"TK1009", "5", "TN501", "Sfax", "Berlin", "2023-12-22", "11:15", "Economic", "890"},
            {"TK1010", "6", "TN601", "Djerba", "Madrid", "2023-12-23", "10:00", "Economic", "810"}
        };
        
        // Prepare statement for inserting tickets
        PreparedStatement insertTicket = conn.prepareStatement(
            "INSERT INTO tickets (ticket_number, flight_id, flight_number, from_city, to_city, departure_date, departure_time, travel_class, price, is_active) " +
            "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, TRUE)"
        );
        
        // Insert each sample ticket
        for (String[] ticket : sampleTickets) {
            insertTicket.setString(1, ticket[0]); // ticket_number
            insertTicket.setInt(2, Integer.parseInt(ticket[1])); // flight_id
            insertTicket.setString(3, ticket[2]); // flight_number
            insertTicket.setString(4, ticket[3]); // from_city
            insertTicket.setString(5, ticket[4]); // to_city
            insertTicket.setString(6, ticket[5]); // departure_date
            insertTicket.setString(7, ticket[6]); // departure_time
            insertTicket.setString(8, ticket[7]); // travel_class
            insertTicket.setInt(9, Integer.parseInt(ticket[8])); // price
            
            try {
                insertTicket.executeUpdate();
                System.out.println("Added sample ticket: " + ticket[0]);
            } catch (SQLException e) {
                System.err.println("Error adding sample ticket " + ticket[0] + ": " + e.getMessage());
            }
        }
        
        insertTicket.close();
    }
} 