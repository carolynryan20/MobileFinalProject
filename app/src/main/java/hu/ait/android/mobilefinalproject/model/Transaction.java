package hu.ait.android.mobilefinalproject.model;

import com.google.firebase.database.Exclude;

import java.util.Dictionary;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.R.attr.name;

/**
 * Created by Carolyn on 12/5/16.
 */

public class Transaction {

    private String title;
    private int type;
    private String whoPaid;
    private Dictionary<String, Float> whoOwesHowMuch;


    public Transaction() {
    }

    public Transaction(String title, int type, String whoPaid, Dictionary<String, Float> whoOwesHowMuch) {
        this.title = title;
        this.type = type;
        this.whoPaid = whoPaid;
        this.whoOwesHowMuch = whoOwesHowMuch;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getWhoPaid() {
        return whoPaid;
    }

    public void setWhoPaid(String whoPaid) {
        this.whoPaid = whoPaid;
    }

    public Dictionary<String, Float> getWhoOwesHowMuch() {
        return whoOwesHowMuch;
    }

    public void setWhoOwesHowMuch(Dictionary<String, Float> whoOwesHowMuch) {
        this.whoOwesHowMuch = whoOwesHowMuch;
    }


    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("title", title);
        result.put("icon", type);
        result.put("whoPaid", whoPaid);
        result.put("whoOwesHowMuch", whoOwesHowMuch);

        return result;
    }
}
