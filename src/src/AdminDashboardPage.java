import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.File;
import java.io.PrintWriter;
import java.sql.*;

public class AdminDashboardPage extends JFrame {

    private JTabbedPane tabs;

    public AdminDashboardPage() {
        setTitle("Admin Dashboard");
        setSize(900, 650);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        tabs = new JTabbedPane();

        createBloodInventoryTab();
        createOrganInventoryTab();
        createReportTab();

        add(tabs);
        setVisible(true);
    }

    // ------------------ BLOOD INVENTORY ------------------
    private void createBloodInventoryTab() {
        JPanel bloodPanel = new JPanel(new GridLayout(3, 2, 10, 10));
        String[] bloodTypes = {"A+", "A-", "B+", "B-", "O+", "O-", "AB+", "AB-"};
        JComboBox<String> bloodTypeDropdown = new JComboBox<>(bloodTypes);

        String[] locations = {"tambaram", "vandalur", "srm", "mm_nagar", "mm_global", "rkp hospital"};
        JComboBox<String> locationDropdown = new JComboBox<>(locations);

        JTextField quantityField = new JTextField();
        quantityField.setBorder(BorderFactory.createTitledBorder("Add Quantity"));

        JButton updateBloodButton = new JButton("Update Inventory");

        bloodPanel.setBorder(BorderFactory.createEmptyBorder(20, 50, 20, 50));
        bloodPanel.add(new JLabel("Select Blood Type"));
        bloodPanel.add(bloodTypeDropdown);
        bloodPanel.add(new JLabel("Select Location"));
        bloodPanel.add(locationDropdown);
        bloodPanel.add(new JLabel("Quantity to Add"));
        bloodPanel.add(quantityField);

        updateBloodButton.addActionListener(e -> {
            String type = (String) bloodTypeDropdown.getSelectedItem();
            String location = (String) locationDropdown.getSelectedItem();
            String qtyText = quantityField.getText().trim();

            if (qtyText.isEmpty() || !qtyText.matches("\\d+")) {
                JOptionPane.showMessageDialog(this, "❌ Please enter a valid quantity.");
                return;
            }

            int quantity = Integer.parseInt(qtyText);

            String url = "jdbc:mysql://localhost:3306/dronedb";
            String user = "root";
            String password = "Sathishdhana#23";

            try (Connection conn = DriverManager.getConnection(url, user, password)) {

                String checkQuery = "SELECT quantity_units FROM blood_inventory WHERE blood_type=? AND location=?";
                try (PreparedStatement checkStmt = conn.prepareStatement(checkQuery)) {
                    checkStmt.setString(1, type);
                    checkStmt.setString(2, location);
                    try (ResultSet rs = checkStmt.executeQuery()) {
                        if (rs.next()) {
                            int currentQty = rs.getInt("quantity_units");
                            String updateQuery = "UPDATE blood_inventory SET quantity_units=? WHERE blood_type=? AND location=?";
                            try (PreparedStatement updateStmt = conn.prepareStatement(updateQuery)) {
                                updateStmt.setInt(1, currentQty + quantity);
                                updateStmt.setString(2, type);
                                updateStmt.setString(3, location);
                                updateStmt.executeUpdate();
                            }
                        } else {
                            String insertQuery = "INSERT INTO blood_inventory (blood_type, quantity_units, location) VALUES (?, ?, ?)";
                            try (PreparedStatement insertStmt = conn.prepareStatement(insertQuery)) {
                                insertStmt.setString(1, type);
                                insertStmt.setInt(2, quantity);
                                insertStmt.setString(3, location);
                                insertStmt.executeUpdate();
                            }
                        }
                    }
                }

                JOptionPane.showMessageDialog(this, "✅ Blood inventory updated!");
                quantityField.setText(""); // Clear field

            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(this, "❌ DB Error: " + ex.getMessage());
            }
        });

        JPanel bloodTab = new JPanel(new BorderLayout());
        bloodTab.add(bloodPanel, BorderLayout.CENTER);
        bloodTab.add(updateBloodButton, BorderLayout.SOUTH);

        tabs.addTab("Update Blood Inventory", bloodTab);
    }

