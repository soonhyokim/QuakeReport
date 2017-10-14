package com.example.android.quakereport;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Helper methods related to requesting and receiving earthquake data from USGS.
 */
public final class QueryUtils {

    public static final String LOG_TAG = QueryUtils.class.getSimpleName();


    private QueryUtils() {
    }
    public static List<EarthquakeOne> fetchEarthquakeData(String requestUrl) {

        // Create URL object
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        URL url = createUrl(requestUrl);



        // Perform HTTP request to the URL and receive a JSON response back

        String jsonResponse = null;

        try {

            jsonResponse = makeHttpRequest(url);

        } catch (IOException e) {

            Log.e(LOG_TAG, "Error closing input stream", e);

        }



        // Extract relevant fields from the JSON response and create an {@link Event} object

        List<EarthquakeOne> earthquakes = extractEarthquakes(jsonResponse);



        // Return the {@link Event}

        return earthquakes;

    }
    /**
     * Return a list of {@link EarthquakeOne} objects that has been built up from
     * parsing a JSON response.
     */
    public static List<EarthquakeOne> extractEarthquakes(String jsonResponse) {
        DecimalFormat formatter = new DecimalFormat("0.00");
        // Create an empty ArrayList that we can start adding earthquakes to
        List<EarthquakeOne> earthquakes = new ArrayList<>();

        // Try to parse the SAMPLE_JSON_RESPONSE. If there's a problem with the way the JSON
        // is formatted, a JSONException exception object will be thrown.
        // Catch the exception so the app doesn't crash, and print the error message to the logs.
        // TODO: Parse the response given by the SAMPLE_JSON_RESPONSE string and
        // build up a list of Earthquake objects with the corresponding data.
        try {
                JSONObject jsonObj = new JSONObject(jsonResponse);



            // getting json array node
            JSONArray features = jsonObj.getJSONArray("features");
            for(int i=0; i<features.length(); i++){
                JSONObject feature = features.getJSONObject(i);

                JSONObject property = feature.getJSONObject("properties");

                double mag = property.getDouble("mag");

                String place = property.getString("place");
                long timeInMillisenconds = property.getLong("time");

                Date dateObject = new Date(timeInMillisenconds);

                String dateToDisplay = formatDate(dateObject);
                String timeToDisplay = formatTime(dateObject);

                String uri = property.getString("url");
              earthquakes.add(new EarthquakeOne(mag, place, dateToDisplay, timeToDisplay, uri));
            }

        } catch (JSONException e) {
            // If an error is thrown when executing any of the above statements in the "try" block,
            // catch the exception here, so the app doesn't crash. Print a log message
            // with the message from the exception.
            Log.e("QueryUtils", "Problem parsing the earthquake JSON results", e);
        }



        // Return the list of earthquakes
        return earthquakes;
    }

    private static String makeHttpRequest(URL url) throws IOException {

        String jsonResponse = "";



        // If the URL is null, then return early.

        if (url == null) {

            return jsonResponse;

        }



        HttpURLConnection urlConnection = null;

        InputStream inputStream = null;

        try {

            urlConnection = (HttpURLConnection) url.openConnection();

            urlConnection.setReadTimeout(10000 /* milliseconds */);

            urlConnection.setConnectTimeout(15000 /* milliseconds */);

            urlConnection.setRequestMethod("GET");

            urlConnection.connect();



            // If the request was successful (response code 200),

            // then read the input stream and parse the response.

            if (urlConnection.getResponseCode() == 200) {

                inputStream = urlConnection.getInputStream();

                jsonResponse = readFromStream(inputStream);

            } else {

                Log.e(LOG_TAG, "Error response code: " + urlConnection.getResponseCode());

            }

        } catch (IOException e) {

            Log.e(LOG_TAG, "Problem retrieving the earthquake JSON results.", e);

        } finally {

            if (urlConnection != null) {

                urlConnection.disconnect();

            }

            if (inputStream != null) {

                inputStream.close();

            }

        }

        return jsonResponse;

    }

    private static String readFromStream(InputStream inputStream) throws IOException {

        StringBuilder output = new StringBuilder();

        if (inputStream != null) {

            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));

            BufferedReader reader = new BufferedReader(inputStreamReader);

            String line = reader.readLine();

            while (line != null) {

                output.append(line);

                line = reader.readLine();

            }

        }

        return output.toString();

    }

    private static URL createUrl(String stringUrl) {

        URL url = null;

        try {

            url = new URL(stringUrl);

        } catch (MalformedURLException e) {

            Log.e(LOG_TAG, "Error with creating URL ", e);

        }

        return url;

    }


    private static String formatDate(Date dateObject) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("LLL dd, yyyy");
        return dateFormat.format(dateObject);
    }

    /**
     * Return the formatted date string (i.e. "4:30 PM") from a Date object.
     */
    private static String formatTime(Date dateObject) {
        SimpleDateFormat timeFormat = new SimpleDateFormat("h:mm a");
        return timeFormat.format(dateObject);
    }

}