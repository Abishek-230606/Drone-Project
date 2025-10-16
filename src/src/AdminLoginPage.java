import javax.swing.*;
import java.awt.*;

public class AdminLoginPage extends JFrame {
    public AdminLoginPage() {
        setTitle("Admin Login");
        setSize(400, 250);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        JLabel title = new JLabel("🔐 Admin Login");
        title.setFont(new Font("Segoe UI", Font.BOLD, 16));
        title.setHorizontalAlignment(SwingConstants.CENTER);
        add(title, BorderLayout.NORTH);

        // 🧾 Username and Password Fields
        JTextField usernameField = new JTextField();
        usernameField.setBorder(BorderFactory.createTitledBorder("Username"));

        JPasswordField passwordField = new JPasswordField();
        passwordField.setBorder(BorderFactory.createTitledBorder("Password"));

        JPanel centerPanel = new JPanel(new GridLayout(2, 1, 10, 10));
        centerPanel.setBorder(BorderFactory.createEmptyBorder(20, 50, 20, 50));
        centerPanel.add(usernameField);
        centerPanel.add(passwordField);
        add(centerPanel, BorderLayout.CENTER);

        // ✅ Login Button
        JButton loginButton = new JButton("Login");
        loginButton.setFont(new Font("Segoe UI", Font.BOLD, 14));
        loginButton.addActionListener(e -> {
            String username = usernameField.getText().trim();
            String password = new String(passwordField.getPassword()).trim();

            // 🔐 Simple hardcoded check (replace with DB check if needed)
            if (username.equals("admin") && password.equals("admin123")) {
                JOptionPane.showMessageDialog(this, "✅ Login successful!");
                new AdminDashboardPage(); // ✅ Correct
                dispose();
            } else {
                JOptionPane.showMessageDialog(this, "❌ Invalid credentials. Try again.");
            }
        });

        JPanel footerPanel = new JPanel();
        footerPanel.add(loginButton);
        add(footerPanel, BorderLayout.SOUTH);

        setVisible(true);
    }
}