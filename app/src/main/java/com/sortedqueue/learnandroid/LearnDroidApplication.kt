package com.sortedqueue.learnandroid

import android.app.Application

import uk.co.chrisjenx.calligraphy.CalligraphyConfig

/**
 * Created by Alok on 07/06/17.
 */

class LearnDroidApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        CalligraphyConfig.initDefault(CalligraphyConfig.Builder()
                .setDefaultFontPath("fonts/VarelaRound-Regular.ttf")
                .setFontAttrId(R.attr.fontPath)
                .build()
        )
    }
}
