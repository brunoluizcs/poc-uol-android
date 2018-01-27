package com.uol.poc.brunosantos.pocuol.feed.main.view;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;

import com.uol.poc.brunosantos.pocuol.R;


public class FeedMainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed_main);
        loadFragment();
    }

    protected void loadFragment(){
        FragmentManager fragmentManager = getSupportFragmentManager();

        Fragment feedMainFragment = fragmentManager.findFragmentByTag(FeedMainFragment.TAG);
        if (feedMainFragment == null){
            feedMainFragment = new FeedMainFragment();
            fragmentManager
                    .beginTransaction()
                    .add(R.id.fl_container,feedMainFragment,FeedMainFragment.TAG)
                    .commit();
        }
    }

}
