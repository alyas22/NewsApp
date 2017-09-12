package com.example.android.newsapp;

import android.app.LoaderManager;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity  extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<News>>
       , SwipeRefreshLayout.OnRefreshListener
{
    private NewsAdapter mAdapter;
    public static final String LOG_TAG = MainActivity.class.getName();
    private static final String USGS_REQUEST_URL = "http://content.guardianapis.com/search?q=debate&tag=education/education&from-date=2012-01-01&api-key=test&page-size=15&show-tags=contributor";
    SwipeRefreshLayout swipe;
    private static final int News_LOADER_ID = 1;
    ListView newsListView;
    TextView mEmptyStateTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.i(LOG_TAG,"TEST MainActivity onCreat()");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        swipe = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh);
        swipe.setOnRefreshListener(this);
        swipe.setColorSchemeColors(getResources().getColor(R.color.colorAccent));
        newsListView = (ListView) findViewById(R.id.list_view);

        mAdapter = new NewsAdapter(this, new ArrayList<News>());
        newsListView.setAdapter(mAdapter);

        mEmptyStateTextView = (TextView) findViewById(R.id.empty_view);
        newsListView.setEmptyView(mEmptyStateTextView);

        newsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                News currentNews = mAdapter.getItem(position);
                Uri newsUri = Uri.parse(currentNews.getUrl());
                Intent websiteIntent = new Intent(Intent.ACTION_VIEW, newsUri);
                startActivity(websiteIntent);
            }
        });

        ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

        if (networkInfo != null && networkInfo.isConnected()) {
            LoaderManager loaderManager = getLoaderManager();
            loaderManager.initLoader(News_LOADER_ID, null, this);
        } else {
            mEmptyStateTextView.setText(R.string.no_connection);
        }
     }

    @Override
    public Loader<List<News>> onCreateLoader(int i, Bundle bundle) {
        Log.i(LOG_TAG,"TEST calling onCreateLoader()");
        return new NewsLoader(this, USGS_REQUEST_URL);
    }

    @Override
    public void onLoadFinished(Loader<List<News>> loader, List<News> news) {
        mEmptyStateTextView.setText(R.string.no_news);
        swipe.setRefreshing(false);
        Log.i(LOG_TAG,"TEST calling onLoadFinished()");
        mAdapter.clear();
        if (news != null && !news.isEmpty()) {
            mAdapter.addAll(news);
        }
    }

    @Override
    public void onLoaderReset(Loader<List<News>> loader) {
        Log.i(LOG_TAG,"TEST calling onLoaderReset()");
        mAdapter.clear();
    }

    @Override
    public void onRefresh() {
        getLoaderManager().restartLoader(News_LOADER_ID,null,this);
    }
}
