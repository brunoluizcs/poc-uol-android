package com.uol.poc.brunosantos.pocuol.feed.main;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.uol.poc.brunosantos.pocuol.R;

public class FeedMainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_main);
    }

    public static Intent launchWith(Context context){
        Intent intent = new Intent(context,FeedMainActivity.class);
        return intent;
    }
}
