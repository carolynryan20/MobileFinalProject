package hu.ait.android.mobilefinalproject.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;


import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Dictionary;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import hu.ait.android.mobilefinalproject.R;
import hu.ait.android.mobilefinalproject.data.Friend;

/**
 * Created by Carolyn on 12/4/16.
 */

public class SingleClumpFragment extends BaseFragment {

    public static final String TAG = "SingleClumpFragment";

    private View root;
    private TextView tvDepts;
    private TextView tvOwed;
    private TextView tvUserWhoPaid;
    private ListView listViewUsersWhoOwe;
    private Bundle args;
    public static final String OWED_USER = "OWED_USER";
    public static final String TITLE = "TITLE";
    public static final String TYPE = "TYPE";
    public static final String DEBT_USERS = "DEBT_USERS";



    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_single_clump_details, container, false);

//        FloatingActionButton fab = (FloatingActionButton) root.findViewById(R.id.fabSingleClump);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
////                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
////                        .setAction("Action", null).show();
//                // Create a new Clump with the username as the title
//                //openAddFriendToClumpFragment();
//
//            }
//        });

        return root;
    }

//    private void openAddFriendToClumpFragment() {
//        AddFriendToClumpDialogFragment addFriendToClumpDialogFragment = new AddFriendToClumpDialogFragment();
//        addFriendToClumpDialogFragment.setTargetFragment(this, 1);
//        Bundle bundle = new Bundle();
////        bundle.putBoolean(IS_EDIT, false);
//        addFriendToClumpDialogFragment.setArguments(bundle);
//        addFriendToClumpDialogFragment.show(getFragmentManager(), AddFriendToClumpDialogFragment.TAG);
//    }


    @Override
    public void onResume() {
        super.onResume();
        setTVs();
    }

    private void setTVs() {
        args = getArguments();

        tvUserWhoPaid = (TextView) root.findViewById(R.id.tvUserWhoPaid);
        tvUserWhoPaid.setText(args.get(OWED_USER)+" Paid");

        listViewUsersWhoOwe = (ListView) root.findViewById(R.id.listViewUsersWhoOwe);
        HashMap<String, Float> dict = (HashMap<String, Float>) args.getSerializable(DEBT_USERS);

        boolean owedUserIsCurrentUser = false;
        if (getUserName().equals(args.getString(OWED_USER))) {
            owedUserIsCurrentUser = true;
        }
        float userDebt = 0;
        float userOwed = 0;

        List<String> userList = new ArrayList<>();
        for (Map.Entry<String, Float> entry : dict.entrySet()) {
            String user = entry.getKey();
            Float owed = entry.getValue();
            if (user.equals(getUserName())) { //if user is currentUser
                userDebt += owed;
            } else if (owedUserIsCurrentUser) {
                userOwed += owed;
            }
            userList.add(user + " owes $"+owed);
        }

        tvDepts = (TextView) root.findViewById(R.id.tvDepts);
        tvDepts.setText("$"+userDebt);

        tvOwed = (TextView) root.findViewById(R.id.tvOwed);
        tvOwed.setText("$"+userOwed);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),
                android.R.layout.simple_list_item_1, android.R.id.text1, userList);

        listViewUsersWhoOwe.setAdapter(adapter);
        listViewUsersWhoOwe.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                int itemPosition = position;
                String  itemValue = (String) listViewUsersWhoOwe.getItemAtPosition(position);
            }
        });
    }

//    @Override
//    public void addFriendToClump(Friend friend) {
////        String key = FirebaseDatabase.getInstance().getReference().child("users").child(getUid()).child("clumps").push().getKey();
////        FirebaseDatabase.getInstance().getReference().child("users").child(getUid()).child("clumps").child(key).setValue(clump);
////        String key = FirebaseDatabase.getInstance().getReference().child("users").child(getUid()).child("clumps").getKey();
//        Toast.makeText(getContext(), "Friend Added to Clump", Toast.LENGTH_SHORT).show();
//
//    }
}