package hu.ait.android.mobilefinalproject;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import hu.ait.android.mobilefinalproject.adapter.ClumpRecyclerAdapter;
import hu.ait.android.mobilefinalproject.adapter.MainPagerAdapter;

public class MainActivity extends BaseActivity {

    private RecyclerView recyclerView;
    private ClumpRecyclerAdapter clumpRecyclerAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        ViewPager pager = (ViewPager) findViewById(R.id.pager);
        pager.setAdapter(new MainPagerAdapter(getSupportFragmentManager(), this));
    }
}
