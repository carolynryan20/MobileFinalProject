package hu.ait.android.mobilefinalproject.fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.ToggleButton;

import hu.ait.android.mobilefinalproject.R;


/**
 * ClumpFragment.java
 *
 * Created by Carolyn Ryan
 * 11/29/2016
 *
 * A class to define the settings fragment with toggle unit button
 */
public class ClumpFragment extends Fragment {

    public static final String TAG = "ClumpFragment";

    private View root;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_clump, container, false);
        return root;
    }

    @Override
    public void onResume() {
        super.onResume();
        TextView tv = (TextView) root.findViewById(R.id.tvTemporary);
        tv.setText("WOWO");
    }
}
