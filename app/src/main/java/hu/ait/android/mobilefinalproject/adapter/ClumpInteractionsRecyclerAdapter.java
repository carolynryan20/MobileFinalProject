package hu.ait.android.mobilefinalproject.adapter;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;

import java.util.ArrayList;
import java.util.List;

import hu.ait.android.mobilefinalproject.R;
import hu.ait.android.mobilefinalproject.model.Transaction;

/**
 * Created by Carolyn on 12/5/16.
 */

public class ClumpInteractionsRecyclerAdapter extends RecyclerView.Adapter<ClumpInteractionsRecyclerAdapter.ViewHolder> {
    private List<Transaction> transactionList;
    private List<String> interactionKey;
//    private CanRespondToCVClumpClick canRespondToCVClumpClick;
    private Context context;
    private int lastPosition = -1;
    private DatabaseReference interactionsRef;


    public ClumpInteractionsRecyclerAdapter(Context context, String uid) {
        //this.clumpList = myUser.getClumps();
        this.transactionList = new ArrayList<>();
        this.interactionKey = new ArrayList<>();

        this.context = context;

        // TODO: 12/5/16 Where to store interactions firebase
        //interactionsRef = FirebaseDatabase.getInstance().getReference("users")
          //      .getRef().child(uid).child("clumps");

        checkActivityImplementsResponseInterface();
    }

    private void checkActivityImplementsResponseInterface() {
//        if (context instanceof CanRespondToCVClumpClick) {
//            this.canRespondToCVClumpClick = (CanRespondToCVClumpClick) context;
//        } else {
//            throw new RuntimeException("Activity does not implement CanRespondToCVClumpClick interface");
//        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.transaction_row, viewGroup, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position) {
        final Transaction tmpTransaction = transactionList.get(position);
        viewHolder.tvInteractionTitle.setText(tmpTransaction.getTitle());

        viewHolder.btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //removeInteraction(viewHolder.getAdapterPosition());
            }
        });

        viewHolder.cvInteraction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               // flipCardView();
            }
        });

        setAnimation(viewHolder.itemView, viewHolder.getAdapterPosition());

    }

    private void setAnimation(View viewToAnimate, int position) {
        // If the bound view wasn't previously displayed on screen, it's animated
        if (position > lastPosition) {
            Animation animation = AnimationUtils.loadAnimation(context, android.R.anim.slide_in_left);
            viewToAnimate.startAnimation(animation);
            lastPosition = position;
        }
    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public void addInteraction(Transaction transaction, String key) {
        transactionList.add(0, transaction);
        interactionKey.add(0, key);
        // refresh the whole list
        notifyDataSetChanged();
        // refresh only one position
        notifyItemInserted(0);
    }

    public void removeInteraction(int index) {
        // remove it from the DB TODO: 12/4/16
        interactionsRef.child(interactionKey.get(index)).removeValue();

        if (index != -1) {
            transactionList.remove(index);
            interactionKey.remove(index);
            notifyItemRemoved(index);
        }
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {;
        public TextView tvInteractionTitle;
        public ImageView ivInteractionType;
        public ImageButton btnDelete;
        public CardView cvInteraction;

        public ViewHolder(View itemView) {
            super(itemView);
            tvInteractionTitle = (TextView) itemView.findViewById(R.id.tvInteractionTitle);
            ivInteractionType = (ImageView) itemView.findViewById(R.id.ivInteractionIcon);
            btnDelete = (ImageButton) itemView.findViewById(R.id.btnDelete);
            cvInteraction = (CardView) itemView.findViewById(R.id.cvInteraction);
        }
    }

}
