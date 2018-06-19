package com.sortedqueue.learnandroid

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.support.v4.app.FragmentTransaction
import android.support.v4.view.ViewPager
import android.support.v7.app.AppCompatActivity
import android.view.*
import android.widget.ImageView

import com.sortedqueue.learnandroid.asynctasks.CodeFileReaderTask
import com.sortedqueue.learnandroid.dashboard.DashboardFragment
import com.sortedqueue.learnandroid.dashboard.DashboardNavigationListener
import com.sortedqueue.learnandroid.topic.PresentationFragment
import com.sortedqueue.learnandroid.topic.TopicFragment

import kotlinx.android.synthetic.main.activity_main.*
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper
import android.content.Intent
import android.graphics.Color
import android.widget.EditText
import com.sortedqueue.learnandroid.R.string.intent
import android.widget.Toast
import android.text.Spannable
import android.text.Selection.getSelectionEnd
import android.text.Selection.getSelectionStart
import android.text.style.BackgroundColorSpan


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
        showSplashDialog()
        Handler().postDelayed({
            dialog.dismiss()
        }, 2500)
        splashImageView.visibility = View.GONE
        splashTextView.visibility = View.GONE
        loadDashboardFragment()
        handleSharedText()
        //codeView = (CodeView) findViewById(R.id.codeView);
        //new FileReaderTask(MainActivity.this, "chapter_1", this).execute();

    }

    private fun handleSharedText() {
        // Get intent, action and MIME type
        val intent = intent
        val action = intent.action
        val type = intent.type
        if (Intent.ACTION_SEND == action && type != null) {
            if ("text/plain" == type) {
                val sharedText = intent.getStringExtra(Intent.EXTRA_TEXT)
                showSharedNotesDialog( sharedText )
            }
        }
    }

    private lateinit var sharedDialog : Dialog
    private fun showSharedNotesDialog( sharedText : String ) {
        sharedDialog = Dialog(this, android.R.style.Theme_Translucent_NoTitleBar)
        sharedDialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        sharedDialog.setContentView(R.layout.dialog_shared_text)

        val window = sharedDialog.window
        val wlp = window.attributes

        wlp.gravity = Gravity.CENTER
        wlp.flags = wlp.flags and WindowManager.LayoutParams.FLAG_BLUR_BEHIND.inv()
        window.attributes = wlp
        sharedDialog.window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
        sharedDialog.show()

        val sharedEditText : EditText = sharedDialog.findViewById<EditText>(R.id.sharedEditText)
        sharedEditText.customSelectionActionModeCallback = object : ActionMode.Callback {
            override fun onActionItemClicked(p0: ActionMode?, item: MenuItem?): Boolean {
                val id = item!!.itemId
                if (id == R.id.item_code) {
                    val start = sharedEditText.selectionStart
                    val end = sharedEditText.selectionEnd
                    val wordToSpan = sharedEditText.text as Spannable
                    wordToSpan.setSpan(BackgroundColorSpan(Color.BLUE),
                            start,
                            end,
                            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
                    Toast.makeText(this@MainActivity, "Marked as code", Toast.LENGTH_SHORT).show()
                    return true
                }
                return false
            }

            override fun onCreateActionMode(mode: ActionMode?, menu: Menu?): Boolean {
                mode!!.title = "Mark as : "
                mode.menuInflater.inflate(R.menu.menu_code, menu);
                return true
            }

            override fun onPrepareActionMode(p0: ActionMode?, menu: Menu?): Boolean {
                menu!!.removeItem(android.R.id.selectAll);
                // Remove the "cut" option
                menu.removeItem(android.R.id.cut);
                // Remove the "copy all" option
                menu.removeItem(android.R.id.copy);
                return true
            }

            override fun onDestroyActionMode(p0: ActionMode?) {

            }

        }
        sharedEditText.setText( sharedText )

    }

    private lateinit var dialog: Dialog

    private fun showSplashDialog() {
        dialog = Dialog(this, android.R.style.Theme_Translucent_NoTitleBar)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.dialog_splash)

        val window = dialog.window
        val wlp = window.attributes

        wlp.gravity = Gravity.CENTER
        wlp.flags = wlp.flags and WindowManager.LayoutParams.FLAG_BLUR_BEHIND.inv()
        window.attributes = wlp
        dialog.window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
        dialog.show()

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
        mFragmentTransaction!!.add(R.id.container, dashboardFragment, DashboardFragment::class.java.simpleName)
        mFragmentTransaction!!.addToBackStack(null)
        mFragmentTransaction!!.commit()
    }

    override fun attachBaseContext(newBase: Context) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase))
    }


    override fun loadTopicFragment() {
        navigateTopicLayout.visibility = View.GONE
        onBackPressed()
        //onProgressStatsUpdate(50);
        /*currentFragmentTAG = "Topics"
        mFragmentTransaction = supportFragmentManager.beginTransaction()
        topicFragment = supportFragmentManager.findFragmentByTag(TopicFragment::class.java.simpleName) as TopicFragment?
        if (topicFragment == null) {
            topicFragment = TopicFragment()
        }
        mFragmentTransaction!!.setCustomAnimations(R.anim.slide_in_left, R.anim.slide_out_left, R.anim.slide_in_right, R.anim.slide_out_right)
        mFragmentTransaction!!.replace(R.id.container, topicFragment, TopicFragment::class.java.simpleName)
        mFragmentTransaction!!.commit()*/
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
        mFragmentTransaction!!.add(R.id.container, presentationFragment, PresentationFragment::class.java.simpleName)
        mFragmentTransaction!!.addToBackStack(null)
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
        /*when (currentFragmentTAG) {
            "Dashboard" -> super.onBackPressed()
            "Topics" -> loadPresentationFragment(currentMainTitle!!, currentTopic!!, topicIndex, topicArray!!)
            "Presentation" -> loadDashboardFragment()
        }*/
        if( supportFragmentManager.backStackEntryCount > 1 ) {
            supportFragmentManager.popBackStack()
        }
        else {
            finish()
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
