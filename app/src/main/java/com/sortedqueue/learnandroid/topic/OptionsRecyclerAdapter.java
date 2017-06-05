package com.sortedqueue.learnandroid.topic;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sortedqueue.learnandroid.R;
import com.sortedqueue.learnandroid.utils.ItemTouchHelperAdapter;

/**
 * Created by Alok on 05/06/17.
 */

public class OptionsRecyclerAdapter extends RecyclerView.Adapter<OptionsRecyclerAdapter.ViewHolder> implements ItemTouchHelperAdapter {

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View adapterView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_option, parent, false);
        return new ViewHolder(adapterView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

    }


    @Override
    public int getItemCount() {
        return 4;
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public ViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {

        }
    }

    @Override
    public void onItemMove(int fromPosition, int toPosition) {
        if (fromPosition < toPosition) {
            /*for (int i = fromPosition; i < toPosition; i++) {
                Collections.swap(optionModels, i, i + 1);
            }*/
        } else {
            /*for (int i = fromPosition; i > toPosition; i--) {
                Collections.swap(optionModels, i, i - 1);
            }*/
        }
        notifyItemMoved(fromPosition, toPosition);
    }

    @Override
    public void onItemDismiss(int position) {
        //interviewQuestionModel.getOptionModels().remove(position);
        notifyItemRemoved(position);
    }
}
