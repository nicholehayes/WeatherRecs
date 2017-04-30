package com.example.groupproject.weatherrecs;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;

import org.json.JSONObject;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import static com.example.groupproject.weatherrecs.MainActivity.iconImageView;

/**
 * Created by Nichole on 4/16/2017.
 */

public class DownloadAPIData extends AsyncTask<String, Void, String> {

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


    @Override
    protected void onPostExecute(String result){
        super.onPostExecute(result);

        Context applicationContext = MainActivity.getContextOfApplication();
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(applicationContext);
        String hotTempPref = prefs.getString("hot_temperature", "80");
        String coldTempPref = prefs.getString("cold_temperature", "45");


        String cityName, weather;
        int temp;
        int msg;
        String uvMsg = "";
        Double uv;
        String wearMsg = "The weather's great. Wear what you feel!";

        try {
            JSONObject jsonObject = new JSONObject(result);
            JSONObject weatherData = jsonObject.getJSONObject("current_observation");
            JSONObject cityData = weatherData.getJSONObject("display_location");
            String iconUrl = weatherData.getString("icon_url");


            //fetches string data from JSON
            cityName = cityData.getString("city");
            weather = weatherData.getString("weather");
            uv = Double.parseDouble(weatherData.getString("UV"));
            msg = (int) Double.parseDouble(weatherData.getString("pressure_in"));


            //generates sunscreen message based on UV index rating
            if (0 <= uv && uv < 3)
                uvMsg = "You might want to wear sunscreen today.";
            else if (3 <= uv && uv < 6)
                uvMsg = "You should wear sunscreen today.";
            else if (6 <= uv)
                uvMsg = "You definitely need to wear sunscreen today.";


            //generates temperature based on F/C preferences
            if (prefs.getString("list_preference_1","1").equals("1")) {
                temp = (int) Double.parseDouble(weatherData.getString("feelslike_f"));
                MainActivity.temperatureTextView.setText(temp + "°F");
            }
            else {
                temp = (int) Double.parseDouble(weatherData.getString("feelslike_c"));
                MainActivity.temperatureTextView.setText(temp + "°C");
            }


            //generates temperature message based on personal preferences
            if (temp >= Double.parseDouble(hotTempPref))
                wearMsg = "It's a good day to wear a shirt and some shorts.";
            else if (temp <= Double.parseDouble(coldTempPref))
                wearMsg = "It's going to be cold. Pants and a sweater are recommended.";


            //Applies parsed data from JSON to UI elements
            MainActivity.cityTextView.setText(cityName);
            MainActivity.statusTextView.setText(weather);
            MainActivity.rainTextView.setText("The chance of it raining is " + msg + "%.");
            MainActivity.uvTextView.setText(uvMsg);
            MainActivity.clothesTextView.setText(wearMsg);
            MainActivity.accessoriesTextView.setText("Make sure to keep well hydrated; bring a water bottle with you.");


            //rain message for an overly rainy day
            if (msg >= 50)
                MainActivity.accessoriesTextView.setText("Bring an umbrella with you, it's going to rain.");

            //Adds icon image
            new DownloadImageTask(iconImageView)
                    .execute(iconUrl);


        } catch (Exception e) {
            e.printStackTrace();
        }


    }

}
