package Service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import db.Database;

public class DetailedFeedbackDAO {
    private Connection connection;

    public DetailedFeedbackDAO() throws SQLException {
        this.connection = Database.getConnection();
    }

    public void insertNotification(int itemId, String message) throws SQLException {
        String sql = "INSERT INTO notification (message, item_id, date) VALUES (?, ?, CURDATE())";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, message);
            statement.setInt(2, itemId);
            statement.executeUpdate();
        }
    }

    public void insertDetailedFeedback(int itemId, String question1, String question2, String question3) throws SQLException {
        String sql = "INSERT INTO detailed_feedback (item_id, question1, question2, question3) VALUES (?, ?, ?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, itemId);
            statement.setString(2, question1);
            statement.setString(3, question2);
            statement.setString(4, question3);
            statement.executeUpdate();
        }
    }
}
