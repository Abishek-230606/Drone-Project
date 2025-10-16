import javax.swing.*;
import java.awt.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class HospitalListPage extends JFrame {
    public HospitalListPage() {
        setTitle("Registered Hospitals");
        setSize(500, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        JLabel title = new JLabel("🏥 Select Your Hospital");
        title.setFont(new Font("Segoe UI", Font.BOLD, 16));
        title.setHorizontalAlignment(SwingConstants.CENTER);
        add(title, BorderLayout.NORTH);

        List<String> hospitals = getHospitalNames();
        JPanel listPanel = new JPanel(new GridLayout(hospitals.size(), 1, 10, 10));
        listPanel.setBorder(BorderFactory.createEmptyBorder(20, 50, 20, 50));

        for (String name : hospitals) {
            JButton hospitalButton = new JButton(name);
            hospitalButton.addActionListener(e -> {
                new HospitalLoginPage(name); // Pass hospital name
                dispose();
            });
            listPanel.add(hospitalButton);
        }

        JScrollPane scrollPane = new JScrollPane(listPanel);
        add(scrollPane, BorderLayout.CENTER);
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