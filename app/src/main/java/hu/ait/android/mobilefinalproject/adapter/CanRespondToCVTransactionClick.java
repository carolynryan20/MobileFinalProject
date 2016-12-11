package hu.ait.android.mobilefinalproject.adapter;

import hu.ait.android.mobilefinalproject.model.Transaction;

/**
 * CanRespondToCVTransactionClick.java
 *
 * Created by Carolyn Ryan
 * 11/29/2016
 *
 * An interface to ensure that the given class can be used by our TransactionRecyclerAdapter
 * Particularly that it can respond to card view clicks
 */
public interface CanRespondToCVTransactionClick {
    public void respondToCVTransactionClick(Transaction transaction);
}
