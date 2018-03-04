package com.sortedqueue.learnandroid.dashboard

import android.content.Context
import android.support.v4.content.ContextCompat
import android.support.v7.widget.CardView
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView

import com.sortedqueue.learnandroid.R

/**
 * Created by Alok on 05/06/17.
 */

class TopicsRecyclerViewAdapter(private val context: Context, private val typeIndex: Int, private val topicsArray: Array<String>, private val color: Int, private val adapterClickListener: AdapterClickListener) : RecyclerView.Adapter<TopicsRecyclerViewAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val adapterView = LayoutInflater.from(parent.context).inflate(R.layout.item_topic, parent, false)
        return ViewHolder(adapterView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.topicTextView!!.text = topicsArray[position]
    }


    override fun getItemCount(): Int {
        return topicsArray.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {

        internal var topicTextView: TextView? = itemView.findViewById(R.id.topicTextView)
        internal var topicCardView: CardView? = itemView.findViewById(R.id.topicCardView)

        init {
            itemView.setOnClickListener(this)
            topicCardView!!.setCardBackgroundColor(ContextCompat.getColor(context, color))
        }

        override fun onClick(view: View) {
            val position = adapterPosition
            if (position != RecyclerView.NO_POSITION) {


                adapterClickListener.onClick(position, typeIndex, topicsArray[position], topicsArray)
            }
        }
    }


}
