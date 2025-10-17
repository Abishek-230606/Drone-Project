import javax.swing.*;
import java.awt.*;
import java.sql.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ConfirmationPage extends JFrame {
    private String hospitalName;
    private String item;
    private int quantity;

    public ConfirmationPage(String hospitalName, String item, int quantity) {
        this.hospitalName = hospitalName;
        this.item = item;
        this.quantity = quantity;

        setTitle("🚁 Drone Dispatch Confirmation");
        setSize(650, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // 🌈 Gradient Background
        JPanel bgPanel = new JPanel() {
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                GradientPaint gp = new GradientPaint(0, 0, new Color(255, 255, 255), 0, getHeight(), new Color(255, 230, 230));
                g2d.setPaint(gp);
                g2d.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        bgPanel.setLayout(new BorderLayout());
        add(bgPanel);

        // 🩺 Header
        JLabel header = new JLabel("🛫 Drone Delivery Confirmation", SwingConstants.CENTER);
        header.setFont(new Font("Segoe UI", Font.BOLD, 20));
        header.setForeground(new Color(178, 34, 34));
        header.setBorder(BorderFactory.createEmptyBorder(20, 0, 10, 0));
        bgPanel.add(header, BorderLayout.NORTH);

        // 🌍 Global Hospital (Origin)
        double globalLat = 12.823331;
        double globalLon = 80.047817;

        // 📍 Fetch hospital coordinates
        double destLat = 0, destLon = 0;
        try {
            Connection conn = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/dronedb", "root", "Sathishdhana#23"
            );
            String query = "SELECT latitude, longitude FROM registered_hospitals WHERE hospital_name=?";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1, hospitalName);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                destLat = rs.getDouble("latitude");
                destLon = rs.getDouble("longitude");
            } else {
                JOptionPane.showMessageDialog(this, "❌ Coordinates not found for " + hospitalName);
            }
            conn.close();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "❌ DB Error: " + e.getMessage());
        }

        // 🧮 Distance & Time
        double distanceKm = haversine(globalLat, globalLon, destLat, destLon);
        double droneSpeed = 75.0;
        int timeMinutes = (int) Math.round((distanceKm / droneSpeed) * 60);

        // 📝 Order Summary Card
        JPanel infoPanel = new JPanel(new GridLayout(6, 1, 5, 5));
        infoPanel.setOpaque(false);
        infoPanel.setBorder(BorderFactory.createEmptyBorder(20, 80, 20, 80));

        infoPanel.add(createLabel("🏥 Hospital: " + hospitalName));
        infoPanel.add(createLabel("📦 Item: " + item));
        infoPanel.add(createLabel("🔢 Quantity: " + quantity));
        infoPanel.add(createLabel("📍 Distance: " + String.format("%.2f", distanceKm) + " km"));
        infoPanel.add(createLabel("⏱ Estimated Delivery: " + timeMinutes + " minutes"));
        infoPanel.add(createLabel("🚀 Drone Base: SRM Global Hospital"));
        bgPanel.add(infoPanel, BorderLayout.CENTER);

        // 🟢 Progress Section
        JPanel footerPanel = new JPanel(new BorderLayout());
        footerPanel.setOpaque(false);
        footerPanel.setBorder(BorderFactory.createEmptyBorder(20, 40, 20, 40));

        JProgressBar progressBar = new JProgressBar(0, timeMinutes);
        progressBar.setValue(0);
        progressBar.setStringPainted(true);
        progressBar.setFont(new Font("Segoe UI", Font.BOLD, 13));
        progressBar.setForeground(new Color(178, 34, 34));
        footerPanel.add(progressBar, BorderLayout.CENTER);

        JLabel statusLabel = new JLabel("🕐 Waiting to dispatch...", SwingConstants.CENTER);
        statusLabel.setFont(new Font("Segoe UI", Font.ITALIC, 14));
        statusLabel.setForeground(Color.DARK_GRAY);
        footerPanel.add(statusLabel, BorderLayout.NORTH);

        JButton startButton = new JButton("🛫 Start Drone Delivery");
        startButton.setFont(new Font("Segoe UI", Font.BOLD, 15));
        startButton.setBackground(new Color(178, 34, 34));
        startButton.setForeground(Color.WHITE);
        startButton.setFocusPainted(false);
        startButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        startButton.setBorder(BorderFactory.createEmptyBorder(10, 25, 10, 25));
        footerPanel.add(startButton, BorderLayout.SOUTH);

        bgPanel.add(footerPanel, BorderLayout.SOUTH);

        // 🚁 Delivery Simulation
        startButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                startButton.setEnabled(false);
                statusLabel.setText("🚁 Drone is now flying to " + hospitalName + "...");

                Timer timer = new Timer(1000, new ActionListener() {
                    int elapsed = 0;

                    @Override
                    public void actionPerformed(ActionEvent e) {
                        elapsed++;
                        progressBar.setValue(elapsed);
                        progressBar.setString("Drone in transit: " + elapsed + " / " + timeMinutes + " min");

                        if (elapsed >= timeMinutes) {
                            ((Timer) e.getSource()).stop();
                            progressBar.setValue(timeMinutes);
                            progressBar.setString("✅ Arrived at destination");
                            statusLabel.setText("🎯 Drone reached " + hospitalName + " successfully!");
                            JOptionPane.showMessageDialog(null, "✅ Drone has reached the hospital!\nProceed to OTP Verification.", "Delivery Complete", JOptionPane.INFORMATION_MESSAGE);

                            new VerificationPage(hospitalName, item, quantity);
                            dispose();
                        }
                    }
                });
                timer.start();
            }
        });

        setVisible(true);
    }

    // Helper Label Creator
    private JLabel createLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        label.setForeground(Color.BLACK);
        return label;
    }

    // 🌍 Haversine Formula
    private double haversine(double lat1, double lon1, double lat2, double lon2) {
        final int R = 6371;
        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
                Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) *
                        Math.sin(dLon / 2) * Math.sin(dLon / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return R * c;
    }
}
