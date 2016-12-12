package hu.ait.android.mobilefinalproject.fragments.user;


import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import hu.ait.android.mobilefinalproject.LoginActivity;
import hu.ait.android.mobilefinalproject.NavDrawerActivity;
import hu.ait.android.mobilefinalproject.R;
import hu.ait.android.mobilefinalproject.adapter.IconImageAdapter;
import hu.ait.android.mobilefinalproject.fragments.BaseFragment;
import hu.ait.android.mobilefinalproject.fragments.friends.FriendsFragment;
import hu.ait.android.mobilefinalproject.model.User;

public class UserFragment extends BaseFragment {

    private ImageView accountIcon;
    public static final String TAG = "UserFragment";
    private View root;
    private UserFragment.OnFragmentInteractionListener mListener;
    private TextView tvUserMoneyDebtAmount;
    private TextView tvUserMoneyOwedAmount;
    private TextView tvLocation;
    private IconImageAdapter iconImageAdapter;
    private int friendCounter = 0;

    private int debt;
    private int owed;


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof UserFragment.OnFragmentInteractionListener) {
            mListener = (UserFragment.OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(String.format(
                    getResources().getString(R.string.mustImplOnFragInteraction), context.toString()));
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_user, container, false);

        setupViews();

        setupTVButtons();

        setDebtAndOwed();
        return root;
    }

    private void setupTVButtons() {
        setFriendsAmtBtn();
        setEditPasswordBtn();
        setLogoutBtn();
        setEditIconBtn();
    }

    private void setEditIconBtn() {
        TextView tvEditIcon = (TextView) root.findViewById(R.id.tvEditIcon);
        tvEditIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showGridDialog();
            }
        });
    }

    private void setLogoutBtn() {
        TextView tvLogout = (TextView) root.findViewById(R.id.tvLogout);
        tvLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(getContext(), LoginActivity.class));
                getActivity().finish();
            }
        });
    }

    private void setEditPasswordBtn() {
        TextView tvEditPassword = (TextView) root.findViewById(R.id.tvEditPassword);
        tvEditPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().sendPasswordResetEmail(getUserEmail());
                Toast.makeText(getContext(), "Password reset email sent.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setFriendsAmtBtn() {
        TextView tvUserFriendsAmount = (TextView) root.findViewById(R.id.tvUserFriendsAmount);
        tvUserFriendsAmount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((NavDrawerActivity) getActivity()).showFragmentByTag(FriendsFragment.TAG, null);
            }
        });
    }

    private void setupLocation() {
        DatabaseReference locRef = FirebaseDatabase.getInstance().getReference().child(USERS).child(getUid()).child(LOCATION);

        Query locQuery = locRef.orderByValue();
        locQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String location = dataSnapshot.getValue().toString();
                tvLocation.setText(location);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

    private void setupUserIcon() {
        DatabaseReference iconRef = FirebaseDatabase.getInstance().getReference().child(USERS).child(getUid()).child(ICON);

        Query iconQuery = iconRef.orderByValue();
        iconQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                User.UserIcon iconInt = User.UserIcon.valueOf(dataSnapshot.getValue().toString());
                accountIcon.setImageResource(iconInt.getIconId());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

    private void setupViews() {
        accountIcon = (ImageView) root.findViewById(R.id.ivAccountIcon);
        tvUserMoneyDebtAmount = (TextView) root.findViewById(R.id.tvUserMoneyDebtAmount);
        tvUserMoneyOwedAmount = (TextView) root.findViewById(R.id.tvUserMoneyOwedAmount);
        tvLocation = (TextView) root.findViewById(R.id.tvUserLocation);

        String userNameString = getUserName();
        TextView tvUsername = (TextView) root.findViewById(R.id.tvUsername);
        tvUsername.setText(userNameString);

        getFriendCount();
        setupUserIcon();
        setupLocation();
    }

    private void getFriendCount() {
        final DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child(USERS).child(getUid()).child(FRIENDS);
        final Query friends = ref.orderByChild(USERNAME);
        friends.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                friendCounter = (int) dataSnapshot.getChildrenCount();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
        setFriendCount();
    }

    private void setFriendCount() {
        TextView friendCount = (TextView) root.findViewById(R.id.tvUserFriendsAmount);
        if (1 == friendCounter) {
            friendCount.setText(String.format(getResources().
                    getString(R.string.singleFriendCounted), friendCounter));
        } else {
            friendCount.setText(String.format(getResources().
                    getString(R.string.friendCounted), friendCounter));
        }
    }

    private void setDebtAndOwed() {
        debt = 0;
        owed = 0;
        DatabaseReference friendsRef = FirebaseDatabase.getInstance().getReference().child(USERS).child(FriendsFragment.getUid()).child(TRANSACTIONS);
        Query friendsQuery = friendsRef.orderByChild(OWEDUSER);
        friendsQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot ss : dataSnapshot.getChildren()) {
                    UserFragment.this.setDebtAndOwed(ss);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

    }

    private void setDebtAndOwed(DataSnapshot ss) {
        String owedUser = (String) ss.child(OWEDUSER).getValue();
        if (owedUser.equals(getUserName())) {
            for (DataSnapshot debtUser : ss.child(DEBTUSERS).getChildren()) {
                calculateOwed(debtUser);
            }
        } else {
            calculateDebts(ss);
        }
    }

    private void calculateDebts(DataSnapshot ss) {
        for (DataSnapshot debtUser : ss.child(DEBTUSERS).getChildren()) {
            if (debtUser.getKey().equals(getUserName())) {
                debt += Integer.parseInt(debtUser.getValue().toString());
                tvUserMoneyDebtAmount.setText(String.valueOf(debt));
            }
        }
    }

    private void calculateOwed(DataSnapshot debtUser) {
        owed += Integer.parseInt(debtUser.getValue().toString());
        tvUserMoneyOwedAmount.setText(String.valueOf(owed));
    }


    private void showGridDialog() {

        final Dialog userIconDialog = new Dialog(getContext());
        userIconDialog.setContentView(R.layout.icon_grid_dialog);
        userIconDialog.setTitle(R.string.chooseIcon);

        GridView iconGridView = (GridView) userIconDialog.findViewById(R.id.grid);

        iconImageAdapter = new IconImageAdapter(getContext());
        iconGridView.setAdapter(iconImageAdapter);
        iconGridView.setNumColumns(3);
        iconGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                setUserIcon(parent, view, position, userIconDialog);
            }
        });

        userIconDialog.show();
    }

    private void setUserIcon(AdapterView<?> parent, View view, int position, Dialog userIconDialog) {
        int editedIcon = iconImageAdapter.getDrawableID(position, view, parent);
        String iconID = User.UserIcon.fromIconId(editedIcon);

        accountIcon.setImageResource(editedIcon);

        DatabaseReference iconRef = FirebaseDatabase.getInstance().getReference().child(USERS).child(getUid()).child(ICON);
        iconRef.setValue(User.UserIcon.valueOf(iconID));

        userIconDialog.dismiss();
    }

    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }
}
