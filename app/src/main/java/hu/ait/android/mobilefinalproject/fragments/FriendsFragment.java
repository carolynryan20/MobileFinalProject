package hu.ait.android.mobilefinalproject.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
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
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

import hu.ait.android.mobilefinalproject.BaseActivity;
import hu.ait.android.mobilefinalproject.NavDrawerActivity;
import hu.ait.android.mobilefinalproject.R;
import hu.ait.android.mobilefinalproject.adapter.FriendRecyclerAdapter;
import hu.ait.android.mobilefinalproject.data.Friend;
import hu.ait.android.mobilefinalproject.model.Clump;


public class FriendsFragment extends BaseFragment implements AddFriendFragmentAnswer {

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private RecyclerView recyclerView;
    private FriendRecyclerAdapter friendRecyclerAdapter;

    public static final String TAG = "FriendsFragment";
    private View root;
    private FriendsFragment.OnFragmentInteractionListener mListener;
    private GridLayoutManager lLayout;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_friends, container, false);
//        setupRecyclerView();

        FloatingActionButton fab = (FloatingActionButton) root.findViewById(R.id.fabFriends);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
                // Create a new Clump with the username as the title
                openAddFriendFragment();
//                String key = FirebaseDatabase.getInstance().getReference().child("users").child(getUid())
//                        .child("friends").push().getKey();
//                Friend newFriend = new Friend("ryanc2", 100, 200);
//
//                //if (isFriendUnique(newFriend.getUsername())) {
//                    FirebaseDatabase.getInstance().getReference().child("users").child(getUid())
//                            .child("friends").child(key).setValue(newFriend);
//
//                    Toast.makeText(getContext(), "Friend added", Toast.LENGTH_SHORT).show();
//                }
//                else {
//                    Toast.makeText(getContext(), "You already have that friend", Toast.LENGTH_SHORT).show();
//                }
            }
        });

        initPostListener();

        return root;
    }

    private void openAddFriendFragment() {
        AddFriendDialogFragment addFriendDialogFragment = new AddFriendDialogFragment();
        addFriendDialogFragment.setTargetFragment(this, 1);
        Bundle bundle = new Bundle();
//        bundle.putBoolean(IS_EDIT, false);
        addFriendDialogFragment.setArguments(bundle);
        addFriendDialogFragment.show(getFragmentManager(), AddFriendDialogFragment.TAG);
    }

    private boolean isFriendUnique(final String username) {
        final boolean[] isUnique = {false};
        final DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference().child("users")
                .child(getUid()).child("friends");
        rootRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                Query users = rootRef.getDatabase().getReference().orderByChild("username").equalTo(username);
//                if (users.getRef().getKey() == null) {
//                    Toast.makeText(getContext(), users.getRef().getKey(), Toast.LENGTH_SHORT).show();
//                }
//                if (users.getRef() == null) {
                if(users.getRef().getKey() == null) {
                    isUnique[0] = true;
                }

//                if (snapshot.hasChild("name")) {
//                    // run some code
//                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        return isUnique[0];
    }

    private void initPostListener() {
        // update list
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("users")
                .child(getUid()).child("friends");
        ref.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Friend newFriend = dataSnapshot.getValue(Friend.class);
                friendRecyclerAdapter.addFriend(newFriend, dataSnapshot.getKey());
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
    public void onResume() {
        super.onResume();
        setupRecyclerView();


//        FloatingActionButton fab = (FloatingActionButton) root.findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof FriendsFragment.OnFragmentInteractionListener) {
            mListener = (FriendsFragment.OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void addFriend(final Friend friend) {
        //clumpRecyclerAdapter.addClump(clump, getUid());

        // check if that friend is a user in firebase!
        final DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("users");
        Query usernameMatch = ref.orderByChild("username").equalTo(friend.getUsername());
        usernameMatch.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot user : dataSnapshot.getChildren()) {
                    if (user.exists()) {
                        String newKey = ref.child(getUid()).child("friends").push().getKey();
                        ref.child(getUid()).child("friends").child(newKey).setValue(friend);
                        Toast.makeText(getContext(), "Friend Added", Toast.LENGTH_SHORT).show();
                    }
                    else {
                    }
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

//        String key = FirebaseDatabase.getInstance().getReference().child("users").child(getUid()).child("friends").push().getKey();
//        FirebaseDatabase.getInstance().getReference().child("users").child(getUid()).child("friends").child(key).setValue(friend);
        //Toast.makeText(getContext(), "Friend added", Toast.LENGTH_SHORT).show();
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    private void setupRecyclerView() {
        recyclerView = (RecyclerView) root.findViewById(R.id.recyclerFriends);
//        recyclerView.setHasFixedSize(true);
//        final LinearLayoutManager mLayoutManager = new LinearLayoutManager(getContext());
//        recyclerView.setLayoutManager(mLayoutManager);
//
//        friendRecyclerAdapter = new FriendRecyclerAdapter(getContext());


        lLayout = new GridLayoutManager(getContext(), 2);
        RecyclerView rview = (RecyclerView) root.findViewById(R.id.recyclerFriends);
//        lLayout.setReverseLayout(true);
//        lLayout.setStackFromEnd(true);
        rview.setHasFixedSize(true);
        rview.setLayoutManager(lLayout);

        friendRecyclerAdapter = new FriendRecyclerAdapter(getContext());

        //List<Friend> rowListItem = friendRecyclerAdapter.getFriends();

        recyclerView.setAdapter(friendRecyclerAdapter);
    }

}
