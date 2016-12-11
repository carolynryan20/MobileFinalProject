package hu.ait.android.mobilefinalproject.fragments.transaction;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.LinearLayoutManager;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import hu.ait.android.mobilefinalproject.R;
import hu.ait.android.mobilefinalproject.adapter.TransactionRecyclerAdapter;
import hu.ait.android.mobilefinalproject.fragments.BaseFragment;
import hu.ait.android.mobilefinalproject.model.Transaction;


public class TransactionFragment extends BaseFragment implements AddTransactionFragmentAnswer {

    public static final String TAG = "TransactionFragment";
    public static final String IS_EDIT = "IS_EDIT";
    public static final String TYPE = "TYPE";
    public static final String CLUMP_TITLE = "CLUMP_TITLE";
    public static final String WHO_PAID = "WHO_PAID";
    public static final String EDIT_INDEX = "EDIT_INDEX";
    public static final String FRIEND_LIST = "FRIEND_LIST";


    private RecyclerView recyclerView;
    private TransactionRecyclerAdapter transactionRecyclerAdapter;
    private View root;
    private ArrayList<String> friendList = new ArrayList<>();

    private float dragX;
    private float dragY;
    private int lastAction;

    public TransactionFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        root = inflater.inflate(R.layout.fragment_clump, container, false);

        setupRecyclerView();
        setFriendsList();

//        FloatingActionButton fab = (FloatingActionButton) root.findViewById(R.id.fabClumpFragment);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
////                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
////                        .setAction("Action", null).show();
//                // Create a new Transaction with the username as the title
//                openAddClumpFragment();
//
//            }
//        });

        setUpFab();
        initPostListener();

        return root;
    }


    private void setUpFab() {
        FloatingActionButton fab = (FloatingActionButton) root.findViewById(R.id.fabClumpFragment);

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
                            openAddClumpFragment();
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

    private void openAddClumpFragment() {
        AddTransactionDialogFragment addTransactionDialogFragment = new AddTransactionDialogFragment();
        addTransactionDialogFragment.setTargetFragment(this, 1);
        Bundle bundle = new Bundle();
        bundle.putBoolean(IS_EDIT, false);
        bundle.putStringArrayList(FRIEND_LIST, friendList);
        addTransactionDialogFragment.setArguments(bundle);
        addTransactionDialogFragment.show(getFragmentManager(), AddTransactionDialogFragment.TAG);
    }

    //    }
    public void openAddClumpFragment(Transaction transaction, String key) {
        AddTransactionDialogFragment addTransactionDialogFragment = new AddTransactionDialogFragment();
        addTransactionDialogFragment.setTargetFragment(this, 1);
        Bundle bundle = new Bundle();
        bundle.putString(CLUMP_TITLE, transaction.getTitle());
        bundle.putString(WHO_PAID, transaction.getOwedUser());
        bundle.putInt(TYPE, transaction.getType().getValue());
        bundle.putBoolean(IS_EDIT, true);
        bundle.putStringArrayList(FRIEND_LIST, friendList);
        bundle.putString(EDIT_INDEX, key);
        addTransactionDialogFragment.setArguments(bundle);
        addTransactionDialogFragment.show(getFragmentManager(), AddTransactionDialogFragment.TAG);
    }

    private void setFriendsList() {
        friendList = new ArrayList<>(); //reinitialize friendlist because otherwise u will repeat
        //Add the current user to the friend list, as you really are your own best friend
        friendList.add(getUserName());
        //Get's users friends, currently has keys (sort of maybe )
        DatabaseReference friendsRef = FirebaseDatabase.getInstance().getReference().child("users").child(getUid()).child("friends");

        friendsRef.orderByChild("username").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String prevChildKey) {
                //Hashmap of username, debts, owed for each friend
                HashMap friendUserName = (HashMap) dataSnapshot.getValue();
                //Log.d("TAGG", String.valueOf(friendUserName));

                //Loop through that hashmap and get all usernames, to add to friend list
                for (Object username : friendUserName.keySet()) {
                    if (String.valueOf(username).equals("username")) {
//                        Log.d("TAGG", String.valueOf(friendUserName.get(username)));
                        friendList.add(String.valueOf(friendUserName.get(username)));
                    }
                }
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

    private void setupRecyclerView() {
        recyclerView = (RecyclerView) root.findViewById(R.id.recyclerClump);
        recyclerView.setHasFixedSize(true);
        final LinearLayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(mLayoutManager);

        transactionRecyclerAdapter = new TransactionRecyclerAdapter(getContext(), getUid(), this);

        recyclerView.setAdapter(transactionRecyclerAdapter);
    }

    private void initPostListener() {
        // update list
        final DatabaseReference ref = FirebaseDatabase.getInstance().getReference("users")
                .child(getUid()).child("clumps");

        //final DatabaseReference ref = FirebaseDatabase.getInstance().getReference("clumps");
        ref.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Transaction newTransaction = dataSnapshot.getValue(Transaction.class);
                //ref.child(dataSnapshot.getKey()).setValue(newTransaction);
                transactionRecyclerAdapter.addClump(newTransaction, dataSnapshot.getKey());
                // when you add a clump, add it to all users in that clump
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
    public void addClump(final Transaction transaction) {
        final DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("users");

        String key = FirebaseDatabase.getInstance().getReference().child("users").child(getUid()).child("clumps").push().getKey();
        FirebaseDatabase.getInstance().getReference().child("users").child(getUid()).child("clumps").child(key).setValue(transaction);

        addToAllContainedUsers(transaction, ref);
        Toast.makeText(getContext(), "Transaction created", Toast.LENGTH_SHORT).show();
    }

    private void addToAllContainedUsers(final Transaction transaction, final DatabaseReference ref) {
        // add to all other users in that transaction:
        Map<String, Integer> clumpUsers = transaction.getDebtUsers();
        if (clumpUsers == null) {
            Toast.makeText(getContext(), "friends list in transaction is empty", Toast.LENGTH_SHORT).show();
        } else {
            for (final Map.Entry<String, Integer> entry : clumpUsers.entrySet()) {
                // find this user in snapshot:
                final Query user = ref.orderByChild("username").equalTo(entry.getKey());
                user.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for (DataSnapshot user : dataSnapshot.getChildren()) {
                            String username = (String) user.child("username").getValue();
                            if (!username.equals(getUserName())) {
                                String userKey = user.getKey();
                                String newClumpKey = ref.child(userKey).child("clumps").push().getKey();
                                ref.child(userKey).child("clumps").child(newClumpKey).setValue(transaction);
                            }
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
    public void addEditClump(Transaction transaction, String key) {
//        transactionRecyclerAdapter.editClump(transaction, index);
//        String key = FirebaseDatabase.getInstance().getReference().child("users").child(getUid()).child("clumps").push().getKey();
        FirebaseDatabase.getInstance().getReference().child("users").child(getUid()).child("clumps").child(key).setValue(transaction);
        Toast.makeText(getContext(), "Transaction edited", Toast.LENGTH_SHORT).show();
        transactionRecyclerAdapter.editClump(transaction,key);
    }

}
