package recommendations;
import java.util.*;

import model.Feedback;
public class RecommendationEngine {
    private List<Feedback> feedbacks;
    public static Map<Integer, Integer> itemFeedbacks = new HashMap<>();
    public static Map<Integer, Double> itemRatings = new HashMap<>();
    private Map<Integer, Double> itemRatingCounts = new HashMap<>();
    private Map<Integer, Double> itemRatingScores = new HashMap<>();
    public RecommendationEngine(List<Feedback> feedbacks) {
        this.feedbacks = feedbacks;
        analyzeFeedbacks();
        getAverageRatings();
    }
    private void analyzeFeedbacks() {
        for(Feedback feedback : feedbacks){
            if(!itemFeedbacks.containsKey(feedback.getItemId())){
                itemFeedbacks.put(feedback.getItemId(),0);
            }
            if(PositiveWords.positiveWords.contains(feedback.getComment())){
                Integer feedBackCount = itemFeedbacks.get(feedback.getItemId()) + 1;
                itemFeedbacks.put(feedback.getItemId(),feedBackCount);
            }
            if(NegativeWords.negativeWords.contains(feedback.getComment())){
                Integer feedBackCount = itemFeedbacks.get(feedback.getItemId()) - 1;
                itemFeedbacks.put(feedback.getItemId(),feedBackCount);
            }
        }
    }
    public void getAverageRatings() {
        for(Feedback feedback : feedbacks){
            if(!itemRatingScores.containsKey(feedback.getItemId())){
                itemRatingScores.put(feedback.getItemId(),0.0);
            }
            Double ratingCount = itemRatingScores.get(feedback.getItemId()) + feedback.getRating();
            itemRatingScores.put(feedback.getItemId(),ratingCount);
        }
        for(Feedback feedback : feedbacks){
            if(!itemRatingCounts.containsKey(feedback.getItemId())){
                itemRatingCounts.put(feedback.getItemId(),0.0);
            }
            Double ratingCount = itemRatingCounts.get(feedback.getItemId()) + 1;
            itemRatingCounts.put(feedback.getItemId(),ratingCount);
        }
        for(Feedback feedback : feedbacks){
            itemRatings.put(feedback.getItemId(),(itemRatingScores.get(feedback.getItemId())/itemRatingCounts.get(feedback.getItemId())));
        }
    }
}
class PositiveWords {
    static List<String> positiveWords = Arrays.asList(
            "Excellent", "Outstanding", "Superb", "Fantastic", "Brilliant",
            "Exceptional", "Impressive", "Marvelous", "Remarkable", "Wonderful",
            "Great", "Amazing", "Terrific", "Fabulous", "Awesome",
            "Stellar", "Extraordinary", "Magnificent", "Perfect", "Commendable"
        );
}
class NegativeWords {
    static List<String> negativeWords = Arrays.asList(
            "Poor", "Disappointing", "Unsatisfactory", "Inadequate", "Subpar",
            "Mediocre", "Lacking", "Unacceptable", "Deficient", "Inferior",
            "Weak", "Flawed", "Ineffective", "Insufficient", "Dismal",
            "Unimpressive", "Below standard", "Troubling", "Frustrating", "Incomplete"
    );
}