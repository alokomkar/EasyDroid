package com.sortedqueue.learnandroid.topic;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.sortedqueue.learnandroid.R;
import com.sortedqueue.learnandroid.utils.SimpleItemTouchHelperCallback;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by Alok on 05/06/17.
 */

public class TopicFragment extends Fragment {

    @BindView(R.id.questionTextView)
    TextView questionTextView;
    @BindView(R.id.questionCardView)
    CardView questionCardView;
    @BindView(R.id.progressTextView)
    TextView progressTextView;
    @BindView(R.id.optionsRecyclerView)
    RecyclerView optionsRecyclerView;
    @BindView(R.id.questionLayout)
    LinearLayout questionLayout;
    @BindView(R.id.lifeLine1ImageView)
    ImageView lifeLine1ImageView;
    @BindView(R.id.lifeLine2ImageView)
    ImageView lifeLine2ImageView;
    @BindView(R.id.lifeLine3ImageView)
    ImageView lifeLine3ImageView;
    @BindView(R.id.lifeLineLayout)
    RelativeLayout lifeLineLayout;
    Unbinder unbinder;
    @BindView(R.id.toolbar)
    Toolbar toolbar;

    private OptionsRecyclerAdapter optionsRecyclerAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View fragmentView = inflater.inflate(R.layout.fragment_topic, container, false);
        unbinder = ButterKnife.bind(this, fragmentView);

        AppCompatActivity activity = (AppCompatActivity) getActivity();
        activity.setSupportActionBar(toolbar);
        toolbar.setTitle(R.string.topics);
        setupRecyclerView();
        return fragmentView;
    }

    private void setupRecyclerView() {
        optionsRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        optionsRecyclerAdapter = new OptionsRecyclerAdapter();
        optionsRecyclerView.setAdapter(optionsRecyclerAdapter);
        ItemTouchHelper.Callback callback =
                new SimpleItemTouchHelperCallback(optionsRecyclerAdapter);
        ItemTouchHelper touchHelper = new ItemTouchHelper(callback);
        touchHelper.attachToRecyclerView(optionsRecyclerView);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
