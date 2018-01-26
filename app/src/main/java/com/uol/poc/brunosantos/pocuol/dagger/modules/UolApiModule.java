package com.uol.poc.brunosantos.pocuol.dagger.modules;

import com.uol.poc.brunosantos.pocuol.rest.api.UolApi;

import javax.inject.Inject;
import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import retrofit2.Retrofit;

/**
 * Created by brunosantos on 26/01/2018.
 */

@Module (includes = NetworkModule.class)
public class UolApiModule {

    @Singleton
    @Provides
    UolApi providesApi(Retrofit retrofit) {
        return retrofit.create(UolApi.class);
    }
}
