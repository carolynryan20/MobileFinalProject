package hu.ait.android.mobilefinalproject.fragments.transaction;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import hu.ait.android.mobilefinalproject.R;
import hu.ait.android.mobilefinalproject.adapter.MainPagerAdapter;

/**
 * TransactionAndSummaryFragment.java
 *
 * Created by Carolyn Ryan
 * 11/29/2016
 *
 * Holds pager summary and transaction fragments
 */
public class TransactionAndSummaryFragment extends Fragment {

    public static final String TAG = "TransactionAndSummaryFragment";
    private View root;
    private ViewPager pager;
    private OnFragmentInteractionListener mListener;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_transaction_summary, container, false);

        pager = (ViewPager) root.findViewById(R.id.pager);
        pager.setAdapter(new MainPagerAdapter(this.getChildFragmentManager(), getContext()));

        return root;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + getString(R.string.implement_on_frag_interaction_listener));
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }
}