package hu.ait.android.mobilefinalproject.fragments;

import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import hu.ait.android.mobilefinalproject.R;

/**
 * Created by Carolyn on 12/1/16.
 */

public class SummaryFragment extends Fragment {

    public static final String TAG = "SummaryFragment";

    private View root;
    private TextView tvDepts;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_summary, container, false);
        return root;
    }

    @Override
    public void onResume() {
        super.onResume();
        setTVs();
    }

    private void setTVs() {
        tvDepts = (TextView) root.findViewById(R.id.tvDepts);
        tvDepts.setText("HEYYO");
    }


}
