package hu.ait.android.mobilefinalproject;

import android.app.ProgressDialog;

import android.os.Bundle;
import android.support.annotation.Nullable;

import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;


import com.google.firebase.auth.FirebaseAuth;

import java.util.Map;


/**
 * Created by Carolyn on 11/14/16.
 */
public class BaseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    private ProgressDialog progressDialog;

    public void showProgressDialog() {
        if (progressDialog == null) {
            progressDialog = new ProgressDialog(this);
            progressDialog.setMessage(getString(R.string.loading_message));
        }

        progressDialog.show();
    }

    public void hideProgressDialog() {
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
    }
}