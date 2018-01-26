package com.uol.poc.brunosantos.pocuol.feed.repository.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by brunosantos on 25/01/2018.
 */
public class Feed implements Parcelable {

    @SerializedName("feed")
    private List<News> news;

    public List<News> getNews() {
        return news;
    }

    public Feed(List<News> news) {
        this.news = news;
    }

    public Feed() {
    }

    protected Feed(Parcel in) {
        if (in.readByte() == 0x01) {
            news = new ArrayList<News>();
            in.readList(news, News.class.getClassLoader());
        } else {
            news = null;
        }
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        if (news == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeList(news);
        }
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<Feed> CREATOR = new Parcelable.Creator<Feed>() {
        @Override
        public Feed createFromParcel(Parcel in) {
            return new Feed(in);
        }

        @Override
        public Feed[] newArray(int size) {
            return new Feed[size];
        }
    };


}
