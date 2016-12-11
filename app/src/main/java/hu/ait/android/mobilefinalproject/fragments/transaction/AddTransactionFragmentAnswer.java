package hu.ait.android.mobilefinalproject.fragments.transaction;

import hu.ait.android.mobilefinalproject.model.Transaction;

/**
 * Created by Carolyn on 12/4/16.
 */

public interface AddTransactionFragmentAnswer {
    public void addTransaction(Transaction transaction);

//    public void addEditTransaction(Transaction transaction, int index);
      public void addEditTransaction(Transaction transaction, String key);

}
