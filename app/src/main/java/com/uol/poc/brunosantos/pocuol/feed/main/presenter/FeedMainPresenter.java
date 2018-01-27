package com.uol.poc.brunosantos.pocuol.feed.main.presenter;

import android.content.Context;
import android.support.v4.content.CursorLoader;

/**
 * Created by brunosantos on 27/01/2018.
 */

public interface FeedMainPresenter {

    CursorLoader getFeedCursorLoader(Context context);
    void syncInitialize(Context context);

}
