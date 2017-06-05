package com.sortedqueue.learnandroid.dashboard;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sortedqueue.learnandroid.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by Alok on 05/06/17.
 */

public class DashboardFragment extends Fragment {

    @BindView(R.id.androidOSRecyclerView)
    RecyclerView androidOSRecyclerView;
    @BindView(R.id.activityRecyclerView)
    RecyclerView activityRecyclerView;
    @BindView(R.id.fragmentRecyclerView)
    RecyclerView fragmentRecyclerView;
    @BindView(R.id.intentRecyclerView)
    RecyclerView intentRecyclerView;
    @BindView(R.id.asyncTaskRecyclerView)
    RecyclerView asyncTaskRecyclerView;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    Unbinder unbinder;

    private DashboardNavigationListener dashboardNavigationListener;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View fragmentView = inflater.inflate(R.layout.fragment_dashboard, container, false);
        unbinder = ButterKnife.bind(this, fragmentView);

        AppCompatActivity activity = (AppCompatActivity) getActivity();
        activity.setSupportActionBar(toolbar);
        toolbar.setTitle(R.string.app_name);

        setupRecyclerViews();
        return fragmentView;
    }

    private AdapterClickListener adapterClickListener = new AdapterClickListener() {
        @Override
        public void onClick(int position) {
            if( dashboardNavigationListener != null )
                dashboardNavigationListener.loadPresentationFragment();
        }
    };

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

    private void setupRecyclerViews() {

        androidOSRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        activityRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        fragmentRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        intentRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        asyncTaskRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));

        androidOSRecyclerView.setAdapter(new TopicsRecyclerViewAdapter(getContext(), R.color.md_amber_800, adapterClickListener));
        activityRecyclerView.setAdapter(new TopicsRecyclerViewAdapter(getContext(), R.color.md_brown_700, adapterClickListener));
        fragmentRecyclerView.setAdapter(new TopicsRecyclerViewAdapter(getContext(), R.color.md_blue_grey_700, adapterClickListener));
        intentRecyclerView.setAdapter(new TopicsRecyclerViewAdapter(getContext(), R.color.md_light_green_900, adapterClickListener));
        asyncTaskRecyclerView.setAdapter(new TopicsRecyclerViewAdapter(getContext(), R.color.md_cyan_900, adapterClickListener));

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
