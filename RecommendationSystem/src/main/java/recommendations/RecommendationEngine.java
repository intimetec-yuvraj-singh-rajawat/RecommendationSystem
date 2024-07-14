package recommendations;

import java.util.*;
import model.Feedback;

public class RecommendationEngine {
    private List<Feedback> feedbacks;
    private Map<Integer, Integer> itemFeedbackScores;
    private Map<Integer, String> itemFeedbackSentiments;
    private Map<Integer, Double> itemRatings;
    private Map<Integer, Integer> itemRatingCounts;
    private Map<Integer, Double> itemRatingScores;
    private Map<Integer, String> itemIdToItemNameMap;

    private static final Set<String> POSITIVE_WORDS = new HashSet<>(Arrays.asList(
    		"delicious", "tasty", "savory", "flavorful", "appetizing",
            "delectable", "scrumptious", "yummy", "mouthwatering", "luscious",
            "succulent", "exquisite", "heavenly", "tempting", "gourmet", "rich",
            "satisfying", "juicy", "nutritious", "fresh", "aromatic", "zesty",
            "flavor-packed", "divine", "tantalizing", "lip-smacking", "indulgent",
            "gratifying", "delightful", "palatable", "bursting with flavor",
            "sumptuous", "decadent", "mouth-filling", "melt-in-your-mouth", "irresistible",
            "crispy", "tender", "velvety", "bright", "fragrant", "clean", "comforting",
            "homemade", "authentic", "freshly baked", "wholesome", "energizing",
            "nourishing", "excellent", "outstanding", "superb", "fantastic", "brilliant",
            "exceptional", "impressive", "marvelous", "remarkable", "wonderful",
            "great", "amazing", "terrific", "fabulous", "awesome", "stellar", "extraordinary",
            "magnificent", "perfect", "commendable", "good"
    ));

    private static final Set<String> NEGATIVE_WORDS = new HashSet<>(Arrays.asList(
    		 "poor", "disappointing", "unsatisfactory", "inadequate", "subpar",
             "mediocre", "lacking", "unacceptable", "deficient", "inferior", "weak",
             "flawed", "ineffective", "insufficient", "dismal", "unimpressive",
             "below standard", "troubling", "frustrating", "incomplete", "bland",
             "tasteless", "soggy", "stale", "rancid", "greasy", "unappetizing",
             "mushy", "watery", "overcooked", "undercooked", "burnt", "sour",
             "rotten", "spoiled", "unpleasant", "bitter", "salty", "dry", "tough",
             "chewy", "gritty", "fatty", "artificial", "frozen", "processed", "stodgy",
             "heavy", "unhealthy", "unpalatable", "flat", "stagnant", "insipid",
             "disappointing", "mismatched", "unbalanced", "off-putting", "uninspired",
             "repulsive", "sickening", "stuffy", "stifling", "unappealing", "stinky",
             "dull", "distasteful", "unimaginative", "fake", "lacking", "ordinary", "bad"
    ));

    private static final Set<String> NEGATION_WORDS = new HashSet<>(Arrays.asList(
        "not", "no", "never", "none", "nobody", "nothing", "neither", "nowhere", "cannot", "can't"
    ));

    public RecommendationEngine(List<Feedback> feedbacks) {
        this.feedbacks = feedbacks;
        this.itemFeedbackScores = new HashMap<>();
        this.itemFeedbackSentiments = new HashMap<>();
        this.itemRatings = new HashMap<>();
        this.itemRatingCounts = new HashMap<>();
        this.itemRatingScores = new HashMap<>();
        this.itemIdToItemNameMap = new HashMap<>();
        analyzeFeedbacks();
        calculateAverageRatings();
    }

    private void analyzeFeedbacks() {
        for (Feedback feedback : feedbacks) {
            int itemId = feedback.getItemId();
            itemIdToItemNameMap.putIfAbsent(itemId, feedback.getItemName());
            itemFeedbackScores.putIfAbsent(itemId, 0);

            String[] words = feedback.getComment().split("\\s+");
            int positiveScore = 0;
            int negativeScore = 0;
            boolean negation = false;

            for (String word : words) {
                String lowerCaseWord = word.toLowerCase().replaceAll("[^a-zA-Z]", "");
                if (NEGATION_WORDS.contains(lowerCaseWord)) {
                    negation = true;
                    continue;
                }

                if (POSITIVE_WORDS.contains(lowerCaseWord)) {
                    if (negation) {
                        negativeScore++;
                        negation = false;
                    } else {
                        positiveScore++;
                    }
                } else if (NEGATIVE_WORDS.contains(lowerCaseWord)) {
                    if (negation) {
                        positiveScore++;
                        negation = false;
                    } else {
                        negativeScore++;
                    }
                }
            }

            int score = positiveScore - negativeScore;
            itemFeedbackScores.put(itemId, itemFeedbackScores.get(itemId) + score);
        }

        for (Map.Entry<Integer, Integer> entry : itemFeedbackScores.entrySet()) {
            int itemId = entry.getKey();
            int score = entry.getValue();
            if (score > 0) {
                itemFeedbackSentiments.put(itemId, "Positive");
            } else if (score < 0) {
                itemFeedbackSentiments.put(itemId, "Negative");
            } else {
                itemFeedbackSentiments.put(itemId, "Neutral");
            }
        }
    }

    private void calculateAverageRatings() {
        for (Feedback feedback : feedbacks) {
            int itemId = feedback.getItemId();
            int rating = feedback.getRating();

            itemRatingCounts.put(itemId, itemRatingCounts.getOrDefault(itemId, 0) + 1);
            itemRatingScores.put(itemId, itemRatingScores.getOrDefault(itemId, 0.0) + rating);
        }

        for (Map.Entry<Integer, Double> entry : itemRatingScores.entrySet()) {
            int itemId = entry.getKey();
            double averageRating = entry.getValue() / itemRatingCounts.get(itemId);
            itemRatings.put(itemId, averageRating);
        }
    }

    public List<Integer> getRecommendedItems(String mealCategory, int numberOfItems) {
        List<Integer> recommendedItems = new ArrayList<>(itemRatings.keySet());
        recommendedItems.sort((itemId1, itemId2) -> {
            int ratingComparison = itemRatings.get(itemId2).compareTo(itemRatings.get(itemId1));
            if (ratingComparison != 0) {
                return ratingComparison;
            } else {
                int sentimentScore1 = itemFeedbackScores.get(itemId1);
                int sentimentScore2 = itemFeedbackScores.get(itemId2);
                return Integer.compare(sentimentScore2, sentimentScore1);
            }
        });

        return recommendedItems.subList(0, Math.min(numberOfItems, recommendedItems.size()));
    }

    public Map<Integer, Double> getItemRatings() {
        return itemRatings;
    }

    public Map<Integer, String> getItemFeedbackSentiments() {
        return itemFeedbackSentiments;
    }
}

