package hu.ait.android.mobilefinalproject.fragments;

import android.support.v4.app.Fragment;

import com.google.firebase.auth.FirebaseAuth;

/**
 * Created by Morgan on 12/4/2016.
 */
public class BaseFragment extends Fragment {

    public static final String USERS = "users";
    public static final String USERNAME = "username";
    public static final String FRIENDS = "friends";
    public static final String ICON = "icon";
    public static final String LOCATION = "location";
    public static final String TRANSACTIONS = "transactions";
    public static final String OWEDUSER = "owedUser";
    public static final String DEBTUSERS = "debtUsers";


//    float debt;
//    static float owed;

    public static String getUid() {
        return FirebaseAuth.getInstance().getCurrentUser().getUid();
    }

    public static String getUserName() {
        return FirebaseAuth.getInstance().getCurrentUser().getDisplayName();
    }

    public String getUserEmail() {
        return FirebaseAuth.getInstance().getCurrentUser().getEmail();
    }
}
