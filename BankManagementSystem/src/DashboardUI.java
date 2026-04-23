import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;
import java.util.List;

public class DashboardUI extends JFrame {
    private Bank bank;
    private Account account;
    private JLabel balanceLabel;
    
    public DashboardUI(Bank bank, Account account) {
        this.bank = bank;
        this.account = account;
        initializeUI();
    }
    
    private void initializeUI() {
        setTitle("Bank Management System - Dashboard");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(900, 600);
        setLocationRelativeTo(null);
        setMinimumSize(new Dimension(800, 500));
        
        // Main gradient background
        JPanel mainPanel = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
                GradientPaint gp = new GradientPaint(0, 0, new Color(44, 62, 80), 
                                                     0, getHeight(), new Color(52, 73, 94));
                g2d.setPaint(gp);
                g2d.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        mainPanel.setOpaque(false);
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        // Header Panel (with logout button clearly visible)
        JPanel headerPanel = createHeaderPanel();
        mainPanel.add(headerPanel, BorderLayout.NORTH);
        
        // Center Panel
        JPanel centerPanel = createCenterPanel();
        mainPanel.add(centerPanel, BorderLayout.CENTER);
        
        // Footer
        JPanel footerPanel = createFooterPanel();
        mainPanel.add(footerPanel, BorderLayout.SOUTH);
        
        setContentPane(mainPanel);
        refreshBalanceDisplay();
    }
    
