package hu.ait.android.mobilefinalproject.model;

import java.util.Map;

import hu.ait.android.mobilefinalproject.R;

/**
 * Created by Morgan on 12/3/2016.
 */
public class Transaction {
    private String title;
    private TransactionType type;
    private String owedUser;

//    private Dictionary<String, Float> debtUsers;
    private Map<String, Integer> debtUsers;

    public Transaction() {
    }


    public Transaction(String title, TransactionType type, String owedUser, Map<String, Integer> debtUsers) {
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


    public Map<String, Integer> getDebtUsers() {
        return debtUsers;
    }

    public void setDebtUsers(Map<String, Integer> debtUsers) {
        this.debtUsers = debtUsers;
    }

    public TransactionType getType() {
        return type;
    }

    public void setType(TransactionType type) {
        this.type = type;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * ShoppingItemType to get icon corresponding to item categories
     * */
    public enum TransactionType {
        FOOD(0, R.drawable.knife_fork),
        DRINKS(1, R.drawable.drink),
        RENT(2, R.drawable.ic_home_black_24dp),
        TRAVEL(3, R.drawable.airplane),
        OTHER(4, R.drawable.ic_all_inclusive_black_24dp);

        private int value;
        private int iconId;

        private TransactionType(int value, int iconId) {
            this.value = value;
            this.iconId = iconId;
        }

        public int getValue() {
            return value;
        }

        public int getIconId() {
            return iconId;
        }

        public static TransactionType fromInt(int value) {
            for (TransactionType s : TransactionType.values()) {
                if (s.value == value) {
                    return s;
                }
            }
            return OTHER;
        }
    }
}
