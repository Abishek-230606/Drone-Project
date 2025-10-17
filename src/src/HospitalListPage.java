import javax.swing.*;
import java.awt.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class HospitalListPage extends JFrame {
    public HospitalListPage() {
        setTitle("Registered Hospitals");
        setSize(1000, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // --- Gradient background panel ---
        JPanel backgroundPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                Color color1 = new Color(220, 20, 60); // crimson red
                Color color2 = Color.WHITE;
                GradientPaint gp = new GradientPaint(0, 0, color1, 0, getHeight(), color2);
                g2d.setPaint(gp);
                g2d.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        backgroundPanel.setLayout(new BorderLayout());
        add(backgroundPanel);

        // --- Title ---
        JLabel title = new JLabel("🏥  Select Your Hospital");
        title.setFont(new Font("SansSerif", Font.BOLD, 22));
        title.setForeground(Color.WHITE);
        title.setHorizontalAlignment(SwingConstants.CENTER);
        title.setBorder(BorderFactory.createEmptyBorder(20, 10, 20, 10));
        backgroundPanel.add(title, BorderLayout.NORTH);

        // --- Hospital List ---
        List<String> hospitals = getHospitalNames();

        JPanel listPanel = new JPanel(new GridLayout(hospitals.size(), 1, 15, 15));
        listPanel.setOpaque(false);
        listPanel.setBorder(BorderFactory.createEmptyBorder(20, 80, 20, 80));

        for (String name : hospitals) {
            JButton hospitalButton = new JButton(name);
            styleHospitalButton(hospitalButton);
            hospitalButton.addActionListener(e -> {
                new HospitalLoginPage(name); // Pass hospital name
                dispose();
            });
            listPanel.add(hospitalButton);
        }

        // --- Scrollable Area ---
        JScrollPane scrollPane = new JScrollPane(listPanel);
        scrollPane.setBorder(null);
        scrollPane.setOpaque(false);
        scrollPane.getViewport().setOpaque(false);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        backgroundPanel.add(scrollPane, BorderLayout.CENTER);

        setVisible(true);
    }

    // --- Button Styling Method ---
    private void styleHospitalButton(JButton button) {
        button.setFont(new Font("SansSerif", Font.BOLD, 16));
        button.setFocusPainted(false);
        button.setBackground(new Color(255, 255, 255, 230)); // semi-transparent white
        button.setForeground(new Color(178, 34, 34)); // red text
        button.setBorder(BorderFactory.createLineBorder(new Color(178, 34, 34), 2, true));
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        button.setPreferredSize(new Dimension(300, 40));
        button.setOpaque(true);

        // Hover effect
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(new Color(178, 34, 34));
                button.setForeground(Color.WHITE);
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(new Color(255, 255, 255, 230));
                button.setForeground(new Color(178, 34, 34));
            }
        });
    }

    // --- Database Method (Unchanged Logic) ---
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
