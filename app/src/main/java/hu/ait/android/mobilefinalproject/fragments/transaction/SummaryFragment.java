package hu.ait.android.mobilefinalproject.fragments.transaction;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import hu.ait.android.mobilefinalproject.R;
import hu.ait.android.mobilefinalproject.fragments.BaseFragment;
import hu.ait.android.mobilefinalproject.fragments.friends.FriendsFragment;

/**
 * SummaryFragment.java
 * <p>
 * Created by Carolyn Ryan
 * 11/29/2016
 * <p>
 * Simple fragment shows owed and debt
 */
public class SummaryFragment extends BaseFragment {

    public static final String TAG = "SummaryFragment";

    private View root;
    private TextView tvDebts;
    private TextView tvOwed;
    private int debt;
    private int owed;

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
        tvDebts = (TextView) root.findViewById(R.id.tvDebts);
        tvOwed = (TextView) root.findViewById(R.id.tvOwed);

        setDebtAndOwed();

    }

    private void setDebtAndOwed() {
        debt = 0;
        owed = 0;
        DatabaseReference friendsRef = FirebaseDatabase.getInstance().getReference().child("users").child(FriendsFragment.getUid()).child("transactions");
        Query friendsQuery = friendsRef.orderByChild("owedUser");
        friendsQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                addDebtAndOwedFromDB(dataSnapshot);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

    }

    private void addDebtAndOwedFromDB(DataSnapshot dataSnapshot) {
        for (DataSnapshot ss : dataSnapshot.getChildren()) {
            String owedUser = (String) ss.child("owedUser").getValue();
            if (owedUser.equals(getUserName())) {
                for (DataSnapshot debtUser : ss.child("debtUsers").getChildren()) {
                    owed += Integer.parseInt(debtUser.getValue().toString());
                    tvOwed.setText(String.valueOf(owed));
                }
            } else {
                for (DataSnapshot debtUser : ss.child("debtUsers").getChildren()) {
                    if (debtUser.getKey().equals(getUserName())) {
                        debt += Integer.parseInt(debtUser.getValue().toString());
                        tvDebts.setText(String.valueOf(debt));
                    }
                }
            }
        }
    }


}
