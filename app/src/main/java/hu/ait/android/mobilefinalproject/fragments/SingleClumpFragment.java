package hu.ait.android.mobilefinalproject.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import hu.ait.android.mobilefinalproject.R;

/**
 * Created by Carolyn on 12/4/16.
 */

public class SingleClumpFragment extends BaseFragment {

    public static final String TAG = "SingleClumpFragment";

    private View root;
    private TextView tvDebts;
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
        tvUserWhoPaid.setText(args.get(OWED_USER) + " Paid");

        listViewUsersWhoOwe = (ListView) root.findViewById(R.id.listViewUsersWhoOwe);
        HashMap<String, Integer> debtUsersMap = (HashMap<String, Integer>) args.getSerializable(DEBT_USERS);

        int userDebt = 0;
        int userOwed = 0;
        if ((debtUsersMap != null) && (!debtUsersMap.isEmpty())) {
            boolean owedUserIsCurrentUser = false;

            if (getUserName().equals(args.getString(OWED_USER))) {
                owedUserIsCurrentUser = true;
            }

            List<String> userList = new ArrayList<>();
            for (Map.Entry<String, Integer> entry : debtUsersMap.entrySet()) {
                String user = entry.getKey();
                int owed = entry.getValue();
                if (user.equals(getUserName())) { //if user is currentUser
                    userDebt += owed;
                } else if (owedUserIsCurrentUser) {
                    userOwed += owed;
                }
                userList.add(user + " owes " + owed + " Ft");
            }

            ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),
                    android.R.layout.simple_list_item_1, android.R.id.text1, userList);

            listViewUsersWhoOwe.setAdapter(adapter);
            listViewUsersWhoOwe.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    int itemPosition = position;
                    String itemValue = (String) listViewUsersWhoOwe.getItemAtPosition(position);
                }
            });
        } else {
            Toast.makeText(getContext(), "No users were inputed as having debts", Toast.LENGTH_SHORT).show();
        }
        tvDebts = (TextView) root.findViewById(R.id.tvDepts);
        tvDebts.setText(userDebt + " Ft");

        tvOwed = (TextView) root.findViewById(R.id.tvOwed);
        tvOwed.setText(userOwed + " Ft");

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