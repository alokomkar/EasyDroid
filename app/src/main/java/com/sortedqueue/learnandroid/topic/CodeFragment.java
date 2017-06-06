package com.sortedqueue.learnandroid.topic;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.pddstudio.highlightjs.HighlightJsView;
import com.pddstudio.highlightjs.models.Language;
import com.pddstudio.highlightjs.models.Theme;
import com.sortedqueue.learnandroid.R;
import com.sortedqueue.learnandroid.asynctasks.FileReaderTask;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;


/**
 * Created by Alok on 06/06/17.
 */

public class CodeFragment extends Fragment implements FileReaderTask.OnDataReadListener, HighlightJsView.OnThemeChangedListener {

    @BindView(R.id.highlightJsView)
    HighlightJsView highlightJsView;
    Unbinder unbinder;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View fragmentView = inflater.inflate(R.layout.fragment_code, container, false);
        unbinder = ButterKnife.bind(this, fragmentView);
        //register theme change listener
        highlightJsView.setOnThemeChangedListener(this);
        //change theme and set language to auto detect
        highlightJsView.setTheme(Theme.ANDROID_STUDIO);
        highlightJsView.setHighlightLanguage(Language.XML);
        //load the source (can be loaded by String, File or URL)
        highlightJsView.setShowLineNumbers(true);
        highlightJsView.setZoomSupportEnabled(true);

        new FileReaderTask(getContext(), "activity_main", this).execute();
        return fragmentView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void onDataReadComplete(String code) {
        highlightJsView.setSource(code);
    }

    @Override
    public void onThemeChanged(@NonNull Theme theme) {

    }
}
