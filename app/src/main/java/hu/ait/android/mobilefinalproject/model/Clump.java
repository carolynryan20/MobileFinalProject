package hu.ait.android.mobilefinalproject.model;

import com.google.firebase.database.Exclude;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import hu.ait.android.mobilefinalproject.data.Friend;

/**
 * Created by Morgan on 12/3/2016.
 */
public class Clump {

    private List<Friend> friendList;
    private short type;
    private String name;
    private String uid;
    private String title;
    private String description;
    private float totalAmount;

    public Clump() {
    }

    public Clump(String name, short type, List<Friend> friendList) {
        this.name = name;
        this.type = type;
        this.friendList = friendList;
    }

    public Clump(String uid, String title, String description, float totalAmount) {
        this.uid = uid;
        this.title = title;
        this.description = description;
        this.totalAmount = totalAmount;
    }

    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("uid", uid);
        result.put("title", title);
        result.put("description", description);
        result.put("totalAmount", totalAmount);

        return result;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
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
