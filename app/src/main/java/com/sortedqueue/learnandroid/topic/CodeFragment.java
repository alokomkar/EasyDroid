package com.sortedqueue.learnandroid.topic;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.pddstudio.highlightjs.HighlightJsView;
import com.pddstudio.highlightjs.models.Language;
import com.pddstudio.highlightjs.models.Theme;
import com.sortedqueue.learnandroid.R;
import com.sortedqueue.learnandroid.asynctasks.CodeFileReaderTask;
import com.sortedqueue.learnandroid.dashboard.DashboardNavigationListener;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;


/**
 * Created by Alok on 06/06/17.
 */

public class CodeFragment extends Fragment implements CodeFileReaderTask.OnDataReadListener, HighlightJsView.OnThemeChangedListener {

    @BindView(R.id.highlightJsView)
    HighlightJsView highlightJsView;
    Unbinder unbinder;
    @BindView(R.id.backTextView)
    TextView backTextView;
    @BindView(R.id.codeProgressBar)
    ProgressBar codeProgressBar;
    private SlideContent slideContent;

    private DashboardNavigationListener dashboardNavigationListener;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View fragmentView = inflater.inflate(R.layout.fragment_code, container, false);
        unbinder = ButterKnife.bind(this, fragmentView);
        //register theme change listener
        highlightJsView.setOnThemeChangedListener(this);
        //change theme and set language to auto detect
        highlightJsView.setTheme(Theme.ANDROID_STUDIO);
        String codeFile = slideContent.getContent();
        if (codeFile.startsWith("xml")) {
            highlightJsView.setHighlightLanguage(Language.XML);
        } else {
            highlightJsView.setHighlightLanguage(Language.JAVA);
        }
        //load the source (can be loaded by String, File or URL)
        highlightJsView.setShowLineNumbers(true);
        highlightJsView.setZoomSupportEnabled(true);
        highlightJsView.getViewTreeObserver().addOnScrollChangedListener(new ViewTreeObserver.OnScrollChangedListener() {
            @Override
            public void onScrollChanged() {
                if( highlightJsView == null ) {
                    return;
                }
                View view = highlightJsView.getChildAt(highlightJsView.getChildCount() - 1);
                if( view != null ) {
                    int diff = (view.getBottom() - (highlightJsView.getHeight() + highlightJsView
                            .getScrollY()));

                    if (diff == 0) {
                        presentationSlideListener.showNextLayout();
                    }
                    else {
                        presentationSlideListener.hideNextLayout();
                    }
                }
                else {
                    presentationSlideListener.showNextLayout();
                }

            }
        });
        backTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dashboardNavigationListener.onNavigateBack();
            }
        });
        new CodeFileReaderTask(codeProgressBar, getContext(), slideContent.getContent(), this).execute();
        return fragmentView;
    }

    private PresentationSlideListener presentationSlideListener;

    public void setPresentationSlideListener(PresentationSlideListener presentationSlideListener) {
        this.presentationSlideListener = presentationSlideListener;
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

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void onDataReadComplete(String code) {
        highlightJsView.setSource(code);
        codeProgressBar.setVisibility(View.GONE);
    }

    @Override
    public void onThemeChanged(@NonNull Theme theme) {

    }

    public void setSlideContent(SlideContent slideContent) {
        this.slideContent = slideContent;
    }
}
