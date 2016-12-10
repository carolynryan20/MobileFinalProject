package hu.ait.android.mobilefinalproject.data;

import hu.ait.android.mobilefinalproject.model.User;

/**
 * Created by Morgan on 12/1/2016.
 */

public class Friend {

    private String username;
    private float debt;
    private float owed;
    private User.UserIcon iconId;

    public Friend() {
    }

    public Friend(String username, float debt, float owed, User.UserIcon iconId) {
        this.username = username;
        this.debt = debt;
        this.owed = owed;
        this.iconId = iconId;
    }

    public String getUsername() {
        return username;
    }

    public float getDebt() {
        return debt;
    }

    public float getOwed() {
        return owed;
    }

    public User.UserIcon getIcon() { return iconId; }
}
