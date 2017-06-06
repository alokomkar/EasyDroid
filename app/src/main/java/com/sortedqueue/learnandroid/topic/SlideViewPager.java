package com.sortedqueue.learnandroid.topic;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

/**
 * Created by Alok on 06/06/17.
 */

public class SlideViewPager extends FragmentPagerAdapter {


    public SlideViewPager(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        return new SlideFragment();
    }

    @Override
    public int getCount() {
        return 4;
    }
}
