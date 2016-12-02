package hu.ait.android.mobilefinalproject.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import hu.ait.android.mobilefinalproject.R;
import hu.ait.android.mobilefinalproject.data.Friend;

/**
 * Created by Morgan on 12/1/2016.
 */

public class FriendRecyclerAdapter extends RecyclerView.Adapter<FriendRecyclerAdapter.ViewHolder> {

    private List<Friend> friendList;

    public FriendRecyclerAdapter() {
        friendList = new ArrayList<Friend>();
        friendList.add(new Friend("FriendName", 600, 200));
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View friendRow = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.friend_row, parent, false);

        return new ViewHolder(friendRow);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return friendList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder  {

        public ViewHolder(final View itemView) {
            super(itemView);

        }
    }

}
