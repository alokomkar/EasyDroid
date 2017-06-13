package com.sortedqueue.learnandroid.dashboard;

import android.speech.tts.UtteranceProgressListener;

/**
 * Created by Alok on 05/06/17.
 */

public interface DashboardNavigationListener {
    void loadDashboardFragment();
    void loadTopicFragment();
    void loadPresentationFragment(String mainTitle, String topic);
    String getCurrentTopic();
    String getCurrentMainTitle();
    void onNavigateBack();
}
