package com.sortedqueue.learnandroid.utils;

/**
 * Created by Alok on 05/06/17.
 */

public interface ItemTouchHelperAdapter {
    void onItemMove(int fromPosition, int toPosition);
    void onItemDismiss(int position);
}
