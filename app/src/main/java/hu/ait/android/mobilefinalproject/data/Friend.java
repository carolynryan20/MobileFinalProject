package hu.ait.android.mobilefinalproject.data;

/**
 * Created by Morgan on 12/1/2016.
 */

public class Friend {

    private String username;
    private float debt;
    private float owed;

    public Friend() { 
    }

    public Friend(String username, float debt, float owed) {
        this.username = username;
        this.debt = debt;
        this.owed = owed;
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
}
