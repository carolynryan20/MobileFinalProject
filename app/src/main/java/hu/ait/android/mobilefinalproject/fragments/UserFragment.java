package hu.ait.android.mobilefinalproject.fragments;


import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import hu.ait.android.mobilefinalproject.LoginActivity;
import hu.ait.android.mobilefinalproject.NavDrawerActivity;
import hu.ait.android.mobilefinalproject.R;
import hu.ait.android.mobilefinalproject.adapter.ImageAdapter;
import hu.ait.android.mobilefinalproject.model.User;

public class UserFragment extends BaseFragment {


//    // TODO: Rename and change types of parameters
//    private String mParam1;
//    private String mParam2;

    private ImageView accountIcon;
    public static final String TAG = "UserFragment";
    private View root;
    private UserFragment.OnFragmentInteractionListener mListener;
    private ImageAdapter imageAdapter;


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof UserFragment.OnFragmentInteractionListener) {
            mListener = (UserFragment.OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
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

//        ViewPager pager = (ViewPager) root.findViewById(R.id.pager);
//        pager.setAdapter(new MainPagerAdapter(this.getFragmentManager(), getContext()));

        accountIcon = (ImageView) root.findViewById(R.id.ivAccountIcon);


        TextView tvUserFriendsAmount = (TextView) root.findViewById(R.id.tvUserFriendsAmount);
        tvUserFriendsAmount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((NavDrawerActivity)getActivity()).showFragmentByTag(FriendsFragment.TAG, null);
//                Toast.makeText(getContext(), "click", Toast.LENGTH_SHORT).show();
                ((NavDrawerActivity)getActivity()).navigationView.getMenu().getItem(1).setChecked(true);
            }
        });

        TextView tvEditPassword = (TextView) root.findViewById(R.id.tvEditPassword);
        tvEditPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // TODO :: UNCOMMENT FOR FINAL VERSION AND PASSWORD CHANGES
//                FirebaseAuth.getInstance().sendPasswordResetEmail(getUserEmail());
            }
        });

        TextView tvLogout = (TextView) root.findViewById(R.id.tvLogout);
        tvLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(getContext(), LoginActivity.class));
                getActivity().finish();
            }
        });

//        @OnClick(R.id.tvUserFriendsAmount)
//        void showFriendsFragment(){
//            ((NavDrawerActivity)getActivity()).showFragmentByTag(FriendsFragment.TAG);
//            Toast.makeText(getContext(), "click", Toast.LENGTH_SHORT).show();
//        }
//

        TextView tvEditIcon = (TextView) root.findViewById(R.id.tvEditIcon);
        tvEditIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Toast.makeText(getContext(), "clicked edit icon", Toast.LENGTH_SHORT).show();
                showGridDialog();
            }
        });
        return root;
    }


    private void showGridDialog() {

        final Dialog userIconDialog = new Dialog(getContext());
        userIconDialog.setContentView(R.layout.icon_grid_dialog);
        userIconDialog.setTitle("Choose icon");

        GridView iconGridView= (GridView)userIconDialog.findViewById(R.id.grid);

        imageAdapter = new ImageAdapter(getContext());
        iconGridView.setAdapter(imageAdapter);
        iconGridView.setNumColumns(3);
        iconGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                accountIcon = (ImageView) imageAdapter.getView(position, view, parent);

                int editedIcon = imageAdapter.getDrawableID(position, view, parent);
                accountIcon.setImageResource(editedIcon);

                userIconDialog.dismiss();
            }
        });

        userIconDialog.show();
    }

    @Override
    public void onResume() {
        super.onResume();

//        FloatingActionButton fab = (FloatingActionButton) root.findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }



    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }



}
