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
        setSize(650, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // 🔷 Header: Pickup Hub Info
        JPanel headerPanel = new JPanel(new GridLayout(3, 1));
        headerPanel.setBackground(new Color(240, 248, 255));
        headerPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JLabel welcomeLabel = new JLabel("Welcome, " + hospitalName);
        welcomeLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        welcomeLabel.setHorizontalAlignment(SwingConstants.CENTER);

        JLabel hubLabel = new JLabel("📍 Pickup Hub: SRM Global Hospital");
        hubLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        hubLabel.setHorizontalAlignment(SwingConstants.CENTER);

        JLabel addressLabel = new JLabel("Address: Mahatma Gandhi Rd, Potheri, SRM Nagar, Kattankulathur, Tamil Nadu 603203 | Emergency: 044 4743 2350");
        addressLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        addressLabel.setHorizontalAlignment(SwingConstants.CENTER);

        headerPanel.add(welcomeLabel);
        headerPanel.add(hubLabel);
        headerPanel.add(addressLabel);
        add(headerPanel, BorderLayout.NORTH);

        // 🔶 Center: Inventory Display
        JTextArea inventoryArea = new JTextArea();
        inventoryArea.setEditable(false);
        inventoryArea.setFont(new Font("Monospaced", Font.PLAIN, 14));
        inventoryArea.setBorder(BorderFactory.createTitledBorder("Available Resources"));

        List<String> bloodItems = BloodInventory.getAllItems();
        List<String> organItems = OrganInventory.getAllItems();

        inventoryArea.append("🩸 Blood Types:\n");
        for (String item : bloodItems) {
            inventoryArea.append(" - " + item + "\n");
        }

        inventoryArea.append("\n🫀 Organs for Transplant:\n");
        for (String item : organItems) {
            inventoryArea.append(" - " + item + "\n");
        }

        JScrollPane scrollPane = new JScrollPane(inventoryArea);
        add(scrollPane, BorderLayout.CENTER);

        // 🔘 Footer: Place Order Button
        JPanel footerPanel = new JPanel();
        JButton orderButton = new JButton("Place Order");
        orderButton.setFont(new Font("Segoe UI", Font.BOLD, 14));
        orderButton.addActionListener(e -> {
            new OrderPage(username, hospitalName); // ✅ Pass both
            dispose();
        });
        footerPanel.add(orderButton);
        add(footerPanel, BorderLayout.SOUTH);

        setVisible(true);
    }
}