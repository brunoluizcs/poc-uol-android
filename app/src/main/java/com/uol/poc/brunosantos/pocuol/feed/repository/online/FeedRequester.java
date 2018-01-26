package com.uol.poc.brunosantos.pocuol.feed.repository.online;

import android.support.annotation.Nullable;
import android.util.Log;

import com.uol.poc.brunosantos.pocuol.feed.repository.model.Feed;
import com.uol.poc.brunosantos.pocuol.rest.api.EmptyBodyException;
import com.uol.poc.brunosantos.pocuol.rest.api.RequestErrorException;
import com.uol.poc.brunosantos.pocuol.rest.api.UolApi;
import com.uol.poc.brunosantos.pocuol.rest.api.UolCallback;

import javax.inject.Inject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by brunosantos on 26/01/2018.
 */
public class FeedRequester {
    private static final String TAG = FeedRequester.class.getSimpleName();
    private final String QUERY_APP = "uol-placar-futebol";
    private final int QUERY_VERION = 2;

    private UolApi mUolApi;

    @Inject
    public FeedRequester(UolApi uolApi) {
        this.mUolApi = uolApi;
    }

    public void getAll(@Nullable final UolCallback<Feed> feedUolCallback){
        Call<Feed> response = this.mUolApi.listNews(QUERY_APP,QUERY_VERION);

        response.enqueue(new Callback<Feed>() {
            @Override
            public void onResponse(Call<Feed> call, Response<Feed> response) {
                String statusResponse = response.isSuccessful() ? "success" : "fail";
                Log.d(TAG, "onResponse: " + statusResponse);

                if (feedUolCallback != null){
                    if (response.isSuccessful()){
                        Feed feed = response.body();
                        if (feed != null) {
                            feedUolCallback.onSuccess(feed);
                        }else{
                            feedUolCallback.onError(new EmptyBodyException());
                        }

                    }else{
                        feedUolCallback.onError(new RequestErrorException());
                    }
                }
            }

            @Override
            public void onFailure(Call<Feed> call, Throwable t) {
                Log.e(TAG, "onFailure: ", t);
                feedUolCallback.onError(t);

            }
        });
    }
}
