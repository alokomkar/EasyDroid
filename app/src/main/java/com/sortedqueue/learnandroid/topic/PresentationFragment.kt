package com.sortedqueue.learnandroid.topic

import android.content.Context
import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.support.v4.view.ViewPager
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.sortedqueue.learnandroid.R
import com.sortedqueue.learnandroid.asynctasks.SlideContentReaderTask
import com.sortedqueue.learnandroid.constants.LearnDroidConstants.Companion.CONTENT_TYPE_CODE
import com.sortedqueue.learnandroid.constants.LearnDroidConstants.Companion.CONTENT_TYPE_IMAGE
import com.sortedqueue.learnandroid.constants.LearnDroidConstants.Companion.CONTENT_TYPE_TEXT
import com.sortedqueue.learnandroid.constants.LearnDroidConstants.Companion.CONTENT_TYPE_URL
import com.sortedqueue.learnandroid.dashboard.DashboardNavigationListener
import com.sortedqueue.learnandroid.view.SwipeDirection
import com.sortedqueue.learnandroid.view.ZoomOutPageTransformer

import java.util.ArrayList

import kotlinx.android.synthetic.main.fragment_presentation.*

/**
 * Created by Alok on 05/06/17.
 */

class PresentationFragment : Fragment(), SlideContentReaderTask.OnDataReadListener, PresentationSlideListener {

    private var slideFragmentPagerAdapter: SlideFragmentPagerAdapter? = null

    private var dashboardNavigationListener: DashboardNavigationListener? = null

    private val TAG = PresentationFragment::class.java.simpleName
    private var currentPosition: Int = 0

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val fragmentView = inflater.inflate(R.layout.fragment_presentation, container, false)


        return fragmentView
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
        val activity = activity as AppCompatActivity?
        activity!!.setSupportActionBar(toolbar)
        toolbar.setTitle(dashboardNavigationListener!!.getCurrentMainTitle())

        val fileId = dashboardNavigationListener!!.getCurrentTopic().toLowerCase().replace("-".toRegex(), "").replace("  ".toRegex(), " ").replace(" ".toRegex(), "_")
        Log.d(TAG, "File Id : " + fileId)
        SlideContentReaderTask(context!!, fileId, this).execute()


        doneFAB.setOnClickListener(View.OnClickListener { scrollForward() })
    }

    private fun scrollForward() {
        if (slideProgressBar.getProgress() == slideProgressBar.getMax()) {
            dashboardNavigationListener!!.loadTopicFragment()
        } else {
            slideViewPager.setCurrentItem(slideViewPager.getCurrentItem() + 1)
        }
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

    private fun loadSlideFragment(fragments: ArrayList<Fragment>) {
        slideFragmentPagerAdapter = SlideFragmentPagerAdapter(childFragmentManager, fragments)
        slideViewPager.setAdapter(slideFragmentPagerAdapter)
        slideViewPager.setPageTransformer(true, ZoomOutPageTransformer())
        slideViewPager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {

            }

            override fun onPageSelected(position: Int) {
                slideProgressBar.setProgress(position + 1)
                toggleFabDrawable(slideProgressBar.getProgress())
                changeScrollBehavior(position)
            }

            override fun onPageScrollStateChanged(state: Int) {

            }
        })
        slideProgressBar.setMax(slideViewPager.getAdapter()!!.getCount())
        slideProgressBar.setProgress(1)
        changeScrollBehavior(0)
    }

    private fun changeScrollBehavior(position: Int) {
        currentPosition = position
        val fragment = slideFragmentPagerAdapter!!.getItem(position)
        if (fragment is CodeFragment || fragment is WebViewFragment) {
            slideViewPager.setAllowedSwipeDirection(SwipeDirection.none)
        } else {
            if (position - 1 != -1) {
                if (slideFragmentPagerAdapter!!.getItem(position - 1) is SlideFragment) {
                    val slideFragment = slideFragmentPagerAdapter!!.getItem(position - 1) as SlideFragment
                    slideFragment?.stopAudioPlayback()
                }
            }
            if (fragment is SlideFragment) {
                val slideFragment = slideFragmentPagerAdapter!!.getItem(position) as SlideFragment
                slideFragment.stopAudioPlayback()
                slideFragment.stopAudioAnimation()
            }
            slideViewPager.setAllowedSwipeDirection(SwipeDirection.all)
        }

    }

    override fun showNextLayout() {
        if (currentPosition + 1 == slideViewPager.getAdapter()!!.getCount()) {
            dashboardNavigationListener!!.showNavigateToNextTopic()
        }
    }

    override fun hideNextLayout() {
        dashboardNavigationListener!!.hideNavigateToLayout()
    }

    private fun toggleFabDrawable(progress: Int) {
        val drawable = if (progress == slideProgressBar.getMax()) R.drawable.ic_done_all else android.R.drawable.ic_media_play
        doneFAB.setImageDrawable(ContextCompat.getDrawable(context!!, drawable))
    }

    private lateinit var slideFragment: SlideFragment

    override fun onDataReadComplete(contentArrayList: ArrayList<SlideContent>) {
        Log.d(TAG, "Content : " + contentArrayList)
        val fragments = ArrayList<Fragment>()
        for (slideContent in contentArrayList) {
            when (slideContent.contentType) {
                CONTENT_TYPE_IMAGE -> {
                    var slideFragment = SlideFragment()
                    slideFragment.setPresentationSlideListener(this)
                    slideFragment.setSlideContent(slideContent)
                    fragments.add(slideFragment)
                }
                CONTENT_TYPE_TEXT -> {
                    slideFragment = SlideFragment()
                    slideFragment.setPresentationSlideListener(this)
                    slideFragment.setSlideContent(slideContent)
                    fragments.add(slideFragment)
                }
                CONTENT_TYPE_CODE -> {
                    val codeFragment = CodeFragment()
                    codeFragment.setPresentationSlideListener(this)
                    codeFragment.setSlideContent(slideContent)
                    fragments.add(codeFragment)
                }
                CONTENT_TYPE_URL -> {
                    val webViewFragment = WebViewFragment()
                    webViewFragment.setSlideContent(slideContent)
                    fragments.add(webViewFragment)
                }
            }
        }
        loadSlideFragment(fragments)
    }

    fun navigateBack() {
        if (slideViewPager != null) {
            slideViewPager.setCurrentItem(slideViewPager.getCurrentItem() - 1)
        }
    }
}
