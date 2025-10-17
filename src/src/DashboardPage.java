import javax.swing.*;
import java.awt.*;
import java.util.List;

public class DashboardPage extends JFrame {
    private String username;
    private String hospitalName;

    public DashboardPage(String username, String hospitalName) {
        this.username = username;
        this.hospitalName = hospitalName;

        setTitle("Dashboard - " + hospitalName);
        setSize(700, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // 🌈 Gradient background
        JPanel mainPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                GradientPaint gp = new GradientPaint(0, 0, new Color(255, 240, 240), 0, getHeight(), Color.WHITE);
                g2d.setPaint(gp);
                g2d.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        mainPanel.setLayout(new BorderLayout());
        add(mainPanel);

        // 🔴 Header
        JPanel headerPanel = new JPanel(new GridLayout(3, 1));
        headerPanel.setBackground(new Color(178, 34, 34)); // deep red
        headerPanel.setBorder(BorderFactory.createEmptyBorder(15, 10, 15, 10));

        JLabel welcomeLabel = new JLabel("  Welcome, " + hospitalName, SwingConstants.CENTER);
        welcomeLabel.setFont(new Font("SansSerif", Font.BOLD, 18));
        welcomeLabel.setForeground(Color.WHITE);

        JLabel hubLabel = new JLabel("📍 Pickup Hub: SRM Global Hospital", SwingConstants.CENTER);
        hubLabel.setFont(new Font("SansSerif", Font.PLAIN, 14));
        hubLabel.setForeground(Color.WHITE);

        JLabel addressLabel = new JLabel("☎  Mahatma Gandhi Rd, Potheri | Kattankulathur, Tamil Nadu 603203 | Emergency: 044 4743 2350", SwingConstants.CENTER);
        addressLabel.setFont(new Font("SansSerif", Font.PLAIN, 12));
        addressLabel.setForeground(Color.WHITE);

        headerPanel.add(welcomeLabel);
        headerPanel.add(hubLabel);
        headerPanel.add(addressLabel);
        mainPanel.add(headerPanel, BorderLayout.NORTH);

        // 🩸 Center Panel (Data Section)
        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
        centerPanel.setOpaque(false);
        centerPanel.setBorder(BorderFactory.createEmptyBorder(20, 40, 20, 40));

        JLabel resourceTitle = new JLabel("Available Resources");
        resourceTitle.setFont(new Font("SansSerif", Font.BOLD, 16));
        resourceTitle.setForeground(new Color(178, 34, 34));
        resourceTitle.setAlignmentX(Component.CENTER_ALIGNMENT);
        centerPanel.add(resourceTitle);
        centerPanel.add(Box.createRigidArea(new Dimension(0, 15)));

        List<String> bloodItems = BloodInventory.getAllItems();
        List<String> organItems = OrganInventory.getAllItems();

        // Blood section
        JLabel bloodLabel = new JLabel("🩸 Blood Types:");
        bloodLabel.setFont(new Font("SansSerif", Font.BOLD, 14));
        bloodLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        centerPanel.add(bloodLabel);

        for (String item : bloodItems) {
            JLabel lbl = new JLabel("   ➤  " + item);
            lbl.setFont(new Font("Monospaced", Font.PLAIN, 13));
            centerPanel.add(lbl);
        }

        centerPanel.add(Box.createRigidArea(new Dimension(0, 15)));

        // Organ section
        JLabel organLabel = new JLabel("❤️ Organs for Transplant:");
        organLabel.setFont(new Font("SansSerif", Font.BOLD, 14));
        organLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        centerPanel.add(organLabel);

        for (String item : organItems) {
            JLabel lbl = new JLabel("   ➤  " + item);
            lbl.setFont(new Font("Monospaced", Font.PLAIN, 13));
            centerPanel.add(lbl);
        }

        JScrollPane scrollPane = new JScrollPane(centerPanel);
        scrollPane.setOpaque(false);
        scrollPane.getViewport().setOpaque(false);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        mainPanel.add(scrollPane, BorderLayout.CENTER);

        // 🟢 Footer: Place Order button
        JPanel footerPanel = new JPanel();
        footerPanel.setOpaque(false);
        JButton orderButton = new JButton("➕  Place Order");
        orderButton.setFont(new Font("SansSerif", Font.BOLD, 15));
        orderButton.setBackground(new Color(178, 34, 34));
        orderButton.setForeground(Color.WHITE);
        orderButton.setFocusPainted(false);
        orderButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        orderButton.setBorder(BorderFactory.createEmptyBorder(10, 30, 10, 30));
        orderButton.setBorder(BorderFactory.createLineBorder(new Color(178, 34, 34), 2, true));

        // Hover effect
        orderButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                orderButton.setBackground(Color.WHITE);
                orderButton.setForeground(new Color(178, 34, 34));
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                orderButton.setBackground(new Color(178, 34, 34));
                orderButton.setForeground(Color.WHITE);
            }
        });

        orderButton.addActionListener(e -> {
            new OrderPage(username, hospitalName);
            dispose();
        });

        footerPanel.add(orderButton);
        mainPanel.add(footerPanel, BorderLayout.SOUTH);

        setVisible(true);
    }
}
