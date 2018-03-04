package com.sortedqueue.learnandroid.topic

import android.content.Context
import android.graphics.Bitmap
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.widget.ContentLoadingProgressBar
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.TextView

import com.sortedqueue.learnandroid.R
import com.sortedqueue.learnandroid.dashboard.DashboardNavigationListener
import kotlinx.android.synthetic.main.fragment_web_view.*


/**
 * Created by Alok on 08/06/17.
 */

class WebViewFragment : Fragment() {

    private var slideContent: SlideContent? = null

    private var dashboardNavigationListener: DashboardNavigationListener? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val fragmentView = inflater.inflate(R.layout.fragment_web_view, container, false)
        backTextView.setOnClickListener(View.OnClickListener { dashboardNavigationListener!!.onNavigateBack() })
        val webSettings = webView.getSettings()
        webSettings.setJavaScriptEnabled(true)
        webView.loadUrl(slideContent!!.content)
        webView.webViewClient = object : WebViewClient() {
            override fun onPageStarted(view: WebView, url: String, favicon: Bitmap) {
                super.onPageStarted(view, url, favicon)
                progressBar.setVisibility(View.VISIBLE)
            }

            override fun onPageFinished(view: WebView, url: String) {
                super.onPageFinished(view, url)
                progressBar.setVisibility(View.GONE)
            }
        }
        return fragmentView
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

    fun setSlideContent(slideContent: SlideContent) {
        this.slideContent = slideContent
    }
}
