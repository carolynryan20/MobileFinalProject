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
import hu.ait.android.mobilefinalproject.fragments.FriendsFragment;
import hu.ait.android.mobilefinalproject.model.User;

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
        this.friendKeys = new ArrayList<String>();
        this.context = context;

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
        iconQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot ss : dataSnapshot.getChildren()) {
                    if (ss.child("username").getValue().toString().equals(currentUsername)) {
                        User.UserIcon iconInt = User.UserIcon.valueOf(ss.child("icon").getValue().toString());
                        holder.ivFriendIcon.setImageResource(iconInt.getIconId());
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void setupBackCard(final ViewHolder holder, final String currentUsername) {
        DatabaseReference friendsRef = FirebaseDatabase.getInstance().getReference().child("users")
                .child(FriendsFragment.getUid()).child("clumps");
        Query friendsQuery = friendsRef.orderByChild("owedUser");
        friendsQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot ss : dataSnapshot.getChildren()) {
                    String owedUser = (String) ss.child("owedUser").getValue();
                    if(owedUser.equals(FriendsFragment.getUserName())) {
                        getFriendOwed(ss, currentUsername, holder);
                    }
                    else if (owedUser.equals(currentUsername)){
                        getFriendDebt(ss, holder);
                    }
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {}
        });
    }

    private void getFriendDebt(DataSnapshot ss, ViewHolder holder) {
        for (DataSnapshot debtUser : ss.child("debtUsers").getChildren()) {
            if (debtUser.getKey().toString().equals(FriendsFragment.getUserName())) {
                String debtUserValue = debtUser.getValue().toString();
                Double debtUserDouble = Double.parseDouble(debtUserValue);

                Double prevDebt = Double.valueOf(holder.tvFriendDebt.getText().toString());
//                holder.tvFriendDebt.setText(String.format("%1$.2f", (prevDebt+debtUserDouble)));
                holder.tvFriendDebt.setText(prevDebt+debtUserDouble.toString() + " Ft");
            }
        }
    }

    private void getFriendOwed(DataSnapshot ss, String currentUsername, ViewHolder holder) {
        for (DataSnapshot debtUser : ss.child("debtUsers").getChildren()) {
            if (debtUser.getKey().toString().equals(currentUsername)) {
                String debtUserValue = debtUser.getValue().toString();
                Double debtUserDouble = Double.parseDouble(debtUserValue);

                Double prevDebt = Double.valueOf(holder.tvFriendOwed.getText().toString());
//                holder.tvFriendOwed.setText(String.format("%1$.2f", (prevDebt+debtUserDouble)));
                holder.tvFriendOwed.setText(prevDebt+debtUserDouble.toString() + " Ft");
            }
        }
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
