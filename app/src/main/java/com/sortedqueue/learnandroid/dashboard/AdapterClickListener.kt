package com.sortedqueue.learnandroid.dashboard

/**
 * Created by Alok on 05/06/17.
 */

interface AdapterClickListener {
    fun onClick(position: Int, itemType: Int, topic: String, nextTopic: Array<String>)
}
