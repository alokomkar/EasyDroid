package com.sortedqueue.learnandroid.topic

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.helper.ItemTouchHelper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.sortedqueue.learnandroid.R
import com.sortedqueue.learnandroid.utils.SimpleItemTouchHelperCallback

import kotlinx.android.synthetic.main.fragment_topic.*

/**
 * Created by Alok on 05/06/17.
 */

class TopicFragment : Fragment() {


    private var optionsRecyclerAdapter: OptionsRecyclerAdapter? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val fragmentView = inflater.inflate(R.layout.fragment_topic, container, false)

        val activity = activity as AppCompatActivity?
        activity!!.setSupportActionBar(toolbar)
        toolbar.setTitle(R.string.topics)
        setupRecyclerView()
        return fragmentView
    }

    private fun setupRecyclerView() {
        optionsRecyclerView.setLayoutManager(LinearLayoutManager(context))
        optionsRecyclerAdapter = OptionsRecyclerAdapter()
        optionsRecyclerView.setAdapter(optionsRecyclerAdapter)
        val callback = SimpleItemTouchHelperCallback(optionsRecyclerAdapter)
        val touchHelper = ItemTouchHelper(callback)
        touchHelper.attachToRecyclerView(optionsRecyclerView)
    }
}
