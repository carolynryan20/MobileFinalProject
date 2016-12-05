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

    //private List<Friend> friendList;
    private List<String> members;
    private int type;
    private String name;

    public Clump() {
    }

    public Clump(String name, short type, List<String> friendList) {
        this.name = name;
        this.type = type;
        this.members = friendList;
    }

    public List<String> getMembersList() {
        return members;
    }

    public void setFriendList(List<String> membersList) {
        this.members = membersList;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("name", name);
        result.put("icon", type);
        result.put("friendsList", members);

        return result;
    }
}
