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

import hu.ait.android.mobilefinalproject.adapter.CanRespondToCVClumpClick;

import hu.ait.android.mobilefinalproject.fragments.friends.FriendsFragment;
import hu.ait.android.mobilefinalproject.fragments.clump.ClumpSummaryFragment;
import hu.ait.android.mobilefinalproject.fragments.clump.SingleClumpFragment;
import hu.ait.android.mobilefinalproject.fragments.user.UserFragment;
import hu.ait.android.mobilefinalproject.model.Clump;


import android.support.v4.app.Fragment;

import com.google.firebase.auth.FirebaseAuth;

import java.io.Serializable;


public class NavDrawerActivity extends BaseActivity
        implements NavigationView.OnNavigationItemSelectedListener,
        ClumpSummaryFragment.OnFragmentInteractionListener, CanRespondToCVClumpClick,
        FriendsFragment.OnFragmentInteractionListener,
        UserFragment.OnFragmentInteractionListener {


    public NavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nav_drawer);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
//
//        Fragment fragment = null;
//        Class fragmentClass = null;
//        fragmentClass = ClumpFragment.class;
//        try {
//            fragment = (Fragment) fragmentClass.newInstance();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }

        FragmentManager fragmentManager = getSupportFragmentManager();
//        fragmentManager.beginTransaction().replace(R.id.flContent, fragment).commit();

//        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
////                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
////                        .setAction("Action", null).show();
//                // Create a new Clump with the username as the title
//                String key = FirebaseDatabase.getInstance().getReference().child("users").child(getUid())
//                        .child("clumps").push().getKey();
//                Clump newPost = new Clump(getUid(), getUserName(), "descrip", 100);
//
//                FirebaseDatabase.getInstance().getReference().child("users").child(getUid())
//                        .child("clumps").child(key).setValue(newPost);
//
//                Toast.makeText(NavDrawerActivity.this, "Clump created", Toast.LENGTH_SHORT).show();
//
//            }
//        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
//        navigationView.getMenu().getItem(0).setChecked(true);

        showFragmentByTag(ClumpSummaryFragment.TAG, null);
    }

    @Override
    public void onBackPressed() {
//        navigationView.getMenu().getItem(0).setChecked(true);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.nav_drawer_in_activity_nav_drawer, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_clump) {
            showFragmentByTag(ClumpSummaryFragment.TAG, null);
            // Handle the camera action
        } else if (id == R.id.nav_friends) {
            showFragmentByTag(FriendsFragment.TAG, null);
        } else if (id == R.id.nav_user) {
            showFragmentByTag(UserFragment.TAG, null);
        } else if (id == R.id.nav_send) {
            showSendDialog();
        } else if (id == R.id.nav_about) {
            showAboutDialog();

        } else if (id == R.id.nav_logout) {
            FirebaseAuth.getInstance().signOut();
            startActivity(new Intent(this, LoginActivity.class));
            finish();

        }
        //else if (id == R.id.nav_share) {
//
//        } else if (id == R.id.nav_send) {
//    }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void showAboutDialog() {

        final Dialog aboutDialog = new Dialog(NavDrawerActivity.this);
        aboutDialog.setContentView(R.layout.about_dialog);
        aboutDialog.setTitle("Choose icon");
        aboutDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        aboutDialog.show();

    }

    private void showSendDialog() {

        final Dialog sendDialog = new Dialog(NavDrawerActivity.this);
        sendDialog.setContentView(R.layout.send_dialog);
        sendDialog.setTitle("Choose icon");
        sendDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        sendDialog.show();

    }

    public void showFragmentByTag(String tag, @Nullable Bundle bundle) {
        Fragment fragment = getSupportFragmentManager().findFragmentByTag(tag);
        if (fragment == null) {
            if (tag.equals(ClumpSummaryFragment.TAG)){
                fragment = new ClumpSummaryFragment();
            } else if (tag.equals(FriendsFragment.TAG)){
                fragment = new FriendsFragment();
            } else if (tag.equals(UserFragment.TAG)){
                fragment = new UserFragment();
            } else if (tag.equals(SingleClumpFragment.TAG)) {
                fragment = new SingleClumpFragment();
            }
        }
        if ((fragment != null) && (bundle != null)) {
            fragment.setArguments(bundle);
        }

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.setCustomAnimations(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
        transaction.replace(R.id.layoutContainer, fragment, tag);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    @Override
    public void respondToCVClumpClick(Clump clump) {
        Bundle bundle = new Bundle();
        bundle.putString(SingleClumpFragment.OWED_USER, clump.getOwedUser());
        bundle.putString(SingleClumpFragment.TITLE,clump.getTitle());
        bundle.putInt(SingleClumpFragment.TYPE, clump.getType().getValue());

        bundle.putSerializable(SingleClumpFragment.DEBT_USERS, (Serializable) clump.getDebtUsers());

        showFragmentByTag(SingleClumpFragment.TAG, bundle);
    }



}
