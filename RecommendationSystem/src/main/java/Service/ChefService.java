package Service;

import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.List;
import model.Feedback;
import model.MenuItem;
import recommendations.RecommendationEngine;

public class ChefService {
    private MenuService menuService = new MenuService();
    private FeedbackService feedbackService = new FeedbackService();
    private DailyMenuService dailyMenuService = new DailyMenuService();
    private RecommendationEngine recommendationEngine;
    private DetailedFeedbackService detailedFeedbackService;

    public ChefService() throws SQLException {
        this.detailedFeedbackService = new DetailedFeedbackService();
    }

    public void viewAllMenuItems(PrintWriter out) throws SQLException {
        List<MenuItem> menuItems = menuService.getAllMenuItems();
        for (MenuItem item : menuItems) {
            out.println(item);
        }
        out.println("END_OF_ITEMS");
        out.flush();
    }

    public void seeRecommendedItems(String mealCategory, int numberOfItems, PrintWriter out) {
        List<Feedback> feedbacks = feedbackService.getFeedbacks();
        recommendationEngine = new RecommendationEngine(feedbacks);

        List<Integer> recommendedItems = recommendationEngine.getRecommendedItems(mealCategory, numberOfItems);

        for (Integer itemId : recommendedItems) {
            out.println(itemId + " = " + recommendationEngine.getItemRatings().get(itemId) + ", Sentiment: " + recommendationEngine.getItemFeedbackSentiments().get(itemId));
        }
        out.println("END_OF_RECOMMENDATIONS");
        out.flush();
    }

    public void rollOutItems(String itemIdsStr, PrintWriter out) throws SQLException {
        String[] itemIdsArray = itemIdsStr.split(",");
        int[] itemIds = new int[itemIdsArray.length];
        int count = 0;

        for (String itemIdStr : itemIdsArray) {
            try {
                itemIds[count] = Integer.parseInt(itemIdStr.trim());
                count++;
            } catch (NumberFormatException e) {
                out.println("Invalid item ID: " + itemIdStr);
            }
        }

        try {
            dailyMenuService.addItemsToDailyMenu(itemIds);
            out.println("Items rolled out successfully. " + count + " items added to the daily menu.");
        } catch (SQLException e) {
            out.println("Error rolling out items: " + e.getMessage());
        }

        out.flush();
    }

    public void viewDiscardItems(PrintWriter out) throws SQLException {
        List<Feedback> feedbacks = feedbackService.getFeedbacks();
        recommendationEngine = new RecommendationEngine(feedbacks);
        List<Integer> items = recommendationEngine.getLowRatedItems();
        List<String> foodItem = menuService.getMenuItemsByIds(items);
        for (String fItem : foodItem) {
            out.println(fItem);
        }
        out.println("END_OF_ITEMS");
        out.flush();
    }

    public void deleteDiscardMenuItems(String itemIdsStr, PrintWriter out) throws SQLException {
        String[] itemIdsArray = itemIdsStr.split(",");
        int[] itemIds = new int[itemIdsArray.length];
        int count = 0;

        for (String itemIdStr : itemIdsArray) {
            try {
                itemIds[count] = Integer.parseInt(itemIdStr.trim());
                count++;
            } catch (NumberFormatException e) {
                out.println("Invalid item ID: " + itemIdStr);
            }
        }

        try {
            for (int item : itemIds) {
                menuService.deleteMenuItem(item);
            }
            out.println("Items deleted successfully. " + count + " items removed from the discard menu.");
        } catch (SQLException e) {
            out.println("Error deleting items: " + e.getMessage());
        }

        out.flush();
    }
    
    public void requestDetailedFeedback(int itemId, String itemName) throws SQLException {
        String message = String.format("We are trying to improve your experience with %s. Please provide your feedback and help us.\nQ1. What didn’t you like about %s?\nQ2. How would you like %s to taste?\nQ3. Share your mom’s recipe", itemName, itemName, itemName);
        detailedFeedbackService.requestDetailedFeedback(itemId, message);
    }
}
