package com.sortedqueue.learnandroid.topic

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver

import com.pddstudio.highlightjs.HighlightJsView
import com.pddstudio.highlightjs.models.Language
import com.pddstudio.highlightjs.models.Theme
import com.sortedqueue.learnandroid.R
import com.sortedqueue.learnandroid.asynctasks.CodeFileReaderTask
import com.sortedqueue.learnandroid.dashboard.DashboardNavigationListener
import kotlinx.android.synthetic.main.fragment_code.*

/**
 * Created by Alok on 06/06/17.
 */

class CodeFragment : Fragment(), CodeFileReaderTask.OnDataReadListener, HighlightJsView.OnThemeChangedListener {

    private var slideContent: SlideContent? = null

    private var dashboardNavigationListener: DashboardNavigationListener? = null

    private var presentationSlideListener: PresentationSlideListener? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_code, container, false)
    }

    /**
     * Called immediately after [.onCreateView]
     * has returned, but before any saved state has been restored in to the view.
     * This gives subclasses a chance to initialize themselves once
     * they know their view hierarchy has been completely created.  The fragment's
     * view hierarchy is not however attached to its parent at this point.
     * @param view The View returned by [.onCreateView].
     * @param savedInstanceState If non-null, this fragment is being re-constructed
     * from a previous saved state as given here.
     */
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //register theme change listener
        highlightJsView.setOnThemeChangedListener(this)
        //change theme and set language to auto detect
        highlightJsView.setTheme(Theme.ANDROID_STUDIO)
        val codeFile = slideContent!!.content
        if (codeFile!!.startsWith("xml")) {
            highlightJsView.setHighlightLanguage(Language.XML)
        } else {
            highlightJsView.setHighlightLanguage(Language.JAVA)
        }
        //load the source (can be loaded by String, File or URL)
        highlightJsView.setShowLineNumbers(true)
        highlightJsView.setZoomSupportEnabled(true)
        highlightJsView.getViewTreeObserver().addOnScrollChangedListener(ViewTreeObserver.OnScrollChangedListener {
            if (highlightJsView == null) {
                return@OnScrollChangedListener
            }
            val view = highlightJsView.getChildAt(highlightJsView.getChildCount() - 1)
            if (view != null) {
                val diff = view!!.getBottom() - (highlightJsView.getHeight() + highlightJsView
                        .getScrollY())

                if (diff == 0) {
                    presentationSlideListener!!.showNextLayout()
                } else {
                    presentationSlideListener!!.hideNextLayout()
                }
            } else {
                presentationSlideListener!!.showNextLayout()
            }
        })
        backTextView.setOnClickListener(View.OnClickListener { dashboardNavigationListener!!.onNavigateBack() })
        CodeFileReaderTask(codeProgressBar, context!!, slideContent!!.content!!, this).execute()
    }

    fun setPresentationSlideListener(presentationSlideListener: PresentationSlideListener) {
        this.presentationSlideListener = presentationSlideListener
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        if (context is DashboardNavigationListener) {
            dashboardNavigationListener = context
        }
    }

    override fun onDetach() {
        super.onDetach()
        dashboardNavigationListener = null
    }


    override fun onDataReadComplete(code: String?) {
        highlightJsView.setSource(code)
        codeProgressBar.setVisibility(View.GONE)
    }

    override fun onThemeChanged(theme: Theme) {

    }

    fun setSlideContent(slideContent: SlideContent) {
        this.slideContent = slideContent
    }
}
