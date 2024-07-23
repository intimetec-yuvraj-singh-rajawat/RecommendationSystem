package model;

public class DetailedFeedback {
    private int itemId;
    private String question1;
    private String question2;
    private String question3;

    public DetailedFeedback(int itemId, String question1, String question2, String question3) {
        this.itemId = itemId;
        this.question1 = question1;
        this.question2 = question2;
        this.question3 = question3;
    }

    public int getItemId() {
        return itemId;
    }

    public void setItemId(int itemId) {
        this.itemId = itemId;
    }

    public String getQuestion1() {
        return question1;
    }

    public void setQuestion1(String question1) {
        this.question1 = question1;
    }

    public String getQuestion2() {
        return question2;
    }

    public void setQuestion2(String question2) {
        this.question2 = question2;
    }

    public String getQuestion3() {
        return question3;
    }

    public void setQuestion3(String question3) {
        this.question3 = question3;
    }
}
