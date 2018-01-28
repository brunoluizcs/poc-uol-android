package com.uol.poc.brunosantos.pocuol.feed.detail;

import android.content.Intent;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Window;

import com.uol.poc.brunosantos.pocuol.R;
import com.uol.poc.brunosantos.pocuol.feed.FeedBaseActivity;
import com.uol.poc.brunosantos.pocuol.feed.main.view.FeedMainFragment;
import com.uol.poc.brunosantos.pocuol.feed.repository.model.News;

import static com.uol.poc.brunosantos.pocuol.feed.detail.FeedDetailFragment.EXTRA_NEWS;

public class FeedDetailActivity extends FeedBaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().requestFeature(Window.FEATURE_PROGRESS);
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void loadFragment() {
        News news = null;
        Intent intent = getIntent();
        if (intent != null && intent.getExtras() != null){
            news = intent.getExtras().containsKey(EXTRA_NEWS) ?
                    (News) intent.getParcelableExtra(EXTRA_NEWS) :
                    null;
        }

        FragmentManager fragmentManager = getSupportFragmentManager();
        Fragment feedDetailFragment = fragmentManager.findFragmentByTag(FeedDetailFragment.TAG);
        if (feedDetailFragment == null){
            if (news == null){
                throw new RuntimeException("News object must not be null.");
            }

            feedDetailFragment = FeedDetailFragment.newInstance(news);
            fragmentManager
                    .beginTransaction()
                    .add(R.id.fl_container,feedDetailFragment, FeedDetailFragment.TAG)
                    .commit();
        }
    }
}
