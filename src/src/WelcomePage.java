import javax.swing.*;
import java.awt.*;

public class WelcomePage extends JFrame {
    public WelcomePage() {
        setTitle("  MEDFLY");
        setSize(1000, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // --- Background panel with red-white gradient ---
        JPanel backgroundPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                Color color1 = new Color(220, 20, 60);   // crimson red
                Color color2 = Color.WHITE;              // white
                GradientPaint gp = new GradientPaint(0, 0, color1, 0, getHeight(), color2);
                g2d.setPaint(gp);
                g2d.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        backgroundPanel.setLayout(new BorderLayout());
        add(backgroundPanel);

        // --- Title Label ---
        JLabel title = new JLabel("🚁 Welcome to MEDFLY");
        title.setFont(new Font("SansSerif", Font.BOLD, 20)); // modern clean font
        title.setForeground(Color.WHITE);
        title.setHorizontalAlignment(SwingConstants.CENTER);
        title.setBorder(BorderFactory.createEmptyBorder(20, 10, 20, 10));
        backgroundPanel.add(title, BorderLayout.NORTH);

        // --- Button Panel ---
        JPanel buttonPanel = new JPanel(new GridLayout(2, 1, 20, 20));
        buttonPanel.setOpaque(false);
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(50, 100, 50, 100));

        // --- Buttons ---
        JButton hospitalButton = new JButton("🏥  Registered Hospitals");
        JButton adminButton = new JButton("🔐  Admin Login");

        // Style buttons individually for different sizes
        styleButton(hospitalButton, new Color(255, 255, 255), new Color(178, 34, 34), 18, 4); // Bigger font
        styleButton(adminButton, new Color(255, 255, 255), new Color(178, 34, 34), 18, 2);   // Smaller font

        // Add actions (unchanged)
        hospitalButton.addActionListener(e -> {
            new HospitalListPage();
            dispose();
        });

        adminButton.addActionListener(e -> {
            new AdminLoginPage();
            dispose();
        });

        // Add buttons to panel
        buttonPanel.add(hospitalButton);
        buttonPanel.add(adminButton);

        backgroundPanel.add(buttonPanel, BorderLayout.CENTER);

        setVisible(true);
    }

    // --- Helper method to style buttons ---
    private void styleButton(JButton button, Color bg, Color fg, int fontSize, int borderThickness) {
        button.setFocusPainted(false);
        button.setFont(new Font("SansSerif", Font.BOLD, fontSize));
        button.setBackground(bg);
        button.setForeground(fg);
        button.setBorder(BorderFactory.createLineBorder(fg, borderThickness, true));
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        // Hover effect
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(fg);
                button.setForeground(Color.WHITE);
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(bg);
                button.setForeground(fg);
            }
        });
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(WelcomePage::new);
    }
}
