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
        return inflater.inflate(R.layout.fragment_topic, container, false)
    }

    /**
     * Called immediately after [.onCreateView]
     * has returned, but before any saved state has been restored in to the view.
     * This gives subclasses a chance to initialize themselves once
     * they know their view hierarchy has been completely created.  The fragment's
     * view hierarchy is not however attached to its parent at this point.
     * @param view The View returned by [.onCreateView].
     * @param savedInstanceState If non-null, this fragment is being re-constructed
     * from a previous saved state as given here.
     */
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val activity = activity as AppCompatActivity?
        activity!!.setSupportActionBar(toolbar)
        toolbar.setTitle(R.string.topics)
        setupRecyclerView()
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
