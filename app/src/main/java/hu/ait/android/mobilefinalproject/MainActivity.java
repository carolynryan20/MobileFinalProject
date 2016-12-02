package hu.ait.android.mobilefinalproject;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import hu.ait.android.mobilefinalproject.adapter.ClumpRecyclerAdapter;
import hu.ait.android.mobilefinalproject.adapter.MainPagerAdapter;

public class MainActivity extends Fragment {

    private RecyclerView recyclerView;
    private ClumpRecyclerAdapter clumpRecyclerAdapter;

    public static final String TAG = "ClumpFragment";
    private View root;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.activity_main, container, false);

        ViewPager pager = (ViewPager) root.findViewById(R.id.pager);
        pager.setAdapter(new MainPagerAdapter(this.getFragmentManager(), getContext()));

        return root;
    }

    @Override
    public void onResume() {
        super.onResume();

        FloatingActionButton fab = (FloatingActionButton) root.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });


    }

}
