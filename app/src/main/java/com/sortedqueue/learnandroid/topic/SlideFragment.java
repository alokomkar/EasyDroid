package com.sortedqueue.learnandroid.topic;

import android.content.Context;
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

import com.bumptech.glide.Glide;
import com.sortedqueue.learnandroid.R;
import com.sortedqueue.learnandroid.dashboard.DashboardNavigationListener;
import com.sortedqueue.learnandroid.utils.ImageUtils;

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

    private DashboardNavigationListener dashboardNavigationListener;
    private SlideContent slideContent;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View fragmentView = inflater.inflate(R.layout.fragment_slide, container, false);
        unbinder = ButterKnife.bind(this, fragmentView);
        questionTextView.setMovementMethod(new ScrollingMovementMethod());
        questionTextView.setText(dashboardNavigationListener.getCurrentTopic());
        contentTextView.setVisibility(View.GONE);
        slideImageView.setVisibility(View.GONE);
        switch ( slideContent.getContentType() ) {
            case CONTENT_TYPE_IMAGE :
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
                break;
            case CONTENT_TYPE_TEXT :
                contentTextView.setVisibility(View.VISIBLE);
                contentTextView.setText(slideContent.getSpannableContent());
                break;
        }
        return fragmentView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
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

    public void setSlideContent(SlideContent slideContent) {
        this.slideContent = slideContent;
    }
}
