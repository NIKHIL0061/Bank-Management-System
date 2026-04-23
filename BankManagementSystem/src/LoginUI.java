import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;

public class LoginUI extends JFrame {
    private Bank bank;
    private JTextField accountNumberField;
    private JPasswordField pinField;
    
    public LoginUI(Bank bank) {
        this.bank = bank;
        initializeUI();
    }
    
    private void initializeUI() {
        setTitle("Bank Management System - Login");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(450, 420);
        setLocationRelativeTo(null);
        setResizable(false);
        
        // Main panel with gradient background
        JPanel mainPanel = new JPanel(new GridBagLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
                GradientPaint gp = new GradientPaint(0, 0, new Color(33, 147, 176), 
                                                     0, getHeight(), new Color(109, 213, 237));
                g2d.setPaint(gp);
                g2d.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        mainPanel.setOpaque(false);
        mainPanel.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        // Title
        JLabel titleLabel = new JLabel("🏦 Bank Management System", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        titleLabel.setForeground(Color.WHITE);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        mainPanel.add(titleLabel, gbc);
        
        // Login panel (semi-transparent white)
        JPanel loginPanel = new JPanel(new GridBagLayout());
        loginPanel.setOpaque(false);
        loginPanel.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(new Color(255, 255, 255, 100), 1, true),
            BorderFactory.createEmptyBorder(25, 25, 25, 25)
        ));
        
        GridBagConstraints loginGbc = new GridBagConstraints();
        loginGbc.insets = new Insets(8, 8, 8, 8);
        loginGbc.fill = GridBagConstraints.HORIZONTAL;
        
        // Account Number
        JLabel accLabel = new JLabel("Account Number");
        accLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        accLabel.setForeground(Color.WHITE);
        loginGbc.gridx = 0;
        loginGbc.gridy = 0;
        loginPanel.add(accLabel, loginGbc);
        
        accountNumberField = new JTextField(15);
        accountNumberField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        accountNumberField.setBackground(new Color(255, 255, 255, 220));
        accountNumberField.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(new Color(100, 100, 100), 1),
            BorderFactory.createEmptyBorder(8, 8, 8, 8)
        ));
        loginGbc.gridx = 1;
        loginGbc.gridy = 0;
        loginPanel.add(accountNumberField, loginGbc);
        
        // PIN
        JLabel pinLabel = new JLabel("PIN (4-6 digits)");
        pinLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        pinLabel.setForeground(Color.WHITE);
        loginGbc.gridx = 0;
        loginGbc.gridy = 1;
        loginPanel.add(pinLabel, loginGbc);
        
        pinField = new JPasswordField(15);
        pinField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        pinField.setBackground(new Color(255, 255, 255, 220));
        pinField.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(new Color(100, 100, 100), 1),
            BorderFactory.createEmptyBorder(8, 8, 8, 8)
        ));
        loginGbc.gridx = 1;
        loginGbc.gridy = 1;
        loginPanel.add(pinField, loginGbc);
        
        // Buttons panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 0));
        buttonPanel.setOpaque(false);
        
        // Styled Login Button
        JButton loginBtn = createStyledButton("Login", new Color(46, 204, 113), new Color(39, 174, 96));
        // Styled Register Button
        JButton registerBtn = createStyledButton("Register", new Color(52, 152, 219), new Color(41, 128, 185));
        
        loginBtn.addActionListener(e -> performLogin());
        registerBtn.addActionListener(e -> openRegisterDialog());
        
        buttonPanel.add(loginBtn);
        buttonPanel.add(registerBtn);
        
        loginGbc.gridx = 0;
        loginGbc.gridy = 2;
        loginGbc.gridwidth = 2;
        loginGbc.anchor = GridBagConstraints.CENTER;
        loginPanel.add(buttonPanel, loginGbc);
        
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 2;
        mainPanel.add(loginPanel, gbc);
        
        setContentPane(mainPanel);
    }
    
    // Helper to create a modern button with gradient and hover effect
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
        
        // Hover effect
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
    
    private void performLogin() {
        try {
            long accNo = Long.parseLong(accountNumberField.getText().trim());
            String pin = new String(pinField.getPassword());
            Account account = bank.validateLogin(accNo, pin);
            if (account != null) {
                JOptionPane.showMessageDialog(this, 
                    "Welcome back, " + account.getHolderName() + "!",
                    "Login Successful", JOptionPane.INFORMATION_MESSAGE);
                new DashboardUI(bank, account).setVisible(true);
                dispose();
            } else {
                JOptionPane.showMessageDialog(this,
                    "Invalid Account Number or PIN!",
                    "Login Failed", JOptionPane.ERROR_MESSAGE);
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this,
                "Please enter a valid account number!",
                "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void openRegisterDialog() {
        RegisterDialog dialog = new RegisterDialog(this, bank);
        dialog.setVisible(true);
    }
}