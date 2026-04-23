import javax.swing.*;

/**
 * Entry point for the Bank Management System
 */
public class Main {
    public static void main(String[] args) {
        // Set Look and Feel to system default for better UI
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        // Load bank data from serialized file
        Bank bank = Bank.loadBank();
        
        // Launch login screen
        SwingUtilities.invokeLater(() -> {
            new LoginUI(bank).setVisible(true);
        });
    }
}