package com.uol.poc.brunosantos.pocuol;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.uol.poc.brunosantos.pocuol.feed.main.FeedMainActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        launchNewsMainActivity();
    }

    protected void launchNewsMainActivity() {
        Intent intentForNewsMain = FeedMainActivity.launchWith(this);
        startActivity(intentForNewsMain);
        finish();
    }
}
