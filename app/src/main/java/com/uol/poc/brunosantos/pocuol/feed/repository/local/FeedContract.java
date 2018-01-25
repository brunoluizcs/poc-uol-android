package com.uol.poc.brunosantos.pocuol.feed.repository.local;

import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by brunosantos on 25/01/2018.
 */

public class FeedContract {

    public static final String CONTENT_AUTHORITY = "com.uol.poc.brunosantos.pocuol";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);
    public static final String PATH_FEED = "feed";

    public static final class NewsEntry implements BaseColumns {

        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon()
                .appendPath(PATH_FEED)
                .build();

        public static final String TABLE_NAME = "news";

        public static final String COLUMN_TYPE = "type";
        public static final String COLUMN_TITLE = "title";
        public static final String COLUMN_THUMB = "thumb";
        public static final String COLUMN_UPDATE = "lastupdate";
        public static final String COLUMN_SHARE_URL = "shareurl";
        public static final String COLUMN_WEBVIEW_URL = "webviewurl";

    }



}
