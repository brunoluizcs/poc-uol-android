package com.uol.poc.brunosantos.pocuol.feed.repository.local;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by brunosantos on 25/01/2018.
 */

public class FeedDbHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "feed.db";
    private static final int DATABASE_VERSION = 1;

    public FeedDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        final String SQL_CREATE_WEATHER_TABLE =
                "CREATE TABLE " + FeedContract.NewsEntry.TABLE_NAME + " (" +
                        FeedContract.NewsEntry._ID     + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        FeedContract.NewsEntry.COLUMN_TYPE  + " TEXT NOT NULL, "                +
                        FeedContract.NewsEntry.COLUMN_TITLE + " TEXT NOT NULL,"                 +
                        FeedContract.NewsEntry.COLUMN_THUMB + " TEXT NOT NULL, "                +
                        FeedContract.NewsEntry.COLUMN_SHARE_URL + " TEXT NOT NULL, "            +
                        FeedContract.NewsEntry.COLUMN_WEBVIEW_URL   + " TEXT NOT NULL, "        +
                        FeedContract.NewsEntry.COLUMN_UPDATE   + " INTEGER NOT NULL, "          +
                        " UNIQUE (" + FeedContract.NewsEntry.COLUMN_UPDATE + ") ON CONFLICT REPLACE);";

        sqLiteDatabase.execSQL(SQL_CREATE_WEATHER_TABLE);
    }


    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + FeedContract.NewsEntry.TABLE_NAME);
        onCreate(sqLiteDatabase);
    }
}
