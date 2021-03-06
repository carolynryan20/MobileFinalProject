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
    public static final String TRANSACTION_TITLE = "TRANSACTION_TITLE";
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
        root = inflater.inflate(R.layout.fragment_transaction, container, false);
        setupRecyclerView();
        setFriendsList();
        setUpFab();
        initPostListener();
        return root;
    }


    private void setUpFab() {
        FloatingActionButton fab = (FloatingActionButton) root.findViewById(R.id.fabTransactionFragment);

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
                            openAddTransactionFragment();
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

    private void openAddTransactionFragment() {
        AddTransactionDialogFragment addTransactionDialogFragment = new AddTransactionDialogFragment();
        setBundleForOpenAddTransaction(addTransactionDialogFragment);
        addTransactionDialogFragment.show(getFragmentManager(), AddTransactionDialogFragment.TAG);
    }

    private void setBundleForOpenAddTransaction(AddTransactionDialogFragment addTransactionDialogFragment) {
        addTransactionDialogFragment.setTargetFragment(this, 1);
        Bundle bundle = new Bundle();
        bundle.putBoolean(IS_EDIT, false);
        bundle.putStringArrayList(FRIEND_LIST, friendList);
        addTransactionDialogFragment.setArguments(bundle);
    }

    //openAddTransaction when transaction is edit transaction
    public void openAddTransactionFragment(Transaction transaction, String key) {
        AddTransactionDialogFragment addTransactionDialogFragment = new AddTransactionDialogFragment();
        addTransactionDialogFragment.setTargetFragment(this, 1);
        setBundleForOpenAddEdit(transaction, key, addTransactionDialogFragment);
        addTransactionDialogFragment.show(getFragmentManager(), AddTransactionDialogFragment.TAG);
    }

    private void setBundleForOpenAddEdit(Transaction transaction, String key, AddTransactionDialogFragment addTransactionDialogFragment) {
        Bundle bundle = new Bundle();
        bundle.putString(TRANSACTION_TITLE, transaction.getTitle());
        bundle.putString(WHO_PAID, transaction.getOwedUser());
        bundle.putInt(TYPE, transaction.getType().getValue());
        bundle.putBoolean(IS_EDIT, true);
        bundle.putStringArrayList(FRIEND_LIST, friendList);
        bundle.putString(EDIT_INDEX, key);
        addTransactionDialogFragment.setArguments(bundle);
    }

    private void setFriendsList() {
        friendList = new ArrayList<>(); //reinitialize friendlist because otherwise u will repeat
        friendList.add(getUserName());
        //Get's users friends, currently has keys (sort of maybe )
        DatabaseReference friendsRef = FirebaseDatabase.getInstance().getReference().child(USERS).child(getUid()).child(FRIENDS);

        friendsRef.orderByChild(USERNAME).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String prevChildKey) {
                //Hashmap of username, debts, owed for each friend
                HashMap friendUserName = (HashMap) dataSnapshot.getValue();

                //Loop through that hashmap and get all usernames, to add to friend list
                for (Object username : friendUserName.keySet()) {
                    if (String.valueOf(username).equals(USERNAME)) {
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
        recyclerView = (RecyclerView) root.findViewById(R.id.recyclerTransaction);
        recyclerView.setHasFixedSize(true);
        final LinearLayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(mLayoutManager);

        transactionRecyclerAdapter = new TransactionRecyclerAdapter(getContext(), getUid(), this);

        recyclerView.setAdapter(transactionRecyclerAdapter);
    }

    private void initPostListener() {
        final DatabaseReference ref = FirebaseDatabase.getInstance().getReference(USERS)
                .child(getUid()).child(TRANSACTIONS);

        ref.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Transaction newTransaction = dataSnapshot.getValue(Transaction.class);
                transactionRecyclerAdapter.addTransaction(newTransaction, dataSnapshot.getKey());
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
    public void addTransaction(final Transaction transaction) {
        final DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child(USERS);

        String key = FirebaseDatabase.getInstance().getReference().child(USERS).child(getUid()).child(TRANSACTIONS).push().getKey();
        FirebaseDatabase.getInstance().getReference().child(USERS).child(getUid()).child(TRANSACTIONS).child(key).setValue(transaction);

        addToAllContainedUsers(transaction, ref);
        Toast.makeText(getContext(), getContext().getString(R.string.transaction_created), Toast.LENGTH_SHORT).show();

    }

    private void addToAllContainedUsers(final Transaction transaction, final DatabaseReference ref) {
        // add to all other users in that transaction:
        Map<String, Integer> transactionUsers = transaction.getDebtUsers();
        if (transactionUsers == null) {
            Toast.makeText(getContext(), getContext().getString(R.string.friends_list_transaction_is_empty), Toast.LENGTH_SHORT).show();
        } else {
            for (final Map.Entry<String, Integer> entry : transactionUsers.entrySet()) {
                // find this user in snapshot:
                final Query user = ref.orderByChild(USERNAME).equalTo(entry.getKey());
                addTransactionToGivenUser(transaction, ref, user);
            }
        }
    }

    private void addTransactionToGivenUser(final Transaction transaction, final DatabaseReference ref, Query user) {
        user.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot user : dataSnapshot.getChildren()) {
                    String username = (String) user.child(USERNAME).getValue();
                    if (!username.equals(getUserName())) {
                        String userKey = user.getKey();
                        String newTransactionKey = ref.child(userKey).child(TRANSACTIONS).push().getKey();
                        ref.child(userKey).child(TRANSACTIONS).child(newTransactionKey).setValue(transaction);
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void addEditTransaction(Transaction transaction, String key) {
        FirebaseDatabase.getInstance().getReference().child(USERS).child(getUid()).child(TRANSACTIONS).child(key).setValue(transaction);
        Toast.makeText(getContext(), getString(R.string.transaction_edited), Toast.LENGTH_SHORT).show();
        transactionRecyclerAdapter.editTransaction(transaction,key);
    }

}
