package com.sortedqueue.learnandroid;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import com.sortedqueue.learnandroid.asynctasks.FileReaderTask;
import com.sortedqueue.learnandroid.dashboard.DashboardFragment;
import com.sortedqueue.learnandroid.dashboard.DashboardNavigationListener;
import com.sortedqueue.learnandroid.topic.TopicFragment;

public class MainActivity extends AppCompatActivity implements FileReaderTask.OnDataReadListener, DashboardNavigationListener {

    private FragmentTransaction mFragmentTransaction;
    private DashboardFragment dashboardFragment;
    private TopicFragment topicFragment;
    private String currentFragmentTAG = "";

    //private CodeView codeView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        loadDashboardFragment();
        //codeView = (CodeView) findViewById(R.id.codeView);
        //new FileReaderTask(MainActivity.this, "chapter_1", this).execute();

    }

    @Override
    public void loadDashboardFragment() {
        currentFragmentTAG = "Dashboard";
        mFragmentTransaction = getSupportFragmentManager().beginTransaction();
        dashboardFragment = (DashboardFragment) getSupportFragmentManager().findFragmentByTag(DashboardFragment.class.getSimpleName());
        if (dashboardFragment == null) {
            dashboardFragment = new DashboardFragment();
        }
        mFragmentTransaction.setCustomAnimations(R.anim.anim_slide_in_left, R.anim.anim_slide_out_right);
        mFragmentTransaction.replace(R.id.container, dashboardFragment, DashboardFragment.class.getSimpleName());
        mFragmentTransaction.commit();
    }

    @Override
    public void loadTopicFragment() {
        currentFragmentTAG = "Topics";
        mFragmentTransaction = getSupportFragmentManager().beginTransaction();
        topicFragment = (TopicFragment) getSupportFragmentManager().findFragmentByTag(TopicFragment.class.getSimpleName());
        if (topicFragment == null) {
            topicFragment = new TopicFragment();
        }
        mFragmentTransaction.setCustomAnimations(R.anim.anim_slide_in_left, R.anim.anim_slide_out_right, R.anim.anim_slide_in_right, R.anim.anim_slide_out_left);
        mFragmentTransaction.replace(R.id.container, topicFragment, TopicFragment.class.getSimpleName());
        mFragmentTransaction.commit();
    }

    @Override
    public void onDataReadComplete(String code) {
        //codeView.setCode(code, "xml");
    }

    @Override
    public void onBackPressed() {
        switch ( currentFragmentTAG ) {
            case "Dashboard" :
                super.onBackPressed();
                break;
            case "Topics" :
                loadDashboardFragment();
                break;
        }
    }
}
