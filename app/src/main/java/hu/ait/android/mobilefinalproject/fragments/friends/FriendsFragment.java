package hu.ait.android.mobilefinalproject.fragments.friends;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
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

import hu.ait.android.mobilefinalproject.R;
import hu.ait.android.mobilefinalproject.adapter.FriendRecyclerAdapter;
import hu.ait.android.mobilefinalproject.data.Friend;
import hu.ait.android.mobilefinalproject.fragments.BaseFragment;


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

    private float dragX;
    private float dragY;
    private int lastAction;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_friends, container, false);
//        setupRecyclerView();
//        setUpFab();

        initPostListener();

        return root;
    }

    private void setUpFab() {
        FloatingActionButton fab = (FloatingActionButton) root.findViewById(R.id.fabFriends);

        fab.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                switch (motionEvent.getActionMasked()) {
                    case MotionEvent.ACTION_DOWN:
                        fabActionDown(view, motionEvent);
                        break;
                    case MotionEvent.ACTION_MOVE:
                        fabActionMove(view, motionEvent);
                        break;
                    case MotionEvent.ACTION_UP:
                        //we know action_down, action_up is a click action so:
                        if (lastAction == MotionEvent.ACTION_DOWN)
                            openAddFriendFragment();
                        break;
                    default:
                        return false;
                }
                return true;
            }
        });
    }

    private void fabActionDown(View view, MotionEvent motionEvent) {
        dragX = view.getX() - motionEvent.getRawX();
        dragY = view.getY() - motionEvent.getRawY();
        lastAction = MotionEvent.ACTION_DOWN;
    }

    private void fabActionMove(View view, MotionEvent motionEvent) {
        view.setX(motionEvent.getRawX() + dragX);
        view.setY(motionEvent.getRawY() + dragY);
        lastAction = MotionEvent.ACTION_MOVE;
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
        setUpFab();


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
        final DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("users");

        Query usernameMatch = ref.orderByChild("username").equalTo(friend.getUsername());
        usernameMatch.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.getChildrenCount() == 0) {
                    Toast.makeText(getContext(), "User '" + friend.getUsername() + "' does not exist!", Toast.LENGTH_SHORT).show();
                } else {
                    for (DataSnapshot user : dataSnapshot.getChildren()) {
                        // check if that username already exists in your friends list:
                        DatabaseReference friendRef = ref.child(getUid()).child("friends");
                        Query friendQuery = friendRef.orderByChild("username").equalTo(friend.getUsername());
                        friendQuery.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                if (dataSnapshot.getChildrenCount() == 0) {
                                    //Toast.makeText(getContext(), "You are not yet friends with " + friend.getUsername(),
                                    //        Toast.LENGTH_SHORT).show();
                                    // add this user as a friend
                                    String newKey = ref.child(getUid()).child("friends").push().getKey();
                                    ref.child(getUid()).child("friends").child(newKey).setValue(friend);
                                    Toast.makeText(getContext(), "Friend Added", Toast.LENGTH_SHORT).show();
                                }
                                else {
                                    Toast.makeText(getContext(), "You are already friends with " + friend.getUsername() + "!",
                                            Toast.LENGTH_SHORT).show();
                                }
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });


                    }
                }
            }


            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
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
