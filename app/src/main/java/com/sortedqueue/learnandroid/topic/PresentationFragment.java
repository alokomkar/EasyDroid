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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sortedqueue.learnandroid.R;
import com.sortedqueue.learnandroid.dashboard.DashboardNavigationListener;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by Alok on 05/06/17.
 */

public class PresentationFragment extends Fragment {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.ProgressBar)
    android.widget.ProgressBar ProgressBar;
    @BindView(R.id.doneFAB)
    FloatingActionButton doneFAB;
    @BindView(R.id.slideViewPager)
    ViewPager slideViewPager;
    private Unbinder unbinder;

    private DashboardNavigationListener dashboardNavigationListener;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View fragmentView = inflater.inflate(R.layout.fragment_presentation, container, false);
        unbinder = ButterKnife.bind(this, fragmentView);

        AppCompatActivity activity = (AppCompatActivity) getActivity();
        activity.setSupportActionBar(toolbar);
        toolbar.setTitle(R.string.presentation);

        loadSlideFragment();
        doneFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                scrollForward();
            }


        });
        return fragmentView;
    }

    private void scrollForward() {
        if( ProgressBar.getProgress() == ProgressBar.getMax() ) {
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

    private void loadSlideFragment() {
        slideViewPager.setAdapter(new SlideViewPager(getChildFragmentManager()));
        slideViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                ProgressBar.setProgress(position + 1);
                toggleFabDrawable(ProgressBar.getProgress());
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        ProgressBar.setMax(slideViewPager.getAdapter().getCount());
        ProgressBar.setProgress(1);
    }

    private void toggleFabDrawable(final int progress) {
        int drawable = progress == ProgressBar.getMax() ? R.drawable.ic_done_all : android.R.drawable.ic_media_play;
        doneFAB.setImageDrawable(ContextCompat.getDrawable(getContext(), drawable));
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
