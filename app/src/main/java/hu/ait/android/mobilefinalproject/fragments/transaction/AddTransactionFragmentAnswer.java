package hu.ait.android.mobilefinalproject.fragments.transaction;

import hu.ait.android.mobilefinalproject.model.Transaction;

/**
 * Created by Carolyn on 12/4/16.
 */

public interface AddTransactionFragmentAnswer {
    public void addClump(Transaction transaction);

//    public void addEditClump(Transaction transaction, int index);
      public void addEditClump(Transaction transaction, String key);

}
