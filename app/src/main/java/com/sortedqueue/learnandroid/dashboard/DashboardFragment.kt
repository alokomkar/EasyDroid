package com.sortedqueue.learnandroid.dashboard

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.sortedqueue.learnandroid.R
import com.sortedqueue.learnandroid.constants.LearnDroidConstants
import kotlinx.android.synthetic.main.fragment_dashboard.*

/**
 * Created by Alok on 05/06/17.
 */

class DashboardFragment : Fragment() {

    private var dashboardNavigationListener: DashboardNavigationListener? = null

    private val adapterClickListener = object : AdapterClickListener {
        override fun onClick(position: Int, itemType: Int, topic: String, nextTopic: Array<String>) {
            var mainTitle = "Learn Android"
            when (itemType) {
                LearnDroidConstants.INDEX_ACTIVITY -> mainTitle = "Activity"
                LearnDroidConstants.INDEX_ANDROID_OS -> mainTitle = "Android OS"
                LearnDroidConstants.INDEX_ASYNC_TASK -> mainTitle = "AsyncTask"
                LearnDroidConstants.INDEX_FRAGMENT -> mainTitle = "Fragments"
                LearnDroidConstants.INDEX_MANIFEST -> mainTitle = "Manifest"
                LearnDroidConstants.INDEX_INTENT -> mainTitle = "Intents"
                LearnDroidConstants.INDEX_VIEW -> mainTitle = "Views, ViewGroups, LayoutManagers"
                LearnDroidConstants.INDEX_BACKGROUND -> mainTitle = "Background"
            }

            if (dashboardNavigationListener != null)
                dashboardNavigationListener!!.loadPresentationFragment(mainTitle, topic, position, nextTopic)
        }

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_dashboard, container, false)
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
        toolbar.setTitle(R.string.app_name)
        setupRecyclerViews()
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        if (context is DashboardNavigationListener) {
            dashboardNavigationListener = context
        }
    }

    override fun onDetach() {
        super.onDetach()
        dashboardNavigationListener = null
    }

    private fun setupRecyclerViews() {

        androidOSRecyclerView.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        androidManifestRecyclerView.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        activityRecyclerView.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        fragmentRecyclerView.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        intentRecyclerView.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        asyncTaskRecyclerView.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        viewsRecyclerView.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        backgroundRecyclerView.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)

        var typeIndex = 1
        val resources = resources
        androidOSRecyclerView.adapter = TopicsRecyclerViewAdapter(context!!, typeIndex++, resources.getStringArray(R.array.android_os_array), R.color.md_amber_800, adapterClickListener)
        androidManifestRecyclerView.adapter = TopicsRecyclerViewAdapter(context!!, typeIndex++, resources.getStringArray(R.array.android_manifest_array), R.color.md_cyan_500, adapterClickListener)
        viewsRecyclerView.adapter = TopicsRecyclerViewAdapter(context!!, typeIndex++, resources.getStringArray(R.array.views_array), R.color.md_purple_500, adapterClickListener)
        activityRecyclerView.adapter = TopicsRecyclerViewAdapter(context!!, typeIndex++, resources.getStringArray(R.array.activity_array), R.color.md_brown_700, adapterClickListener)
        fragmentRecyclerView.adapter = TopicsRecyclerViewAdapter(context!!, typeIndex++, resources.getStringArray(R.array.fragment_array), R.color.md_blue_grey_700, adapterClickListener)
        intentRecyclerView.adapter = TopicsRecyclerViewAdapter(context!!, typeIndex++, resources.getStringArray(R.array.intent_array), R.color.md_light_green_900, adapterClickListener)
        asyncTaskRecyclerView.adapter = TopicsRecyclerViewAdapter(context!!, typeIndex++, resources.getStringArray(R.array.async_task_array), R.color.md_cyan_900, adapterClickListener)
        backgroundRecyclerView.adapter = TopicsRecyclerViewAdapter(context!!, typeIndex, resources.getStringArray(R.array.background_array), R.color.md_deep_orange_900, adapterClickListener)

    }

}
