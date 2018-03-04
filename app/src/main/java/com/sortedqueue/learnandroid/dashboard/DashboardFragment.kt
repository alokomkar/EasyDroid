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

    private val adapterClickListener = AdapterClickListener { position, itemType, topic, topicsArray ->
        var mainTitle = "Learn Android"
        when (itemType) {
            LearnDroidConstants.INDEX_ACTIVITY -> mainTitle = "Activity"
            LearnDroidConstants.INDEX_ANDROID_OS -> mainTitle = "Android OS"
            LearnDroidConstants.INDEX_ASYNC_TASK -> mainTitle = "AsyncTask"
            LearnDroidConstants.INDEX_FRAGMENT -> mainTitle = "Fragments"
            LearnDroidConstants.INDEX_MANIFEST -> mainTitle = "Manifest"
            LearnDroidConstants.INDEX_INTENT -> mainTitle = "Intents"
            LearnDroidConstants.INDEX_VIEW -> mainTitle = "Views, ViewGroups, LayoutManagers"
        }

        if (dashboardNavigationListener != null)
            dashboardNavigationListener!!.loadPresentationFragment(mainTitle, topic, position, topicsArray)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val fragmentView = inflater.inflate(R.layout.fragment_dashboard, container, false)
        val activity = activity as AppCompatActivity?
        activity!!.setSupportActionBar(toolbar)
        toolbar.setTitle(R.string.app_name)

        setupRecyclerViews()
        return fragmentView
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

        androidOSRecyclerView.setLayoutManager(LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false))
        androidManifestRecyclerView.setLayoutManager(LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false))
        activityRecyclerView.setLayoutManager(LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false))
        fragmentRecyclerView.setLayoutManager(LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false))
        intentRecyclerView.setLayoutManager(LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false))
        asyncTaskRecyclerView.setLayoutManager(LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false))
        viewsRecyclerView.setLayoutManager(LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false))

        var typeIndex = 1
        val resources = resources
        androidOSRecyclerView.setAdapter(TopicsRecyclerViewAdapter(context, typeIndex++, resources.getStringArray(R.array.android_os_array), R.color.md_amber_800, adapterClickListener))
        androidManifestRecyclerView.setAdapter(TopicsRecyclerViewAdapter(context, typeIndex++, resources.getStringArray(R.array.android_manifest_array), R.color.md_cyan_500, adapterClickListener))
        viewsRecyclerView.setAdapter(TopicsRecyclerViewAdapter(context, typeIndex++, resources.getStringArray(R.array.views_array), R.color.md_purple_500, adapterClickListener))
        activityRecyclerView.setAdapter(TopicsRecyclerViewAdapter(context, typeIndex++, resources.getStringArray(R.array.activity_array), R.color.md_brown_700, adapterClickListener))
        fragmentRecyclerView.setAdapter(TopicsRecyclerViewAdapter(context, typeIndex++, resources.getStringArray(R.array.fragment_array), R.color.md_blue_grey_700, adapterClickListener))
        intentRecyclerView.setAdapter(TopicsRecyclerViewAdapter(context, typeIndex++, resources.getStringArray(R.array.intent_array), R.color.md_light_green_900, adapterClickListener))
        asyncTaskRecyclerView.setAdapter(TopicsRecyclerViewAdapter(context, typeIndex++, resources.getStringArray(R.array.async_task_array), R.color.md_cyan_900, adapterClickListener))

    }

}
