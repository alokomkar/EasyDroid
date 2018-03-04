package com.sortedqueue.learnandroid.topic

import android.text.Html
import android.text.Spanned

/**
 * Created by Alok on 07/06/17.
 */

class SlideContent(var content: String?, var contentType: Int) {

    val spannableContent: Spanned
        get() = Html.fromHtml(content)

    override fun toString(): String {
        return "SlideContent{" +
                "content='" + content + '\'' +
                ", contentType=" + contentType +
                '}'
    }
}
