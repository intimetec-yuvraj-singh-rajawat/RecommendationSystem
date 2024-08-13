package Service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDate;
import db.Database;

public class DailyMenuService {

    public void addItemToDailyMenu(int itemId) throws SQLException {
        try (Connection conn = Database.getConnection()) {
            String query = "INSERT INTO dailymenuitem (item_id, date) VALUES (?, ?)";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setInt(1, itemId);
            stmt.setDate(2, java.sql.Date.valueOf(LocalDate.now()));
            stmt.executeUpdate();
        }
    }

    public void addItemsToDailyMenu(int[] itemIds) throws SQLException {
        try (Connection conn = Database.getConnection()) {
            String query = "INSERT INTO dailymenuitem (item_id, date) VALUES (?, ?)";
            PreparedStatement stmt = conn.prepareStatement(query);

            for (int itemId : itemIds) {
                stmt.setInt(1, itemId);
                stmt.setDate(2, java.sql.Date.valueOf(LocalDate.now()));
                stmt.addBatch();
            }

            stmt.executeBatch();
        }
    }
}
