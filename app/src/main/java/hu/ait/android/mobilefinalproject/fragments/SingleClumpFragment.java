package hu.ait.android.mobilefinalproject.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;


import java.io.Serializable;
import java.util.ArrayList;
import java.util.Dictionary;
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
        tvUserWhoPaid.setText(args.get(OWED_USER)+" Paid");


        tvDepts = (TextView) root.findViewById(R.id.tvDepts);
        tvDepts.setText("$500");

        tvOwed = (TextView) root.findViewById(R.id.tvOwed);
        tvOwed.setText("$5");

        listViewUsersWhoOwe = (ListView) root.findViewById(R.id.listViewUsersWhoOwe);
        HashMap<String, Float> dict = (HashMap<String, Float>) args.getSerializable(DEBT_USERS);

        List<String> userList = new ArrayList<>();
        for (Map.Entry<String, Float> entry : dict.entrySet()) {
            String user = entry.getKey();
            Float owed = entry.getValue();
            userList.add(user + " owes $"+owed);
        }

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

}