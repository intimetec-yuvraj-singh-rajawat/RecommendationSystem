package model;

public class Notification {
    private int id;
    private String message;
    private int itemId;
    private String itemName;

    public Notification(int id, String message, int itemId, String itemName) {
        this.id = id;
        this.message = message;
        this.itemId = itemId;
        this.itemName = itemName;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getItemId() {
        return itemId;
    }

    public void setItemId(int itemId) {
        this.itemId = itemId;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }
    @Override
    public String toString() {
        return "Notification='" + message;
    }
}
