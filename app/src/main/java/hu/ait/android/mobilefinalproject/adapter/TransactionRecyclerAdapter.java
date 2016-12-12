package hu.ait.android.mobilefinalproject.adapter;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

import hu.ait.android.mobilefinalproject.R;
import hu.ait.android.mobilefinalproject.fragments.transaction.TransactionFragment;
import hu.ait.android.mobilefinalproject.model.Transaction;

/**
 * TransactionRecyclerAdapter.java
 *
 * Created by Carolyn Ryan
 * 11/29/2016
 *
 * Recycler Adapter class for user's list of transactions
 */
public class TransactionRecyclerAdapter extends RecyclerView.Adapter<TransactionRecyclerAdapter.ViewHolder> {

    private List<Transaction> transactionList;
    private List<String> transactionKeys;

    private CanRespondToCVTransactionClick canRespondToCVTransactionClick;
    private Context context;

    private DatabaseReference transactionsRef;
    private TransactionFragment parentFragment;

    public TransactionRecyclerAdapter(Context context, String uid, TransactionFragment parentFragment) {
        this.transactionList = new ArrayList<>();
        this.transactionKeys = new ArrayList<>();
        this.parentFragment = parentFragment;
        this.context = context;
        transactionsRef = FirebaseDatabase.getInstance()
                .getReference("users").child(uid).child("transactions");
        checkActivityImplementsResponseInterface();
    }

    private void checkActivityImplementsResponseInterface() {
        if (context instanceof CanRespondToCVTransactionClick) {
            this.canRespondToCVTransactionClick = (CanRespondToCVTransactionClick) context;
        } else {
            throw new RuntimeException(context.getString(R.string.cv_transaction_click_error));
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.transaction_row, viewGroup, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, final int position) {
        final Transaction tmpTransaction = transactionList.get(position);

        setTitleAndIcon(viewHolder, tmpTransaction);
        setButtonClicks(viewHolder, tmpTransaction);
        setCardViewClick(viewHolder);
    }

    private void setTitleAndIcon(ViewHolder viewHolder, Transaction tmpTransaction) {
        viewHolder.tvTransactionName.setText(tmpTransaction.getTitle());
        viewHolder.ivTransactionIcon.setImageResource(tmpTransaction.getType().getIconId());
    }

    private void setButtonClicks(final ViewHolder viewHolder, final Transaction tmpTransaction) {
        setDeleteButton(viewHolder);
        setEditButton(viewHolder, tmpTransaction);
    }

    private void setDeleteButton(final ViewHolder viewHolder) {
        viewHolder.btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                removeTransaction(viewHolder.getAdapterPosition());
            }
        });
    }

    private void setEditButton(final ViewHolder viewHolder, final Transaction tmpTransaction) {
        viewHolder.btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String thisKey = transactionKeys.get(viewHolder.getAdapterPosition());
                showEditDialog(tmpTransaction, thisKey, viewHolder.getAdapterPosition());
            }
        });
    }

    private void setCardViewClick(final ViewHolder viewHolder) {
        viewHolder.cvTransaction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                canRespondToCVTransactionClick.respondToCVTransactionClick((transactionList.get(viewHolder.getAdapterPosition())));
            }
        });
    }

    public void showEditDialog(Transaction transactionToEdit, String key, int position) {
        parentFragment.openAddTransactionFragment(transactionToEdit, key);
        notifyItemChanged(position);
    }

    @Override
    public int getItemCount() {
        return transactionList.size();
    }

    public void addTransaction(Transaction transaction, String key) {
        if (!transactionKeys.contains(key)) {
            transactionList.add(0, transaction);
            transactionKeys.add(0, key);
            notifyDataSetChanged();
        }
    }

    public void addTransaction(Transaction transaction, int index) {
        transactionList.add(index, transaction);
        notifyDataSetChanged();
    }

    public void removeTransaction(int index) {
        transactionsRef.child(transactionKeys.get(index)).removeValue();
        if (index != -1) {
            transactionList.remove(index);
            transactionKeys.remove(index);
            notifyItemRemoved(index);
        }
    }

    public void editTransaction(Transaction transaction, String key) {
        int idx = transactionKeys.indexOf(key);
        transactionList.set(idx, transaction);
        notifyDataSetChanged();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView tvTransactionName;
        public ImageButton btnDelete;
        public ImageButton btnEdit;
        public CardView cvTransaction;
        public ImageView ivTransactionIcon;

        public ViewHolder(View itemView) {
            super(itemView);
            tvTransactionName = (TextView) itemView.findViewById(R.id.tvTransactionName);
            btnDelete = (ImageButton) itemView.findViewById(R.id.btnDelete);
            btnEdit = (ImageButton) itemView.findViewById(R.id.btnEdit);
            cvTransaction = (CardView) itemView.findViewById(R.id.cvTransaction);
            ivTransactionIcon = (ImageView) itemView.findViewById(R.id.ivTransactionIcon);
        }
    }
}