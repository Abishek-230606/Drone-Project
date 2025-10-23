import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.net.URI;

public class ContactAdminPage extends JFrame {
    private String username;
    private String hospitalName;
    private String hospitalEmail; // ✅ FIX 1: Add variable to store email

    // ✅ FIX 2: Update constructor to accept the email
    public ContactAdminPage(String username, String hospitalName, String hospitalEmail) {
        this.username = username;
        this.hospitalName = hospitalName;
        this.hospitalEmail = hospitalEmail; // Store the email

        setTitle("Contact Admin - " + hospitalName);
        setSize(600, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createEmptyBorder(40, 50, 40, 50));

        JLabel title = new JLabel("💬 Contact Admin");
        title.setFont(new Font("Segoe UI", Font.BOLD, 22));
        title.setAlignmentX(Component.CENTER_ALIGNMENT);
        title.setForeground(new Color(178, 34, 34));
        panel.add(title);
        panel.add(Box.createRigidArea(new Dimension(0, 40)));

        JLabel emailLabel = new JLabel("📧 Email: as2008@srmist.edu.in");
        emailLabel.setFont(new Font("SansSerif", Font.PLAIN, 16));
        emailLabel.setForeground(Color.BLUE);
        emailLabel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        emailLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        emailLabel.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                try {
                    Desktop.getDesktop().browse(new URI("mailto:as2008@srmist.edu.in"));
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(null, "Unable to open mail client.");
                }
            }
        });
        panel.add(emailLabel);
        panel.add(Box.createRigidArea(new Dimension(0, 20)));

        JLabel phoneLabel = new JLabel("📞 Phone: +91 98765 43210");
        phoneLabel.setFont(new Font("SansSerif", Font.PLAIN, 16));
        phoneLabel.setForeground(new Color(100, 0, 0));
        phoneLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(phoneLabel);

        panel.add(Box.createVerticalGlue());

        JButton backBtn = new JButton("⬅ Back");
        backBtn.setBackground(new Color(178, 34, 34));
        backBtn.setForeground(Color.WHITE);
        backBtn.setFont(new Font("SansSerif", Font.BOLD, 14));
        backBtn.setFocusPainted(false);

        // ✅ FIX 3: Pass all 3 variables back to the Dashboard
        backBtn.addActionListener(e -> {
            new DashboardPage(username, hospitalName, hospitalEmail);
            dispose();
        });
        backBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(backBtn);

        add(panel);
        setVisible(true);
    }
}