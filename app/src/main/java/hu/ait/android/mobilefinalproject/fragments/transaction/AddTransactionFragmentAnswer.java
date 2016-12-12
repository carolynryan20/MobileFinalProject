package hu.ait.android.mobilefinalproject.fragments.transaction;

import hu.ait.android.mobilefinalproject.model.Transaction;

/**
 * AddTransactionFragmentAnswer.java
 *
 * Created by Carolyn Ryan
 * 11/29/2016
 *
 * Ensures implementing class can add and edit transactions
 */
public interface AddTransactionFragmentAnswer {
    public void addTransaction(Transaction transaction);
    public void addEditTransaction(Transaction transaction, String key);

}
