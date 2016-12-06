package hu.ait.android.mobilefinalproject.fragments;

import java.util.List;

import hu.ait.android.mobilefinalproject.data.Friend;
import hu.ait.android.mobilefinalproject.model.Clump;

/**
 * Created by Carolyn on 12/4/16.
 */

public interface AddClumpFragmentAnswer {
    public void addClump(Clump clump);

    public void addEditClump(Clump clump, int index);
}
