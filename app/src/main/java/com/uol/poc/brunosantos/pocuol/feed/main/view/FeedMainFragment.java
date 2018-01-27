package com.uol.poc.brunosantos.pocuol.feed.main.view;

import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.uol.poc.brunosantos.pocuol.R;
import com.uol.poc.brunosantos.pocuol.feed.main.presenter.FeedMainPresenter;
import com.uol.poc.brunosantos.pocuol.feed.main.presenter.FeedMainPresenterImpl;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by brunosantos on 25/01/2018.
 */
public class FeedMainFragment extends Fragment implements
        LoaderManager.LoaderCallbacks<Cursor>{

    FeedMainPresenter mPresenter;
    private FeedAdapter mFeedAdapter;

    @BindView(R.id.tb_main) Toolbar mToolbar;
    @BindView(R.id.iv_backdrop) ImageView mBackDropImageView;
    @BindView(R.id.rv_feed) RecyclerView mFeedRecyclerView;


    private static final int ID_FEED_LOADER = 44;
    public static final String TAG = FeedMainFragment.class.getSimpleName();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_feed_main,container,false);
        ButterKnife.bind(this,view);
        Glide.with(getContext())
                .load(getContext().getString(R.string.banner_image_view))
                .into(mBackDropImageView);

        mFeedRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mFeedRecyclerView.setHasFixedSize(true);
        mFeedRecyclerView.setAdapter(mFeedAdapter);

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        mPresenter.syncInitialize(getContext());
        getLoaderManager().initLoader(ID_FEED_LOADER, null, this);
        super.onActivityCreated(savedInstanceState);

        ((AppCompatActivity) getActivity()).setSupportActionBar(mToolbar);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        mPresenter = new FeedMainPresenterImpl();
        mFeedAdapter = new FeedAdapter();
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
        mFeedAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mFeedAdapter.swapCursor(null);
    }
}
