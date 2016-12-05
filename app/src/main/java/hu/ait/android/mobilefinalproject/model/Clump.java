package hu.ait.android.mobilefinalproject.model;

import com.google.firebase.database.Exclude;

import java.util.Dictionary;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Morgan on 12/3/2016.
 */
public class Clump {
    private String title;
    private int type;
    private String owedUser;
    private Dictionary<String, Float> debtUsers;

    public Clump() {
    }

    public Clump(String title, int type, String owedUser, Dictionary<String, Float> debtUsers) {
        this.title = title;
        this.type = type;
        this.owedUser = owedUser;
        this.debtUsers = debtUsers;
    }

    public String getOwedUser() {
        return owedUser;
    }

    public void setOwedUser(String owedUser) {
        this.owedUser = owedUser;
    }

    public Dictionary<String, Float> getDebtUsers() {
        return debtUsers;
    }

    public void setDebtUsers(Dictionary<String, Float> debtUsers) {
        this.debtUsers = debtUsers;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("title", title);
        result.put("icon", type);
        result.put("owedUser", owedUser);
        result.put("debtUsers", debtUsers);

        return result;
    }
}
