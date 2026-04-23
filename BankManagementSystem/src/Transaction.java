import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Represents a single transaction (Deposit or Withdrawal)
 * Stores type, amount, timestamp, and balance after transaction
 */
public class Transaction implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private String type;          // "DEPOSIT" or "WITHDRAWAL"
    private double amount;
    private LocalDateTime timestamp;
    private double balanceAfter;
    
    public Transaction(String type, double amount, double balanceAfter) {
        this.type = type;
        this.amount = amount;
        this.balanceAfter = balanceAfter;
        this.timestamp = LocalDateTime.now();
    }
    
    public String getType() { return type; }
    public double getAmount() { return amount; }
    public double getBalanceAfter() { return balanceAfter; }
    public LocalDateTime getTimestamp() { return timestamp; }
    
    public String getFormattedTimestamp() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return timestamp.format(formatter);
    }
    
    @Override
    public String toString() {
        return String.format("[%s] %s: ₹%.2f | Balance: ₹%.2f", 
                getFormattedTimestamp(), type, amount, balanceAfter);
    }
}