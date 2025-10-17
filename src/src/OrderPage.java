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
        setSize(550, 480);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // 🌈 Gradient background
        JPanel mainPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                GradientPaint gp = new GradientPaint(0, 0, Color.WHITE, 0, getHeight(), new Color(255, 240, 240));
                g2d.setPaint(gp);
                g2d.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        mainPanel.setLayout(new BorderLayout());
        add(mainPanel);

        // 🔴 Header
        JLabel titleLabel = new JLabel("🩺 Place Medical Supply Order", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 20));
        titleLabel.setForeground(new Color(178, 34, 34));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(20, 0, 10, 0));
        mainPanel.add(titleLabel, BorderLayout.NORTH);

        // ⚙️ Center Form Panel
        JPanel formPanel = new JPanel(new GridLayout(6, 1, 10, 10));
        formPanel.setOpaque(false);
        formPanel.setBorder(BorderFactory.createEmptyBorder(30, 80, 30, 80));

        JLabel bloodLabel = new JLabel("🩸 Select Blood Type:");
        bloodLabel.setFont(new Font("SansSerif", Font.PLAIN, 14));
        formPanel.add(bloodLabel);

        JComboBox<String> bloodDropdown = new JComboBox<>(new String[]{"None", "A+", "A-", "B+", "B-", "AB+", "AB-", "O+", "O-"});
        bloodDropdown.setFont(new Font("SansSerif", Font.PLAIN, 14));
        formPanel.add(bloodDropdown);

        JLabel organLabel = new JLabel("🫀 Select Organ Type:");
        organLabel.setFont(new Font("SansSerif", Font.PLAIN, 14));
        formPanel.add(organLabel);

        JComboBox<String> organDropdown = new JComboBox<>(new String[]{"None", "Heart", "Kidney", "Liver", "Lungs", "Cornea", "Pancreas"});
        organDropdown.setFont(new Font("SansSerif", Font.PLAIN, 14));
        formPanel.add(organDropdown);

        JLabel qtyLabel = new JLabel("📦 Enter Quantity:");
        qtyLabel.setFont(new Font("SansSerif", Font.PLAIN, 14));
        formPanel.add(qtyLabel);

        JTextField quantityField = new JTextField();
        quantityField.setFont(new Font("SansSerif", Font.PLAIN, 14));
        quantityField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(178, 34, 34), 1),
                BorderFactory.createEmptyBorder(8, 8, 8, 8)
        ));
        formPanel.add(quantityField);

        mainPanel.add(formPanel, BorderLayout.CENTER);

        // 🟢 Confirm Button
        JButton confirmButton = new JButton("🚀 Confirm Order");
        confirmButton.setFont(new Font("SansSerif", Font.BOLD, 15));
        confirmButton.setBackground(new Color(178, 34, 34));
        confirmButton.setForeground(Color.WHITE);
        confirmButton.setFocusPainted(false);
        confirmButton.setBorder(BorderFactory.createEmptyBorder(10, 25, 10, 25));
        confirmButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        // Hover effect
        confirmButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                confirmButton.setBackground(Color.WHITE);
                confirmButton.setForeground(new Color(178, 34, 34));
                confirmButton.setBorder(BorderFactory.createLineBorder(new Color(178, 34, 34), 2));
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                confirmButton.setBackground(new Color(178, 34, 34));
                confirmButton.setForeground(Color.WHITE);
                confirmButton.setBorder(BorderFactory.createEmptyBorder(10, 25, 10, 25));
            }
        });

        JPanel footerPanel = new JPanel();
        footerPanel.setOpaque(false);
        footerPanel.add(confirmButton);
        mainPanel.add(footerPanel, BorderLayout.SOUTH);

        // ✅ Confirm Button Action
        confirmButton.addActionListener(e -> {
            String selectedBlood = (String) bloodDropdown.getSelectedItem();
            String selectedOrgan = (String) organDropdown.getSelectedItem();
            String qtyText = quantityField.getText().trim();

            if (qtyText.isEmpty() || !qtyText.matches("\\d+")) {
                JOptionPane.showMessageDialog(this, "⚠ Please enter a valid numeric quantity.", "Input Error", JOptionPane.WARNING_MESSAGE);
                return;
            }

            if (selectedBlood.equals("None") && selectedOrgan.equals("None")) {
                JOptionPane.showMessageDialog(this, "⚠ Please select either a Blood Type or an Organ Type.", "Selection Error", JOptionPane.WARNING_MESSAGE);
                return;
            }

            String item;
            boolean isBlood;

            if (!selectedBlood.equals("None") && selectedOrgan.equals("None")) {
                item = selectedBlood;
                isBlood = true;
            } else if (selectedBlood.equals("None") && !selectedOrgan.equals("None")) {
                item = selectedOrgan;
                isBlood = false;
            } else {
                JOptionPane.showMessageDialog(this, "⚠ Please select only one option (Blood OR Organ).", "Invalid Selection", JOptionPane.WARNING_MESSAGE);
                return;
            }

            int quantity = Integer.parseInt(qtyText);
            int otp = 1000 + new Random().nextInt(9000);

            String inventoryTable = isBlood ? "blood_inventory" : "organ_inventory";
            String quantityColumn = isBlood ? "quantity_units" : "quantity";
            String itemColumn = isBlood ? "blood_type" : "organ_type";

            try {
                Connection conn = DriverManager.getConnection(
                        "jdbc:mysql://localhost:3306/dronedb", "root", "Sathishdhana#23"
                );

                // Check stock
                String checkQuery = "SELECT " + quantityColumn + " FROM " + inventoryTable + " WHERE " + itemColumn + " = ?";
                PreparedStatement checkStmt = conn.prepareStatement(checkQuery);
                checkStmt.setString(1, item);
                ResultSet rs = checkStmt.executeQuery();

                if (rs.next()) {
                    int available = rs.getInt(quantityColumn);
                    if (quantity > available) {
                        JOptionPane.showMessageDialog(this,
                                "⚠ Not enough stock.\nAvailable: " + available + " units.",
                                "Stock Alert", JOptionPane.WARNING_MESSAGE);
                        conn.close();
                        return;
                    }
                } else {
                    JOptionPane.showMessageDialog(this, "❌ Item not found in " + inventoryTable + ".", "Error", JOptionPane.ERROR_MESSAGE);
                    conn.close();
                    return;
                }

                // Insert order
                String insertQuery = "INSERT INTO orders (hospital_name, item, quantity, status, otp_code) VALUES (?, ?, ?, ?, ?)";
                PreparedStatement insertStmt = conn.prepareStatement(insertQuery);
                insertStmt.setString(1, hospitalName);
                insertStmt.setString(2, item);
                insertStmt.setInt(3, quantity);
                insertStmt.setString(4, "Pending");
                insertStmt.setInt(5, otp);
                int rows = insertStmt.executeUpdate();

                if (rows > 0) {
                    // Update inventory
                    String updateQuery = "UPDATE " + inventoryTable + " SET " + quantityColumn + " = " + quantityColumn + " - ? WHERE " + itemColumn + " = ?";
                    PreparedStatement updateStmt = conn.prepareStatement(updateQuery);
                    updateStmt.setInt(1, quantity);
                    updateStmt.setString(2, item);
                    updateStmt.executeUpdate();

                    JOptionPane.showMessageDialog(this,
                            "✅ Order Placed Successfully!\n\n" +
                                    "Item: " + item + "\nQuantity: " + quantity + "\nStatus: Pending\n" +
                                    "Delivery Code (OTP): " + otp,
                            "Order Success", JOptionPane.INFORMATION_MESSAGE);

                    new ConfirmationPage(hospitalName, item, quantity);
                    dispose();
                } else {
                    JOptionPane.showMessageDialog(this, "❌ Failed to register order.", "Database Error", JOptionPane.ERROR_MESSAGE);
                }

                conn.close();

            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(this, "❌ Database error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        setVisible(true);
    }
}
