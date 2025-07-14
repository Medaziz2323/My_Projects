import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;

/**
 * AppStyle - Centralized styling for Tunisia Airways application
 * Uses the Orange Drip color palette
 */
public class AppStyle {
    // Color palette - Orange Drip
    public static final Color DARK_NAVY = new Color(5, 24, 33);      // #051821
    public static final Color DARK_TEAL = new Color(26, 70, 69);     // #1A4645
    public static final Color TEAL = new Color(38, 104, 103);        // #266867
    public static final Color ORANGE = new Color(245, 136, 0);       // #F58800
    public static final Color YELLOW = new Color(248, 194, 116);     // #F8C274
    
    // Additional UI colors
    public static final Color BACKGROUND = new Color(245, 245, 245); // Light gray background
    public static final Color TEXT = new Color(33, 33, 33);          // Dark text
    public static final Color SUCCESS = new Color(0, 128, 0);        // Green for success messages
    public static final Color ERROR = new Color(204, 0, 0);          // Red for error messages
    public static final Color WHITE = Color.WHITE;
    
    // Fonts
    public static final Font FONT_REGULAR = new Font("Arial", Font.PLAIN, 14);
    public static final Font FONT_BOLD = new Font("Arial", Font.BOLD, 14);
    public static final Font FONT_TITLE = new Font("Arial", Font.BOLD, 22);
    public static final Font FONT_SUBTITLE = new Font("Arial", Font.BOLD, 18);
    public static final Font FONT_SMALL = new Font("Arial", Font.ITALIC, 12); // Added for small text (hints, etc)
    
    /**
     * Makes a JFrame display in fullscreen mode
     */
    public static void setFullScreen(JFrame frame) {
        // Get screen dimensions
        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        GraphicsDevice gd = ge.getDefaultScreenDevice();
        
        // Check if fullscreen is supported
        if (gd.isFullScreenSupported()) {
            frame.setUndecorated(false); // Keep window decorations (minimize, maximize, close buttons)
            frame.setExtendedState(JFrame.MAXIMIZED_BOTH); // Maximize the window
        } else {
            // Fallback to maximized window if fullscreen not supported
            frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        }
    }
    
    /**
     * Styles a primary button
     */
    public static void stylePrimaryButton(JButton button) {
        styleButton(button, ORANGE, DARK_NAVY);
    }
    
    /**
     * Styles a secondary button
     */
    public static void styleSecondaryButton(JButton button) {
        styleButton(button, TEAL, YELLOW);
    }
    
    /**
     * Styles a danger/cancel button
     */
    public static void styleDangerButton(JButton button) {
        styleButton(button, ERROR, YELLOW);
    }
    
    /**
     * Styles a neutral button
     */
    public static void styleNeutralButton(JButton button) {
        styleButton(button, BACKGROUND, DARK_NAVY);
    }
    
    /**
     * Styles a button with the given colors
     */
    public static void styleButton(JButton button, Color bgColor, Color fgColor) {
        // Set colors
        button.setBackground(bgColor);
        button.setForeground(fgColor);
        
        // Set font
        button.setFont(FONT_BOLD);
        
        // Set border
        button.setBorderPainted(true);
        button.setBorder(BorderFactory.createLineBorder(DARK_NAVY, 1));
        
        // Set size and cursor
        button.setPreferredSize(new Dimension(150, 40));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setFocusPainted(false);
        
        // Add hover effect
        Color hoverColor = new Color(
            Math.max(0, bgColor.getRed() - 20),
            Math.max(0, bgColor.getGreen() - 20),
            Math.max(0, bgColor.getBlue() - 20)
        );
        
        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                button.setBackground(hoverColor);
            }
            
            @Override
            public void mouseExited(MouseEvent e) {
                button.setBackground(bgColor);
            }
        });
    }
    
    /**
     * Styles a menu button
     */
    public static void styleMenuButton(JButton button, boolean isActive) {
        button.setFont(FONT_BOLD);
        button.setForeground(isActive ? DARK_NAVY : YELLOW);
        button.setAlignmentX(Component.CENTER_ALIGNMENT);
        button.setMaximumSize(new Dimension(160, 40));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setFocusPainted(false);
        
        if (isActive) {
            button.setBackground(ORANGE);
        } else {
            button.setBackground(DARK_TEAL);
        }
        
        button.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));
        
        // Add hover effect for non-active buttons
        if (!isActive) {
            button.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseEntered(MouseEvent e) {
                    button.setBackground(TEAL);
                }
                
                @Override
                public void mouseExited(MouseEvent e) {
                    button.setBackground(DARK_TEAL);
                }
            });
        }
    }
    
    /**
     * Styles a panel with a card-like appearance
     */
    public static void stylePanel(JPanel panel) {
        panel.setBackground(WHITE);
        panel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(230, 230, 230), 1),
            BorderFactory.createEmptyBorder(20, 20, 20, 20)
        ));
    }
    
    /**
     * Styles a text field
     */
    public static void styleTextField(JTextField textField) {
        textField.setFont(FONT_REGULAR);
        textField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(230, 230, 230), 1),
            BorderFactory.createEmptyBorder(8, 8, 8, 8)
        ));
    }
    
    /**
     * Styles a combo box
     */
    public static void styleComboBox(JComboBox<?> comboBox) {
        comboBox.setFont(FONT_REGULAR);
        comboBox.setBackground(WHITE);
        comboBox.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(230, 230, 230), 1),
            BorderFactory.createEmptyBorder(8, 8, 8, 8)
        ));
    }
    
    /**
     * Styles a table with consistent appearance
     */
    public static void styleTable(JTable table) {
        table.setRowHeight(35);
        table.setFont(FONT_REGULAR);
        table.setSelectionBackground(new Color(232, 242, 254));
        table.setSelectionForeground(TEXT);
        table.setShowGrid(true);
        table.setGridColor(new Color(230, 230, 230));
        
        // Style the header
        JTableHeader header = table.getTableHeader();
        header.setFont(FONT_BOLD);
        header.setBackground(DARK_NAVY);
        header.setForeground(YELLOW);
        header.setReorderingAllowed(false);
    }
    
    /**
     * Creates and styles a title label
     */
    public static JLabel createTitleLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(FONT_TITLE);
        label.setForeground(DARK_NAVY);
        return label;
    }
    
    /**
     * Creates and styles a bold label
     */
    public static JLabel createBoldLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(FONT_BOLD);
        label.setForeground(TEXT);
        return label;
    }
    
    /**
     * Creates and styles a regular label
     */
    public static JLabel createLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(FONT_REGULAR);
        label.setForeground(TEXT);
        return label;
    }
} 