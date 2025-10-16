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

        setTitle("Order Confirmation");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // 🌍 Global Hospital Coordinates (Origin)
        double globalLat = 12.823331;
        double globalLon = 80.047817;

        // 📍 Fetch destination coordinates from DB
        double destLat = 0;
        double destLon = 0;

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

        // 🧮 Calculate Distance + Time
        double distanceKm = haversine(globalLat, globalLon, destLat, destLon);
        double droneSpeed = 50.0;
        int timeMinutes = (int) Math.round((distanceKm / droneSpeed) * 60);

        // 📝 Summary
        JTextArea summary = new JTextArea();
        summary.setEditable(false);
        summary.setFont(new Font("Monospaced", Font.PLAIN, 14));
        summary.setText(
                "✅ Order Confirmed\n\n" +
                        "Hospital: " + hospitalName + "\n" +
                        "Item: " + item + "\n" +
                        "Quantity: " + quantity + "\n\n" +
                        "📍 Distance: " + String.format("%.2f", distanceKm) + " km\n" +
                        "🚁 Estimated Delivery Time: " + timeMinutes + " minutes\n" +
                        "🛫 Drone dispatched from SRM Global Hospital"
        );

        JScrollPane scrollPane = new JScrollPane(summary);
        add(scrollPane, BorderLayout.CENTER);

        // 🔄 Emulated Progress Bar
        JProgressBar progressBar = new JProgressBar(0, timeMinutes);
        progressBar.setStringPainted(true);
        progressBar.setValue(0);

        Timer timer = new Timer(1000, new ActionListener() {
            int elapsed = 0;

            @Override
            public void actionPerformed(ActionEvent e) {
                elapsed++;
                progressBar.setValue(elapsed);
                progressBar.setString("Drone in transit: " + elapsed + "/" + timeMinutes + " min");

                if (elapsed >= timeMinutes) {
                    ((Timer) e.getSource()).stop();
                    progressBar.setString("✅ Arrived at destination");

                    // 🚪 Move to OTP verification
                    new VerificationPage(hospitalName, item, quantity);
                    dispose();
                }
            }
        });
        timer.start();

        JPanel footerPanel = new JPanel(new BorderLayout());
        footerPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        footerPanel.add(progressBar, BorderLayout.CENTER);

        add(footerPanel, BorderLayout.SOUTH);
        setVisible(true);
    }

    // 🌍 Haversine Formula
    private double haversine(double lat1, double lon1, double lat2, double lon2) {
        final int R = 6371; // Earth radius in km
        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
                Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) *
                        Math.sin(dLon / 2) * Math.sin(dLon / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return R * c;
    }
}