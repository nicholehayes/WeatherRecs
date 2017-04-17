package com.example.groupproject.weatherrecs;

import android.os.AsyncTask;
import android.view.inputmethod.InputConnection;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.HttpURLConnection;

/**
 * Created by Nichole on 4/16/2017.
 */

public class DownloadAPIData extends AsyncTask<String, Void, String> {


    //This connects to the Wunderground API's URL to grab current weather data upon opening the app.
    @Override
    protected String doInBackground(String... urls){

        String result = "";
        URL url;
        HttpURLConnection urlConnection = null;

        try {
            url = new URL(urls[0]);
            urlConnection = (HttpURLConnection) url.openConnection();

            InputStream in = urlConnection.getInputStream();
            InputStreamReader reader = new InputStreamReader(in);

            int data = reader.read();

            while(data != -1){
                char current = (char) data;
                result += current;
                data = reader.read();
            }

            return result;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    //This parses the downloaded Wunderground JSON file for certain attributes, like current temperature or UV index, etc.
    @Override
    protected void onPostExecute(String result){
        super.onPostExecute(result);

        //TODO: change basically all of this to fit Wunderground JSON stuff
        try {
            JSONObject jsonObject = new JSONObject(result);
            JSONObject weatherData = new JSONObject(jsonObject.getString("current_observation"));

            String cityName = jsonObject.getString(weatherData.getString("city"));
            String tempF = jsonObject.getString(weatherData.getString("temp_f"));

            MainActivity.cityTextView.append(cityName);
            MainActivity.temperatureTextView.append(tempF);

            /*JSONArray jsonArray = new JSONArray(weatherInfo);

            for (int i = 0; i < jsonArray.length(); i++){
                JSONObject jsonPart = jsonArray.getJSONObject(i);



            }*/


        } catch (Exception e) {
            e.printStackTrace();
        }


    }


}