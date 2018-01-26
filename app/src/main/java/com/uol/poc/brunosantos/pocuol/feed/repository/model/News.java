package com.uol.poc.brunosantos.pocuol.feed.repository.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.Date;

/**
 * Created by brunosantos on 25/01/2018.
 */

public class News implements Parcelable {

    @SerializedName("type") private String type;
    @SerializedName("title") private String title;
    @SerializedName("thumb") private String thumb;
    @SerializedName("updated") private Date updated;
    @SerializedName("share-url") private String shareUrl;
    @SerializedName("webview-url") private String webViewUrl;

    public News() {
    }

    public News(String type, String title, String thumb, Date updated, String shareUrl, String webViewUrl) {
        this.type = type;
        this.title = title;
        this.thumb = thumb;
        this.updated = updated;
        this.shareUrl = shareUrl;
        this.webViewUrl = webViewUrl;
    }

    protected News(Parcel in) {
        type = in.readString();
        title = in.readString();
        thumb = in.readString();
        long tmpUpdated = in.readLong();
        updated = tmpUpdated != -1 ? new Date(tmpUpdated) : null;
        shareUrl = in.readString();
        webViewUrl = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(type);
        dest.writeString(title);
        dest.writeString(thumb);
        dest.writeLong(updated != null ? updated.getTime() : -1L);
        dest.writeString(shareUrl);
        dest.writeString(webViewUrl);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<News> CREATOR = new Parcelable.Creator<News>() {
        @Override
        public News createFromParcel(Parcel in) {
            return new News(in);
        }

        @Override
        public News[] newArray(int size) {
            return new News[size];
        }
    };

    public String getType() {
        return type;
    }

    public String getTitle() {
        return title;
    }

    public String getThumb() {
        return thumb;
    }

    public Date getUpdated() {
        return updated;
    }

    public String getShareUrl() {
        return shareUrl;
    }

    public String getWebViewUrl() {
        return webViewUrl;
    }
}
