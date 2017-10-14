package com.example.android.quakereport;

import android.app.Activity;
import android.graphics.drawable.GradientDrawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.util.List;

/**
 * Created by kshyo on 2017-09-28.
 */

public class EarthquakeAdapter extends ArrayAdapter<EarthquakeOne> {
    private String originalLocation;
    private String primaryLocation;
    private String locationOffset;

    private static final String LOCATION_SEPARATOR = " of ";

    public EarthquakeAdapter(Activity context, List<EarthquakeOne> earthquakeOnes) {
        super(context, 0, earthquakeOnes);

    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {


        View listItemView = convertView;

        if (listItemView == null) {

            listItemView = LayoutInflater.from(getContext()).inflate(

                    R.layout.list_item, parent, false);

        }


        EarthquakeOne currentEarthquakeOne = getItem(position);

        originalLocation = currentEarthquakeOne.getmPlace();

        if (originalLocation.contains(LOCATION_SEPARATOR)) {
            String[] parts = originalLocation.split(LOCATION_SEPARATOR);
            locationOffset = parts[0] + LOCATION_SEPARATOR;

            primaryLocation = parts[1];
        } else {
            locationOffset = getContext().getString(R.string.near_the);
            primaryLocation = originalLocation;
        }


        TextView magTextView = (TextView) listItemView.findViewById(R.id.magnitude);


        magTextView.setText(formatMagnitude(currentEarthquakeOne.getmMag()));

        GradientDrawable magnitudeCircle = (GradientDrawable) magTextView.getBackground();


        int magnitudeColor = getMagnitudeColor(currentEarthquakeOne.getmMag());

        magnitudeCircle.setColor(magnitudeColor);


        TextView primaryLocationTextView = (TextView) listItemView.findViewById(R.id.primary_location);


        primaryLocationTextView.setText(primaryLocation);


        TextView locationOffsetView = (TextView) listItemView.findViewById(R.id.location_offset);

        locationOffsetView.setText(locationOffset);
        // date

        TextView dateTextView = (TextView) listItemView.findViewById(R.id.date);

        dateTextView.setText(currentEarthquakeOne.getmDate());

        // time
        TextView timeTextView = (TextView) listItemView.findViewById(R.id.time);

        timeTextView.setText(currentEarthquakeOne.getmTime());


        return listItemView;
    }

    private int getMagnitudeColor(double mag) {
        switch ((int) Math.floor(mag)) {
            case 10:
                return ContextCompat.getColor(getContext(), R.color.magnitude10plus);
            case 9:
                return ContextCompat.getColor(getContext(), R.color.magnitude9);
            case 8:
                return ContextCompat.getColor(getContext(), R.color.magnitude8);
            case 7:
                return ContextCompat.getColor(getContext(), R.color.magnitude7);
            case 6:
                return ContextCompat.getColor(getContext(), R.color.magnitude6);
            case 5:
                return ContextCompat.getColor(getContext(), R.color.magnitude5);
            case 4:
                return ContextCompat.getColor(getContext(), R.color.magnitude4);
            case 3:
                return ContextCompat.getColor(getContext(), R.color.magnitude3);
            case 2:
                return ContextCompat.getColor(getContext(), R.color.magnitude2);
            case 1:
                return ContextCompat.getColor(getContext(), R.color.magnitude1);
            default:
                return 0;
        }
    }

    private static String formatMagnitude(double magnitude) {
        DecimalFormat magnitudeFormat = new DecimalFormat("0.0");
        return magnitudeFormat.format(magnitude);
    }
}
