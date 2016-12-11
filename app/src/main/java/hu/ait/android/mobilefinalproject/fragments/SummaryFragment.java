package hu.ait.android.mobilefinalproject.fragments;

import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

import hu.ait.android.mobilefinalproject.R;

/**
 * Created by Carolyn on 12/1/16.
 */

public class SummaryFragment extends BaseFragment {

    public static final String TAG = "SummaryFragment";

    private View root;
    private TextView tvDepts;
    private TextView tvOwed;
    private int debt;
    private int owed;
    private RecyclerView recyclerClumpInteraction;
//    private BaseFragment baseFragment = new BaseFragment();
//    private Button btnSeeFriends;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_summary, container, false);
        return root;
    }

    @Override
    public void onResume() {
        super.onResume();
        setTVs();
    }

    private void setTVs() {
        tvDepts = (TextView) root.findViewById(R.id.tvDepts);
        tvOwed = (TextView) root.findViewById(R.id.tvOwed);

        setDebtAndOwed();
        recyclerClumpInteraction = (RecyclerView) root.findViewById(R.id.recyclerClumpInteractions);

    }

    private void setDebtAndOwed() {
        debt = 0;
        owed = 0;
        DatabaseReference friendsRef = FirebaseDatabase.getInstance().getReference().child("users").child(FriendsFragment.getUid()).child("clumps");
        Query friendsQuery = friendsRef.orderByChild("owedUser");
        friendsQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot ss : dataSnapshot.getChildren()) {
                    String owedUser = (String) ss.child("owedUser").getValue();
                    Log.d("TAGG_oweduseris", owedUser);
                    if (owedUser.equals(getUserName())){
                        //TODO add to owes
                        for (DataSnapshot debtUser : ss.child("debtUsers").getChildren()) {
//                            owed+= Float.parseFloat(debtUser.getValue().toString());
                            owed+= Integer.parseInt(debtUser.getValue().toString());
                            tvOwed.setText(String.valueOf(owed) + " Ft");
                        }
                    } else {
                        for (DataSnapshot debtUser : ss.child("debtUsers").getChildren()) {
                            if (debtUser.getKey().equals(getUserName())) {
                                Log.d("TAGG", debtUser.getValue().toString());
//                                debt += Float.parseFloat(debtUser.getValue().toString());
                                debt+= Integer.parseInt(debtUser.getValue().toString());
                                tvDepts.setText(String.valueOf(debt) + " Ft");
                            }
                        }
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

    }


}
