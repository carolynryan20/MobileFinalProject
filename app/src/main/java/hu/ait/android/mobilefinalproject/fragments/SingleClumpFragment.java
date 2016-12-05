package hu.ait.android.mobilefinalproject.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Dictionary;

import hu.ait.android.mobilefinalproject.R;
import hu.ait.android.mobilefinalproject.adapter.ClumpInteractionsRecyclerAdapter;
import hu.ait.android.mobilefinalproject.model.Transaction;

/**
 * Created by Carolyn on 12/4/16.
 */

public class SingleClumpFragment extends BaseFragment {

    public static final String TAG = "SingleClumpFragment";

    private View root;
    private TextView tvDepts;
    private TextView tvOwed;
    private Button btnSeeFriends;
    private RecyclerView recyclerView;
    private ClumpInteractionsRecyclerAdapter clumpInteractionsRecyclerAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_single_clump_details, container, false);
        setupRecyclerView();

        FloatingActionButton fab = (FloatingActionButton) root.findViewById(R.id.fabClumpFragment);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
                // Create a new Clump with the username as the title
//                openAddInteractionFragment();
                Dictionary dict1 = null;
                Dictionary dict2 = null;
                clumpInteractionsRecyclerAdapter.addInteraction(new Transaction("Demo", 1, "username", dict2), "DemoKey");
                Toast.makeText(getActivity(), "Adding transactions (maybe)", Toast.LENGTH_SHORT).show();

            }
        });

        return root;
    }

    @Override
    public void onResume() {
        super.onResume();
        setTVs();
    }

    private void setTVs() {
        tvDepts = (TextView) root.findViewById(R.id.tvDepts);
        tvDepts.setText("$500");
        tvOwed = (TextView) root.findViewById(R.id.tvOwed);
        tvOwed.setText("$5");
        btnSeeFriends = (Button) root.findViewById(R.id.btnSeeFriends);
        btnSeeFriends.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                FriendsInClumpFragment friendsInClumpFragment = new FriendsInClumpFragment();
                friendsInClumpFragment.show(getFragmentManager(), FriendsInClumpFragment.TAG);

                Toast.makeText(getActivity(), "You are not able to see friends yet :( \n" +
                        "This may be because you don't have any. \n" +
                        "Loser.", Toast.LENGTH_LONG).show();
            }
        });
    }

    private void setupRecyclerView() {
        recyclerView = (RecyclerView) root.findViewById(R.id.recyclerClumpInteractions);
        recyclerView.setHasFixedSize(true);
        final LinearLayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(mLayoutManager);

        clumpInteractionsRecyclerAdapter = new ClumpInteractionsRecyclerAdapter(getContext(), getUid());

        recyclerView.setAdapter(clumpInteractionsRecyclerAdapter);
    }
}