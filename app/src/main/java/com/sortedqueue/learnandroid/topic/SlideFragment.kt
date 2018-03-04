package com.sortedqueue.learnandroid.topic

import android.content.Context
import android.os.Build
import android.os.Bundle
import android.speech.tts.TextToSpeech
import android.speech.tts.UtteranceProgressListener
import android.support.design.widget.FloatingActionButton
import android.support.v4.app.Fragment
import android.support.v4.widget.NestedScrollView
import android.support.v7.widget.CardView
import android.text.method.ScrollingMovementMethod
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import android.view.animation.AlphaAnimation
import android.view.animation.Animation
import android.view.animation.LinearInterpolator
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView

import com.bumptech.glide.Glide
import com.sortedqueue.learnandroid.R
import com.sortedqueue.learnandroid.dashboard.DashboardNavigationListener
import com.sortedqueue.learnandroid.utils.ImageUtils

import java.util.Locale
import com.sortedqueue.learnandroid.constants.LearnDroidConstants.CONTENT_TYPE_IMAGE
import com.sortedqueue.learnandroid.constants.LearnDroidConstants.CONTENT_TYPE_TEXT
import kotlinx.android.synthetic.main.fragment_slide.*

/**
 * Created by Alok on 05/06/17.
 */

class SlideFragment : Fragment() {


    private var textToSpeech: TextToSpeech? = null
    private val TAG = SlideFragment::class.java.simpleName

    private var dashboardNavigationListener: DashboardNavigationListener? = null
    private var slideContent: SlideContent? = null
    private var presentationSlideListener: PresentationSlideListener? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val fragmentView = inflater.inflate(R.layout.fragment_slide, container, false)
        questionTextView.setMovementMethod(ScrollingMovementMethod())
        questionTextView.setText(dashboardNavigationListener!!.currentTopic)
        contentTextView.setVisibility(View.GONE)
        slideImageView.setVisibility(View.GONE)


        when (slideContent!!.contentType) {
            CONTENT_TYPE_IMAGE -> {
                val imageUrl = slideContent!!.content
                Glide.with(context)
                        .load(imageUrl)
                        .asBitmap()
                        .fitCenter()
                        .error(R.color.md_red_400)
                        .placeholder(android.R.drawable.ic_menu_report_image)
                        .into(slideImageView)
                slideImageView.setOnClickListener(View.OnClickListener { ImageUtils.zoomImageFromThumb(context, slideLayout, slideImageView, expandedImageView, imageUrl) })
                slideImageView.setVisibility(View.VISIBLE)
                audioFAB.setVisibility(View.GONE)
            }
            CONTENT_TYPE_TEXT -> {
                contentTextView.setVisibility(View.VISIBLE)
                contentTextView.setText(slideContent!!.spannableContent)
                audioFAB.setVisibility(View.VISIBLE)
                audioFAB.setOnClickListener(View.OnClickListener {
                    audioProgressBar.setVisibility(View.VISIBLE)
                    audioProgressBar.setIndeterminate(true)
                    playNotes(contentTextView.getText().toString())
                })
            }
        }

        slideScrollView.getViewTreeObserver().addOnScrollChangedListener(ViewTreeObserver.OnScrollChangedListener {
            if (slideScrollView == null) {
                return@OnScrollChangedListener
            }
            val view = slideScrollView.getChildAt(slideScrollView.getChildCount() - 1)

            val diff = view.getBottom() - (slideScrollView.getHeight() + slideScrollView
                    .getScrollY())

            if (diff == 0) {
                presentationSlideListener!!.showNextLayout()
            } else {
                presentationSlideListener!!.hideNextLayout()
            }
        })

        return fragmentView
    }

    fun setPresentationSlideListener(presentationSlideListener: PresentationSlideListener) {
        this.presentationSlideListener = presentationSlideListener
    }

    fun stopAudioAnimation() {
        if (audioFAB != null) {
            if (activity == null) {
                return
            }
            activity!!.runOnUiThread {
                audioFAB.clearAnimation()
                audioProgressBar.setVisibility(View.GONE)
            }
        }
    }

    fun stopAudioPlayback() {
        if (textToSpeech != null) {
            textToSpeech!!.stop()
            textToSpeech!!.shutdown()
            stopAudioAnimation()
        }
    }

    fun playNotes(speechText: String) {
        textToSpeech = TextToSpeech(context, TextToSpeech.OnInitListener { status ->
            if (status != TextToSpeech.ERROR) {
                textToSpeech!!.language = Locale.US
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    textToSpeech!!.speak(speechText, TextToSpeech.QUEUE_FLUSH, null, speechText.hashCode().toString())
                }
            }
        }, null)
        textToSpeech!!.setPitch(0.85f)
        textToSpeech!!.setSpeechRate(0.9f)
        textToSpeech!!.setOnUtteranceProgressListener(object : UtteranceProgressListener() {
            override fun onStart(s: String) {
                Log.d(TAG, "Started audio : " + s)
                startAnimation()

            }

            override fun onDone(s: String) {
                Log.d(TAG, "onDone audio : " + s)
                stopAudioAnimation()
            }

            override fun onError(s: String) {
                Log.d(TAG, "onError audio : " + s)
                stopAudioAnimation()
            }
        })

    }

    override fun onPause() {
        stopAudioPlayback()
        super.onPause()
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


    private fun startAnimation() {
        if (activity == null) {
            return
        }
        activity!!.runOnUiThread {
            val animation = AlphaAnimation(1f, 0f)
            animation.duration = 800
            animation.interpolator = LinearInterpolator()
            animation.repeatCount = Animation.INFINITE
            animation.repeatMode = Animation.REVERSE
            audioFAB.startAnimation(animation)
            audioProgressBar.setVisibility(View.GONE)
        }

    }

}
