package com.example.android.quakereport;

import android.content.AsyncTaskLoader;
import android.content.Context;

import java.util.List;

/**
 * Created by kshyo on 2017-10-01.
 */

public class EarthquakeLoader extends AsyncTaskLoader<List<EarthquakeOne>> {

    List<EarthquakeOne> earthquakes;

    @Override
    protected void onStartLoading() {
        forceLoad();
    }

    private String urls;

    public EarthquakeLoader(Context context, String url) {
        super(context);
        urls = url;
    }

    @Override
    public List<EarthquakeOne> loadInBackground() {
        // Create a fake list of earthquake locations.
        earthquakes = QueryUtils.fetchEarthquakeData(urls);
        return earthquakes;
    }

}

