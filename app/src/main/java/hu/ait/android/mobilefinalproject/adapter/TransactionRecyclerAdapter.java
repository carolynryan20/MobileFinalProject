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
 * Recycler Adapter class for user's list of clumps
 */
public class TransactionRecyclerAdapter extends RecyclerView.Adapter<TransactionRecyclerAdapter.ViewHolder> {

    private List<Transaction> transactionList;
    private List<String> clumpKeys;

    private CanRespondToCVTransactionClick canRespondToCVTransactionClick;
    private Context context;

    private DatabaseReference clumpsRef;
    private TransactionFragment parentFragment;

    public TransactionRecyclerAdapter(Context context, String uid, TransactionFragment parentFragment) {
        this.transactionList = new ArrayList<>();
        this.clumpKeys = new ArrayList<>();
        this.parentFragment = parentFragment;
        this.context = context;
        clumpsRef = FirebaseDatabase.getInstance()
                .getReference("users").child(uid).child("clumps");
        checkActivityImplementsResponseInterface();
    }

    private void checkActivityImplementsResponseInterface() {
        if (context instanceof CanRespondToCVTransactionClick) {
            this.canRespondToCVTransactionClick = (CanRespondToCVTransactionClick) context;
        } else {
            throw new RuntimeException("Activity does not implement CanRespondToCVTransactionClick interface");
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.clump_row, viewGroup, false);
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
        viewHolder.tvClumpName.setText(tmpTransaction.getTitle());
        viewHolder.ivClumpIcon.setImageResource(tmpTransaction.getType().getIconId());
    }

    private void setButtonClicks(final ViewHolder viewHolder, final Transaction tmpTransaction) {
        setDeleteButton(viewHolder);
        setEditButton(viewHolder, tmpTransaction);
    }

    private void setDeleteButton(final ViewHolder viewHolder) {
        viewHolder.btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                removeClump(viewHolder.getAdapterPosition());
            }
        });
    }

    private void setEditButton(final ViewHolder viewHolder, final Transaction tmpTransaction) {
        viewHolder.btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String thisKey = clumpKeys.get(viewHolder.getAdapterPosition());
                showEditDialog(tmpTransaction, thisKey, viewHolder.getAdapterPosition());
            }
        });
    }

    private void setCardViewClick(final ViewHolder viewHolder) {
        viewHolder.cvClump.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                canRespondToCVTransactionClick.respondToCVClumpClick((transactionList.get(viewHolder.getAdapterPosition())));
            }
        });
    }

    public void showEditDialog(Transaction transactionToEdit, String key, int position) {
        parentFragment.openAddClumpFragment(transactionToEdit, key);
        notifyItemChanged(position);
    }

    @Override
    public int getItemCount() {
        return transactionList.size();
    }

    public void addClump(Transaction transaction, String key) {
        if (!clumpKeys.contains(key)) {
            transactionList.add(0, transaction);
            clumpKeys.add(0, key);
            notifyDataSetChanged();
        }
    }

    public void addClump(Transaction transaction, int index) {
        transactionList.add(index, transaction);
        notifyDataSetChanged();
    }

    public void removeClump(int index) {
        clumpsRef.child(clumpKeys.get(index)).removeValue();
        if (index != -1) {
            transactionList.remove(index);
            clumpKeys.remove(index);
            notifyItemRemoved(index);
        }
    }

    public void editClump(Transaction transaction, String key) {
        int idx = clumpKeys.indexOf(key);
        transactionList.set(idx, transaction);
        notifyDataSetChanged();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView tvClumpName;
        public ImageButton btnDelete;
        public ImageButton btnEdit;
        public CardView cvClump;
        public ImageView ivClumpIcon;

        public ViewHolder(View itemView) {
            super(itemView);
            tvClumpName = (TextView) itemView.findViewById(R.id.tvClumpName);
            btnDelete = (ImageButton) itemView.findViewById(R.id.btnDelete);
            btnEdit = (ImageButton) itemView.findViewById(R.id.btnEdit);
            cvClump = (CardView) itemView.findViewById(R.id.cvClump);
            ivClumpIcon = (ImageView) itemView.findViewById(R.id.ivClumpIcon);
        }
    }
}