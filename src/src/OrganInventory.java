import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class OrganInventory {
    public static List<String> getAllItems() {
        List<String> items = new ArrayList<>();
        try {
            Connection conn = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/dronedb", "root", "Sathishdhana#23"
            );
            String query = "SELECT organ_type, quantity FROM organ_inventory"; // updated query
            PreparedStatement stmt = conn.prepareStatement(query);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                String organ = rs.getString("organ_type");
                int qty = rs.getInt("quantity");
                items.add(organ + " - " + qty + " units"); // add to list
            }

            conn.close();
        } catch (SQLException e) {
            items.add("❌ Error loading organ inventory: " + e.getMessage());
        }

        return items;
    }
}
