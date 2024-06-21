package Service;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

import db.Database;
import model.Feedback;
public class FeedbackService {
	
	public static List<Feedback> getFeedbacks(){
		List<Feedback> feedbacks = new ArrayList<>();
        try (Connection conn = Database.getConnection()) {
            String query = "SELECT * FROM Feedback";
            PreparedStatement stmt = conn.prepareStatement(query);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                int id = rs.getInt("id");
                String comment = rs.getString("comment");
                int rating = rs.getInt("rating");
                Date feedbackDate = rs.getDate("date");
                int itemId = rs.getInt("item_Id");
                String userId = rs.getString("user_Id");
                feedbacks.add(new Feedback(id, comment, rating, feedbackDate, itemId, userId));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return feedbacks;
    }
		
}
