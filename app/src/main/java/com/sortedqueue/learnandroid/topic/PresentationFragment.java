package com.sortedqueue.learnandroid.topic;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

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
    @BindView(R.id.slideContainer)
    FrameLayout slideContainer;
    private Unbinder unbinder;
    private FragmentTransaction mFragmentTransaction;
    private SlideFragment slideFragment;

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
                dashboardNavigationListener.loadTopicFragment();
            }
        });
        return fragmentView;
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
        mFragmentTransaction = getChildFragmentManager().beginTransaction();
        slideFragment = (SlideFragment) getChildFragmentManager().findFragmentByTag(SlideFragment.class.getSimpleName());
        if (slideFragment == null) {
            slideFragment = new SlideFragment();
        }
        mFragmentTransaction.setCustomAnimations(R.anim.anim_slide_in_left, R.anim.anim_slide_out_right);
        mFragmentTransaction.replace(R.id.slideContainer, slideFragment, SlideFragment.class.getSimpleName());
        mFragmentTransaction.commit();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
