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

import java.util.List;

import hu.ait.android.mobilefinalproject.R;
import hu.ait.android.mobilefinalproject.adapter.ClumpRecyclerAdapter;
import hu.ait.android.mobilefinalproject.data.Friend;
import hu.ait.android.mobilefinalproject.model.Clump;
import hu.ait.android.mobilefinalproject.model.User;


public class ClumpFragment extends BaseFragment implements AddClumpFragmentAnswer{
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    public static final String TAG = "ClumpFragment";
    private RecyclerView recyclerView;
    private ClumpRecyclerAdapter clumpRecyclerAdapter;
    private View root;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public ClumpFragment() {
        // Required empty public constructor
    }

//    /**
//     * Use this factory method to create a new instance of
//     * this fragment using the provided parameters.
//     *
//     * @param param1 Parameter 1.
//     * @param param2 Parameter 2.
//     * @return A new instance of fragment ClumpFragment.
//     */
//    // TODO: Rename and change types and number of parameters
////    public static ClumpFragment newInstance(String param1, String param2) {
////        ClumpFragment fragment = new ClumpFragment();
////        Bundle args = new Bundle();
////        args.putString(ARG_PARAM1, param1);
////        args.putString(ARG_PARAM2, param2);
////        fragment.setArguments(args);
////        return fragment;
////    }

//    @Override
//    public void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        if (getArguments() != null) {
//            mParam1 = getArguments().getString(ARG_PARAM1);
//            mParam2 = getArguments().getString(ARG_PARAM2);
//        }
//    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
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
//        getFragmentManager().beginTransaction().add(this, TAG).commit();
        addClumpDialogFragment.show(getFragmentManager(), AddClumpDialogFragment.TAG);
    }

    private void setupRecyclerView() {
        recyclerView = (RecyclerView) root.findViewById(R.id.recyclerClump);
        recyclerView.setHasFixedSize(true);
        final LinearLayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(mLayoutManager);

        clumpRecyclerAdapter = new ClumpRecyclerAdapter(getContext(), getUid());

        recyclerView.setAdapter(clumpRecyclerAdapter);
    }

    private void initPostListener() {
        // update list
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("users")
                .child(getUid()).child("clumps");
        ref.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Clump newClump = dataSnapshot.getValue(Clump.class);
                clumpRecyclerAdapter.addClump(newClump, dataSnapshot.getKey());
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                //clumpRecyclerAdapter.removeClumpB(dataSnapshot.getKey());
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
    public void addClump(String clumpName, short clumpType, List<Friend> friendList) {
        Clump newPost = new Clump(getUid(), getUserName(), "descrip", 100);
        clumpRecyclerAdapter.addClump(newPost, getUid());

//        String key = FirebaseDatabase.getInstance().getReference().child("users").child(getUid()).child("clumps").push().getKey();
//
//        FirebaseDatabase.getInstance().getReference().child("users").child(getUid()).child("clumps").child(key).setValue(newPost);

        Toast.makeText(getContext(), "Clump created", Toast.LENGTH_SHORT).show();
    }
}
