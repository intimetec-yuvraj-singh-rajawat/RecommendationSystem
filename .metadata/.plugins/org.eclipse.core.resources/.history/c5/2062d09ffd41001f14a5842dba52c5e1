package Service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import db.Database;
import model.Notification;

public class EmployeeService {
    private Connection connection;

    public EmployeeService() throws SQLException {
        this.connection = Database.getConnection();
    }

    public List<Notification> getNotifications() throws SQLException {
        List<Notification> notifications = new ArrayList<>();
        String sql = "SELECT * FROM notification WHERE date = CURDATE() ORDER BY date DESC";
        try (PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String message = resultSet.getString("message");
                int itemId = resultSet.getInt("item_id");
                String itemName = getMenuItemName(itemId); // Get item name
                notifications.add(new Notification(id, message, itemId, itemName));
            }
        }
        return notifications;
    }

    public List<String> getRolledOutItemsForToday() throws SQLException {
        List<String> items = new ArrayList<>();
        String sql = "SELECT m.id, m.name FROM dailymenuitem d JOIN menuitem m ON d.item_id = m.id WHERE d.date = CURDATE()";
        try (PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                int itemId = resultSet.getInt("id");
                String name = resultSet.getString("name");
                items.add(itemId + ": " + name);
            }
        }
        return items;
    }

    public void voteForItem(int itemId) throws SQLException {
        String sql = "UPDATE dailymenuitem SET vote = vote + 1 WHERE item_id = ? AND date = CURDATE()";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, itemId);
            int rowsUpdated = statement.executeUpdate();
            System.out.println("Rows updated: " + rowsUpdated); // Logging
        }
    }

    private String getMenuItemName(int itemId) throws SQLException {
        String sql = "SELECT name FROM menuitem WHERE id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, itemId);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getString("name");
                }
            }
        }
        return null;
    }
}
