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


import java.util.List;

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
        tvDepts = (TextView) root.findViewById(R.id.tvDepts);
        tvDepts.setText("$500");

        tvOwed = (TextView) root.findViewById(R.id.tvOwed);
        tvOwed.setText("$5");

        tvUserWhoPaid = (TextView) root.findViewById(R.id.tvUserWhoPaid);
        tvUserWhoPaid.setText("Carolyn Paid");

        listViewUsersWhoOwe = (ListView) root.findViewById(R.id.listViewUsersWhoOwe);

        String[] userList = {"Mo owes $90", "Sam owes $258"};
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