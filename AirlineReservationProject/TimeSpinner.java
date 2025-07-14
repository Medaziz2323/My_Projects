import javax.swing.*;
import javax.swing.event.*;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * A time spinner component for selecting hours and minutes
 */
public class TimeSpinner extends JSpinner {
    private SimpleDateFormat format;

    /**
     * Constructs a new TimeSpinner with 24-hour format
     */
    public TimeSpinner() {
        this("HH:mm");
    }

    /**
     * Constructs a new TimeSpinner with the specified format
     * @param format The time format (e.g., "HH:mm" for 24-hour format)
     */
    public TimeSpinner(String format) {
        this.format = new SimpleDateFormat(format);
        
        // Create a date model with the current time
        SpinnerDateModel model = new SpinnerDateModel();
        model.setCalendarField(Calendar.MINUTE);
        setModel(model);
        
        // Set the editor to display only the time
        JSpinner.DateEditor editor = new JSpinner.DateEditor(this, format);
        setEditor(editor);
        
        // Make date field not editable directly (only through spinner)
        JFormattedTextField textField = editor.getTextField();
        textField.setEditable(false);
        
        // Add change listener to limit the range to 00:00 - 23:59
        addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                Date date = (Date) getValue();
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(date);
                
                // Reset date part, keep only time part (prevents date from changing)
                Calendar now = Calendar.getInstance();
                calendar.set(Calendar.YEAR, now.get(Calendar.YEAR));
                calendar.set(Calendar.MONTH, now.get(Calendar.MONTH));
                calendar.set(Calendar.DAY_OF_MONTH, now.get(Calendar.DAY_OF_MONTH));
                
                setValue(calendar.getTime());
            }
        });
    }
    
    /**
     * Gets the time as a formatted string
     * @return The time as a string in the specified format
     */
    public String getTimeAsString() {
        return format.format((Date) getValue());
    }
    
    /**
     * Sets the time from a string
     * @param timeString The time string in the expected format
     */
    public void setTimeFromString(String timeString) {
        try {
            Date time = format.parse(timeString);
            setValue(time);
        } catch (Exception e) {
            // If parsing fails, set to current time
            setValue(new Date());
        }
    }
} 