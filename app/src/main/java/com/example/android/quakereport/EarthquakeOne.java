package com.example.android.quakereport;

/**
 * Created by kshyo on 2017-09-28.
 */

public class EarthquakeOne {
    // magnitude
    private double mMag;
    // location
    private String mPlace;
    //when did occur
    private String mDate;
    private String mTime;

    private String mUri;

    //constructor
    EarthquakeOne(double mag, String place, String date, String time, String uri) {
        mMag = mag;
        mPlace = place;
        mDate = date;
        mTime = time;
        mUri = uri;
    }

    //getter method
    public double getmMag() {
        return mMag;
    }

    public String getmPlace() {
        return mPlace;
    }

    public String getmDate() {
        return mDate;
    }

    public String getmTime() {
        return mTime;
    }

    public String getmUrl() {
        return mUri;
    }

}
