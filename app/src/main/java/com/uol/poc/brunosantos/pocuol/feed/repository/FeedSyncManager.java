package com.uol.poc.brunosantos.pocuol.feed.repository;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.support.annotation.NonNull;

import com.firebase.jobdispatcher.Constraint;
import com.firebase.jobdispatcher.Driver;
import com.firebase.jobdispatcher.FirebaseJobDispatcher;
import com.firebase.jobdispatcher.GooglePlayDriver;
import com.firebase.jobdispatcher.Job;
import com.firebase.jobdispatcher.Lifetime;
import com.firebase.jobdispatcher.Trigger;
import com.uol.poc.brunosantos.pocuol.feed.repository.local.FeedContract;
import com.uol.poc.brunosantos.pocuol.feed.service.FeedIntentService;
import com.uol.poc.brunosantos.pocuol.feed.service.FeedJobService;

import java.util.concurrent.TimeUnit;

/**
 * Created by brunosantos on 27/01/2018.
 */

public class FeedSyncManager {
    private static final int SYNC_INTERVAL_HOURS = 1;
    private static final int SYNC_INTERVAL_SECONDS = (int) TimeUnit.HOURS.toSeconds(SYNC_INTERVAL_HOURS);
    private static final int SYNC_FLEXTIME_SECONDS = SYNC_INTERVAL_SECONDS / SYNC_INTERVAL_HOURS;

    private static boolean sInitialized;

    private static final String FEED_SYNC_UTILS = "feed-sync-utils";



    static void scheduleFirebaseJobDispatcherSync(@NonNull final Context context) {

        Driver driver = new GooglePlayDriver(context);
        FirebaseJobDispatcher dispatcher = new FirebaseJobDispatcher(driver);

        Job syncSunshineJob = dispatcher.newJobBuilder()
                .setService(FeedJobService.class)
                .setTag(FEED_SYNC_UTILS)
                .setConstraints(Constraint.ON_ANY_NETWORK)
                .setLifetime(Lifetime.FOREVER)
                .setRecurring(true)
                .setReplaceCurrent(true)
                .setTrigger(Trigger.executionWindow(SYNC_INTERVAL_SECONDS,SYNC_INTERVAL_SECONDS +
                        SYNC_FLEXTIME_SECONDS))
                .build();
        dispatcher.schedule(syncSunshineJob);
    }

    synchronized public static void initialize(@NonNull final Context context) {
        if (sInitialized) return;

        sInitialized = true;

        scheduleFirebaseJobDispatcherSync(context);

        Thread checkForEmpty = new Thread(new Runnable() {
            @Override
            public void run() {
                Uri feedUri = FeedContract.NewsEntry.CONTENT_URI;

                String[] projectionColumns = {FeedContract.NewsEntry._ID};

                Cursor cursor = context.getContentResolver().query(
                        feedUri,
                        projectionColumns,
                        null,
                        null,
                        null);

                if (cursor == null || cursor.getCount() == 0) {
                    startImmediateSync(context);
                }

                cursor.close();
            }
        });
        checkForEmpty.start();
        checkForEmpty.run();
    }


    public static void startImmediateSync(@NonNull final Context context) {
        Intent intentToSyncImmediately = new Intent(context, FeedIntentService.class);
        context.startService(intentToSyncImmediately);
    }
}
