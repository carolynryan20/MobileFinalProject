package hu.ait.android.mobilefinalproject.fragments.transaction;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import hu.ait.android.mobilefinalproject.R;
import hu.ait.android.mobilefinalproject.fragments.BaseFragment;

/**
 * SingleTransactionFragment.java
 *
 * Created by Carolyn Ryan
 * 11/29/2016
 *
 * Summary for a single transaction
 */
public class SingleTransactionFragment extends BaseFragment {

    public static final String TAG = "SingleTransactionFragment";
    public static final String OWED_USER = "OWED_USER";
    public static final String TITLE = "TITLE";
    public static final String TYPE = "TYPE";
    public static final String DEBT_USERS = "DEBT_USERS";

    private View root;
    private TextView tvDebts;
    private TextView tvOwed;
    private TextView tvUserWhoPaid;
    private ListView listViewUsersWhoOwe;
    private Bundle args;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_single_transaction_details, container, false);

        return root;
    }

    @Override
    public void onResume() {
        super.onResume();
        setTVs();
    }

    private void setTVs() {
        args = getArguments();

        tvUserWhoPaid = (TextView) root.findViewById(R.id.tvUserWhoPaid);
        tvUserWhoPaid.setText(args.get(OWED_USER) + getString(R.string.paid));

        setTVDebtAndOwed();

    }

    private void setTVDebtAndOwed() {
        listViewUsersWhoOwe = (ListView) root.findViewById(R.id.listViewUsersWhoOwe);
        HashMap<String, Integer> debtUsersMap = (HashMap<String, Integer>) args.getSerializable(DEBT_USERS);
        String paidUser = args.getString(OWED_USER);

        int userDebt = 0;
        int userOwed = 0;
        if ((debtUsersMap != null) && (!debtUsersMap.isEmpty())) {
            boolean owedUserIsCurrentUser = isOwedUserIsCurrentUser();

            List<String> userList = new ArrayList<>();
            for (Map.Entry<String, Integer> entry : debtUsersMap.entrySet()) {
                String user = entry.getKey().toString();
                int owed = entry.getValue();
                Log.d("TAGG_GET", getUserName());
                Log.d("TAGG_OTHER", user);
                if (user.equals(getUserName())) { //if user is currentUser
                    userDebt += owed;
                    Log.d("TAGG", String.valueOf(userDebt));
                } else if (owedUserIsCurrentUser) {
                    userOwed += owed;
                    Log.d("TAGG", String.valueOf(userOwed));
                }

                userList.add(user + getString(R.string.owes) + owed + getString(R.string.ft));
            }

            ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(),
                    android.R.layout.simple_list_item_1, android.R.id.text1, userList);

            listViewUsersWhoOwe.setAdapter(adapter);
        } else {
            Toast.makeText(getContext(), R.string.no_users_have_debt, Toast.LENGTH_SHORT).show();
        }
        tvDebts = (TextView) root.findViewById(R.id.tvDebts);
        tvDebts.setText(userDebt + getString(R.string.ft));

        tvOwed = (TextView) root.findViewById(R.id.tvOwed);
        tvOwed.setText(userOwed + getString(R.string.ft));
    }

    private boolean isOwedUserIsCurrentUser() {
        boolean owedUserIsCurrentUser = false;
        if (getUserName().equals(args.getString(OWED_USER))) {
            owedUserIsCurrentUser = true;
        }
        return owedUserIsCurrentUser;
    }

}