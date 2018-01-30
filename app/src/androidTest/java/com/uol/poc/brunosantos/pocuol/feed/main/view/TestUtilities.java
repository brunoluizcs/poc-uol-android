package com.uol.poc.brunosantos.pocuol.feed.main.view;

import android.content.ContentValues;

import java.util.Date;

import static com.uol.poc.brunosantos.pocuol.feed.repository.local.FeedContract.NewsEntry.COLUMN_SHARE_URL;
import static com.uol.poc.brunosantos.pocuol.feed.repository.local.FeedContract.NewsEntry.COLUMN_THUMB;
import static com.uol.poc.brunosantos.pocuol.feed.repository.local.FeedContract.NewsEntry.COLUMN_TITLE;
import static com.uol.poc.brunosantos.pocuol.feed.repository.local.FeedContract.NewsEntry.COLUMN_TYPE;
import static com.uol.poc.brunosantos.pocuol.feed.repository.local.FeedContract.NewsEntry.COLUMN_UPDATE;
import static com.uol.poc.brunosantos.pocuol.feed.repository.local.FeedContract.NewsEntry.COLUMN_WEBVIEW_URL;

/**
 * Created by brunosantos on 29/01/2018.
 */

class TestUtilities {

    static ContentValues getFeedValues(String title){
        ContentValues feedValues = new ContentValues();

        feedValues.put(COLUMN_UPDATE, new Date().getTime());
        feedValues.put(COLUMN_TYPE, "news");
        feedValues.put(COLUMN_THUMB, "http://lorempixel.com/142/100");
        feedValues.put(COLUMN_TITLE, title);
        feedValues.put(COLUMN_SHARE_URL, "http://share.com");
        feedValues.put(COLUMN_WEBVIEW_URL, "http://www.google.com");

        return feedValues;


    }


}
