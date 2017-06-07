package com.sortedqueue.learnandroid.topic;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

/**
 * Created by Alok on 06/06/17.
 */

public class SlideFragmentPagerAdapter extends FragmentPagerAdapter {


    public SlideFragmentPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        if( position == 2 ) {
            return new CodeFragment();
        }
        return new SlideFragment();
    }

    @Override
    public int getCount() {
        return 4;
    }
}
