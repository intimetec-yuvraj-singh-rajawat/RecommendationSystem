package Service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import db.Database;
import model.Notification;

public class EmployeeService {
    private Connection connection;
    private DetailedFeedbackService detailedFeedbackService;

    public EmployeeService() throws SQLException {
        this.connection = Database.getConnection();
        this.detailedFeedbackService = new DetailedFeedbackService();

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
                String itemName = getMenuItemName(itemId);
                notifications.add(new Notification(id, message, itemId, itemName));
            }
        }
        return notifications;
    }

    public List<String> getRolledOutItemsForToday(int userId) throws SQLException {
        Map<String, String> userPreferences = getUserPreferences(userId);

        List<String> items = new ArrayList<>();
        String sql = "SELECT m.id, m.name, m.preference, m.spice_level, m.cuisine, m.sweet_tooth FROM dailymenuitem d JOIN menuitem m ON d.item_id = m.id WHERE d.date = CURDATE()";
        try (PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                int itemId = resultSet.getInt("id");
                String name = resultSet.getString("name");
                String preference = resultSet.getString("preference");
                String spiceLevel = resultSet.getString("spice_level");
                String cuisine = resultSet.getString("cuisine");
                int sweetTooth = resultSet.getInt("sweet_tooth");

                int points = calculatePreferencePoints(userPreferences, preference, spiceLevel, cuisine, sweetTooth);
                items.add(points + "#" + itemId + ": " + name);
            }
        }

        items.sort(Comparator.comparingInt(item -> -Integer.parseInt(item.split("#")[0]))); // Sort by points in descending order

        List<String> sortedItems = new ArrayList<>();
        for (String item : items) {
            sortedItems.add(item.split("#")[1]);
        }

        return sortedItems;
    }

    private Map<String, String> getUserPreferences(int userId) throws SQLException {
        Map<String, String> preferences = new HashMap<>();
        String sql = "SELECT preference, spice_level, cuisine, sweet_tooth FROM Profile WHERE user_id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, userId);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    preferences.put("preference", resultSet.getString("preference"));
                    preferences.put("spice_level", resultSet.getString("spice_level"));
                    preferences.put("cuisine", resultSet.getString("cuisine"));
                    preferences.put("sweet_tooth", resultSet.getString("sweet_tooth"));
                }
            }
        }
        return preferences;
    }

    private int calculatePreferencePoints(Map<String, String> userPreferences, String itemPreference, String itemSpiceLevel, String itemCuisine, int itemSweetTooth) {
        int points = 0;
        if (userPreferences.get("preference").equalsIgnoreCase(itemPreference)) {
            points += 4;
        }
        if (userPreferences.get("spice_level").equalsIgnoreCase(itemSpiceLevel)) {
            points += 2;
        }
        if (userPreferences.get("cuisine").equalsIgnoreCase(itemCuisine)) {
            points += 3;
        }
        String userSweetTooth = userPreferences.get("sweet_tooth");
        if ((userSweetTooth.equalsIgnoreCase("Yes") && itemSweetTooth == 1) ||
            (userSweetTooth.equalsIgnoreCase("No") && itemSweetTooth == 0)) {
            points += 1;
        }
        return points;
    }

    public void voteForItem(int itemId) throws SQLException {
        String sql = "UPDATE dailymenuitem SET vote = vote + 1 WHERE item_id = ? AND date = CURDATE()";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, itemId);
            statement.executeUpdate();
        }
    }

    public void updateProfile(int userId, String preference, String spiceLevel, String cuisine, String sweetTooth) throws SQLException {
        String sql = "INSERT INTO Profile (user_id, preference, spice_level, cuisine, sweet_tooth) VALUES (?, ?, ?, ?, ?) " +
                     "ON DUPLICATE KEY UPDATE preference = VALUES(preference), spice_level = VALUES(spice_level), " +
                     "cuisine = VALUES(cuisine), sweet_tooth = VALUES(sweet_tooth)";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, userId);
            statement.setString(2, preference);
            statement.setString(3, spiceLevel);
            statement.setString(4, cuisine);
            statement.setString(5, sweetTooth);
            statement.executeUpdate();
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
    
    public void submitDetailedFeedback(int itemId, String ques1, String ques2, String ques3) throws SQLException {
        String sql = "INSERT INTO detailed_feedback (item_id, ques1, ques2, ques3) VALUES (?, ?, ?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, itemId);
            statement.setString(2, ques1);
            statement.setString(3, ques2);
            statement.setString(4, ques3);
            statement.executeUpdate();
        }
    }
}
