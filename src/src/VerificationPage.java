import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.sql.*;

public class VerificationPage extends JFrame {
    private String hospitalName;
    private String item;
    private int quantity;
    private int storedOtp;

    public VerificationPage(String hospitalName, String item, int quantity, int otp) {
        this.hospitalName = hospitalName;
        this.item = item;
        this.quantity = quantity;
        this.storedOtp = otp;

        setTitle("🚁 Delivery Verification");
        setSize(450, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // 🔴 Red to White Gradient
        JPanel mainPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                GradientPaint gp = new GradientPaint(0, 0, new Color(178, 34, 34),
                        0, getHeight(), Color.WHITE);
                g2d.setPaint(gp);
                g2d.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        mainPanel.setLayout(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        add(mainPanel);

        // (We don't need to fetch from DB, the OTP is passed in)

        // 📝 Prompt Panel
        JPanel promptPanel = new JPanel();
        promptPanel.setOpaque(false);
        promptPanel.setLayout(new BoxLayout(promptPanel, BoxLayout.Y_AXIS));

        // ✅ FIX 1: Changed "4-digit" to "6-digit"
        JLabel prompt = new JLabel("<html><center>🚁 Package arrived at <b>" + hospitalName +
                "</b><br>Enter the 6-digit delivery code to confirm receipt</center></html>");
        prompt.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        prompt.setForeground(Color.WHITE);
        prompt.setAlignmentX(Component.CENTER_ALIGNMENT);
        promptPanel.add(prompt);
        promptPanel.add(Box.createRigidArea(new Dimension(0, 20)));

        // OTP Field (6-digit)
        JTextField otpField = new JTextField();
        otpField.setFont(new Font("Monospaced", Font.BOLD, 24));
        otpField.setHorizontalAlignment(SwingConstants.CENTER);
        // ✅ FIX 2: Made the field slightly wider for 6 digits
        otpField.setMaximumSize(new Dimension(150, 50));
        otpField.setBorder(BorderFactory.createLineBorder(Color.WHITE, 2, true));

        // ✅ FIX 3: Changed length limit from 4 to 6
        otpField.addKeyListener(new KeyAdapter() {
            public void keyTyped(KeyEvent e) {
                if (!Character.isDigit(e.getKeyChar()) || otpField.getText().length() >= 6) { // Changed 4 to 6
                    e.consume();
                }
            }
        });
        promptPanel.add(otpField);
        promptPanel.add(Box.createRigidArea(new Dimension(0, 20)));

        mainPanel.add(promptPanel, BorderLayout.CENTER);

        // ✅ Confirm Button
        JPanel footer = new JPanel();
        footer.setOpaque(false);
        JButton confirmButton = new JButton("✔ Confirm Receipt");
        confirmButton.setFont(new Font("Segoe UI", Font.BOLD, 16));
        confirmButton.setBackground(Color.WHITE);
        confirmButton.setForeground(new Color(178, 34, 34));
        confirmButton.setFocusPainted(false);
        confirmButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        confirmButton.setBorder(BorderFactory.createLineBorder(Color.WHITE, 2, true));
        confirmButton.setPreferredSize(new Dimension(180, 40));

        // Hover effect
        confirmButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                confirmButton.setBackground(new Color(178, 34, 34));
                confirmButton.setForeground(Color.WHITE);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                confirmButton.setBackground(Color.WHITE);
                confirmButton.setForeground(new Color(178, 34, 34));
            }
        });

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

        footer.add(confirmButton);
        mainPanel.add(footer, BorderLayout.SOUTH);

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