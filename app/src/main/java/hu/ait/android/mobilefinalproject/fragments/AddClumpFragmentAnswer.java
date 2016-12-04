package hu.ait.android.mobilefinalproject.fragments;

import java.util.List;

import hu.ait.android.mobilefinalproject.data.Friend;

/**
 * Created by Carolyn on 12/4/16.
 */

public interface AddClumpFragmentAnswer {
    public void addClump(String clumpName, short clumpType, List<Friend> friendList);
}
