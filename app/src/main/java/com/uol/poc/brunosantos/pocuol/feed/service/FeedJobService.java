package com.uol.poc.brunosantos.pocuol.feed.service;

import android.util.Log;

import com.firebase.jobdispatcher.JobParameters;
import com.firebase.jobdispatcher.JobService;
import com.uol.poc.brunosantos.pocuol.App;
import com.uol.poc.brunosantos.pocuol.feed.repository.FeedRepositoryManager;
import com.uol.poc.brunosantos.pocuol.feed.repository.online.FeedRequester;

import javax.inject.Inject;

/**
 * Created by brunosantos on 27/01/2018.
 */

public class FeedJobService extends JobService {
    private static final String TAG = FeedJobService.class.getSimpleName();

    @Inject
    FeedRequester mFeedRequester;

    @Override
    public boolean onStartJob(JobParameters jobParameters) {
        Log.d(TAG, "onStartJob: ");
        ((App)getApplication()).getAppComponent().inject(this);
        FeedRepositoryManager.sync(getContentResolver(),mFeedRequester);
        return true;
    }

    @Override
    public boolean onStopJob(JobParameters jobParameters) {
        Log.d(TAG, "onStopJob: ");
        return false;
    }
}
