import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.net.URI;
import java.util.List;

public class DashboardPage extends JFrame {
    private String username;
    private String hospitalName;
    private String hospitalEmail; // ✅ This variable now exists

    // ✅ Constructor now accepts the hospitalEmail
    public DashboardPage(String username, String hospitalName, String hospitalEmail) {
        this.username = username;
        this.hospitalName = hospitalName;
        this.hospitalEmail = hospitalEmail; // ✅ And it's stored here

        setTitle("Dashboard - " + hospitalName);
        setSize(1200, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // 🌈 Main Background Panel with Gradient
        JPanel mainPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                GradientPaint gp = new GradientPaint(0, 0, new Color(255, 235, 235), getWidth(), getHeight(), Color.WHITE);
                g2d.setPaint(gp);
                g2d.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        mainPanel.setLayout(new BorderLayout());
        add(mainPanel);

        // 🧭 Left Sidebar (Navigation Panel)
        JPanel sidePanel = new JPanel();
        sidePanel.setBackground(new Color(178, 34, 34)); // deep red
        sidePanel.setPreferredSize(new Dimension(220, getHeight()));
        sidePanel.setLayout(new BoxLayout(sidePanel, BoxLayout.Y_AXIS));
        sidePanel.setBorder(BorderFactory.createEmptyBorder(30, 10, 30, 10));

        JLabel logoLabel = new JLabel("🏥 MEDFLY", SwingConstants.CENTER);
        logoLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        logoLabel.setFont(new Font("SansSerif", Font.BOLD, 18));
        logoLabel.setForeground(Color.WHITE);
        logoLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 40, 0));

        JButton dashboardBtn = createNavButton("🏠  Dashboard");

        // --- Orders Button Fix ---
        JButton ordersBtn = createNavButton("📦  Orders");
        ordersBtn.addActionListener(e -> {
            // ✅ Now passes the email
            new OrdersPage(username, hospitalName, hospitalEmail);
            dispose();
        });

        // --- Contact Button Fix ---
        JButton contactBtn = createNavButton("✉  Contact Admin");
        contactBtn.addActionListener(e -> {
            // ✅ Now passes the email
            new ContactAdminPage(username, hospitalName, hospitalEmail);
            dispose();
        });

        // Add event to Contact Admin
        contactBtn.addActionListener(e -> {
            try {
                Desktop.getDesktop().browse(new URI("mailto:admin_support@srmhospital.org"));
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(null, "Unable to open mail app!");
            }
        });

        sidePanel.add(logoLabel);
        sidePanel.add(Box.createRigidArea(new Dimension(0, 20)));
        sidePanel.add(dashboardBtn);
        sidePanel.add(Box.createRigidArea(new Dimension(0, 10)));
        sidePanel.add(ordersBtn);
        sidePanel.add(Box.createRigidArea(new Dimension(0, 10)));
        sidePanel.add(contactBtn);
        sidePanel.add(Box.createVerticalGlue());

        mainPanel.add(sidePanel, BorderLayout.WEST);

