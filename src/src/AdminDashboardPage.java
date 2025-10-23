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
        setSize(1000, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Background Panel
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(Color.WHITE);

        // Header with Logout Button
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(new Color(220, 20, 60)); // Crimson Red
        JLabel titleLabel = new JLabel("🩺 Drone Medical Supply - Admin Dashboard", JLabel.LEFT);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 20));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 10));

        JButton logoutButton = new JButton("Logout");
        logoutButton.setBackground(Color.WHITE);
        logoutButton.setForeground(Color.RED);
        logoutButton.setFont(new Font("Segoe UI", Font.BOLD, 14));
        logoutButton.addActionListener(e -> {
            dispose();
            JOptionPane.showMessageDialog(null, "You have been logged out successfully.");
            // You can redirect to login page here
        });

        headerPanel.add(titleLabel, BorderLayout.WEST);
        headerPanel.add(logoutButton, BorderLayout.EAST);

        // Tabs Section
        tabs = new JTabbedPane();
        tabs.setFont(new Font("Segoe UI", Font.BOLD, 14));

        createBloodInventoryTab();
        createOrganInventoryTab();
        createReportTab();
        createHospitalTab();

        mainPanel.add(headerPanel, BorderLayout.NORTH);
        mainPanel.add(tabs, BorderLayout.CENTER);

        add(mainPanel);
        setVisible(true);
    }

    // ------------------ BLOOD INVENTORY ------------------
    private void createBloodInventoryTab() {
        JPanel bloodPanel = new JPanel(new GridLayout(3, 2, 10, 10));
        bloodPanel.setBackground(Color.WHITE);

        String[] bloodTypes = {"A+", "A-", "B+", "B-", "O+", "O-", "AB+", "AB-"};
        JComboBox<String> bloodTypeDropdown = new JComboBox<>(bloodTypes);

        JTextField quantityField = new JTextField();
        quantityField.setBorder(BorderFactory.createTitledBorder("Add Quantity"));

        JButton updateBloodButton = new JButton("Update Inventory");
        styleButton(updateBloodButton);

        bloodPanel.setBorder(BorderFactory.createEmptyBorder(20, 50, 20, 50));
        bloodPanel.add(new JLabel("Select Blood Type"));
        bloodPanel.add(bloodTypeDropdown);
        bloodPanel.add(new JLabel("Quantity to Add"));
        bloodPanel.add(quantityField);

        updateBloodButton.addActionListener(e -> {
            String type = (String) bloodTypeDropdown.getSelectedItem();
            String qtyText = quantityField.getText().trim();

            if (qtyText.isEmpty() || !qtyText.matches("\\d+")) {
                JOptionPane.showMessageDialog(this, "❌ Please enter a valid quantity.");
                return;
            }

            int quantity = Integer.parseInt(qtyText);

            try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/dronedb", "root", "Sathishdhana#23")) {
                String checkQuery = "SELECT quantity_units FROM blood_inventory WHERE blood_type=?";
                try (PreparedStatement checkStmt = conn.prepareStatement(checkQuery)) {
                    checkStmt.setString(1, type);
                    try (ResultSet rs = checkStmt.executeQuery()) {
                        if (rs.next()) {
                            int currentQty = rs.getInt("quantity_units");
                            String updateQuery = "UPDATE blood_inventory SET quantity_units=? WHERE blood_type=?";
                            try (PreparedStatement updateStmt = conn.prepareStatement(updateQuery)) {
                                updateStmt.setInt(1, currentQty + quantity);
                                updateStmt.setString(2, type);
                                updateStmt.executeUpdate();
                            }
                        } else {
                            String insertQuery = "INSERT INTO blood_inventory (blood_type, quantity_units) VALUES (?, ?)";
                            try (PreparedStatement insertStmt = conn.prepareStatement(insertQuery)) {
                                insertStmt.setString(1, type);
                                insertStmt.setInt(2, quantity);
                                insertStmt.executeUpdate();
                            }
                        }
                    }
                }

                JOptionPane.showMessageDialog(this, "✅ Blood inventory updated!");
                quantityField.setText("");

            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(this, "❌ DB Error: " + ex.getMessage());
            }
        });

        JPanel bloodTab = new JPanel(new BorderLayout());
        bloodTab.setBackground(Color.WHITE);
        bloodTab.add(bloodPanel, BorderLayout.CENTER);
        bloodTab.add(updateBloodButton, BorderLayout.SOUTH);

        tabs.addTab("Update Blood Inventory", bloodTab);
    }

    // ------------------ ORGAN INVENTORY ------------------
    private void createOrganInventoryTab() {
        JPanel organPanel = new JPanel(new GridLayout(3, 2, 10, 10));
        organPanel.setBackground(Color.WHITE);

        String[] organTypes = {"Kidney", "Liver", "Cornea"};
        JComboBox<String> organTypeDropdown = new JComboBox<>(organTypes);

        JTextField organQtyField = new JTextField();
        organQtyField.setBorder(BorderFactory.createTitledBorder("Add Quantity"));

        JButton updateOrganButton = new JButton("Update Organ Inventory");
        styleButton(updateOrganButton);

        organPanel.setBorder(BorderFactory.createEmptyBorder(20, 50, 20, 50));
        organPanel.add(new JLabel("Select Organ Type"));
        organPanel.add(organTypeDropdown);
        organPanel.add(new JLabel("Quantity to Add"));
        organPanel.add(organQtyField);

        updateOrganButton.addActionListener(e -> {
            String organ = (String) organTypeDropdown.getSelectedItem();
            String qtyText = organQtyField.getText().trim();

            if (qtyText.isEmpty() || !qtyText.matches("\\d+")) {
                JOptionPane.showMessageDialog(this, "❌ Please enter a valid quantity.");
                return;
            }

            int quantity = Integer.parseInt(qtyText);

            try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/dronedb", "root", "Sathishdhana#23")) {
                String checkQuery = "SELECT quantity FROM organ_inventory WHERE organ_type=?";
                try (PreparedStatement checkStmt = conn.prepareStatement(checkQuery)) {
                    checkStmt.setString(1, organ);
                    try (ResultSet rs = checkStmt.executeQuery()) {
                        if (rs.next()) {
                            int currentQty = rs.getInt("quantity");
                            String updateQuery = "UPDATE organ_inventory SET quantity=? WHERE organ_type=?";
                            try (PreparedStatement updateStmt = conn.prepareStatement(updateQuery)) {
                                updateStmt.setInt(1, currentQty + quantity);
                                updateStmt.setString(2, organ);
                                updateStmt.executeUpdate();
                            }
                        } else {
                            String insertQuery = "INSERT INTO organ_inventory (organ_type, quantity) VALUES (?, ?)";
                            try (PreparedStatement insertStmt = conn.prepareStatement(insertQuery)) {
                                insertStmt.setString(1, organ);
                                insertStmt.setInt(2, quantity);
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
        organTab.setBackground(Color.WHITE);
        organTab.add(organPanel, BorderLayout.CENTER);
        organTab.add(updateOrganButton, BorderLayout.SOUTH);

        tabs.addTab("Update Organ Inventory", organTab);
    }

    // ------------------ REPORT TAB ------------------
    private void createReportTab() {
        JPanel reportTab = new JPanel(new BorderLayout());
        reportTab.setBackground(Color.WHITE);

        DefaultTableModel tableModel = new DefaultTableModel();
        JTable reportTable = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(reportTable);

        JButton refreshButton = new JButton("Refresh Report");
        JButton downloadButton = new JButton("Download CSV");
        styleButton(refreshButton);
        styleButton(downloadButton);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(Color.WHITE);
        buttonPanel.add(refreshButton);
        buttonPanel.add(downloadButton);

        reportTab.add(scrollPane, BorderLayout.CENTER);
        reportTab.add(buttonPanel, BorderLayout.SOUTH);

        refreshButton.addActionListener(e -> loadReport(tableModel));
        downloadButton.addActionListener(e -> downloadReport(tableModel));

        tabs.addTab("View Orders & Inventory", reportTab);
    }

    private void loadReport(DefaultTableModel tableModel) {
        tableModel.setRowCount(0);
        tableModel.setColumnCount(0);

        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/dronedb", "root", "Sathishdhana#23")) {
            tableModel.addColumn("Type");
            tableModel.addColumn("Name");
            tableModel.addColumn("Quantity");
            tableModel.addColumn("Status");

            // Orders
            try (PreparedStatement ps = conn.prepareStatement("SELECT * FROM orders ORDER BY order_time DESC");
                 ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    tableModel.addRow(new Object[]{
                            "Order",
                            rs.getString("hospital_name") + " - " + rs.getString("item"),
                            rs.getInt("quantity"),
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
                            "-"
                    });
                }
            }

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "❌ Error loading report: " + ex.getMessage());
        }
    }

    // ✅ FIXED CSV DOWNLOAD
    private void downloadReport(DefaultTableModel tableModel) {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Save Report As");
        fileChooser.setSelectedFile(new File("report.csv"));

        int userSelection = fileChooser.showSaveDialog(this);
        if (userSelection == JFileChooser.APPROVE_OPTION) {
            File fileToSave = fileChooser.getSelectedFile();

            try (PrintWriter pw = new PrintWriter(fileToSave)) {
                // Header
                for (int i = 0; i < tableModel.getColumnCount(); i++) {
                    pw.print(tableModel.getColumnName(i));
                    if (i < tableModel.getColumnCount() - 1) pw.print(",");
                }
                pw.println();

                // Data
                for (int i = 0; i < tableModel.getRowCount(); i++) {
                    for (int j = 0; j < tableModel.getColumnCount(); j++) {
                        pw.print(tableModel.getValueAt(i, j));
                        if (j < tableModel.getColumnCount() - 1) pw.print(",");
                    }
                    pw.println();
                }

                JOptionPane.showMessageDialog(this, "✅ Report saved at:\n" + fileToSave.getAbsolutePath());
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "❌ Error saving CSV: " + ex.getMessage());
            }
        }
    }

    // ------------------ HOSPITAL REGISTRATION TAB ------------------
    private void createHospitalTab() {
        JPanel hospitalPanel = new JPanel(new GridLayout(8, 2, 10, 10));
        hospitalPanel.setBackground(Color.WHITE);

        JTextField hospitalIdField = new JTextField();
        JPasswordField passwordField = new JPasswordField();
        JTextField nameField = new JTextField();
        JTextField addressField = new JTextField();
        JTextField contactField = new JTextField();
        JTextField regDateField = new JTextField();
        JTextField latitudeField = new JTextField();
        JTextField longitudeField = new JTextField();

        hospitalPanel.add(new JLabel("Hospital ID"));
        hospitalPanel.add(hospitalIdField);
        hospitalPanel.add(new JLabel("Password"));
        hospitalPanel.add(passwordField);
        hospitalPanel.add(new JLabel("Hospital Name"));
        hospitalPanel.add(nameField);
        hospitalPanel.add(new JLabel("Address"));
        hospitalPanel.add(addressField);
        hospitalPanel.add(new JLabel("Contact Number"));
        hospitalPanel.add(contactField);
        hospitalPanel.add(new JLabel("Registration Date"));
        hospitalPanel.add(regDateField);
        hospitalPanel.add(new JLabel("Latitude"));
        hospitalPanel.add(latitudeField);
        hospitalPanel.add(new JLabel("Longitude"));
        hospitalPanel.add(longitudeField);

        JButton registerButton = new JButton("Register Hospital");
        styleButton(registerButton);

        registerButton.addActionListener(e -> {
            try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/dronedb", "root", "Sathishdhana#23")) {
                String sql = "INSERT INTO registered_hospitals (hospital_id, password, hospital_name, address, contact_number, registration_date, latitude, longitude) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
                try (PreparedStatement ps = conn.prepareStatement(sql)) {
                    ps.setString(1, hospitalIdField.getText());
                    ps.setString(2, new String(passwordField.getPassword()));
                    ps.setString(3, nameField.getText());
                    ps.setString(4, addressField.getText());
                    ps.setString(5, contactField.getText());
                    ps.setString(6, regDateField.getText());
                    ps.setString(7, latitudeField.getText());
                    ps.setString(8, longitudeField.getText());
                    ps.executeUpdate();
                }
                JOptionPane.showMessageDialog(this, "✅ Hospital Registered Successfully!");
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(this, "❌ DB Error: " + ex.getMessage());
            }
        });

        JPanel hospitalTab = new JPanel(new BorderLayout());
        hospitalTab.setBackground(Color.WHITE);
        hospitalTab.add(hospitalPanel, BorderLayout.CENTER);
        hospitalTab.add(registerButton, BorderLayout.SOUTH);

        tabs.addTab("Registered Hospitals", hospitalTab);
    }

    // ------------------ BUTTON STYLE ------------------
    private void styleButton(JButton button) {
        button.setBackground(new Color(220, 20, 60));
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setFont(new Font("Segoe UI", Font.BOLD, 14));
        button.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(AdminDashboardPage::new);
    }
}
