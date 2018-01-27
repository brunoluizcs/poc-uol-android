package com.uol.poc.brunosantos.pocuol.feed.main.presenter;

import android.content.Context;
import android.net.Uri;
import android.support.v4.content.CursorLoader;

import com.uol.poc.brunosantos.pocuol.feed.repository.local.FeedContract;
import com.uol.poc.brunosantos.pocuol.feed.repository.FeedSyncManager;

/**
 * Created by brunosantos on 27/01/2018.
 */

public class FeedMainPresenterImpl implements FeedMainPresenter {

    @Override
    public CursorLoader getFeedCursorLoader(Context context) {
        Uri feedUri = FeedContract.NewsEntry.CONTENT_URI;
        String sortOrder = FeedContract.NewsEntry.COLUMN_UPDATE + " DESC";

        return new CursorLoader(context,
                feedUri,
                FeedContract.NewsEntry.MAIN_PROJECTION,
                null,
                null,
                sortOrder);
    }

    @Override
    public void syncInitialize(Context context) {
        FeedSyncManager.initialize(context);
    }
}
