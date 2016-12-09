package hu.ait.android.mobilefinalproject.fragments;

import android.support.v4.app.Fragment;

import com.google.firebase.auth.FirebaseAuth;

/**
 * Created by Morgan on 12/4/2016.
 */
public class BaseFragment extends Fragment {

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
