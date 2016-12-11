package hu.ait.android.mobilefinalproject.data;

import hu.ait.android.mobilefinalproject.model.User;

/**
 * Created by Morgan on 12/1/2016.
 */

public class Friend {

    private String username;
    private int debt;
    private int owed;
    private User.UserIcon iconId;

    public Friend() {
    }

    public Friend(String username, int debt, int owed, User.UserIcon iconId) {
        this.username = username;
        this.debt = debt;
        this.owed = owed;
        this.iconId = iconId;
    }

    public String getUsername() {
        return username;
    }

    public int getDebt() {
        return debt;
    }

    public int getOwed() {
        return owed;
    }

    public User.UserIcon getIcon() { return iconId; }
}
