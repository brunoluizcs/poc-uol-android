package com.uol.poc.brunosantos.pocuol.feed.service;

import android.app.IntentService;
import android.content.Intent;
import android.support.annotation.Nullable;

import com.uol.poc.brunosantos.pocuol.App;
import com.uol.poc.brunosantos.pocuol.feed.repository.FeedRepositoryManager;
import com.uol.poc.brunosantos.pocuol.feed.repository.online.FeedRequester;

import javax.inject.Inject;

/**
 * Created by brunosantos on 27/01/2018.
 */

public class FeedIntentService extends IntentService {
    @Inject
    FeedRequester mFeedRequester;

    public FeedIntentService() {
        super(FeedIntentService.class.getSimpleName());
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        ((App) getApplication()).getAppComponent().inject(this);
        FeedRepositoryManager.sync(getContentResolver(),mFeedRequester);
    }
}
