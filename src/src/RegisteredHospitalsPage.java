import javax.swing.*;
import java.awt.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class RegisteredHospitalsPage extends JFrame {
    private String username;

    public RegisteredHospitalsPage(String username) {
        this.username = username;
        setTitle("Registered Hospitals");
        setSize(600, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        JLabel titleLabel = new JLabel("🏥 Hospitals Tied to Our Network");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        add(titleLabel, BorderLayout.NORTH);

        // 🔍 Fetch hospital list
        List<String> hospitals = getHospitalNames();

        // 📋 Dropdown for selection
        JComboBox<String> hospitalDropdown = new JComboBox<>();
        for (String name : hospitals) {
            hospitalDropdown.addItem(name);
        }

        JPanel centerPanel = new JPanel(new GridLayout(2, 1, 10, 10));
        centerPanel.setBorder(BorderFactory.createEmptyBorder(20, 50, 20, 50));
        centerPanel.add(new JLabel("Select a hospital to place an order:"));
        centerPanel.add(hospitalDropdown);
        add(centerPanel, BorderLayout.CENTER);

        // 🔘 Continue button
        JButton continueButton = new JButton("Proceed to Order Page");
        continueButton.setFont(new Font("Segoe UI", Font.BOLD, 14));
        continueButton.addActionListener(e -> {
            String selectedHospital = (String) hospitalDropdown.getSelectedItem();
            new OrderPage(username, selectedHospital); // ✅ Pass hospital name
            dispose();
        });

        JPanel footerPanel = new JPanel();
        footerPanel.add(continueButton);
        add(footerPanel, BorderLayout.SOUTH);

        setVisible(true);
    }

    private List<String> getHospitalNames() {
        List<String> list = new ArrayList<>();
        try {
            Connection conn = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/dronedb", "root", "Sathishdhana#23"
            );
            PreparedStatement stmt = conn.prepareStatement("SELECT hospital_name FROM registered_hospitals");
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                list.add(rs.getString("hospital_name"));
            }

            conn.close();
        } catch (SQLException e) {
            list.add("❌ Error loading hospitals: " + e.getMessage());
        }

        return list;
    }
}