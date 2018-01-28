package com.uol.poc.brunosantos.pocuol.feed.detail;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;

import com.uol.poc.brunosantos.pocuol.R;
import com.uol.poc.brunosantos.pocuol.feed.repository.model.News;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by brunosantos on 25/01/2018.
 */
public class FeedDetailFragment extends Fragment {

    public static final String TAG = FeedDetailFragment.class.getSimpleName();
    public static final String EXTRA_NEWS = "extra-news";

    @BindView(R.id.wv_detail) WebView mDetailWebView;
    @BindView(R.id.tb_main) Toolbar mToolbar;



    public static FeedDetailFragment newInstance(@NonNull News news) {
        Bundle args = new Bundle();
        FeedDetailFragment fragment = new FeedDetailFragment();
        args.putParcelable(EXTRA_NEWS,news);
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        ((AppCompatActivity) getActivity()).setSupportActionBar(mToolbar);
        mToolbar.setBackgroundColor(getContext().getResources().getColor(R.color.colorPrimary));
        mToolbar.setTitleTextColor(getContext().getResources().getColor(R.color.white));
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_feed_detail,container,false);
        ButterKnife.bind(this,view);
        setupWebView();
        return view;
    }


    protected void setupWebView(){
        News news = getArguments().getParcelable(EXTRA_NEWS);
        String url = news != null ?
                news.getWebViewUrl() :
                "";

        mDetailWebView.getSettings().setJavaScriptEnabled(true);
        mDetailWebView.loadUrl(url);
    }
}
