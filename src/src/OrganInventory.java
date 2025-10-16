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
            PreparedStatement stmt = conn.prepareStatement("SELECT organ_type, quantity, urgency_level FROM organ_inventory");
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                String organ = rs.getString("organ_type");
                int qty = rs.getInt("quantity");
                String urgency = rs.getString("urgency_level");
                items.add(organ + " : " + qty + " units | Urgency: " + urgency);
            }

            conn.close();
        } catch (SQLException e) {
            items.add("❌ Error loading organ inventory: " + e.getMessage());
        }

        return items;
    }
}