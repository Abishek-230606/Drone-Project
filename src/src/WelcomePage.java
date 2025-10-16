import javax.swing.*;
import java.awt.*;

public class WelcomePage extends JFrame {
    public WelcomePage() {
        setTitle("Drone-Assisted Medical Supplies");
        setSize(500, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        JLabel title = new JLabel("🚁 Welcome to Drone-Assisted Medical Supplies");
        title.setFont(new Font("Segoe UI", Font.BOLD, 16));
        title.setHorizontalAlignment(SwingConstants.CENTER);
        add(title, BorderLayout.NORTH);

        JPanel buttonPanel = new JPanel(new GridLayout(2, 1, 20, 20));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(50, 100, 50, 100));

        JButton hospitalButton = new JButton("Registered Hospitals");
        hospitalButton.addActionListener(e -> {
            new HospitalListPage();
            dispose();
        });

        JButton adminButton = new JButton("Admin Login");
        adminButton.addActionListener(e -> {
            new AdminLoginPage();
            dispose();
        });

        buttonPanel.add(hospitalButton);
        buttonPanel.add(adminButton);
        add(buttonPanel, BorderLayout.CENTER);

        setVisible(true);
    }

    public static void main(String[] args) {
        new WelcomePage();
    }
}