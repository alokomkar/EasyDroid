package com.sortedqueue.learnandroid.topic;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sortedqueue.learnandroid.R;
import com.sortedqueue.learnandroid.asynctasks.SlideContentReaderTask;
import com.sortedqueue.learnandroid.dashboard.DashboardNavigationListener;
import com.sortedqueue.learnandroid.view.OneDirectionalScrollableViewPager;
import com.sortedqueue.learnandroid.view.SwipeDirection;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

import static com.sortedqueue.learnandroid.constants.LearnDroidConstants.CONTENT_TYPE_CODE;
import static com.sortedqueue.learnandroid.constants.LearnDroidConstants.CONTENT_TYPE_IMAGE;
import static com.sortedqueue.learnandroid.constants.LearnDroidConstants.CONTENT_TYPE_TEXT;
import static com.sortedqueue.learnandroid.constants.LearnDroidConstants.CONTENT_TYPE_URL;

/**
 * Created by Alok on 05/06/17.
 */

public class PresentationFragment extends Fragment implements SlideContentReaderTask.OnDataReadListener {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.slideProgressBar)
    android.widget.ProgressBar slideProgressBar;
    @BindView(R.id.doneFAB)
    FloatingActionButton doneFAB;
    @BindView(R.id.slideViewPager)
    OneDirectionalScrollableViewPager slideViewPager;
    private Unbinder unbinder;
    private SlideFragmentPagerAdapter slideFragmentPagerAdapter;

    private DashboardNavigationListener dashboardNavigationListener;

    private String TAG = PresentationFragment.class.getSimpleName();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View fragmentView = inflater.inflate(R.layout.fragment_presentation, container, false);
        unbinder = ButterKnife.bind(this, fragmentView);

        AppCompatActivity activity = (AppCompatActivity) getActivity();
        activity.setSupportActionBar(toolbar);
        toolbar.setTitle(dashboardNavigationListener.getCurrentMainTitle());

        String fileId = dashboardNavigationListener.getCurrentTopic().toLowerCase().replaceAll("-", "").replaceAll("  ", " ").replaceAll(" ", "_");
        Log.d(TAG, "File Id : " + fileId);
        new SlideContentReaderTask(getContext(), fileId, this).execute();


        doneFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                scrollForward();
            }


        });
        return fragmentView;
    }

    private void scrollForward() {
        if( slideProgressBar.getProgress() == slideProgressBar.getMax() ) {
            dashboardNavigationListener.loadTopicFragment();
        }
        else {
            slideViewPager.setCurrentItem(slideViewPager.getCurrentItem() + 1);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if( context instanceof DashboardNavigationListener ) {
            dashboardNavigationListener = (DashboardNavigationListener) context;
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        dashboardNavigationListener = null;
    }

    private void loadSlideFragment(ArrayList<Fragment> fragments) {
        slideFragmentPagerAdapter = new SlideFragmentPagerAdapter(getChildFragmentManager(), fragments);
        slideViewPager.setAdapter(slideFragmentPagerAdapter);
        slideViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                slideProgressBar.setProgress(position + 1);
                toggleFabDrawable(slideProgressBar.getProgress());
                changeScrollBehavior(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        slideProgressBar.setMax(slideViewPager.getAdapter().getCount());
        slideProgressBar.setProgress(1);
        changeScrollBehavior(0);
    }

    private void changeScrollBehavior(int position) {
        Fragment fragment = slideFragmentPagerAdapter.getItem(position);
        if( fragment instanceof CodeFragment || fragment instanceof WebViewFragment ) {
            slideViewPager.setAllowedSwipeDirection(SwipeDirection.none );
        }
        else {
            slideViewPager.setAllowedSwipeDirection(SwipeDirection.all);
        }
    }

    private void toggleFabDrawable(final int progress) {
        int drawable = progress == slideProgressBar.getMax() ? R.drawable.ic_done_all : android.R.drawable.ic_media_play;
        doneFAB.setImageDrawable(ContextCompat.getDrawable(getContext(), drawable));
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void onDataReadComplete(ArrayList<SlideContent> contentArrayList) {
        Log.d(TAG, "Content : " + contentArrayList);
        ArrayList<Fragment> fragments = new ArrayList<>();
        for( SlideContent slideContent : contentArrayList ) {
            switch ( slideContent.getContentType() ) {
                case CONTENT_TYPE_IMAGE :
                    SlideFragment slideFragment = new SlideFragment();
                    slideFragment.setSlideContent( slideContent );
                    fragments.add(slideFragment);
                    break;
                case CONTENT_TYPE_TEXT :
                    slideFragment = new SlideFragment();
                    slideFragment.setSlideContent( slideContent );
                    fragments.add(slideFragment);
                    break;
                case CONTENT_TYPE_CODE :
                    CodeFragment codeFragment = new CodeFragment();
                    codeFragment.setSlideContent( slideContent );
                    fragments.add(codeFragment);
                    break;
                case CONTENT_TYPE_URL :
                    WebViewFragment webViewFragment = new WebViewFragment();
                    webViewFragment.setSlideContent( slideContent );
                    fragments.add(webViewFragment);
                    break;
            }
        }
        loadSlideFragment(fragments);
    }

    public void navigateBack() {
        if( slideViewPager != null ) {
            slideViewPager.setCurrentItem(slideViewPager.getCurrentItem() - 1);
        }
    }
}
