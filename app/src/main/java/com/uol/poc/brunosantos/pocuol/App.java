package com.uol.poc.brunosantos.pocuol;

import android.app.Application;

import com.uol.poc.brunosantos.pocuol.dagger.components.AppComponent;
import com.uol.poc.brunosantos.pocuol.dagger.components.DaggerAppComponent;
import com.uol.poc.brunosantos.pocuol.dagger.modules.AppModule;
import com.uol.poc.brunosantos.pocuol.dagger.modules.NetworkModule;
import com.uol.poc.brunosantos.pocuol.dagger.modules.UolApiModule;

/**
 * Created by brunosantos on 26/01/2018.
 */

public class App extends Application {
    private AppComponent mAppComponent;


    @Override
    public void onCreate() {
        super.onCreate();

        mAppComponent = DaggerAppComponent.builder()
                .appModule(new AppModule(this))
                .networkModule(new NetworkModule(getString(R.string.api_base_url)))
                .uolApiModule(new UolApiModule())
                .build();

    }

    public AppComponent getAppComponent() {
        return mAppComponent;
    }
}
