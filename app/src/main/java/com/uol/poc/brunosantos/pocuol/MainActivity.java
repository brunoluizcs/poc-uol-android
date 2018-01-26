package com.uol.poc.brunosantos.pocuol;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.uol.poc.brunosantos.pocuol.feed.view.main.FeedMainActivity;
import com.uol.poc.brunosantos.pocuol.feed.repository.model.Feed;
import com.uol.poc.brunosantos.pocuol.feed.repository.online.FeedRequester;
import com.uol.poc.brunosantos.pocuol.rest.api.UolCallback;

import javax.inject.Inject;

public class MainActivity extends AppCompatActivity {
    @Inject
    FeedRequester mFeedRequester;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ((App) getApplication()).getAppComponent().inject(this);
        mFeedRequester.getAll(new UolCallback<Feed>() {
            @Override
            public void onSuccess(@NonNull Feed object) {
                Toast.makeText(MainActivity.this, object.getNews().get(0).getTitle(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(@NonNull Throwable throwable) {
                Toast.makeText(MainActivity.this, throwable.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });

        //launchNewsMainActivity();
    }

    protected void launchNewsMainActivity() {
        Intent intentForNewsMain = FeedMainActivity.launchWith(this);
        startActivity(intentForNewsMain);
        finish();
    }
}
