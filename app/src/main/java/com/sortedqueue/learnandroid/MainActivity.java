package com.sortedqueue.learnandroid;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.speech.tts.TextToSpeech;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.sortedqueue.learnandroid.asynctasks.CodeFileReaderTask;
import com.sortedqueue.learnandroid.dashboard.DashboardFragment;
import com.sortedqueue.learnandroid.dashboard.DashboardNavigationListener;
import com.sortedqueue.learnandroid.topic.PresentationFragment;
import com.sortedqueue.learnandroid.topic.TopicFragment;

import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class MainActivity extends AppCompatActivity implements CodeFileReaderTask.OnDataReadListener, DashboardNavigationListener {

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
    private String currentTopic = "Learn Android";
    private String currentMainTitle;

    private TextToSpeech textToSpeech;

    //private CodeView codeView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        textToSpeech = new TextToSpeech(MainActivity.this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if(status != TextToSpeech.ERROR) {
                    textToSpeech.setLanguage(Locale.US);
                }
            }
        }, null);
        textToSpeech.setPitch(0.8f);
        textToSpeech.setSpeechRate(0.85f);
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

    @Override
    protected void onPause() {
        stopAudioPlayback();
        super.onPause();
    }

    @Override
    public void stopAudioPlayback() {
        if( textToSpeech != null ) {
            textToSpeech.stop();
            textToSpeech.shutdown();
        }
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
        mFragmentTransaction.setCustomAnimations(R.anim.slide_in_left, R.anim.slide_out_right, R.anim.slide_in_right, R.anim.slide_out_left);
        mFragmentTransaction.replace(R.id.container, topicFragment, TopicFragment.class.getSimpleName());
        mFragmentTransaction.commit();
    }

    @Override
    public void loadPresentationFragment(String mainTitle, String topic) {
        //onProgressStatsUpdate(50);
        currentTopic = topic;
        currentMainTitle = mainTitle;
        currentFragmentTAG = "Presentation";
        mFragmentTransaction = getSupportFragmentManager().beginTransaction();
        presentationFragment = (PresentationFragment) getSupportFragmentManager().findFragmentByTag(PresentationFragment.class.getSimpleName());
        if (presentationFragment == null) {
            presentationFragment = new PresentationFragment();
        }
        mFragmentTransaction.setCustomAnimations(R.anim.slide_in_left, R.anim.slide_out_right, R.anim.slide_in_right, R.anim.slide_out_left);
        mFragmentTransaction.replace(R.id.container, presentationFragment, PresentationFragment.class.getSimpleName());
        mFragmentTransaction.commit();
    }

    @Override
    public String getCurrentTopic() {
        return currentTopic;
    }

    @Override
    public String getCurrentMainTitle() {
        return currentMainTitle;
    }

    @Override
    public void onNavigateBack() {
        if( presentationFragment != null ) {
            presentationFragment.navigateBack();
        }
    }

    @Override
    public void playNotes(String speechText) {
        textToSpeech.speak(speechText, TextToSpeech.QUEUE_FLUSH, null, null);
    }

    @Override
    public void onDataReadComplete(String code) {
        //codeView.setCode(code, "xml");
    }

    @Override
    public void onBackPressed() {
        switch (currentFragmentTAG) {
            case "Dashboard":
                stopAudioPlayback();
                super.onBackPressed();
                break;
            case "Topics":
                stopAudioPlayback();
                loadPresentationFragment(currentMainTitle, currentTopic);
                break;
            case "Presentation":
                stopAudioPlayback();
                loadDashboardFragment();
                break;
        }
    }
}
