package com.sortedqueue.learnandroid.dashboard

/**
 * Created by Alok on 05/06/17.
 */

interface DashboardNavigationListener {
    fun loadDashboardFragment()
    fun loadTopicFragment()
    fun loadPresentationFragment(mainTitle: String, topic: String, topicIndex: Int, topicArray: Array<String>)
    fun onNavigateBack()
    fun showNavigateToNextTopic()
    fun hideNavigateToLayout()
}
