package com.uol.poc.brunosantos.pocuol.feed.main.view;


import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.uol.poc.brunosantos.pocuol.R;
import com.uol.poc.brunosantos.pocuol.feed.main.presenter.FeedMainPresenter;
import com.uol.poc.brunosantos.pocuol.feed.main.presenter.FeedMainPresenterImpl;


public class FeedMainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed_main);
        loadFragment();
    }

    protected void loadFragment(){
        FeedMainPresenter mainPresenter = new FeedMainPresenterImpl();
        FeedMainFragment feedMainFragment = FeedMainFragment.newInstance(mainPresenter);

        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.fl_container,feedMainFragment)
                .commit();
    }

}
