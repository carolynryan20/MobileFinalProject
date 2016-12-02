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
import android.widget.TextView;

import java.util.Arrays;
import java.util.List;

import hu.ait.android.mobilefinalproject.R;
import hu.ait.android.mobilefinalproject.data.Clump;
import hu.ait.android.mobilefinalproject.model.User;

/**
 * Created by Carolyn on 12/1/16.
 */

public class ClumpRecyclerAdapter extends RecyclerView.Adapter<ClumpRecyclerAdapter.ViewHolder> {

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView tvClumpName;
        public ImageButton btnDelete;
        public CardView cvClump;

        public ViewHolder(View itemView) {
            super(itemView);
            tvClumpName = (TextView) itemView.findViewById(R.id.tvClumpName);
            btnDelete = (ImageButton) itemView.findViewById(R.id.btnDelete);
            cvClump = (CardView) itemView.findViewById(R.id.cvClump);
        }
    }

    private List<Clump> clumpList;
    private CanRespondToCVClumpClick canRespondToCVClumpClick;
    private Context context;
    private int lastPosition = -1;

    public ClumpRecyclerAdapter(Context context, User myUser) {
        //this.clumpList = myUser.getClumps();
        List<Clump> clumpp = Arrays.asList(
                new Clump("Roommates"),
                new Clump("AIT"),
                new Clump("Rome Trip")
        );

        this.clumpList.addAll(clumpp);
        this.context = context;

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
        viewHolder.tvClumpName.setText(clumpList.get(position).getName());

        viewHolder.btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                removeClump(viewHolder.getAdapterPosition());
            }
        });
        viewHolder.cvClump.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                canRespondToCVClumpClick.respondToCVClumpClick((clumpList.get(viewHolder.getAdapterPosition()).getName()));
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
        return clumpList.size();
    }

    public void addClump(Clump clump) {
        clumpList.add(0, clump);
        // refresh the whole list
        notifyDataSetChanged();
        // refresh only one position
        notifyItemInserted(0);
    }

    public void removeClump(int index) {
        // remove it from the DB
        // remove it from the list
        clumpList.remove(index);
        notifyDataSetChanged();
    }

    public void removeAllClumps() {
        while (! clumpList.isEmpty()) {
            clumpList.remove(0);
            notifyItemRemoved(0);
        }
    }

    public Clump getCity(int i) {
        return clumpList.get(i);
    }

}
