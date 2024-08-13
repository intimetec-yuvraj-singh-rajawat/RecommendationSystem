package Service;

import java.sql.SQLException;
import Service.DetailedFeedbackDAO;

public class DetailedFeedbackService {
    private DetailedFeedbackDAO detailedFeedbackDAO;

    public DetailedFeedbackService() throws SQLException {
        this.detailedFeedbackDAO = new DetailedFeedbackDAO();
    }

    public void requestDetailedFeedback(int itemId, String message) throws SQLException {
        detailedFeedbackDAO.insertNotification(itemId, message);
    }

    public void storeDetailedFeedback(int itemId, String question1, String question2, String question3) throws SQLException {
        detailedFeedbackDAO.insertDetailedFeedback(itemId, question1, question2, question3);
    }
}
