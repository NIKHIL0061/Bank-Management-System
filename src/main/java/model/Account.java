package model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Account implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private String username;
    private String password;
    private double balance;
    private List<String> transactions;

    public Account(String username, String password) {
        this.username = username;
        this.password = password;
        this.balance = 0.0;
        this.transactions = new ArrayList<>();
        addTransaction("Account created successfully.");
    }

    public String getUsername() { return username; }
    public String getPassword() { return password; }
    public double getBalance() { return balance; }
    public List<String> getTransactions() { return transactions; }

    public void deposit(double amount) {
        if (amount > 0) {
            this.balance += amount;
            addTransaction("Deposited: $" + amount);
        }
    }

    public boolean withdraw(double amount) {
        if (amount > 0 && this.balance >= amount) {
            this.balance -= amount;
            addTransaction("Withdrew: $" + amount);
            return true;
        }
        return false;
    }

    private void addTransaction(String message) {
        transactions.add(message);
    }
}