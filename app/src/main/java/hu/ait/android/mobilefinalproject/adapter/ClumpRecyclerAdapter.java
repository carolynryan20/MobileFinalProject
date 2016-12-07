package hu.ait.android.mobilefinalproject.adapter;

import android.content.Context;
import android.content.Intent;
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
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

import hu.ait.android.mobilefinalproject.NavDrawerActivity;
import hu.ait.android.mobilefinalproject.R;
import hu.ait.android.mobilefinalproject.fragments.ClumpFragment;
import hu.ait.android.mobilefinalproject.model.Clump;

/**
 * Created by Carolyn on 12/1/16.
 */

public class ClumpRecyclerAdapter extends RecyclerView.Adapter<ClumpRecyclerAdapter.ViewHolder> {

    private List<Clump> clumpList;
    private List<String> clumpKeys;
    private String uid;
    private CanRespondToCVClumpClick canRespondToCVClumpClick;
    private Context context;
    private int lastPosition = -1;
    private DatabaseReference clumpsRef;
    private ClumpFragment parentFragment;

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

    public ClumpRecyclerAdapter(Context context, String uid, ClumpFragment parentFragment) {
        //this.clumpList = myUser.getClumps();


        this.clumpList = new ArrayList<>();
        this.clumpKeys = new ArrayList<>();
        this.uid = uid;
        this.parentFragment = parentFragment;

        this.context = context;

//        clumpsRef = FirebaseDatabase.getInstance().getReference("clumps");
        clumpsRef = FirebaseDatabase.getInstance().getReference("users")
                .child(uid).child("clumps");


        checkActivityImplementsResponseInterface();
    }

    private void checkActivityImplementsResponseInterface() {
        if (context instanceof CanRespondToCVClumpClick) {
            this.canRespondToCVClumpClick = (CanRespondToCVClumpClick) context;
        } else {
            throw new RuntimeException("Activity does not implement CanRespondToCVClumpClick interface");
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
        final Clump tmpClump = clumpList.get(position);
        viewHolder.tvClumpName.setText(tmpClump.getTitle());
        viewHolder.ivClumpIcon.setImageResource(tmpClump.getType().getIconId());


        viewHolder.btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                removeClump(viewHolder.getAdapterPosition());
            }
        });

        viewHolder.btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showEditDialog(tmpClump, viewHolder.getAdapterPosition());
            }
        });

        viewHolder.cvClump.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                canRespondToCVClumpClick.respondToCVClumpClick((clumpList.get(viewHolder.getAdapterPosition())));
            }
        });

        setAnimation(viewHolder.itemView, viewHolder.getAdapterPosition());
    }

    public void showEditDialog(Clump clumpToEdit, int position) {
        parentFragment.openAddClumpFragment(clumpToEdit, position);
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
        return clumpList.size();
    }

    public void addClump(Clump clump, String key) {
        clumpList.add(0, clump);
        clumpKeys.add(0, key);
        // refresh the whole list
        notifyDataSetChanged();
        // refresh only one position
        notifyItemInserted(0);
    }

    public void addClump(Clump clump, int index) {
        clumpList.add(index, clump);
        // refresh the whole list
        notifyDataSetChanged();
    }

    public void removeClump(int index) {
        // remove it from the DB TODO: 12/4/16
        clumpsRef.child(clumpKeys.get(index)).removeValue();
        if (index != -1) {
            clumpList.remove(index);
            clumpKeys.remove(index);
            notifyItemRemoved(index);
        }
    }

    public boolean checkClumpAlreadyInList(Clump toCheck) {
        for (Clump clump : clumpList) {
           if (clump.equals(toCheck)){
               return true;
           }
        }
        return false;
    }

    public void editClump(Clump clump, int index) {
        removeClump(index);
        addClump(clump, index);
    }

    public void removeAllClumps() {
        while (!clumpList.isEmpty()) {
            clumpList.remove(0);
            notifyItemRemoved(0);
        }
    }

    public Clump getCity(int i) {
        return clumpList.get(i);
    }

}
