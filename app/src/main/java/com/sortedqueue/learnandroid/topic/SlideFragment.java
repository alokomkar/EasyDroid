package com.sortedqueue.learnandroid.topic;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.sortedqueue.learnandroid.R;
import com.sortedqueue.learnandroid.utils.ImageUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

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

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View fragmentView = inflater.inflate(R.layout.fragment_slide, container, false);
        unbinder = ButterKnife.bind(this, fragmentView);
        questionTextView.setMovementMethod(new ScrollingMovementMethod());
        slideImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ImageUtils.zoomImageFromThumb(getContext(), slideLayout, slideImageView, expandedImageView, slideImageView.getDrawable());
            }
        });
        return fragmentView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
