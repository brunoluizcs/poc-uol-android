package com.uol.poc.brunosantos.pocuol.rest.api;

import com.uol.poc.brunosantos.pocuol.feed.repository.model.Feed;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by brunosantos on 26/01/2018.
 */
public interface UolApi {
    @GET("c/api/v1/list/news")
    Call<Feed> listNews(@Query("app") String app,
                        @Query("version") int version);

}
