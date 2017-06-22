package com.sortedqueue.learnandroid.dashboard;

/**
 * Created by Alok on 05/06/17.
 */

public interface DashboardNavigationListener {
    void loadDashboardFragment();
    void loadTopicFragment();
    void loadPresentationFragment(String mainTitle, String topic, int topicIndex, String[] topicArray);
    String getCurrentTopic();
    String getCurrentMainTitle();
    void onNavigateBack();
    void showNavigateToNextTopic();
}
