package hu.ait.android.mobilefinalproject;

import android.os.Bundle;
import android.app.Activity;
import android.widget.Toast;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class AccountActivity extends Activity{



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_user);


        ButterKnife.bind(this);
//        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
//        mapFragment.getMapAsync(this);
    }

//    @OnClick(R.id.tvEditPassword)
//    void editPassword(){
//        Toast.makeText(this, "clicked", Toast.LENGTH_SHORT).show();
//    }

    @OnClick(R.id.tvUserFriendsAmount)
    void openFriendsActivity(){

    }

    @OnClick(R.id.tvLogout)
    void logout(){

    }



}
