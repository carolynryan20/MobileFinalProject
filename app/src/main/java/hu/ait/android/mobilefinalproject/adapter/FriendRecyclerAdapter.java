package hu.ait.android.mobilefinalproject.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import hu.ait.android.mobilefinalproject.R;
import hu.ait.android.mobilefinalproject.data.Friend;
import hu.ait.android.mobilefinalproject.fragments.friends.FriendsFragment;
import hu.ait.android.mobilefinalproject.model.User;

/**
 * TransactionRecyclerAdapter.java
 *
 * Created by Morgan Rozman
 * 11/29/2016
 *
 * Recycler Adapter class for user's list of friends
 */
public class FriendRecyclerAdapter extends RecyclerView.Adapter<FriendRecyclerAdapter.ViewHolder> {

    private List<Friend> friendList;
    private List<String> friendKeys;
    private Context context;

    public FriendRecyclerAdapter(Context context) {
        friendList = new ArrayList<>();
        this.friendKeys = new ArrayList<>();
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View layoutView = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.friend_row, parent, false);
        ViewHolder rcv = new ViewHolder(layoutView);
        return rcv;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        final String currentUsername = friendList.get(holder.getAdapterPosition()).getUsername();
        holder.tvFriendUsername.setText(currentUsername);
        setupBackCard(holder, currentUsername);
        setupFrontCard(holder, currentUsername);

    }

    private void setupFrontCard(final ViewHolder holder, final String currentUsername) {
        DatabaseReference iconRef = FirebaseDatabase.getInstance().getReference().child("users");
        Query iconQuery = iconRef.orderByChild("username");
        addIconQueryListener(holder, currentUsername, iconQuery);
    }

    private void addIconQueryListener(final ViewHolder holder, final String currentUsername, Query iconQuery) {
        iconQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                setFriendIconIVs(dataSnapshot, currentUsername, holder);
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void setFriendIconIVs(DataSnapshot dataSnapshot, String currentUsername, ViewHolder holder) {
        for (DataSnapshot ss : dataSnapshot.getChildren()) {
            String dataUser = ss.child("username").getValue().toString();
            if (dataUser.equals(currentUsername)) {
                setFriendIcon(holder, ss);
            }
        }
    }

    private void setFriendIcon(ViewHolder holder, DataSnapshot ss) {
        User.UserIcon iconInt = User.UserIcon.valueOf(ss.child("icon").getValue().toString());
        holder.ivFriendIcon.setImageResource(iconInt.getIconId());
    }


    private void setupBackCard(final ViewHolder holder, final String currentUsername) {
        DatabaseReference friendsRef = FirebaseDatabase.getInstance().getReference().child("users").child(FriendsFragment.getUid()).child("transactions");
        Query friendsQuery = friendsRef.orderByChild("owedUser");
        addFriendsDebtOwedQueryListener(holder, currentUsername, friendsQuery);
    }

    private void addFriendsDebtOwedQueryListener(final ViewHolder holder, final String currentUsername, Query friendsQuery) {
        friendsQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                setFriendDebtAndOwedTVs(dataSnapshot, currentUsername, holder);
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {}
        });
    }

    private void setFriendDebtAndOwedTVs(DataSnapshot dataSnapshot, String currentUsername, ViewHolder holder) {
        for (DataSnapshot ss : dataSnapshot.getChildren()) {
            String owedUser = (String) ss.child("owedUser").getValue();
            checkDebtOrOwed(currentUsername, holder, ss, owedUser);
        }
    }

    private void checkDebtOrOwed(String currentUsername, ViewHolder holder, DataSnapshot ss, String owedUser) {
        if(owedUser.equals(FriendsFragment.getUserName())) {
            getFriendOwed(ss, currentUsername, holder);
        } else if (owedUser.equals(currentUsername)){
            getFriendDebt(ss, holder);
        }
    }

    private void getFriendDebt(DataSnapshot ss, ViewHolder holder) {
        for (DataSnapshot debtUser : ss.child("debtUsers").getChildren()) {
            String dataUser = debtUser.getKey();
            if (dataUser.equals(FriendsFragment.getUserName())) {
                addDebtTV(holder, debtUser);
            }
        }
    }

    private void addDebtTV(ViewHolder holder, DataSnapshot debtUser) {
        String debtUserValue = debtUser.getValue().toString();
        int debtUserInt = Integer.parseInt(debtUserValue);
        int prevDebt = Integer.parseInt(holder.tvFriendDebt.getText().toString());
        holder.tvFriendDebt.setText("" + (prevDebt+debtUserInt));
    }

    private void getFriendOwed(DataSnapshot ss, String currentUsername, ViewHolder holder) {
        for (DataSnapshot debtUser : ss.child("debtUsers").getChildren()) {
            String dataUser = debtUser.getKey();
            if (dataUser.equals(currentUsername)) {
                addOwedTV(holder, debtUser);
            }
        }
    }

    private void addOwedTV(ViewHolder holder, DataSnapshot debtUser) {
        String debtUserValue = debtUser.getValue().toString();
        int debtUserInt = Integer.parseInt(debtUserValue);
        int prevDebt = Integer.parseInt(holder.tvFriendOwed.getText().toString());
        holder.tvFriendOwed.setText(context.getString(R.string.empty) + (prevDebt+debtUserInt));
    }

    @Override
    public int getItemCount() {
        return friendList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public TextView tvFriendUsername;
        public TextView tvFriendOwed;
        public TextView tvFriendDebt;
        public ImageView ivFriendIcon;

        public ViewHolder(final View itemView) {
            super(itemView);
            tvFriendUsername = (TextView) itemView.findViewById(R.id.tvFriendUsername);
            tvFriendOwed = (TextView) itemView.findViewById(R.id.tvFriendOwed);
            tvFriendDebt = (TextView) itemView.findViewById(R.id.tvFriendDebt);
            ivFriendIcon = (ImageView) itemView.findViewById(R.id.ivFriendIcon);
        }
    }

    public void addFriend(Friend friend, String key) {
        if (!friendKeys.contains(key)) {
            friendList.add(0, friend);
            friendKeys.add(0, key);
            notifyDataSetChanged();
        }
    }

    public List<Friend> getFriends() {
        return friendList;
    }
}
