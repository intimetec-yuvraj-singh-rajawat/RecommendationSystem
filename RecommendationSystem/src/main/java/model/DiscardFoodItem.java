package model;

import java.util.Date;

public class DiscardFoodItem {
    private int id;
    private int itemId;
    private Date date;

    public DiscardFoodItem(int itemId, Date date) {
        this.itemId = itemId;
        this.date = date;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getItemId() {
        return itemId;
    }

    public void setItemId(int itemId) {
        this.itemId = itemId;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}
