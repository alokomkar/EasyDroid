package com.sortedqueue.learnandroid.dashboard;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.sortedqueue.learnandroid.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Alok on 05/06/17.
 */

public class TopicsRecyclerViewAdapter extends RecyclerView.Adapter<TopicsRecyclerViewAdapter.ViewHolder>  {

    private int color;
    private Context context;
    private AdapterClickListener adapterClickListener;
    private int typeIndex;
    private String[] topicsArray;

    public TopicsRecyclerViewAdapter(Context context, int typeIndex, String[] topicsArray, int color, AdapterClickListener adapterClickListener) {
        this.color = color;
        this.context = context;
        this.typeIndex = typeIndex;
        this.topicsArray = topicsArray;
        this.adapterClickListener = adapterClickListener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View adapterView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_topic, parent, false);
        return new ViewHolder(adapterView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.topicTextView.setText(topicsArray[position]);
    }


    @Override
    public int getItemCount() {
        return topicsArray.length;
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        @BindView(R.id.topicTextView)
        TextView topicTextView;
        @BindView(R.id.topicCardView)
        CardView topicCardView;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(this);
            topicCardView.setCardBackgroundColor(context.getResources().getColor(color));
        }

        @Override
        public void onClick(View view) {
            int position = getAdapterPosition();
            if( position != RecyclerView.NO_POSITION ) {


                adapterClickListener.onClick(position, typeIndex, topicsArray[position], topicsArray);
            }
        }
    }



}
