package com.sortedqueue.learnandroid;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.sortedqueue.learnandroid.asynctasks.FileReaderTask;
import com.sortedqueue.learnandroid.dashboard.DashboardFragment;
import com.sortedqueue.learnandroid.dashboard.DashboardNavigationListener;
import com.sortedqueue.learnandroid.topic.CodeFragment;
import com.sortedqueue.learnandroid.topic.PresentationFragment;
import com.sortedqueue.learnandroid.topic.TopicFragment;

import butterknife.BindView;
import butterknife.ButterKnife;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class MainActivity extends AppCompatActivity implements FileReaderTask.OnDataReadListener, DashboardNavigationListener {

    @BindView(R.id.reputationProgressBar)
    ProgressBar reputationProgressBar;
    @BindView(R.id.reputationTextView)
    TextView reputationTextView;
    @BindView(R.id.progressLayout)
    LinearLayout progressLayout;
    @BindView(R.id.splashImageView)
    ImageView splashImageView;
    @BindView(R.id.splashTextView)
    TextView splashTextView;
    private FragmentTransaction mFragmentTransaction;
    private DashboardFragment dashboardFragment;
    private TopicFragment topicFragment;
    private String currentFragmentTAG = "";
    private PresentationFragment presentationFragment;
    private Handler handler;
    private Runnable runnable;

    //private CodeView codeView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                splashImageView.setVisibility(View.GONE);
                splashTextView.setVisibility(View.GONE);
                loadDashboardFragment();
            }
        }, 2500);
        //codeView = (CodeView) findViewById(R.id.codeView);
        //new FileReaderTask(MainActivity.this, "chapter_1", this).execute();

    }


    public void onProgressStatsUpdate(int points) {
        progressLayout.setVisibility(View.VISIBLE);
        animateProgress(points);
    }

    private int progressBarStatus;

    public void animateProgress(final int points) {
        try {
            if (reputationProgressBar != null) {

                if (handler == null) {
                    handler = new Handler();
                }

                final int progress = points;
                reputationProgressBar.setVisibility(View.VISIBLE);
                reputationTextView.setVisibility(View.VISIBLE);
                runnable = new Runnable() {
                    @Override
                    public void run() {
                        for (progressBarStatus = 0; progressBarStatus <= progress; progressBarStatus++) {

                            handler.post(new Runnable() {
                                public void run() {
                                    if (reputationProgressBar != null) {
                                        reputationProgressBar.setProgress(progressBarStatus);

                                        reputationTextView.setText("You've gained " + points + "xp\n" + progressBarStatus + "% Complete");
                                        int level = 1290 / 100;
                                        if (level > 0) {
                                            reputationTextView.setText("You've gained " + points + "xp\n" + progressBarStatus + "% Complete : Level : " + level);
                                        }
                                    }
                                }
                            });
                            try {
                                Thread.sleep(40);
                            } catch (Exception ex) {
                            }
                        }
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                progressLayout.setVisibility(View.GONE);
                            }
                        }, 1500);

                    }
                };
                new Thread(runnable).start();
            }
        } catch (Exception e) {
            e.printStackTrace();
            if (progressLayout != null) {
                progressLayout.setVisibility(View.GONE);
            }
        }

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

        //loadCodeFragment();
    }

    private void loadCodeFragment() {
        currentFragmentTAG = "Dashboard";
        mFragmentTransaction = getSupportFragmentManager().beginTransaction();
        CodeFragment codeFragment = (CodeFragment) getSupportFragmentManager().findFragmentByTag(CodeFragment.class.getSimpleName());
        if (codeFragment == null) {
            codeFragment = new CodeFragment();
        }
        mFragmentTransaction.setCustomAnimations(R.anim.anim_slide_in_left, R.anim.anim_slide_out_right);
        mFragmentTransaction.replace(R.id.container, codeFragment, CodeFragment.class.getSimpleName());
        mFragmentTransaction.commit();
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }


    @Override
    public void loadTopicFragment() {
        onProgressStatsUpdate(50);
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
    public void loadPresentationFragment() {
        onProgressStatsUpdate(50);
        currentFragmentTAG = "Presentation";
        mFragmentTransaction = getSupportFragmentManager().beginTransaction();
        presentationFragment = (PresentationFragment) getSupportFragmentManager().findFragmentByTag(PresentationFragment.class.getSimpleName());
        if (presentationFragment == null) {
            presentationFragment = new PresentationFragment();
        }
        mFragmentTransaction.setCustomAnimations(R.anim.anim_slide_in_left, R.anim.anim_slide_out_right, R.anim.anim_slide_in_right, R.anim.anim_slide_out_left);
        mFragmentTransaction.replace(R.id.container, presentationFragment, PresentationFragment.class.getSimpleName());
        mFragmentTransaction.commit();
    }

    @Override
    public void onDataReadComplete(String code) {
        //codeView.setCode(code, "xml");
    }

    @Override
    public void onBackPressed() {
        switch (currentFragmentTAG) {
            case "Dashboard":
                super.onBackPressed();
                break;
            case "Topics":
                loadPresentationFragment();
                break;
            case "Presentation":
                loadDashboardFragment();
                break;
        }
    }
}
