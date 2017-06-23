package com.sortedqueue.learnandroid.topic;

import android.content.Context;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.speech.tts.UtteranceProgressListener;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.CardView;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.sortedqueue.learnandroid.R;
import com.sortedqueue.learnandroid.dashboard.DashboardNavigationListener;
import com.sortedqueue.learnandroid.utils.ImageUtils;

import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

import static com.sortedqueue.learnandroid.constants.LearnDroidConstants.CONTENT_TYPE_IMAGE;
import static com.sortedqueue.learnandroid.constants.LearnDroidConstants.CONTENT_TYPE_TEXT;

/**
 * Created by Alok on 05/06/17.
 */

public class SlideFragment extends Fragment {

    @BindView(R.id.questionTextView)
    TextView questionTextView;
    @BindView(R.id.questionCardView)
    CardView questionCardView;
    @BindView(R.id.progressTextView)
    TextView progressTextView;
    @BindView(R.id.contentTextView)
    TextView contentTextView;
    Unbinder unbinder;
    @BindView(R.id.slideImageView)
    ImageView slideImageView;
    @BindView(R.id.expandedImageView)
    ImageView expandedImageView;
    @BindView(R.id.slideLayout)
    LinearLayout slideLayout;
    @BindView(R.id.audioFAB)
    FloatingActionButton audioFAB;
    @BindView(R.id.audioProgressBar)
    ProgressBar audioProgressBar;
    @BindView(R.id.slideScrollView)
    NestedScrollView slideScrollView;
    private TextToSpeech textToSpeech;
    private String TAG = SlideFragment.class.getSimpleName();

    private DashboardNavigationListener dashboardNavigationListener;
    private SlideContent slideContent;
    private PresentationSlideListener presentationSlideListener;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View fragmentView = inflater.inflate(R.layout.fragment_slide, container, false);
        unbinder = ButterKnife.bind(this, fragmentView);
        questionTextView.setMovementMethod(new ScrollingMovementMethod());
        questionTextView.setText(dashboardNavigationListener.getCurrentTopic());
        contentTextView.setVisibility(View.GONE);
        slideImageView.setVisibility(View.GONE);


        switch (slideContent.getContentType()) {
            case CONTENT_TYPE_IMAGE:
                final String imageUrl = slideContent.getContent();
                Glide.with(getContext())
                        .load(imageUrl)
                        .asBitmap()
                        .fitCenter()
                        .error(R.color.md_red_400)
                        .placeholder(android.R.drawable.ic_menu_report_image)
                        .into(slideImageView);
                slideImageView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        ImageUtils.zoomImageFromThumb(getContext(), slideLayout, slideImageView, expandedImageView, imageUrl);
                    }
                });
                slideImageView.setVisibility(View.VISIBLE);
                audioFAB.setVisibility(View.GONE);
                break;
            case CONTENT_TYPE_TEXT:
                contentTextView.setVisibility(View.VISIBLE);
                contentTextView.setText(slideContent.getSpannableContent());
                audioFAB.setVisibility(View.VISIBLE);
                audioFAB.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        audioProgressBar.setVisibility(View.VISIBLE);
                        audioProgressBar.setIndeterminate(true);
                        playNotes(contentTextView.getText().toString());
                    }
                });
                break;
        }

        slideScrollView.getViewTreeObserver().addOnScrollChangedListener(new ViewTreeObserver.OnScrollChangedListener() {
            @Override
            public void onScrollChanged() {
                if( slideScrollView == null ) {
                    return;
                }
                View view = slideScrollView.getChildAt(slideScrollView.getChildCount() - 1);

                int diff = (view.getBottom() - (slideScrollView.getHeight() + slideScrollView
                        .getScrollY()));

                if (diff == 0) {
                    presentationSlideListener.showNextLayout();
                }
                else {
                    presentationSlideListener.hideNextLayout();
                }
            }
        });

        return fragmentView;
    }

    public void setPresentationSlideListener(PresentationSlideListener presentationSlideListener) {
        this.presentationSlideListener = presentationSlideListener;
    }

    public void stopAudioAnimation() {
        if (audioFAB != null) {
            if (getActivity() == null) {
                return;
            }
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    audioFAB.clearAnimation();
                    audioProgressBar.setVisibility(View.GONE);
                }
            });
        }
    }

    public void stopAudioPlayback() {
        if (textToSpeech != null) {
            textToSpeech.stop();
            textToSpeech.shutdown();
            stopAudioAnimation();
        }
    }

    public void playNotes(final String speechText) {
        textToSpeech = new TextToSpeech(getContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if (status != TextToSpeech.ERROR) {
                    textToSpeech.setLanguage(Locale.US);
                    textToSpeech.speak(speechText, TextToSpeech.QUEUE_FLUSH, null, String.valueOf(speechText.hashCode()));
                }
            }
        }, null);
        textToSpeech.setPitch(0.85f);
        textToSpeech.setSpeechRate(0.9f);
        textToSpeech.setOnUtteranceProgressListener(new UtteranceProgressListener() {
            @Override
            public void onStart(String s) {
                Log.d(TAG, "Started audio : " + s);
                startAnimation();

            }

            @Override
            public void onDone(String s) {
                Log.d(TAG, "onDone audio : " + s);
                stopAudioAnimation();
            }

            @Override
            public void onError(String s) {
                Log.d(TAG, "onError audio : " + s);
                stopAudioAnimation();
            }
        });

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void onPause() {
        stopAudioPlayback();
        super.onPause();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof DashboardNavigationListener) {
            dashboardNavigationListener = (DashboardNavigationListener) context;
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        dashboardNavigationListener = null;
    }

    public void setSlideContent(SlideContent slideContent) {
        this.slideContent = slideContent;
    }


    private void startAnimation() {
        if (getActivity() == null) {
            return;
        }
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Animation animation = new AlphaAnimation(1, 0);
                animation.setDuration(800);
                animation.setInterpolator(new LinearInterpolator());
                animation.setRepeatCount(Animation.INFINITE);
                animation.setRepeatMode(Animation.REVERSE);
                audioFAB.startAnimation(animation);
                audioProgressBar.setVisibility(View.GONE);
            }
        });

    }

}
