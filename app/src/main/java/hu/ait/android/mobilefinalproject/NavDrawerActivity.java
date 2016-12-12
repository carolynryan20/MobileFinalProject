package hu.ait.android.mobilefinalproject;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;

import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import hu.ait.android.mobilefinalproject.adapter.CanRespondToCVTransactionClick;

import hu.ait.android.mobilefinalproject.fragments.friends.FriendsFragment;
import hu.ait.android.mobilefinalproject.fragments.transaction.TransactionAndSummaryFragment;
import hu.ait.android.mobilefinalproject.fragments.transaction.SingleTransactionFragment;
import hu.ait.android.mobilefinalproject.fragments.user.UserFragment;
import hu.ait.android.mobilefinalproject.model.Transaction;


import android.support.v4.app.Fragment;

import com.google.firebase.auth.FirebaseAuth;

import java.io.Serializable;

public class NavDrawerActivity extends BaseActivity
        implements NavigationView.OnNavigationItemSelectedListener,
        TransactionAndSummaryFragment.OnFragmentInteractionListener, CanRespondToCVTransactionClick,
        FriendsFragment.OnFragmentInteractionListener,
        UserFragment.OnFragmentInteractionListener {

    public NavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nav_drawer);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

//        FragmentManager fragmentManager = getSupportFragmentManager();

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        showFragmentByTag(TransactionAndSummaryFragment.TAG, null);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.nav_transaction) {
            showFragmentByTag(TransactionAndSummaryFragment.TAG, null);
        } else if (id == R.id.nav_friends) {
            showFragmentByTag(FriendsFragment.TAG, null);
        } else if (id == R.id.nav_user) {
            showFragmentByTag(UserFragment.TAG, null);
        } else if (id == R.id.nav_send) {
            showSendDialog();
        } else if (id == R.id.nav_about) {
            showAboutDialog();
        } else if (id == R.id.nav_logout) {
            handleSignout();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void handleSignout() {
        FirebaseAuth.getInstance().signOut();
        startActivity(new Intent(this, LoginActivity.class));
        finish();
    }

    private void showAboutDialog() {

        final Dialog aboutDialog = new Dialog(NavDrawerActivity.this);
        aboutDialog.setContentView(R.layout.about_dialog);
        aboutDialog.setTitle(R.string.about);
        aboutDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        aboutDialog.show();
    }

    private void showSendDialog() {
        final Dialog sendDialog = new Dialog(NavDrawerActivity.this);
        sendDialog.setContentView(R.layout.send_dialog);
        sendDialog.setTitle(R.string.contact_us);
        sendDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        sendDialog.show();
    }

    public void showFragmentByTag(String tag, @Nullable Bundle bundle) {
        Fragment fragment = getSupportFragmentManager().findFragmentByTag(tag);
        if (fragment == null) {
            fragment = getFragment(tag, fragment);
        }
        if ((fragment != null) && (bundle != null)) {
            fragment.setArguments(bundle);
        }
        openFragment(tag, fragment);
    }

    private void openFragment(String tag, Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.setCustomAnimations(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
        transaction.replace(R.id.layoutContainer, fragment, tag);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    private Fragment getFragment(String tag, Fragment fragment) {
        if (tag.equals(TransactionAndSummaryFragment.TAG)){
            fragment = new TransactionAndSummaryFragment();
        } else if (tag.equals(FriendsFragment.TAG)){
            fragment = new FriendsFragment();
        } else if (tag.equals(UserFragment.TAG)){
            fragment = new UserFragment();
        } else if (tag.equals(SingleTransactionFragment.TAG)) {
            fragment = new SingleTransactionFragment();
        }
        return fragment;
    }

    @Override
    public void onFragmentInteraction(Uri uri) {}

    @Override
    public void respondToCVTransactionClick(Transaction transaction) {
        Bundle bundle = new Bundle();
        bundle.putString(SingleTransactionFragment.OWED_USER, transaction.getOwedUser());
        bundle.putString(SingleTransactionFragment.TITLE, transaction.getTitle());
        bundle.putInt(SingleTransactionFragment.TYPE, transaction.getType().getValue());

        bundle.putSerializable(SingleTransactionFragment.DEBT_USERS, (Serializable) transaction.getDebtUsers());

        showFragmentByTag(SingleTransactionFragment.TAG, bundle);
    }
}
