package hu.ait.android.mobilefinalproject.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import hu.ait.android.mobilefinalproject.R;

/**
 * Created by ssheppe on 12/8/16.
 */

public class ImageAdapter extends BaseAdapter {

    private Context mContext;

    public ImageAdapter(Context c) {
        mContext = c;
    }

    public int getCount() {
        return mThumbIds.length;
    }

    public Object getItem(int position) {
        return null;
    }

    public long getItemId(int position) {
        return 0;
    }

    // create a new ImageView for each item referenced by the Adapter
    public View getView(int position, View convertView, ViewGroup parent) {
        ImageView imageView;
        if (convertView == null) {
            // if it's not recycled, initialize some attributes
            imageView = new ImageView(mContext);
            imageView.setLayoutParams(new GridView.LayoutParams(200, 200));
            imageView.setScaleType(ImageView.ScaleType.FIT_XY);
            imageView.setPadding(10,10,10,10);
        } else {
            imageView = (ImageView) convertView;
        }

        imageView.setImageResource(mThumbIds[position]);
        return imageView;
    }


    // create a new ImageView for each item referenced by the Adapter
    public int getDrawableID(int position, View convertView, ViewGroup parent) {
        return (mThumbIds[position]);
    }

    // references to our images
    private Integer[] mThumbIds = {
           R.drawable.circle_alex,
           R.drawable.circle_cal,
           R.drawable.circle_keith,
           R.drawable.circle_carolyn1,
           R.drawable.circle_carolyn2,
           R.drawable.circle_charlie,
           R.drawable.circle_dylan,
           R.drawable.circle_oscar,
           R.drawable.circle_farida,
           R.drawable.circle_sam,
           R.drawable.circle_mo,
           R.drawable.circle_nick

    };


}
