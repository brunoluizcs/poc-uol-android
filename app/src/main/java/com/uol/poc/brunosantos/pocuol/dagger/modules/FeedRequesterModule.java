package com.uol.poc.brunosantos.pocuol.dagger.modules;

import com.uol.poc.brunosantos.pocuol.rest.api.UolApi;
import com.uol.poc.brunosantos.pocuol.feed.repository.online.FeedRequester;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by brunosantos on 26/01/2018.
 */

@Module(includes = {UolApiModule.class})
public class FeedRequesterModule {

    @Provides
    @Singleton
    FeedRequester provides(UolApi api) {
        return new FeedRequester(api);
    }
}
