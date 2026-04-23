import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents a bank account with account number, holder name, PIN, balance, and transaction history
 */
public class Account implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private long accountNumber;
    private String holderName;
    private String pin;           // Simple PIN for authentication (4-6 digits)
    private double balance;
    private List<Transaction> transactions;
    
    public Account(long accountNumber, String holderName, String pin) {
        this.accountNumber = accountNumber;
        this.holderName = holderName;
        this.pin = pin;
        this.balance = 0.0;
        this.transactions = new ArrayList<>();
    }
    
    // Getters
    public long getAccountNumber() { return accountNumber; }
    public String getHolderName() { return holderName; }
    public String getPin() { return pin; }
    public double getBalance() { return balance; }
    public List<Transaction> getTransactions() { return new ArrayList<>(transactions); }
    
    // Deposit money
    public boolean deposit(double amount) {
        if (amount <= 0) {
            return false;
        }
        balance += amount;
        transactions.add(new Transaction("DEPOSIT", amount, balance));
        return true;
    }
    
    // Withdraw money
    public boolean withdraw(double amount) {
        if (amount <= 0) {
            return false;
        }
        if (amount > balance) {
            return false; // Insufficient balance
        }
        balance -= amount;
        transactions.add(new Transaction("WITHDRAWAL", amount, balance));
        return true;
    }
    
    // Validate PIN
    public boolean validatePin(String inputPin) {
        return this.pin.equals(inputPin);
    }
    
    @Override
    public String toString() {
        return String.format("Account[%d] %s | Balance: ₹%.2f", accountNumber, holderName, balance);
    }
}