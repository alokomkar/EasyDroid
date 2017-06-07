package com.sortedqueue.learnandroid;

import android.app.Application;

import uk.co.chrisjenx.calligraphy.CalligraphyConfig;

/**
 * Created by Alok on 07/06/17.
 */

public class LearnDroidApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath("fonts/VarelaRound-Regular.ttf")
                .setFontAttrId(R.attr.fontPath)
                .build()
        );
    }
}
