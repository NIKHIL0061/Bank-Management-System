import java.io.*;
import java.util.*;
import java.time.LocalDateTime;

/**
 * Core banking logic - manages all accounts and handles file persistence via serialization
 */
public class Bank implements Serializable {
    private static final long serialVersionUID = 1L;
    private static final String DATA_FILE = "bank_data.ser";
    
    private Map<Long, Account> accounts;
    private Random random;
    
    public Bank() {
        accounts = new HashMap<>();
        random = new Random();
    }
    
    /**
     * Load bank data from serialized file
     */
    @SuppressWarnings("unchecked")
    public static Bank loadBank() {
        File file = new File(DATA_FILE);
        if (file.exists()) {
            try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
                Bank bank = (Bank) ois.readObject();
                System.out.println("Bank data loaded successfully from " + DATA_FILE);
                return bank;
            } catch (IOException | ClassNotFoundException e) {
                System.out.println("Error loading bank data: " + e.getMessage());
                System.out.println("Creating new bank instance.");
            }
        }
        return new Bank();
    }
    
    /**
     * Save bank data to serialized file
     */
    public void saveData() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(DATA_FILE))) {
            oos.writeObject(this);
            System.out.println("Bank data saved successfully to " + DATA_FILE);
        } catch (IOException e) {
            System.err.println("Error saving bank data: " + e.getMessage());
        }
    }
    
    /**
     * Generate a unique 8-digit account number
     */
    private long generateAccountNumber() {
        long accNo;
        do {
            accNo = 10000000 + random.nextInt(90000000); // 8-digit number
        } while (accounts.containsKey(accNo));
        return accNo;
    }
    
    /**
     * Create a new account
     * @return The generated account number, or -1 if creation failed
     */
    public long createAccount(String holderName, String pin, double initialDeposit) {
        if (holderName == null || holderName.trim().isEmpty()) {
            return -1;
        }
        if (pin == null || pin.length() < 4 || pin.length() > 6) {
            return -1;
        }
        if (initialDeposit < 0) {
            return -1;
        }
        
        long accNo = generateAccountNumber();
        Account account = new Account(accNo, holderName.trim(), pin);
        
        if (initialDeposit > 0) {
            boolean success = account.deposit(initialDeposit);
            if (!success) {
                return -1;
            }
        }
        
        accounts.put(accNo, account);
        saveData(); // Persist after account creation
        return accNo;
    }
    
    /**
     * Validate login credentials
     */
    public Account validateLogin(long accountNumber, String pin) {
        Account account = accounts.get(accountNumber);
        if (account != null && account.validatePin(pin)) {
            return account;
        }
        return null;
    }
    
    /**
     * Deposit money to an account
     * @return null if successful, error message if failed
     */
    public String deposit(long accountNumber, double amount) {
        if (amount <= 0) {
            return "Amount must be greater than zero!";
        }
        
        Account account = accounts.get(accountNumber);
        if (account == null) {
            return "Account not found!";
        }
        
        boolean success = account.deposit(amount);
        if (success) {
            saveData();
            return null; // Success
        }
        return "Deposit failed!";
    }
    
    /**
     * Withdraw money from an account
     * @return null if successful, error message if failed
     */
    public String withdraw(long accountNumber, double amount) {
        if (amount <= 0) {
            return "Amount must be greater than zero!";
        }
        
        Account account = accounts.get(accountNumber);
        if (account == null) {
            return "Account not found!";
        }
        
        if (amount > account.getBalance()) {
            return "Insufficient balance! Current balance: ₹" + String.format("%.2f", account.getBalance());
        }
        
        boolean success = account.withdraw(amount);
        if (success) {
            saveData();
            return null; // Success
        }
        return "Withdrawal failed!";
    }
    
    /**
     * Get account balance
     */
    public double getBalance(long accountNumber) {
        Account account = accounts.get(accountNumber);
        return account != null ? account.getBalance() : -1;
    }
    
    /**
     * Get transaction history for an account
     */
    public List<Transaction> getTransactionHistory(long accountNumber) {
        Account account = accounts.get(accountNumber);
        return account != null ? account.getTransactions() : new ArrayList<>();
    }
    
    /**
     * Check if account exists
     */
    public boolean accountExists(long accountNumber) {
        return accounts.containsKey(accountNumber);
    }
}