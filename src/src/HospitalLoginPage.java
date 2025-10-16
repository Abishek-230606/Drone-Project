import javax.swing.*;
import java.awt.*;
import java.sql.*;

public class HospitalLoginPage extends JFrame {
    private String hospitalName;

    public HospitalLoginPage(String hospitalName) {
        this.hospitalName = hospitalName;
        setTitle("Login - " + hospitalName);
        setSize(400, 250);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new GridLayout(4, 1, 10, 10));

        JLabel title = new JLabel("Login for " + hospitalName);
        title.setHorizontalAlignment(SwingConstants.CENTER);
        title.setFont(new Font("Segoe UI", Font.BOLD, 16));
        add(title);

        JTextField hospitalIdField = new JTextField();
        hospitalIdField.setBorder(BorderFactory.createTitledBorder("Hospital ID"));
        add(hospitalIdField);

        JPasswordField passwordField = new JPasswordField();
        passwordField.setBorder(BorderFactory.createTitledBorder("Password"));
        add(passwordField);

        JButton loginButton = new JButton("Login");
        loginButton.addActionListener(e -> {
            String hospitalId = hospitalIdField.getText();
            String password = String.valueOf(passwordField.getPassword());

            try {
                Connection conn = DriverManager.getConnection(
                        "jdbc:mysql://localhost:3306/dronedb", "root", "Sathishdhana#23"
                );
                String query = "SELECT * FROM registered_hospitals WHERE hospital_id=? AND password=?";
                PreparedStatement stmt = conn.prepareStatement(query);
                stmt.setString(1, hospitalId);
                stmt.setString(2, password);

                ResultSet rs = stmt.executeQuery();
                if (rs.next()) {
                    new DashboardPage(hospitalId, hospitalName); // ✅ Launch dashboard
                    dispose();
                } else {
                    JOptionPane.showMessageDialog(this, "❌ Invalid credentials.");
                }

                conn.close();
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(this, "❌ Database error: " + ex.getMessage());
            }
        });
        add(loginButton);

        setVisible(true);
    }
}