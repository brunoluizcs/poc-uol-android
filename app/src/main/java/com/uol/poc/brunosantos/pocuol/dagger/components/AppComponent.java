package com.uol.poc.brunosantos.pocuol.dagger.components;

import com.uol.poc.brunosantos.pocuol.MainActivity;
import com.uol.poc.brunosantos.pocuol.dagger.modules.AppModule;
import com.uol.poc.brunosantos.pocuol.dagger.modules.FeedRequesterModule;
import com.uol.poc.brunosantos.pocuol.dagger.modules.NetworkModule;
import com.uol.poc.brunosantos.pocuol.dagger.modules.UolApiModule;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Created by brunosantos on 26/01/2018.
 */

@Singleton
@Component(modules = {AppModule.class,
        NetworkModule.class,
        UolApiModule.class,
        FeedRequesterModule.class})

public interface AppComponent {
    void inject(MainActivity mainActivity);
}
