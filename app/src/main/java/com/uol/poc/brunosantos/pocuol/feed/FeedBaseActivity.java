package com.uol.poc.brunosantos.pocuol.feed;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.uol.poc.brunosantos.pocuol.R;

/**
 * Created by brunosantos on 28/01/2018.
 */

public abstract class FeedBaseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed_base);
        loadFragment();
    }

    protected abstract void loadFragment();


}
