package hu.ait.android.mobilefinalproject.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import hu.ait.android.mobilefinalproject.R;
import hu.ait.android.mobilefinalproject.data.Friend;
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
        friendList.add(new Friend("FriendName", 600, 200));
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
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.tvFriendUsername.setText(friendList.get(holder.getAdapterPosition()).getUsername());
    }

    @Override
    public int getItemCount() {
        return friendList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder  {

        public TextView tvFriendUsername;

        public ViewHolder(final View itemView) {
            super(itemView);
            tvFriendUsername = (TextView) itemView.findViewById(R.id.tvFriendUsername);
        }
    }

    public void addFriend(Friend friend, String key) {
        friendList.add(0, friend);
        friendKeys.add(0,key);
        // refresh the whole list
        notifyDataSetChanged();
    }

    public List<Friend> getFriends() {
        return friendList;
    }


}
