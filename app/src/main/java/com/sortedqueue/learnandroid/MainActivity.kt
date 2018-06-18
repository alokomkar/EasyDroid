package com.sortedqueue.learnandroid

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.support.v4.app.FragmentTransaction
import android.support.v7.app.AppCompatActivity
import android.view.View

import com.sortedqueue.learnandroid.asynctasks.CodeFileReaderTask
import com.sortedqueue.learnandroid.dashboard.DashboardFragment
import com.sortedqueue.learnandroid.dashboard.DashboardNavigationListener
import com.sortedqueue.learnandroid.topic.PresentationFragment
import com.sortedqueue.learnandroid.topic.TopicFragment

import kotlinx.android.synthetic.main.activity_main.*
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper

@SuppressLint("CommitTransaction", "SetTextI18n")
class MainActivity : AppCompatActivity(), CodeFileReaderTask.OnDataReadListener, DashboardNavigationListener, View.OnClickListener {

    private var mFragmentTransaction: FragmentTransaction? = null
    private var dashboardFragment: DashboardFragment? = null
    private var topicFragment: TopicFragment? = null
    private var currentFragmentTAG = ""
    private var presentationFragment: PresentationFragment? = null
    //private var handler: Handler? = null
    //private var runnable: Runnable? = null
    private var currentTopic: String? = "Learn Android"
    private var currentMainTitle: String? = null
    private var topicArray: Array<String>? = null
    private var nextTopic: String? = null
    private var topicIndex: Int = 0

    //private var progressBarStatus: Int = 0


    //private CodeView codeView;
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        navigationTextView.setOnClickListener(this)
        cancelImageView.setOnClickListener(this)
        navigateTopicLayout.visibility = View.GONE
        Handler().postDelayed({
            splashImageView.visibility = View.GONE
            splashTextView.visibility = View.GONE
            loadDashboardFragment()
        }, 2500)
        //codeView = (CodeView) findViewById(R.id.codeView);
        //new FileReaderTask(MainActivity.this, "chapter_1", this).execute();

    }

    /*fun onProgressStatsUpdate(points: Int) {
        progressLayout.visibility = View.VISIBLE
        animateProgress(points)
    }*/

    
    /*private fun animateProgress(points: Int) {
        try {
            if (reputationProgressBar != null) {

                if (handler == null) {
                    handler = Handler()
                }

                reputationProgressBar.visibility = View.VISIBLE
                reputationTextView.visibility = View.VISIBLE
                runnable = Runnable {
                    progressBarStatus = 0
                    while (progressBarStatus <= points) {

                        handler!!.post {
                            if (reputationProgressBar != null) {
                                reputationProgressBar.progress = progressBarStatus

                                reputationTextView.text = "You've gained " + points + "xp\n" + progressBarStatus + "% Complete"
                                val level = 1290 / 100
                                reputationTextView.text = "You've gained " + points + "xp\n" + progressBarStatus + "% Complete : Level : " + level
                            }
                        }
                        try {
                            Thread.sleep(40)
                        } catch (ex: Exception) {
                        }

                        progressBarStatus++
                    }
                    handler!!.postDelayed({ progressLayout.visibility = View.GONE }, 1500)
                }
                Thread(runnable).start()
            }
        } catch (e: Exception) {
            e.printStackTrace()
            if (progressLayout != null) {
                progressLayout.visibility = View.GONE
            }
        }

    }*/

    
    override fun loadDashboardFragment() {
        currentFragmentTAG = "Dashboard"
        mFragmentTransaction = supportFragmentManager.beginTransaction()
        dashboardFragment = supportFragmentManager.findFragmentByTag(DashboardFragment::class.java.simpleName) as DashboardFragment?
        if (dashboardFragment == null) {
            dashboardFragment = DashboardFragment()
        }
        mFragmentTransaction!!.setCustomAnimations(R.anim.anim_slide_in_left, R.anim.anim_slide_out_right)
        mFragmentTransaction!!.replace(R.id.container, dashboardFragment, DashboardFragment::class.java.simpleName)
        mFragmentTransaction!!.commitAllowingStateLoss()
    }

    override fun attachBaseContext(newBase: Context) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase))
    }


    override fun loadTopicFragment() {
        //onProgressStatsUpdate(50);
        currentFragmentTAG = "Topics"
        mFragmentTransaction = supportFragmentManager.beginTransaction()
        topicFragment = supportFragmentManager.findFragmentByTag(TopicFragment::class.java.simpleName) as TopicFragment?
        if (topicFragment == null) {
            topicFragment = TopicFragment()
        }
        mFragmentTransaction!!.setCustomAnimations(R.anim.slide_in_left, R.anim.slide_out_left, R.anim.slide_in_right, R.anim.slide_out_right)
        mFragmentTransaction!!.replace(R.id.container, topicFragment, TopicFragment::class.java.simpleName)
        mFragmentTransaction!!.commit()
    }

    override fun loadPresentationFragment(mainTitle: String, topic: String, topicIndex: Int, topicArray: Array<String>) {
        //onProgressStatsUpdate(50);
        currentTopic = topic
        currentMainTitle = mainTitle
        currentFragmentTAG = "Presentation"
        this.topicArray = topicArray
        this.nextTopic = null
        this.topicIndex = topicIndex
        if (topicIndex + 1 < topicArray.size) {
            nextTopic = topicArray[topicIndex + 1]
        }

        mFragmentTransaction = supportFragmentManager.beginTransaction()
        /*presentationFragment = (PresentationFragment) getSupportFragmentManager().findFragmentByTag(PresentationFragment.class.simpleName);
        if (presentationFragment == null) {
            presentationFragment = new PresentationFragment();
        }*/
        presentationFragment = PresentationFragment()
        mFragmentTransaction!!.setCustomAnimations(R.anim.slide_in_left, R.anim.slide_out_left, R.anim.slide_in_right, R.anim.slide_out_right)
        mFragmentTransaction!!.replace(R.id.container, presentationFragment, PresentationFragment::class.java.simpleName)
        mFragmentTransaction!!.commit()
    }

    override fun getCurrentTopic(): String {
        return currentTopic!!
    }

    override fun getCurrentMainTitle(): String {
        return currentMainTitle!!
    }

    override fun onNavigateBack() {
        if (presentationFragment != null) {
            presentationFragment!!.navigateBack()
        }
    }

    override fun showNavigateToNextTopic() {
        if (nextTopic != null) {
            navigationTextView.text = "Proceed to Next Topic : " + nextTopic!!
            navigateTopicLayout.visibility = View.VISIBLE
        }
    }

    override fun hideNavigateToLayout() {
        navigateTopicLayout.visibility = View.GONE
    }

    override fun onDataReadComplete(code: String?) {
        //codeView.setCode(code, "xml");
    }

    override fun onBackPressed() {
        navigateTopicLayout.visibility = View.GONE
        when (currentFragmentTAG) {
            "Dashboard" -> super.onBackPressed()
            "Topics" -> loadPresentationFragment(currentMainTitle!!, currentTopic!!, topicIndex, topicArray!!)
            "Presentation" -> loadDashboardFragment()
        }
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.navigationTextView -> {
                loadPresentationFragment(currentMainTitle!!, nextTopic!!, topicIndex + 1, topicArray!!)
                navigateTopicLayout.visibility = View.GONE
            }
            R.id.cancelImageView -> navigateTopicLayout.visibility = View.GONE
        }
    }
}
