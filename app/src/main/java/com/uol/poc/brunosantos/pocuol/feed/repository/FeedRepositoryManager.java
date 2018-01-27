package com.uol.poc.brunosantos.pocuol.feed.repository;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v4.content.CursorLoader;
import android.util.Log;

import com.uol.poc.brunosantos.pocuol.feed.repository.local.FeedContract;
import com.uol.poc.brunosantos.pocuol.feed.repository.model.Feed;
import com.uol.poc.brunosantos.pocuol.feed.repository.model.News;
import com.uol.poc.brunosantos.pocuol.feed.repository.online.FeedRequester;
import com.uol.poc.brunosantos.pocuol.rest.api.UolCallback;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Created by brunosantos on 26/01/2018.
 */

public class FeedRepositoryManager {
    private static final String TAG = FeedRepositoryManager.class.getSimpleName();

    public static void sync(final ContentResolver contentResolver, FeedRequester feedRequester){
        Log.d(TAG, "sync: ");
        feedRequester.getAll(new UolCallback<Feed>() {
            @Override
            public void onSuccess(@NonNull Feed object) {
                ContentValues[] values = new ContentValues[object.getNews().size()];
                List<News> newsList = object.getNews();
                int pos = 0;
                for(News news : newsList){
                    ContentValues c = new ContentValues();
                    c.put(FeedContract.NewsEntry.COLUMN_TYPE,news.getType());
                    c.put(FeedContract.NewsEntry.COLUMN_TITLE,news.getTitle());
                    c.put(FeedContract.NewsEntry.COLUMN_THUMB,news.getThumb());
                    c.put(FeedContract.NewsEntry.COLUMN_SHARE_URL,news.getShareUrl());
                    c.put(FeedContract.NewsEntry.COLUMN_WEBVIEW_URL,news.getWebViewUrl());
                    c.put(FeedContract.NewsEntry.COLUMN_UPDATE,news.getUpdated() != null ?
                            news.getUpdated().getTime() : 0);
                    values[pos] = c;
                    pos++;
                }
                Log.d(TAG, "onSuccess: ");
                contentResolver.bulkInsert(FeedContract.NewsEntry.CONTENT_URI,values);
            }

            @Override
            public void onError(@NonNull Throwable throwable) {
                Log.e(TAG, "onError: ", throwable);
            }
        });
    }

    public static Feed getFeed(@NonNull Cursor cursor){
        List<News> newsList = new ArrayList<>();
        if (cursor.moveToFirst()){
            do{
               newsList.add(new News(
                       cursor.getString(cursor.getColumnIndex(FeedContract.NewsEntry.COLUMN_TYPE)),
                       cursor.getString(cursor.getColumnIndex(FeedContract.NewsEntry.COLUMN_TITLE)),
                       cursor.getString(cursor.getColumnIndex(FeedContract.NewsEntry.COLUMN_THUMB)),
                       new Date(cursor.getInt(cursor.getColumnIndex(FeedContract.NewsEntry.COLUMN_UPDATE))),
                       cursor.getString(cursor.getColumnIndex(FeedContract.NewsEntry.COLUMN_SHARE_URL)),
                       cursor.getString(cursor.getColumnIndex(FeedContract.NewsEntry.COLUMN_WEBVIEW_URL))

               ));
            }while(cursor.moveToNext());
        }
        return new Feed(newsList);


    }

}