        // 🔝 Top Bar
        JPanel topBar = new JPanel(new BorderLayout());
        topBar.setBackground(new Color(255, 250, 250));
        topBar.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(200, 200, 200)));
        topBar.setPreferredSize(new Dimension(getWidth(), 60));

        JLabel titleLabel = new JLabel("Welcome, " + hospitalName);
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 17));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(0, 20, 0, 0));
        titleLabel.setForeground(new Color(120, 0, 0));

        JButton logoutButton = new JButton("Logout ⎋");
        logoutButton.setFont(new Font("SansSerif", Font.PLAIN, 13));
        logoutButton.setBackground(new Color(255, 240, 240));
        logoutButton.setForeground(new Color(178, 34, 34));
        logoutButton.setFocusPainted(false);
        logoutButton.setBorder(BorderFactory.createLineBorder(new Color(178, 34, 34), 1, true));
        logoutButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        logoutButton.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                logoutButton.setBackground(new Color(178, 34, 34));
                logoutButton.setForeground(Color.WHITE);
            }

            public void mouseExited(MouseEvent e) {
                logoutButton.setBackground(new Color(255, 240, 240));
                logoutButton.setForeground(new Color(178, 34, 34));
            }
        });

        topBar.add(titleLabel, BorderLayout.WEST);
        topBar.add(logoutButton, BorderLayout.EAST);
        mainPanel.add(topBar, BorderLayout.NORTH);

        // 🩸 Center Content (Main Dashboard area)
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setOpaque(false);
        contentPanel.setBorder(BorderFactory.createEmptyBorder(30, 40, 30, 40));

        JLabel title = new JLabel("🩺 Available Resources");
        title.setFont(new Font("SansSerif", Font.BOLD, 22));
        title.setForeground(new Color(178, 34, 34));
        title.setAlignmentX(Component.CENTER_ALIGNMENT);
        contentPanel.add(title);
        contentPanel.add(Box.createRigidArea(new Dimension(0, 20)));

        // Blood Section
        JLabel bloodLabel = new JLabel("🩸 Blood Types:");
        bloodLabel.setFont(new Font("SansSerif", Font.BOLD, 16));
        contentPanel.add(bloodLabel);

        List<String> bloodItems = BloodInventory.getAllItems();
        for (String item : bloodItems) {
            JLabel lbl = new JLabel("   ➤  " + item);
            lbl.setFont(new Font("Monospaced", Font.PLAIN, 14));
            contentPanel.add(lbl);
        }

        contentPanel.add(Box.createRigidArea(new Dimension(0, 20)));

        // Organ Section
        JLabel organLabel = new JLabel("❤️ Organs for Transplant:");
        organLabel.setFont(new Font("SansSerif", Font.BOLD, 16));
        contentPanel.add(organLabel);

        List<String> organItems = OrganInventory.getAllItems();
        for (String item : organItems) {
            JLabel lbl = new JLabel("   ➤  " + item);
            lbl.setFont(new Font("Monospaced", Font.PLAIN, 14));
            contentPanel.add(lbl);
        }

        JScrollPane scrollPane = new JScrollPane(contentPanel);
        scrollPane.setOpaque(false);
        scrollPane.getViewport().setOpaque(false);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        mainPanel.add(scrollPane, BorderLayout.CENTER);

        // 🟢 Floating "Place Order" Button (bottom-right corner)
        JButton placeOrderBtn = new JButton("➕  Place Order");
        placeOrderBtn.setFont(new Font("SansSerif", Font.BOLD, 15));
        placeOrderBtn.setBackground(new Color(178, 34, 34));
        placeOrderBtn.setForeground(Color.WHITE);
        placeOrderBtn.setFocusPainted(false);
        placeOrderBtn.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        placeOrderBtn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        placeOrderBtn.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                placeOrderBtn.setBackground(Color.WHITE);
                placeOrderBtn.setForeground(new Color(178, 34, 34));
            }

            public void mouseExited(MouseEvent e) {
                placeOrderBtn.setBackground(new Color(178, 34, 34));
                placeOrderBtn.setForeground(Color.WHITE);
            }
        });

        // --- Place Order Button Fix ---
        placeOrderBtn.addActionListener(e -> {
            // ✅ Passes the real hospitalName and hospitalEmail
            new OrderPage(hospitalName, hospitalEmail);
            dispose();
        });

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.setOpaque(false);
        buttonPanel.add(placeOrderBtn);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        setVisible(true);
    }

    // 🔧 Helper method to create side buttons
    private JButton createNavButton(String text) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("SansSerif", Font.PLAIN, 14));
        btn.setFocusPainted(false);
        btn.setBackground(new Color(178, 34, 34));
        btn.setForeground(Color.WHITE);
        btn.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));
        btn.setAlignmentX(Component.CENTER_ALIGNMENT);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        btn.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                btn.setBackground(new Color(255, 240, 240));
                btn.setForeground(new Color(178, 34, 34));
            }

            public void mouseExited(MouseEvent e) {
                btn.setBackground(new Color(178, 34, 34));
                btn.setForeground(Color.WHITE);
            }
        });

        return btn;
    }
}