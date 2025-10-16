import javax.swing.*;
import java.awt.*;
import java.sql.*;

public class VerificationPage extends JFrame {
    private String hospitalName;
    private String item;
    private int quantity;
    private int storedOtp;

    public VerificationPage(String hospitalName, String item, int quantity) {
        this.hospitalName = hospitalName;
        this.item = item;
        this.quantity = quantity;

        setTitle("Delivery Verification");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // 🔐 Fetch OTP from DB (latest pending order)
        try {
            Connection conn = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/dronedb", "root", "Sathishdhana#23"
            );
            String query = "SELECT otp_code FROM orders " +
                    "WHERE hospital_name=? AND item=? AND quantity=? AND status='Pending' " +
                    "ORDER BY order_time DESC LIMIT 1";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1, hospitalName);
            stmt.setString(2, item);
            stmt.setInt(3, quantity);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                storedOtp = rs.getInt("otp_code");
            } else {
                JOptionPane.showMessageDialog(this, "❌ No matching pending order found.");
                dispose();
                return;
            }

            conn.close();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "❌ DB Error: " + e.getMessage());
            dispose();
            return;
        }

        JLabel prompt = new JLabel("<html><center>🚁 Package arrived at " + hospitalName +
                "<br><br>Enter the 4-digit delivery code to confirm receipt</center></html>");
        prompt.setHorizontalAlignment(SwingConstants.CENTER);
        prompt.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        add(prompt, BorderLayout.NORTH);

        JTextField otpField = new JTextField();
        otpField.setBorder(BorderFactory.createTitledBorder("Enter Delivery Code"));
        add(otpField, BorderLayout.CENTER);

        JButton confirmButton = new JButton("Confirm Receipt");
        confirmButton.setFont(new Font("Segoe UI", Font.BOLD, 14));
        confirmButton.addActionListener(e -> {
            String entered = otpField.getText().trim();
            if (entered.equals(String.valueOf(storedOtp))) {
                markOrderAsDelivered();
                JOptionPane.showMessageDialog(this, "✅ Package verified and marked as Delivered!");
                dispose();
            } else {
                JOptionPane.showMessageDialog(this, "❌ Incorrect code. Please try again.");
            }
        });

        JPanel footer = new JPanel();
        footer.add(confirmButton);
        add(footer, BorderLayout.SOUTH);

        // 🧾 Show OTP for simulation (remove in production)
        JOptionPane.showMessageDialog(this, "Delivery Code: " + storedOtp);

        setVisible(true);
    }

    private void markOrderAsDelivered() {
        try {
            Connection conn = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/dronedb", "root", "Sathishdhana#23"
            );
            String query = "UPDATE orders SET status='Delivered', delivery_time=NOW() " +
                    "WHERE hospital_name=? AND item=? AND quantity=? AND otp_code=? AND status='Pending'";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1, hospitalName);
            stmt.setString(2, item);
            stmt.setInt(3, quantity);
            stmt.setInt(4, storedOtp);
            stmt.executeUpdate();
            conn.close();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "❌ DB Error: " + e.getMessage());
        }
    }
}