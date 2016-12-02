package hu.ait.android.mobilefinalproject;

import android.os.Bundle;
import android.app.Activity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import hu.ait.android.mobilefinalproject.adapter.FriendRecyclerAdapter;

public class FriendsActivity extends Activity {

    private FriendRecyclerAdapter friendRecyclerAdapter;
    private RecyclerView recyclerFriend;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friends);

        recyclerFriend = (RecyclerView) findViewById(
                R.id.recyclerFriends);
        recyclerFriend.setHasFixedSize(true);
        final LinearLayoutManager mLayoutManager =
                new LinearLayoutManager(this);
        recyclerFriend.setLayoutManager(mLayoutManager);
        friendRecyclerAdapter = new FriendRecyclerAdapter();

        recyclerFriend.setAdapter(friendRecyclerAdapter);
    }

}
