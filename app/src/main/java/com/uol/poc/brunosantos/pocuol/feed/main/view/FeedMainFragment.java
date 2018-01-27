package com.uol.poc.brunosantos.pocuol.feed.main.view;

import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.uol.poc.brunosantos.pocuol.R;
import com.uol.poc.brunosantos.pocuol.feed.main.presenter.FeedMainPresenter;
import com.uol.poc.brunosantos.pocuol.feed.repository.FeedRepositoryManager;
import com.uol.poc.brunosantos.pocuol.feed.repository.model.Feed;
import com.uol.poc.brunosantos.pocuol.feed.repository.model.News;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by brunosantos on 25/01/2018.
 */
public class FeedMainFragment extends Fragment implements
        LoaderManager.LoaderCallbacks<Cursor>{

    FeedMainPresenter mPresenter;
    @BindView(R.id.tv_news) TextView mNewsTextView;


    private static final int ID_FEED_LOADER = 44;
    private final String TAG = FeedMainFragment.class.getSimpleName();

    public static FeedMainFragment newInstance(FeedMainPresenter presenter) {
        Bundle args = new Bundle();
        FeedMainFragment fragment = new FeedMainFragment();
        fragment.setArguments(args);
        fragment.mPresenter = presenter;
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_feed_main,container,false);
        ButterKnife.bind(this,view);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        mPresenter.syncInitialize(getContext());
        getLoaderManager().initLoader(ID_FEED_LOADER, null, this);
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int loaderId, Bundle args) {
        switch (loaderId) {
            case ID_FEED_LOADER:
                return mPresenter.getFeedCursorLoader(getContext());
            default:
                throw new RuntimeException("Loader Not Implemented: " + loaderId);
        }
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        //TODO: Trocar os dados do adapter
        Feed feed = FeedRepositoryManager.getFeed(data);
        for (News news : feed.getNews()){
            mNewsTextView.append(news.getTitle() + "\n\n\n");
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mNewsTextView.setText("");
        //TODO: Limpar o adapter
    }
}
