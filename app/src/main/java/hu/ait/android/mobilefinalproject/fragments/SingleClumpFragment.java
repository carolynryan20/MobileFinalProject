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

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

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
                String key = FirebaseDatabase.getInstance().getReference("users")
                        .getRef().child(getUid()).child("clumps").child("transactions")
                        .push().getKey();

                Dictionary<String, Float> dict1 = null;
                Transaction newTransaction = new Transaction("Demo Title", 1, "UserWhoPaid", dict1);
                FirebaseDatabase.getInstance().getReference().child("posts").child(key).setValue(newTransaction);

                Toast.makeText(getActivity(), "Post created", Toast.LENGTH_SHORT).show();
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
        clumpInteractionsRecyclerAdapter = new ClumpInteractionsRecyclerAdapter(getContext(), getUid());
        RecyclerView recyclerView = (RecyclerView) root.findViewById(
                R.id.recyclerClumpInteractions);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setReverseLayout(true);
        layoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(clumpInteractionsRecyclerAdapter);

        initPostsListener();
    }

    private void initPostsListener() {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("posts");
        ref.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Transaction newTransaction = dataSnapshot.getValue(Transaction.class);
                clumpInteractionsRecyclerAdapter.addInteraction(newTransaction, dataSnapshot.getKey());
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                clumpInteractionsRecyclerAdapter.removeInteractionByKey(dataSnapshot.getKey());
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}