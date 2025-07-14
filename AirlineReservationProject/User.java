import java.io.Serializable;
import java.util.Date;

/**
 * User class to handle user information and authentication.
 * Users can have either ADMIN or USER roles.
 */
public class User implements Serializable {
    // User roles
    public enum Role {
        ADMIN, USER
    }
    
    // User properties
    private int userId;
    private String username;
    private String password;
    private String fullName;
    private String email;
    private Role role;
    private Date createdAt;
    
    /**
     * Constructor for creating a user with all details
     */
    public User(int userId, String username, String password, String fullName, String email, Role role) {
        this.userId = userId;
        this.username = username;
        this.password = password;
        this.fullName = fullName;
        this.email = email;
        this.role = role;
    }
    
    /**
     * Constructor for creating a new user without ID (ID will be assigned by database)
     */
    public User(String username, String password, String fullName, String email, Role role) {
        this(-1, username, password, fullName, email, role);
    }
    
    // Getters and setters
    
    public int getUserId() {
        return userId;
    }
    
    public void setUserId(int userId) {
        this.userId = userId;
    }
    
    public String getUsername() {
        return username;
    }
    
    public void setUsername(String username) {
        this.username = username;
    }
    
    public String getPassword() {
        return password;
    }
    
    public void setPassword(String password) {
        this.password = password;
    }
    
    public String getFullName() {
        return fullName;
    }
    
    public void setFullName(String fullName) {
        this.fullName = fullName;
    }
    
    public String getEmail() {
        return email;
    }
    
    public void setEmail(String email) {
        this.email = email;
    }
    
    public Role getRole() {
        return role;
    }
    
    public void setRole(Role role) {
        this.role = role;
    }
    
    public Date getCreatedAt() {
        return createdAt;
    }
    
    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }
    
    /**
     * Check if the user is an admin
     */
    public boolean isAdmin() {
        return role == Role.ADMIN;
    }
    
    /**
     * To string method for debugging
     */
    @Override
    public String toString() {
        return "User{" +
                "userId=" + userId +
                ", username='" + username + '\'' +
                ", fullName='" + fullName + '\'' +
                ", email='" + email + '\'' +
                ", role=" + role +
                '}';
    }
} 