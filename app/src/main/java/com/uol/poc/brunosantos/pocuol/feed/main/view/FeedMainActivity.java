package com.uol.poc.brunosantos.pocuol.feed.main.view;


import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

import com.uol.poc.brunosantos.pocuol.R;
import com.uol.poc.brunosantos.pocuol.feed.FeedBaseActivity;


public class FeedMainActivity extends FeedBaseActivity {

    @Override
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
