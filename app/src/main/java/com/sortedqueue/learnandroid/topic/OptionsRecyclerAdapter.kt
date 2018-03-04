package com.sortedqueue.learnandroid.topic

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView

import com.sortedqueue.learnandroid.R
import com.sortedqueue.learnandroid.utils.ItemTouchHelperAdapter

/**
 * Created by Alok on 05/06/17.
 */

class OptionsRecyclerAdapter : RecyclerView.Adapter<OptionsRecyclerAdapter.ViewHolder>(), ItemTouchHelperAdapter {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val adapterView = LayoutInflater.from(parent.context).inflate(R.layout.item_option, parent, false)
        return ViewHolder(adapterView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

    }


    override fun getItemCount(): Int {
        return 4
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {

        internal var optionTextView: TextView? = itemView.findViewById(R.id.optionTextView)
        internal var reorderImageView: ImageView? = itemView.findViewById(R.id.reorderImageView)
        internal var optionCardView: RelativeLayout? = itemView.findViewById(R.id.optionCardView)

        init {
            itemView.setOnClickListener(this)
        }

        override fun onClick(view: View) {

        }
    }

    override fun onItemMove(fromPosition: Int, toPosition: Int) {
        if (fromPosition < toPosition) {
            /*for (int i = fromPosition; i < toPosition; i++) {
                Collections.swap(optionModels, i, i + 1);
            }*/
        } else {
            /*for (int i = fromPosition; i > toPosition; i--) {
                Collections.swap(optionModels, i, i - 1);
            }*/
        }
        notifyItemMoved(fromPosition, toPosition)
    }

    override fun onItemDismiss(position: Int) {
        //interviewQuestionModel.getOptionModels().remove(position);
        notifyItemRemoved(position)
    }

}
