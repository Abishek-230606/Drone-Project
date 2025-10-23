import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;

public class OrdersPage extends JFrame {
    private String username;
    private String hospitalName;
    private String hospitalEmail; // ✅ FIX 1: Add variable to store email

    // ✅ FIX 2: Update constructor to accept the email
    public OrdersPage(String username, String hospitalName, String hospitalEmail) {
        this.username = username;
        this.hospitalName = hospitalName;
        this.hospitalEmail = hospitalEmail; // Store the email

        setTitle("Orders - " + hospitalName);
        setSize(900, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);
        add(panel);

        JLabel title = new JLabel("📦 Your Orders", SwingConstants.CENTER);
        title.setFont(new Font("Segoe UI", Font.BOLD, 22));
        title.setForeground(new Color(178, 34, 34));
        title.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        panel.add(title, BorderLayout.NORTH);

        // Table setup
        String[] columns = {"Order ID", "Item", "Quantity", "Status", "Order Time", "Delivery Time", "OTP"};
        DefaultTableModel model = new DefaultTableModel(columns, 0);
        JTable table = new JTable(model);
        table.setRowHeight(28);
        table.setFont(new Font("SansSerif", Font.PLAIN, 14));
        table.getTableHeader().setFont(new Font("SansSerif", Font.BOLD, 14));

        JScrollPane scrollPane = new JScrollPane(table);
        panel.add(scrollPane, BorderLayout.CENTER);

        // Fetch data
        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/dronedb", "root", "Sathishdhana#23")) {
            String sql = "SELECT * FROM orders WHERE hospital_name = ?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, hospitalName);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Object[] row = {
                        rs.getInt("id"),
                        rs.getString("item"),
                        rs.getInt("quantity"),
                        rs.getString("status"),
                        rs.getTimestamp("order_time"),
                        rs.getTimestamp("delivery_time"),
                        rs.getInt("otp_code")
                };
                model.addRow(row);
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "❌ Database Error: " + e.getMessage());
        }

        // Back Button
        JButton backButton = new JButton("⬅ Back to Dashboard");
        backButton.setFont(new Font("SansSerif", Font.BOLD, 14));
        backButton.setBackground(new Color(178, 34, 34));
        backButton.setForeground(Color.WHITE);
        backButton.setFocusPainted(false);

        // ✅ FIX 3: Pass all 3 variables back to the Dashboard
        backButton.addActionListener(e -> {
            new DashboardPage(username, hospitalName, hospitalEmail);
            dispose();
        });

        JPanel bottomPanel = new JPanel();
        bottomPanel.setBackground(Color.WHITE);
        bottomPanel.add(backButton);
        panel.add(bottomPanel, BorderLayout.SOUTH);

        setVisible(true);
    }
}