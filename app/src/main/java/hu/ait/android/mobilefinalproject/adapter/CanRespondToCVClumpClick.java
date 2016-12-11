package hu.ait.android.mobilefinalproject.adapter;

import hu.ait.android.mobilefinalproject.model.Clump;

/**
 * CanRespondToCVClumpClick.java
 *
 * Created by Carolyn Ryan
 * 11/29/2016
 *
 * An interface to ensure that the given class can be used by our ClumpRecyclerAdapter
 * Particularly that it can respond to card view clicks
 */
public interface CanRespondToCVClumpClick {
    public void respondToCVClumpClick(Clump clump);
}
