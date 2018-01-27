package com.uol.poc.brunosantos.pocuol;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.uol.poc.brunosantos.pocuol.feed.main.view.FeedMainActivity;
import com.uol.poc.brunosantos.pocuol.feed.repository.online.FeedRequester;

import javax.inject.Inject;

public class MainActivity extends AppCompatActivity {
    @Inject
    FeedRequester mFeedRequester;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        launchNewsMainActivity();
    }

    protected void launchNewsMainActivity() {
        startActivity(new Intent(this,FeedMainActivity.class));
        finish();
    }
}
