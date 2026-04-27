package util;

import model.Account;
import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class DataManager {
    // Saves the file in the user's home directory to avoid permission issues
    private static final String FILE_PATH = System.getProperty("user.home") + File.separator + "bank_data.ser";

    // Save all accounts to file
    public static void saveAccounts(Map<String, Account> accounts) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(FILE_PATH))) {
            oos.writeObject(accounts);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Load accounts from file
    @SuppressWarnings("unchecked")
    public static Map<String, Account> loadAccounts() {
        File file = new File(FILE_PATH);
        if (!file.exists()) {
            return new HashMap<>(); // Return empty map if no file exists
        }
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
            return (Map<String, Account>) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            return new HashMap<>();
        }
    }
}