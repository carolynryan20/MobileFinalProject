package hu.ait.android.mobilefinalproject.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import hu.ait.android.mobilefinalproject.R;
import hu.ait.android.mobilefinalproject.data.Friend;
import hu.ait.android.mobilefinalproject.fragments.FriendsFragment;
import hu.ait.android.mobilefinalproject.model.Clump;

/**
 * Created by Morgan on 12/1/2016.
 */


public class FriendRecyclerAdapter extends RecyclerView.Adapter<FriendRecyclerAdapter.ViewHolder> {

    private List<Friend> friendList;
    private List<String> friendKeys;

    private Context context;
    private CanRespondToCVClumpClick canRespondToCVClumpClick;

    public FriendRecyclerAdapter(Context context) {
        friendList = new ArrayList<Friend>();
//        friendList.add(new Friend("FriendName", 600, 200));
        //this.clumpList = myUser.getClumps();
        this.friendKeys = new ArrayList<String>();

        this.context = context;

//        clumpsRef = FirebaseDatabase.getInstance().getReference("users")
//                .getRef().child(uid).child("clumps");

        checkActivityImplementsResponseInterface();
    }

    private void checkActivityImplementsResponseInterface() {
        if (context instanceof CanRespondToCVClumpClick) {
            this.canRespondToCVClumpClick = (CanRespondToCVClumpClick) context;
        } else {
            throw new RuntimeException("Activity does not implement CanRespondToCVClumpClick interface");
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View layoutView = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.friend_row, parent, false);
        ViewHolder rcv = new ViewHolder(layoutView);
        return rcv;
    }

    //    @Override
//    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
//        View friendRow = LayoutInflater.from(parent.getContext()).inflate(
//                R.layout.friend_row, parent, false);
//
//
//        return new ViewHolder(friendRow);
//    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        final String currentUsername = friendList.get(holder.getAdapterPosition()).getUsername();
        holder.tvFriendUsername.setText(currentUsername);

        DatabaseReference friendsRef = FirebaseDatabase.getInstance().getReference().child("users")
                .child(FriendsFragment.getUid()).child("clumps");
        Query friendsQuery = friendsRef.orderByChild("owedUser");
        friendsQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // create the friend/owed map
                Map<String, Double> owedMap = new HashMap<String, Double>();
                for (DataSnapshot ss : dataSnapshot.getChildren()) {
//                    Toast.makeText(context, ss.toString(), Toast.LENGTH_SHORT).show();
                    String owedUser = (String) ss.child("owedUser").getValue();
                    if(owedUser.equals(FriendsFragment.getUserName())) {
                        List<Double> debtList = new ArrayList<Double>();
                        int numDebtUsers = (int) ss.child("debtUsers").getChildrenCount();
                        for (DataSnapshot debtUser : ss.child("debtUsers").getChildren()) {
                            if (debtUser.getKey().toString().equals(currentUsername)) {
                                String debtUserValue = debtUser.getValue().toString();
                                Double debtUserDouble = Double.parseDouble(debtUserValue);

                                debtList.add(debtUserDouble);
//                                totalOwed += debtUserDouble;
//                                Toast.makeText(context, "" + totalOwed, Toast.LENGTH_SHORT).show();
//                                holder.tvFriendDebt.setText(totalOwed.toString());
                            }
                        }
                        // try iterating through list to sum all values and set to textview
//                        holder.tvFriendDebt.setText();
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    @Override
    public int getItemCount() {
        return friendList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public TextView tvFriendUsername;
        public TextView tvFriendOwed;
        public TextView tvFriendDebt;

        public ViewHolder(final View itemView) {
            super(itemView);
            tvFriendUsername = (TextView) itemView.findViewById(R.id.tvFriendUsername);
            tvFriendOwed = (TextView) itemView.findViewById(R.id.tvFriendOwed);
            tvFriendDebt = (TextView) itemView.findViewById(R.id.tvFriendDebt);
        }
    }

    public void addFriend(Friend friend, String key) {
        if (!friendKeys.contains(key)) {
            friendList.add(0, friend);
            friendKeys.add(0, key);
            // refresh the whole list
            notifyDataSetChanged();
        }
    }

    public List<Friend> getFriends() {
        return friendList;
    }


}
