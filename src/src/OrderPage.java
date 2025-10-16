import javax.swing.*;
import java.awt.*;
import java.sql.*;
import java.util.Random;

public class OrderPage extends JFrame {
    private String username;
    private String hospitalName;

    public OrderPage(String username, String hospitalName) {
        this.username = username;
        this.hospitalName = hospitalName;

        setTitle("Place Order - " + hospitalName);
        setSize(500, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        JLabel titleLabel = new JLabel("📝 Place Medical Supply Order");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        add(titleLabel, BorderLayout.NORTH);

        String[] resources = {"Blood - A+", "Blood - B+", "Blood - O-", "Kidney", "Liver", "Cornea"};
        JComboBox<String> resourceDropdown = new JComboBox<>(resources);

        JTextField quantityField = new JTextField();
        quantityField.setBorder(BorderFactory.createTitledBorder("Quantity"));

        JPanel centerPanel = new JPanel(new GridLayout(2, 1, 10, 10));
        centerPanel.setBorder(BorderFactory.createEmptyBorder(20, 50, 20, 50));
        centerPanel.add(resourceDropdown);
        centerPanel.add(quantityField);
        add(centerPanel, BorderLayout.CENTER);

        JButton confirmButton = new JButton("Confirm Order");
        confirmButton.addActionListener(e -> {
            String item = (String) resourceDropdown.getSelectedItem();
            String qtyText = quantityField.getText().trim();

            if (qtyText.isEmpty() || !qtyText.matches("\\d+")) {
                JOptionPane.showMessageDialog(this, "❌ Please enter a valid quantity.");
                return;
            }

            int quantity = Integer.parseInt(qtyText);
            int otp = 1000 + new Random().nextInt(9000);

            boolean isBlood = item.toLowerCase().contains("blood");
            String inventoryTable = isBlood ? "blood_inventory" : "organ_inventory";
            String quantityColumn = isBlood ? "quantity_units" : "quantity";
            String itemColumn = isBlood ? "blood_type" : "organ_type";

            try {
                Connection conn = DriverManager.getConnection(
                        "jdbc:mysql://localhost:3306/dronedb", "root", "Sathishdhana#23"
                );

                // 🔍 Check inventory
                String checkQuery = "SELECT " + quantityColumn + " FROM " + inventoryTable + " WHERE " + itemColumn + " = ?";
                PreparedStatement checkStmt = conn.prepareStatement(checkQuery);
                checkStmt.setString(1, item.replace("Blood - ", "").trim()); // remove prefix for blood
                ResultSet rs = checkStmt.executeQuery();

                if (rs.next()) {
                    int available = rs.getInt(quantityColumn);
                    if (quantity > available) {
                        JOptionPane.showMessageDialog(this, "❌ Not enough stock. Only " + available + " units available.");
                        conn.close();
                        return;
                    }
                } else {
                    JOptionPane.showMessageDialog(this, "❌ Item not found in " + inventoryTable + ".");
                    conn.close();
                    return;
                }

                // ✅ Insert order
                String insertQuery = "INSERT INTO orders (hospital_name, item, quantity, status, otp_code) VALUES (?, ?, ?, ?, ?)";
                PreparedStatement insertStmt = conn.prepareStatement(insertQuery);
                insertStmt.setString(1, hospitalName);
                insertStmt.setString(2, item);
                insertStmt.setInt(3, quantity);
                insertStmt.setString(4, "Pending");
                insertStmt.setInt(5, otp);

                int rows = insertStmt.executeUpdate();

                if (rows > 0) {
                    // 🔄 Update inventory
                    String updateQuery = "UPDATE " + inventoryTable + " SET " + quantityColumn + " = " + quantityColumn + " - ? WHERE " + itemColumn + " = ?";
                    PreparedStatement updateStmt = conn.prepareStatement(updateQuery);
                    updateStmt.setInt(1, quantity);
                    updateStmt.setString(2, item.replace("Blood - ", "").trim());
                    updateStmt.executeUpdate();

                    JOptionPane.showMessageDialog(this,
                            "✅ Order placed and registered!\n" +
                                    "Item: " + item + "\nQuantity: " + quantity + "\nStatus: Pending\n" +
                                    "Delivery Code (OTP): " + otp
                    );

                    new ConfirmationPage(hospitalName, item, quantity);
                    dispose();
                } else {
                    JOptionPane.showMessageDialog(this, "❌ Failed to register order.");
                }

                conn.close();

            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(this, "❌ Database error: " + ex.getMessage());
            }
        });

        JPanel footerPanel = new JPanel();
        footerPanel.add(confirmButton);
        add(footerPanel, BorderLayout.SOUTH);

        setVisible(true);
    }
}