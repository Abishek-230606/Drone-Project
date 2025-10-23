import javax.swing.*;
import java.awt.*;
import java.sql.*;
import java.util.Properties;
import java.util.Random;
import jakarta.mail.*;
import jakarta.mail.internet.*;

public class OrderPage extends JFrame {
    private String hospitalName;
    private String hospitalEmail; // ✅ Email for sending order OTP

    public OrderPage(String hospitalName, String hospitalEmail) {
        this.hospitalName = hospitalName;
        // ✅ This cleans the email from the database
        this.hospitalEmail = hospitalEmail.replaceAll("[\\s\\p{Cntrl}]+", "");

        setTitle("Place Order - " + hospitalName);
        setSize(550, 500);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // 🌈 Gradient Background
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

        // ⚙️ Form Panel
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

        // Hover Effect
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

        // 🔑 Confirm Button Action
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
            int otp = 100000 + new Random().nextInt(900000); // ✅ 6-digit OTP for order

            try (Connection conn = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/dronedb", "root", "Sathishdhana#23")) {

                // Insert order
                String insertQuery = "INSERT INTO orders (hospital_name, item, quantity, status, otp_code) VALUES (?, ?, ?, ?, ?)";
                PreparedStatement stmt = conn.prepareStatement(insertQuery);
                stmt.setString(1, hospitalName);
                stmt.setString(2, item);
                stmt.setInt(3, quantity);
                stmt.setString(4, "Pending");
                stmt.setInt(5, otp);
                int rows = stmt.executeUpdate();

                // Inside OrderPage.java, in the confirmButton listener...

                if (rows > 0) {
                    // Send OTP to hospital email
                    sendOrderOTP(hospitalEmail, item, quantity, otp);

                    JOptionPane.showMessageDialog(this,
                            "✅ Order Placed Successfully!\n" +
                                    "Item: " + item + "\nQuantity: " + quantity + "\nStatus: Pending\n" +
                                    "Delivery OTP sent to your email.",
                            "Order Success", JOptionPane.INFORMATION_MESSAGE);

                    // ❌ THIS WAS MY OLD, BAD SUGGESTION:
                    // new DispatchPage(String.valueOf(otp));

                    // ✅ THIS IS THE CORRECT FIX:
                    // Open the ConfirmationPage and pass it all the info
                    new ConfirmationPage(hospitalName, item, quantity, otp);
                    dispose(); // Close the OrderPage

                } else {
                    JOptionPane.showMessageDialog(this, "❌ Failed to register order.", "Database Error", JOptionPane.ERROR_MESSAGE);
                }

                // ✅ THIS IS THE FIX
            } catch (Exception ex) {

                // THIS IS THE NEW LINE:
                System.out.println("DEBUG: The bad email address is: >>>" + hospitalEmail + "<<<");

                ex.printStackTrace(); // This gives us more detail in the console

                JOptionPane.showMessageDialog(this, "❌ Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        setVisible(true);
    }

    // --- Send Order OTP ---
    private void sendOrderOTP(String recipientEmail, String item, int quantity, int otp) throws MessagingException {
        final String SENDER_EMAIL = "jsabishek77@gmail.com";
        final String SENDER_PASSWORD = "veig xhwe aaie rgxt";

        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");

        Session session = Session.getInstance(props, new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(SENDER_EMAIL, SENDER_PASSWORD);
            }
        });

        Message message = new MimeMessage(session);

        //
        // ✅ ✅ ✅ THIS IS THE FINAL FIX ✅ ✅ ✅
        // This cleans both emails of ALL hidden spaces and control characters
        //
        message.setFrom(new InternetAddress(SENDER_EMAIL.replaceAll("[\\s\\p{Cntrl}]+", "")));
        message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(recipientEmail.replaceAll("[\\s\\p{Cntrl}]+", "")));

        message.setSubject("Order OTP - Drone Medical System");
        message.setText("Dear " + hospitalName + ",\n\n" +
                "Your order has been placed successfully!\n\n" +
                "Item: " + item + "\nQuantity: " + quantity + "\nDelivery OTP: " + otp + "\n\n" +
                "This OTP is valid for verification at delivery.\n\n- Drone Medical System Team");

        Transport.send(message);
    }
}