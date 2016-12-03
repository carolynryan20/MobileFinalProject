package hu.ait.android.mobilefinalproject.model;

/**
 * Created by Morgan on 12/3/2016.
 */
public class Clump {

    private String title;
    private String description;
    private float totalAmount;

    public Clump(String title, String description, float totalAmount) {
        this.title = title;
        this.description = description;
        this.totalAmount = totalAmount;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public float getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(float totalAmount) {
        this.totalAmount = totalAmount;
    }
}
