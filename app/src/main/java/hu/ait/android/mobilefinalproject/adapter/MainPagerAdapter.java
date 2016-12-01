//package hu.ait.android.mobilefinalproject.adapter;
//
//import android.content.Context;
//import android.support.v4.app.Fragment;
//import android.support.v4.app.FragmentManager;
//import android.support.v4.app.FragmentPagerAdapter;
//
//import hu.ait.android.weatherinfo.R;
//import hu.ait.android.weatherinfo.fragments.DetailsFragment;
//import hu.ait.android.weatherinfo.fragments.MainFragment;
//
///**
// * MainPagerAdapter.java
// *
// * Created by Carolyn Ryan
// * 11/29/2016
// *
// * A class to define interactions of the pager adapter, handle weather fragments
// */
//
//public class MainPagerAdapter extends FragmentPagerAdapter {
//
//    Context context;
//
//    public MainPagerAdapter(FragmentManager fm, Context context) {
//        super(fm);
//        this.context = context;
//    }
//
//    @Override
//    public Fragment getItem(int position) {
//        Fragment fragment = null;
//        switch (position) {
//            case 0:
//                //fragment = new MainFragment();
//                break;
//            case 1:
//                //fragment = new DetailsFragment();
//                break;
//            default:
//                //fragment = new MainFragment();
//                break;
//        }
//
//        return fragment;
//    }
//
//    @Override
//    public CharSequence getPageTitle(int position) {
//        switch (position) {
//            case 0:
//                return context.getString(R.string.main_weather);
//            case 1:
//                return context.getString(R.string.weather_details);
//        }
//        return context.getString(R.string.main);
//    }
//
//    @Override
//    public int getCount() {
//        return 2;
//    }
//}
