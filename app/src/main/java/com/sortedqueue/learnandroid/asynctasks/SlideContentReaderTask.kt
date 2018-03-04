package com.sortedqueue.learnandroid.asynctasks

import android.annotation.SuppressLint
import android.app.ProgressDialog
import android.content.Context
import android.os.AsyncTask

import com.sortedqueue.learnandroid.constants.LearnDroidConstants
import com.sortedqueue.learnandroid.topic.SlideContent

import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.nio.charset.Charset
import java.util.ArrayList

/**
 * Created by Alok on 07/06/17.
 */
@SuppressLint("StaticFieldLeak")
class SlideContentReaderTask(private val context: Context, private val fileId: String, //Interface to communicate back the response to UI
                             private val onDataReadListener: OnDataReadListener?) : AsyncTask<Void, Void, Void>() {
    private var contentText: String? = null
    private val contentArrayList: ArrayList<SlideContent>

    //To display progress Dialog
    private var progressDialog: ProgressDialog? = null

    interface OnDataReadListener {
        fun onDataReadComplete(contentArrayList: ArrayList<SlideContent>)
    }

    init {
        this.contentArrayList = ArrayList()
    }

    override fun doInBackground(vararg voids: Void): Void? {
        readFileAsList(context)
        return null
    }

    override fun onPreExecute() {
        super.onPreExecute()
        progressDialog = ProgressDialog(context)
        progressDialog!!.isIndeterminate = true
        progressDialog!!.setCancelable(false)
        progressDialog!!.setMessage("Loading")
        progressDialog!!.show()
    }

    override fun onPostExecute(aVoid: Void) {
        super.onPostExecute(aVoid)
        if (progressDialog != null) {
            progressDialog!!.dismiss()
        }
        onDataReadListener?.onDataReadComplete(contentArrayList)
    }

    private fun readFileAsList(context: Context) {
        var line: String?
        try {

            val inputStream = context.resources.openRawResource(
                    context.resources.getIdentifier(fileId,
                            "raw", context.packageName))
            val inputStreamReader = InputStreamReader(inputStream, Charset.forName("UTF-8"))
            val bufferedReader = BufferedReader(inputStreamReader)
            contentText = ""
            line = bufferedReader.readLine()
            while ((line) != null) {
                if (line.startsWith("<image>")) {
                    contentText = ""
                    line = line.replace("<image>", "")
                    line = line.replace("</image>", "")
                    contentArrayList.add(SlideContent(line, LearnDroidConstants.CONTENT_TYPE_IMAGE))

                } else if (line.startsWith("<code>")) {
                    contentText = ""
                    line = line.replace("<code>", "")
                    line = line.replace("</code>", "")
                    contentArrayList.add(SlideContent(line, LearnDroidConstants.CONTENT_TYPE_CODE))
                } else if (line.startsWith("<url>")) {
                    contentText = ""
                    line = line.replace("<url>", "")
                    line = line.replace("</url>", "")
                    contentArrayList.add(SlideContent(line, LearnDroidConstants.CONTENT_TYPE_URL))
                } else if (line.startsWith("<text>")) {
                    line = bufferedReader.readLine()
                    while (true) {
                        if (!line!!.startsWith("</text>"))
                            contentText += line + "<br>"
                        else
                            break
                        line = bufferedReader.readLine()
                    }
                    contentArrayList.add(SlideContent(contentText, LearnDroidConstants.CONTENT_TYPE_TEXT))
                    contentText = ""
                }
                line = bufferedReader.readLine()

            }
            if (contentArrayList.size == 0) {
                if (contentText!!.trim { it <= ' ' }.length > 0) {
                    contentArrayList.add(SlideContent(contentText, LearnDroidConstants.CONTENT_TYPE_TEXT))
                }
            } else if (contentText!!.trim { it <= ' ' }.length > 0) {
                contentArrayList.add(SlideContent(contentText, LearnDroidConstants.CONTENT_TYPE_TEXT))
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }

    }
}
