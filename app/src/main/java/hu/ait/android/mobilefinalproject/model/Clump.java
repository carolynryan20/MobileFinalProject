package hu.ait.android.mobilefinalproject.model;

import com.google.firebase.database.Exclude;

import java.util.Dictionary;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import hu.ait.android.mobilefinalproject.R;

/**
 * Created by Morgan on 12/3/2016.
 */
public class Clump {
    private String title;
    private ClumpType type;
    private String owedUser;
//    private Dictionary<String, Float> debtUsers;
    private Map<String, Float> debtUsers;

    public Clump() {
    }

    public Clump(String title, ClumpType type, String owedUser, Map<String, Float> debtUsers) {
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

    public Map<String, Float> getDebtUsers() {
        return debtUsers;
    }

    public void setDebtUsers(Map<String, Float> debtUsers) {
        this.debtUsers = debtUsers;
    }

    public ClumpType getType() {
        return type;
    }

    public void setType(ClumpType type) {
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

    /**
     * ShoppingItemType to get icon corresponding to item categories
     * */
    public enum ClumpType {
        FOOD(0, R.drawable.ic_menu_camera),
        DRINKS(1, R.drawable.ic_menu_gallery),
        RENT(2, R.drawable.ic_menu_manage),
        TRAVEL(3, R.drawable.ic_menu_send),
        OTHER(4, R.drawable.ic_menu_share);

        private int value;
        private int iconId;

        private ClumpType(int value, int iconId) {
            this.value = value;
            this.iconId = iconId;
        }

        public int getValue() {
            return value;
        }

        public int getIconId() {
            return iconId;
        }

        public static ClumpType fromInt(int value) {
            for (ClumpType s : ClumpType.values()) {
                if (s.value == value) {
                    return s;
                }
            }
            return OTHER;
        }
    }
}
