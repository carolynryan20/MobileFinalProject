package hu.ait.android.mobilefinalproject.adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;

import hu.ait.android.mobilefinalproject.fragments.ClumpFragment;
import hu.ait.android.mobilefinalproject.fragments.SummaryFragment;

/**
 * MainPagerAdapter.java
 *
 * Created by Carolyn Ryan
 * 11/29/2016
 *
 * A class to define interactions of the pager adapter, handle weather fragments
 */

public class MainPagerAdapter extends FragmentPagerAdapter {

    Context context;
    FragmentManager fm;

    public MainPagerAdapter(FragmentManager fm, Context context) {
        super(fm);
        this.fm = fm;
        this.context = context;
    }

    @Override
    public Fragment getItem(int position) {
        Fragment fragment = null;
        switch (position) {
            case 0:
                fragment = new ClumpFragment();
                break;
            case 1:
                fragment = new SummaryFragment();
                break;
            default:
                fragment = new ClumpFragment();
                break;
        }

        return fragment;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:
                return "Your Clumps";
            case 1:
                return "Summary";
        }
        return "";
    }

    @Override
    public int getCount() {
        return 2;
    }
}