    private JPanel createHeaderPanel() {
        JPanel header = new JPanel(new BorderLayout());
        header.setOpaque(false);
        header.setBorder(BorderFactory.createEmptyBorder(10, 20, 20, 20));
        
        // Left side - welcome info
        JLabel welcomeLabel = new JLabel("Welcome, " + account.getHolderName() + "!");
        welcomeLabel.setFont(new Font("Segoe UI", Font.BOLD, 22));
        welcomeLabel.setForeground(Color.WHITE);
        
        JLabel accountLabel = new JLabel("Account: " + account.getAccountNumber());
        accountLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        accountLabel.setForeground(new Color(200, 200, 200));
        
        JPanel leftPanel = new JPanel(new GridLayout(2, 1));
        leftPanel.setOpaque(false);
        leftPanel.add(welcomeLabel);
        leftPanel.add(accountLabel);
        
        // Right side - logout button (clearly visible)
        JButton logoutBtn = new JButton(" LOGOUT ");
        logoutBtn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        logoutBtn.setBackground(new Color(231, 76, 60));
        logoutBtn.setForeground(Color.WHITE);
        logoutBtn.setFocusPainted(false);
        logoutBtn.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        logoutBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        // Add hover effect
        logoutBtn.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                logoutBtn.setBackground(new Color(192, 57, 43));
            }
            public void mouseExited(MouseEvent e) {
                logoutBtn.setBackground(new Color(231, 76, 60));
            }
        });
        
        logoutBtn.addActionListener(e -> logout());
        
        JPanel rightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        rightPanel.setOpaque(false);
        rightPanel.add(logoutBtn);
        
        header.add(leftPanel, BorderLayout.WEST);
        header.add(rightPanel, BorderLayout.EAST);
        
        return header;
    }
    
    private JPanel createCenterPanel() {
        JPanel center = new JPanel(new GridBagLayout());
        center.setOpaque(false);
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        
        // Balance Card
        JPanel balanceCard = createCardPanel("💰 Current Balance");
        balanceLabel = new JLabel("₹0.00");
        balanceLabel.setFont(new Font("Segoe UI", Font.BOLD, 36));
        balanceLabel.setForeground(new Color(46, 204, 113));
        balanceLabel.setHorizontalAlignment(SwingConstants.CENTER);
        balanceCard.add(balanceLabel, BorderLayout.CENTER);
        
        // Action Buttons with modern gradient styling
        JPanel buttonsPanel = new JPanel(new GridLayout(2, 2, 15, 15));
        buttonsPanel.setOpaque(false);
        
        JButton depositBtn = createActionButton("💰 Deposit Money", new Color(46, 204, 113));
        JButton withdrawBtn = createActionButton("💸 Withdraw Money", new Color(231, 76, 60));
        JButton balanceBtn = createActionButton("📊 Check Balance", new Color(52, 152, 219));
        JButton historyBtn = createActionButton("📜 Transaction History", new Color(155, 89, 182));
        
        depositBtn.addActionListener(e -> depositMoney());
        withdrawBtn.addActionListener(e -> withdrawMoney());
        balanceBtn.addActionListener(e -> showBalance());
        historyBtn.addActionListener(e -> showTransactionHistory());
        
        buttonsPanel.add(depositBtn);
        buttonsPanel.add(withdrawBtn);
        buttonsPanel.add(balanceBtn);
        buttonsPanel.add(historyBtn);
        
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        center.add(balanceCard, gbc);
        
        gbc.gridy = 1;
        gbc.fill = GridBagConstraints.BOTH;
        center.add(buttonsPanel, gbc);
        
        return center;
    }
    
    private JPanel createCardPanel(String title) {
        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(new Color(255, 255, 255, 240));
        card.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(new Color(200, 200, 200), 1, true),
            BorderFactory.createEmptyBorder(20, 20, 20, 20)
        ));
        
        JLabel titleLabel = new JLabel(title, SwingConstants.CENTER);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        titleLabel.setForeground(new Color(44, 62, 80));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 15, 0));
        card.add(titleLabel, BorderLayout.NORTH);
        
        return card;
    }
    
    // Modern gradient action button
    private JButton createActionButton(String text, Color baseColor) {
        Color darker = baseColor.darker();
        JButton button = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                GradientPaint gp = new GradientPaint(0, 0, baseColor, getWidth(), getHeight(), darker);
                g2.setPaint(gp);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 15, 15);
                g2.dispose();
                super.paintComponent(g);
            }
        };
        button.setFont(new Font("Segoe UI", Font.BOLD, 14));
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));
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
    
    private JPanel createFooterPanel() {
        JPanel footer = new JPanel();
        footer.setOpaque(false);
        JLabel footerLabel = new JLabel("© 2024 Bank Management System | Secure Banking");
        footerLabel.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        footerLabel.setForeground(new Color(180, 180, 180));
        footer.add(footerLabel);
        return footer;
    }
    
    private void refreshBalanceDisplay() {
        double balance = bank.getBalance(account.getAccountNumber());
        balanceLabel.setText(String.format("₹%.2f", balance));
    }
    
    private void depositMoney() {
        String input = JOptionPane.showInputDialog(this, "Enter amount to deposit:", "Deposit Money", JOptionPane.QUESTION_MESSAGE);
        if (input != null && !input.trim().isEmpty()) {
            try {
                double amount = Double.parseDouble(input.trim());
                if (amount <= 0) {
                    JOptionPane.showMessageDialog(this, "Amount must be greater than zero!", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                String error = bank.deposit(account.getAccountNumber(), amount);
                if (error == null) {
                    refreshBalanceDisplay();
                    JOptionPane.showMessageDialog(this, "✅ Successfully deposited ₹" + String.format("%.2f", amount), "Success", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(this, error, "Error", JOptionPane.ERROR_MESSAGE);
                }
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this, "Please enter a valid amount!", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    private void withdrawMoney() {
        String input = JOptionPane.showInputDialog(this, "Enter amount to withdraw:", "Withdraw Money", JOptionPane.QUESTION_MESSAGE);
        if (input != null && !input.trim().isEmpty()) {
            try {
                double amount = Double.parseDouble(input.trim());
                if (amount <= 0) {
                    JOptionPane.showMessageDialog(this, "Amount must be greater than zero!", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                String error = bank.withdraw(account.getAccountNumber(), amount);
                if (error == null) {
                    refreshBalanceDisplay();
                    JOptionPane.showMessageDialog(this, "✅ Successfully withdrew ₹" + String.format("%.2f", amount), "Success", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(this, error, "Error", JOptionPane.ERROR_MESSAGE);
                }
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this, "Please enter a valid amount!", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    private void showBalance() {
        double balance = bank.getBalance(account.getAccountNumber());
        JOptionPane.showMessageDialog(this,
            "🏦 Account Balance\n\nAccount Holder: " + account.getHolderName() + "\nAccount Number: " + account.getAccountNumber() + "\nCurrent Balance: ₹" + String.format("%.2f", balance),
            "Balance Information", JOptionPane.INFORMATION_MESSAGE);
    }
    
    private void showTransactionHistory() {
        List<Transaction> transactions = bank.getTransactionHistory(account.getAccountNumber());
        if (transactions.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No transactions found for this account.", "Transaction History", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        String[] columns = {"Date & Time", "Type", "Amount (₹)", "Balance After (₹)"};
        Object[][] data = new Object[transactions.size()][4];
        for (int i = 0; i < transactions.size(); i++) {
            Transaction t = transactions.get(i);
            data[i][0] = t.getFormattedTimestamp();
            data[i][1] = t.getType();
            data[i][2] = String.format("%.2f", t.getAmount());
            data[i][3] = String.format("%.2f", t.getBalanceAfter());
        }
        JTable table = new JTable(data, columns);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        table.setRowHeight(25);
        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 12));
        table.getTableHeader().setBackground(new Color(52, 152, 219));
        table.getTableHeader().setForeground(Color.WHITE);
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setPreferredSize(new Dimension(700, 400));
        JDialog historyDialog = new JDialog(this, "Transaction History", true);
        historyDialog.setLayout(new BorderLayout());
        historyDialog.add(scrollPane, BorderLayout.CENTER);
        JButton closeBtn = new JButton("Close");
        closeBtn.addActionListener(e -> historyDialog.dispose());
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(closeBtn);
        historyDialog.add(buttonPanel, BorderLayout.SOUTH);
        historyDialog.pack();
        historyDialog.setLocationRelativeTo(this);
        historyDialog.setVisible(true);
    }
    
    private void logout() {
        int confirm = JOptionPane.showConfirmDialog(this, "Are you sure you want to logout?", "Logout", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            dispose();
            new LoginUI(bank).setVisible(true);
        }
    }
}