    // ------------------ ORGAN INVENTORY ------------------
    private void createOrganInventoryTab() {
        JPanel organPanel = new JPanel(new GridLayout(3, 2, 10, 10));
        String[] organTypes = {"Kidney", "Liver", "Cornea"};
        JComboBox<String> organTypeDropdown = new JComboBox<>(organTypes);
        String[] urgencyLevels = {"High", "Medium", "Low"};
        JComboBox<String> urgencyDropdown = new JComboBox<>(urgencyLevels);
        JTextField organQtyField = new JTextField();
        organQtyField.setBorder(BorderFactory.createTitledBorder("Add Quantity"));

        JButton updateOrganButton = new JButton("Update Organ Inventory");

        organPanel.setBorder(BorderFactory.createEmptyBorder(20, 50, 20, 50));
        organPanel.add(new JLabel("Select Organ Type"));
        organPanel.add(organTypeDropdown);
        organPanel.add(new JLabel("Urgency Level"));
        organPanel.add(urgencyDropdown);
        organPanel.add(new JLabel("Quantity to Add"));
        organPanel.add(organQtyField);

        updateOrganButton.addActionListener(e -> {
            String organ = (String) organTypeDropdown.getSelectedItem();
            String urgency = (String) urgencyDropdown.getSelectedItem();
            String qtyText = organQtyField.getText().trim();

            if (qtyText.isEmpty() || !qtyText.matches("\\d+")) {
                JOptionPane.showMessageDialog(this, "❌ Please enter a valid quantity.");
                return;
            }

            int quantity = Integer.parseInt(qtyText);

            String url = "jdbc:mysql://localhost:3306/dronedb";
            String user = "root";
            String password = "Sathishdhana#23";

            try (Connection conn = DriverManager.getConnection(url, user, password)) {

                String checkQuery = "SELECT quantity FROM organ_inventory WHERE organ_type=?";
                try (PreparedStatement checkStmt = conn.prepareStatement(checkQuery)) {
                    checkStmt.setString(1, organ);
                    try (ResultSet rs = checkStmt.executeQuery()) {
                        if (rs.next()) {
                            int currentQty = rs.getInt("quantity");
                            String updateQuery = "UPDATE organ_inventory SET quantity=?, urgency_level=? WHERE organ_type=?";
                            try (PreparedStatement updateStmt = conn.prepareStatement(updateQuery)) {
                                updateStmt.setInt(1, currentQty + quantity);
                                updateStmt.setString(2, urgency);
                                updateStmt.setString(3, organ);
                                updateStmt.executeUpdate();
                            }
                        } else {
                            String insertQuery = "INSERT INTO organ_inventory (organ_type, quantity, urgency_level) VALUES (?, ?, ?)";
                            try (PreparedStatement insertStmt = conn.prepareStatement(insertQuery)) {
                                insertStmt.setString(1, organ);
                                insertStmt.setInt(2, quantity);
                                insertStmt.setString(3, urgency);
                                insertStmt.executeUpdate();
                            }
                        }
                    }
                }

                JOptionPane.showMessageDialog(this, "✅ Organ inventory updated!");
                organQtyField.setText("");

            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(this, "❌ DB Error: " + ex.getMessage());
            }
        });

        JPanel organTab = new JPanel(new BorderLayout());
        organTab.add(organPanel, BorderLayout.CENTER);
        organTab.add(updateOrganButton, BorderLayout.SOUTH);

        tabs.addTab("Update Organ Inventory", organTab);
    }

    // ------------------ REPORT TAB ------------------
    private void createReportTab() {
        JPanel reportTab = new JPanel(new BorderLayout());

        DefaultTableModel tableModel = new DefaultTableModel();
        JTable reportTable = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(reportTable);

        JButton refreshButton = new JButton("Refresh Report");
        JButton downloadButton = new JButton("Download CSV");

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(refreshButton);
        buttonPanel.add(downloadButton);

        reportTab.add(scrollPane, BorderLayout.CENTER);
        reportTab.add(buttonPanel, BorderLayout.SOUTH);

        refreshButton.addActionListener(e -> loadReport(tableModel));
        downloadButton.addActionListener(e -> downloadReport(tableModel));

        tabs.addTab("View Orders & Inventory", reportTab);
    }

    private void loadReport(DefaultTableModel tableModel) {
        String url = "jdbc:mysql://localhost:3306/dronedb";
        String user = "root";
        String password = "Sathishdhana#23";

        tableModel.setRowCount(0);
        tableModel.setColumnCount(0);

        try (Connection conn = DriverManager.getConnection(url, user, password)) {
            tableModel.addColumn("Type");
            tableModel.addColumn("Name");
            tableModel.addColumn("Quantity");
            tableModel.addColumn("Location / Urgency");
            tableModel.addColumn("Status");

            // Orders
            try (PreparedStatement ps = conn.prepareStatement("SELECT * FROM orders ORDER BY order_time DESC");
                 ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    tableModel.addRow(new Object[]{
                            "Order",
                            rs.getString("hospital_name") + " - " + rs.getString("item"),
                            rs.getInt("quantity"),
                            "-",
                            rs.getString("status")
                    });
                }
            }

            // Blood Inventory
            try (PreparedStatement ps = conn.prepareStatement("SELECT * FROM blood_inventory");
                 ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    tableModel.addRow(new Object[]{
                            "Blood",
                            rs.getString("blood_type"),
                            rs.getInt("quantity_units"),
                            rs.getString("location"),
                            "-"
                    });
                }
            }

            // Organ Inventory
            try (PreparedStatement ps = conn.prepareStatement("SELECT * FROM organ_inventory");
                 ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    tableModel.addRow(new Object[]{
                            "Organ",
                            rs.getString("organ_type"),
                            rs.getInt("quantity"),
                            rs.getString("urgency_level"),
                            "-"
                    });
                }
            }

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "❌ Error loading report: " + ex.getMessage());
        }
    }

    private void downloadReport(DefaultTableModel tableModel) {
        try (PrintWriter pw = new PrintWriter(new File("report.csv"))) {
            // Write header
            for (int i = 0; i < tableModel.getColumnCount(); i++) {
                pw.print(tableModel.getColumnName(i));
                if (i < tableModel.getColumnCount() - 1) pw.print(",");
            }
            pw.println();

            // Write data
            for (int i = 0; i < tableModel.getRowCount(); i++) {
                for (int j = 0; j < tableModel.getColumnCount(); j++) {
                    pw.print(tableModel.getValueAt(i, j));
                    if (j < tableModel.getColumnCount() - 1) pw.print(",");
                }
                pw.println();
            }

            JOptionPane.showMessageDialog(this, "✅ Report saved as report.csv!");

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "❌ Error saving CSV: " + ex.getMessage());
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(AdminDashboardPage::new);
    }
}
