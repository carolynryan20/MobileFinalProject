package hu.ait.android.mobilefinalproject.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Dictionary;
import java.util.List;

import hu.ait.android.mobilefinalproject.R;
import hu.ait.android.mobilefinalproject.adapter.ClumpRecyclerAdapter;
import hu.ait.android.mobilefinalproject.data.Friend;
import hu.ait.android.mobilefinalproject.model.Clump;
import hu.ait.android.mobilefinalproject.model.User;


public class ClumpFragment extends BaseFragment implements AddClumpFragmentAnswer{

    public static final String TAG = "ClumpFragment";
    public static final String IS_EDIT = "IS_EDIT";
    public static final String TYPE = "TYPE";
    public static final String CLUMP_TITLE = "CLUMP_TITLE";
    public static final String WHO_PAID = "WHO_PAID";
    public static final String EDIT_INDEX = "EDIT_INDEX";
    private RecyclerView recyclerView;
    private ClumpRecyclerAdapter clumpRecyclerAdapter;
    private View root;

    public ClumpFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        root = inflater.inflate(R.layout.fragment_clump, container, false);

        setupRecyclerView();

        FloatingActionButton fab = (FloatingActionButton) root.findViewById(R.id.fabClumpFragment);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
                // Create a new Clump with the username as the title
                openAddClumpFragment();

            }
        });

        initPostListener();


        return root;
    }

    private void openAddClumpFragment() {
        AddClumpDialogFragment addClumpDialogFragment = new AddClumpDialogFragment();
        addClumpDialogFragment.setTargetFragment(this, 1);
        Bundle bundle = new Bundle();
        bundle.putBoolean(IS_EDIT, false);
        addClumpDialogFragment.setArguments(bundle);
        addClumpDialogFragment.show(getFragmentManager(), AddClumpDialogFragment.TAG);
    }

    public void openAddClumpFragment(Clump clump, int position) {
        AddClumpDialogFragment addClumpDialogFragment = new AddClumpDialogFragment();
        addClumpDialogFragment.setTargetFragment(this, 1);
        Bundle bundle = new Bundle();
        bundle.putString(CLUMP_TITLE, clump.getTitle());
        bundle.putString(WHO_PAID, clump.getOwedUser());
        bundle.putInt(TYPE, clump.getType().getValue());
        bundle.putBoolean(IS_EDIT, true);
        bundle.putInt(EDIT_INDEX, position);
        addClumpDialogFragment.setArguments(bundle);
        addClumpDialogFragment.show(getFragmentManager(), AddClumpDialogFragment.TAG);
    }

    private void setupRecyclerView() {
        recyclerView = (RecyclerView) root.findViewById(R.id.recyclerClump);
        recyclerView.setHasFixedSize(true);
        final LinearLayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(mLayoutManager);

        clumpRecyclerAdapter = new ClumpRecyclerAdapter(getContext(), getUid(), this);

        recyclerView.setAdapter(clumpRecyclerAdapter);
    }

    private void initPostListener() {
        // update list
        final DatabaseReference ref = FirebaseDatabase.getInstance().getReference("users")
                .child(getUid()).child("clumps");

        //final DatabaseReference ref = FirebaseDatabase.getInstance().getReference("clumps");
        ref.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Clump newClump = dataSnapshot.getValue(Clump.class);
                //ref.child(dataSnapshot.getKey()).setValue(newClump);
                clumpRecyclerAdapter.addClump(newClump, dataSnapshot.getKey());
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }


    @Override
    public void addClump(Clump clump) {
        //clumpRecyclerAdapter.addClump(clump, getUid());

        String key = FirebaseDatabase.getInstance().getReference().child("users").child(getUid()).child("clumps").push().getKey();
        FirebaseDatabase.getInstance().getReference().child("users").child(getUid()).child("clumps").child(key).setValue(clump);
        Toast.makeText(getContext(), "Clump created", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void addEditClump(Clump clump, int index) {
//        clumpRecyclerAdapter.editClump(clump, index);
        String key = FirebaseDatabase.getInstance().getReference().child("users").child(getUid()).child("clumps").push().getKey();
        FirebaseDatabase.getInstance().getReference().child("users").child(getUid()).child("clumps").child(key).setValue(clump);
        Toast.makeText(getContext(), "Clump edited", Toast.LENGTH_SHORT).show();
    }
}
