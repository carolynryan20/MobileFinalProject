package hu.ait.android.mobilefinalproject.fragments;

import android.content.Context;
import android.support.v4.app.DialogFragment;

import hu.ait.android.mobilefinalproject.R;

/**
 * Created by Morgan on 12/4/2016.
 */
public class CreateClumpFragment extends DialogFragment {

    private OnAddItemFragmentAnswer onAddItemFragmentAnswer = null;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        if (context instanceof OnAddItemFragmentAnswer) {
            onAddItemFragmentAnswer = (OnAddItemFragmentAnswer) context;
        } else {
            throw new RuntimeException("OnAddItemFragmentAnswer interface not implemented");
        }
    }

}
