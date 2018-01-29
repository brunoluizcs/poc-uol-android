package com.uol.poc.brunosantos.pocuol.feed.detail;

import android.app.Activity;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

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
    @BindView(R.id.view_root) View mViewRoot;



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


    private void setupActionBar() {
        mToolbar.setBackgroundColor(getContext().getResources().getColor(R.color.toolbarColor));
        mToolbar.setTitleTextColor(getContext().getResources().getColor(R.color.toolbarTitleColor));
        ((AppCompatActivity) getActivity()).setSupportActionBar(mToolbar);

        ActionBar supportActionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
        if (supportActionBar != null) {
            supportActionBar.setDisplayHomeAsUpEnabled(true);
        }


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                getActivity().onBackPressed();
                return true;
            case R.id.action_share:
                share();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    protected void share() {
        News news = getArguments().getParcelable(EXTRA_NEWS);
        if (news != null) {
            Uri webpage = Uri.parse(news.getShareUrl());

            Intent intent = new Intent(Intent.ACTION_SEND, webpage);
            intent.setType("text/plain");
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);

            intent.putExtra(Intent.EXTRA_SUBJECT, news.getTitle());
            intent.putExtra(Intent.EXTRA_TEXT, news.getShareUrl());

            if (intent.resolveActivity(getContext().getPackageManager()) != null) {
                startActivity(Intent.createChooser(intent, getContext().getString(R.string.app_name)));
            }
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.detail_menu, menu);


        super.onCreateOptionsMenu(menu,inflater);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_feed_detail,container,false);
        ButterKnife.bind(this,view);
        setupActionBar();
        setHasOptionsMenu(true);
        setupWebView();
        return view;
    }


    protected void setupWebView(){
        News news = getArguments().getParcelable(EXTRA_NEWS);
        String url = news != null ?
                news.getWebViewUrl() :
                "";
        mDetailWebView.setWebViewClient(new WebViewClient() {
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                String errorMessage = getContext().getString(R.string.error_load_url,failingUrl);
                Snackbar snackBar = Snackbar.make(mViewRoot, errorMessage, Snackbar.LENGTH_INDEFINITE);
                snackBar.getView().setBackgroundColor(getContext().getResources().getColor(R.color.snackBarBackground));
                snackBar.setAction(R.string.back, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        getActivity().onBackPressed();
                    }
                });
            }
        });

        mDetailWebView.getSettings().setJavaScriptEnabled(true);
        mDetailWebView.loadUrl(url);
    }
}
