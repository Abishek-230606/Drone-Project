import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class SignupPage extends JFrame implements ActionListener {
    JTextField usernameField;
    JPasswordField passwordField;
    JButton registerButton;

    public SignupPage() {
        setTitle("Create New Account");
        setSize(350, 200);
        setLocationRelativeTo(null);
        setLayout(new GridLayout(3, 2, 10, 10));

        add(new JLabel("New Username:"));
        usernameField = new JTextField();
        add(usernameField);

        add(new JLabel("New Password:"));
        passwordField = new JPasswordField();
        add(passwordField);

        registerButton = new JButton("Register");
        registerButton.setBackground(new Color(255, 140, 0)); // dark orange
        registerButton.setForeground(Color.WHITE);
        registerButton.addActionListener(this);
        add(new JLabel()); // empty cell
        add(registerButton);

        setVisible(true);
    }

    public void actionPerformed(ActionEvent e) {
        String username = usernameField.getText();
        String password = String.valueOf(passwordField.getPassword());

        try {
            Connection conn = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/dronedb", "root", "Sathishdhana#23"
            );

            String query = "INSERT INTO users (username, password) VALUES (?, ?)";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1, username);
            stmt.setString(2, password);

            stmt.executeUpdate();
            JOptionPane.showMessageDialog(this, "✅ Account created successfully!");
            conn.close();
            dispose(); // close signup window
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "❌ Error: " + ex.getMessage());
        }
    }
}