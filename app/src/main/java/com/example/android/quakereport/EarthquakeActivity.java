/*
 * Copyright (C) 2016 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.android.quakereport;

import android.app.LoaderManager;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class EarthquakeActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<EarthquakeOne>> {
    private List<EarthquakeOne> earthquakes;

    private ProgressBar spinner;

    private EarthquakeAdapter adapter;

    private ListView earthquakeListView;

    private TextView emptyView;

    public static final String LOG_TAG = EarthquakeActivity.class.getName();

    int i = 1;


    private static final String SAMPLE_JSON_RESPONSE = "https://earthquake.usgs.gov/fdsnws/event/1/query?format=geojson&eventtype=earthquake&orderby=time&minmag=6&limit=10";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.earthquake_activity);
        //network check
        ConnectivityManager cm =
                (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();
//

        spinner = (ProgressBar) findViewById(R.id.progressBar1);


        earthquakes = new ArrayList<EarthquakeOne>();

        earthquakeListView = (ListView) findViewById(R.id.list);

        emptyView = (TextView) findViewById(R.id.empty_view);
        emptyView.setVisibility(View.GONE);
        if (isConnected == false) {
            spinner.setVisibility(View.GONE);
            emptyView.setText("No internet connection");
            emptyView.setVisibility(View.VISIBLE);
            return;
        }

        adapter = new EarthquakeAdapter(EarthquakeActivity.this, earthquakes);
        Log.v("number", " : " + i);
        i++;

        earthquakeListView.setAdapter(adapter);


        earthquakeListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                EarthquakeOne currentEarthquakeOne = adapter.getItem(i);

                Uri earthquakeUri = Uri.parse(currentEarthquakeOne.getmUrl());

                Intent websiteIntent = new Intent(Intent.ACTION_VIEW, earthquakeUri);

                startActivity(websiteIntent);


            }
        });


        getLoaderManager().initLoader(0, null, this).forceLoad();
    }

    @Override
    public Loader<List<EarthquakeOne>> onCreateLoader(int i, Bundle bundle) {
        return new EarthquakeLoader(this, SAMPLE_JSON_RESPONSE);
    }

    @Override
    public void onLoadFinished(Loader<List<EarthquakeOne>> loader, List<EarthquakeOne> earthquakeOnes) {
        spinner.setVisibility(View.GONE);
        if (adapter != null) adapter.clear();

        if (earthquakeOnes != null && !earthquakeOnes.isEmpty()) {
            adapter.addAll(earthquakeOnes);
        }

        earthquakeListView.setEmptyView(emptyView);

    }

    @Override
    public void onLoaderReset(Loader<List<EarthquakeOne>> loader) {
        adapter.clear();
    }


}
