package hu.ait.android.mobilefinalproject.adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import hu.ait.android.mobilefinalproject.R;
import hu.ait.android.mobilefinalproject.fragments.transaction.TransactionFragment;
import hu.ait.android.mobilefinalproject.fragments.transaction.SummaryFragment;

/**
 * MainPagerAdapter.java
 *
 * Created by Carolyn Ryan
 * 11/29/2016
 *
 * A class define interactions of the pager adapter, handle weather fragments
 */

public class MainPagerAdapter extends FragmentPagerAdapter {

    private Context context;
    private FragmentManager fm;
    private Fragment transactionFragment;


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
                fragment = new TransactionFragment();
                break;
            case 1:
                fragment = new SummaryFragment();
                break;
            default:
                fragment = new TransactionFragment();
                break;
        }
        if (fragment instanceof TransactionFragment) transactionFragment = fragment;
        return fragment;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:
                return context.getString(R.string.your_transactions);
            case 1:
                return context.getString(R.string.summary);
        }
        return context.getString(R.string.empty);
    }

    @Override
    public int getCount() {
        return 2;
    }

    public Fragment getFragment() {
        return transactionFragment;
    }
}
