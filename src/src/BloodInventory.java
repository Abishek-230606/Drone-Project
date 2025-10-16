import java.sql.*;
import java.util.*;

public class BloodInventory {
    public static List<String> getAllItems() {
        List<String> items = new ArrayList<>();

        try {
            Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/dronedb", "root", "Sathishdhana#23");
            String query = "SELECT blood_type, quantity_units, location FROM blood_inventory";
            PreparedStatement stmt = conn.prepareStatement(query);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                String type = rs.getString("blood_type");
                int qty = rs.getInt("quantity_units");
                String location = rs.getString("location");
                items.add(type + " - " + qty + " units (" + location + ")");
            }

            conn.close();
        } catch (SQLException e) {
            items.add("❌ Error loading blood inventory: " + e.getMessage());
        }

        return items;
    }
}