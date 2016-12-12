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
        initPostListener();
        return root;
    }

    private void setUpFab() {
        FloatingActionButton fab = (FloatingActionButton) root.findViewById(R.id.fabFriends);

        fab.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                return getFabMotion(view, motionEvent);
            }
        });
    }

    private boolean getFabMotion(View view, MotionEvent motionEvent) {
        switch (motionEvent.getActionMasked()) {
            case MotionEvent.ACTION_DOWN:
                fabActionDown(view, motionEvent);
                break;
            case MotionEvent.ACTION_MOVE:
                fabActionMove(view, motionEvent);
                break;
            case MotionEvent.ACTION_UP:
                if (lastAction == MotionEvent.ACTION_DOWN)
                    openAddFriendFragment();
                break;
            default:
                return false;
        }
        return true;
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
        addFriendDialogFragment.setArguments(bundle);
        addFriendDialogFragment.show(getFragmentManager(), AddFriendDialogFragment.TAG);
    }

    private void initPostListener() {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference(USERS)
                .child(getUid()).child(FRIENDS);
        ref.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Friend newFriend = dataSnapshot.getValue(Friend.class);
                friendRecyclerAdapter.addFriend(newFriend, dataSnapshot.getKey());
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {}

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {}

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {}

            @Override
            public void onCancelled(DatabaseError databaseError) {}
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        setupRecyclerView();
        setUpFab();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof FriendsFragment.OnFragmentInteractionListener) {
            mListener = (FriendsFragment.OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(String.format(getResources().
                    getString(R.string.mustImplOnFragInteraction), context.toString()));
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void addFriend(final Friend friend) {
        final DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child(USERS);

        Query usernameMatch = ref.orderByChild(USERNAME).equalTo(friend.getUsername());
        usernameMatch.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                addFriendOnRequirements(dataSnapshot, friend, ref);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {}
        });
    }

    private void addFriendOnRequirements(DataSnapshot dataSnapshot, final Friend friend, final DatabaseReference ref) {
        if (dataSnapshot.getChildrenCount() == 0) {
            Toast.makeText(getContext(), String.format(getResources()
                    .getString(R.string.userDoesNotExist), friend.getUsername()),
                    Toast.LENGTH_SHORT).show();
        } else {
            for (DataSnapshot user : dataSnapshot.getChildren()) {
                isFriendUnique(friend, ref);
            }
        }
    }

    private void isFriendUnique(final Friend friend, final DatabaseReference ref) {
        DatabaseReference friendRef = ref.child(getUid()).child(FRIENDS);
        Query friendQuery = friendRef.orderByChild(USERNAME).equalTo(friend.getUsername());
        friendQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.getChildrenCount() == 0) {
                    addThisFriend(ref, friend);
                }
                else {
                    Toast.makeText(getContext(), String.format(
                            getResources().getString(R.string.alreadyFriends), friend.getUsername()),
                            Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {}
        });
    }

    private void addThisFriend(DatabaseReference ref, Friend friend) {
        String newKey = ref.child(getUid()).child(FRIENDS).push().getKey();
        ref.child(getUid()).child(FRIENDS).child(newKey).setValue(friend);
        Toast.makeText(getContext(), R.string.friendAdded, Toast.LENGTH_SHORT).show();
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    private void setupRecyclerView() {
        recyclerView = (RecyclerView) root.findViewById(R.id.recyclerFriends);
        lLayout = new GridLayoutManager(getContext(), 2);
        RecyclerView rview = (RecyclerView) root.findViewById(R.id.recyclerFriends);
        rview.setHasFixedSize(true);
        rview.setLayoutManager(lLayout);

        friendRecyclerAdapter = new FriendRecyclerAdapter(getContext());
        recyclerView.setAdapter(friendRecyclerAdapter);
    }
}
