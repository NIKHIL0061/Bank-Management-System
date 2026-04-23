import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;

public class RegisterDialog extends JDialog {
    private JTextField nameField;
    private JTextField initialDepositField;
    private JPasswordField pinField;
    private JPasswordField confirmPinField;
    private Bank bank;
    
    public RegisterDialog(LoginUI parent, Bank bank) {
        super(parent, "Create New Account", true);
        this.bank = bank;
        initializeUI();
    }
    
    private void initializeUI() {
        setSize(450, 500);
        setLocationRelativeTo(getParent());
        setResizable(false);
        
        JPanel mainPanel = new JPanel(new GridBagLayout());
        mainPanel.setBackground(new Color(240, 248, 255));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(25, 25, 25, 25));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        // Title
        JLabel titleLabel = new JLabel("📝 Open New Account", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 22));
        titleLabel.setForeground(new Color(33, 147, 176));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        mainPanel.add(titleLabel, gbc);
        
        // Full Name
        JLabel nameLabel = new JLabel("Full Name:");
        nameLabel.setFont(new Font("Segoe UI", Font.BOLD, 13));
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        mainPanel.add(nameLabel, gbc);
        
        nameField = new JTextField(20);
        nameField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        nameField.setBorder(createTextFieldBorder());
        gbc.gridx = 1;
        gbc.gridy = 1;
        mainPanel.add(nameField, gbc);
        
        // Initial Deposit
        JLabel depositLabel = new JLabel("Initial Deposit (₹):");
        depositLabel.setFont(new Font("Segoe UI", Font.BOLD, 13));
        gbc.gridx = 0;
        gbc.gridy = 2;
        mainPanel.add(depositLabel, gbc);
        
        initialDepositField = new JTextField(20);
        initialDepositField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        initialDepositField.setBorder(createTextFieldBorder());
        gbc.gridx = 1;
        gbc.gridy = 2;
        mainPanel.add(initialDepositField, gbc);
        
        // PIN
        JLabel pinLabel = new JLabel("PIN (4-6 digits):");
        pinLabel.setFont(new Font("Segoe UI", Font.BOLD, 13));
        gbc.gridx = 0;
        gbc.gridy = 3;
        mainPanel.add(pinLabel, gbc);
        
        pinField = new JPasswordField(20);
        pinField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        pinField.setBorder(createTextFieldBorder());
        gbc.gridx = 1;
        gbc.gridy = 3;
        mainPanel.add(pinField, gbc);
        
        // Confirm PIN
        JLabel confirmLabel = new JLabel("Confirm PIN:");
        confirmLabel.setFont(new Font("Segoe UI", Font.BOLD, 13));
        gbc.gridx = 0;
        gbc.gridy = 4;
        mainPanel.add(confirmLabel, gbc);
        
        confirmPinField = new JPasswordField(20);
        confirmPinField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        confirmPinField.setBorder(createTextFieldBorder());
        gbc.gridx = 1;
        gbc.gridy = 4;
        mainPanel.add(confirmPinField, gbc);
        
        // Styled Create Account Button
        JButton createBtn = createStyledButton("Create Account", new Color(46, 204, 113), new Color(39, 174, 96));
        createBtn.addActionListener(e -> createAccount());
        
        JPanel buttonPanel = new JPanel();
        buttonPanel.setOpaque(false);
        buttonPanel.add(createBtn);
        
        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        mainPanel.add(buttonPanel, gbc);
        
        setContentPane(mainPanel);
    }
    
    private Border createTextFieldBorder() {
        return BorderFactory.createCompoundBorder(
            new LineBorder(new Color(200, 200, 200), 1),
            BorderFactory.createEmptyBorder(8, 8, 8, 8)
        );
    }
    
    private JButton createStyledButton(String text, Color startColor, Color endColor) {
        JButton button = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                GradientPaint gp = new GradientPaint(0, 0, startColor, getWidth(), 0, endColor);
                g2.setPaint(gp);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 10, 10);
                g2.dispose();
                super.paintComponent(g);
            }
        };
        button.setFont(new Font("Segoe UI", Font.BOLD, 14));
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(10, 25, 10, 25));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setContentAreaFilled(false);
        button.setOpaque(false);
        
        button.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                button.setForeground(new Color(255, 255, 200));
            }
            public void mouseExited(MouseEvent e) {
                button.setForeground(Color.WHITE);
            }
        });
        return button;
    }
    
    private void createAccount() {
        String name = nameField.getText().trim();
        String pin = new String(pinField.getPassword());
        String confirmPin = new String(confirmPinField.getPassword());
        String depositStr = initialDepositField.getText().trim();
        
        if (name.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter your full name!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        if (pin.isEmpty() || pin.length() < 4 || pin.length() > 6 || !pin.matches("\\d+")) {
            JOptionPane.showMessageDialog(this, "PIN must be 4-6 digits only!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        if (!pin.equals(confirmPin)) {
            JOptionPane.showMessageDialog(this, "PINs do not match!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        double initialDeposit = 0;
        if (!depositStr.isEmpty()) {
            try {
                initialDeposit = Double.parseDouble(depositStr);
                if (initialDeposit < 0) throw new NumberFormatException();
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this, "Please enter a valid deposit amount!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
        }
        long accountNumber = bank.createAccount(name, pin, initialDeposit);
        if (accountNumber != -1) {
            JOptionPane.showMessageDialog(this,
                "✅ Account Created Successfully!\n\nAccount Number: " + accountNumber + "\nHolder Name: " + name + "\nInitial Balance: ₹" + String.format("%.2f", initialDeposit),
                "Success", JOptionPane.INFORMATION_MESSAGE);
            dispose();
        } else {
            JOptionPane.showMessageDialog(this, "Account creation failed! Please check your details.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}