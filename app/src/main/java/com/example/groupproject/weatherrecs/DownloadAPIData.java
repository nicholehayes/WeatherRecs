package com.example.groupproject.weatherrecs;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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


        String cityName, weather, temp, intTemp;
        String uvMsg = "";
        Double uv;

        try {
            JSONObject jsonObject = new JSONObject(result);
            JSONObject weatherData = jsonObject.getJSONObject("current_observation");
            JSONObject cityData = weatherData.getJSONObject("display_location");


            //fetches string data from JSON
            cityName = cityData.getString("city");
            weather = weatherData.getString("weather");
            temp = weatherData.getString("feelslike_f");
            uv = Double.parseDouble(weatherData.getString("UV"));



            //removes decimal point from temperature string
            intTemp = temp.substring(0, temp.length() - 2);



            //TODO: UV index fluctuates throughout the day. Consider moving this to hourly if we have time for it.
            //generates sunscreen message based on UV index rating
            if (0 <= uv && uv < 3)
                uvMsg = "You might want to wear sunscreen today.";
            else if (3 <= uv && uv < 6)
                uvMsg = "You should wear sunscreen today.";
            else if (6 <= uv)
                uvMsg = "You definitely need to wear sunscreen today.";
            
            
            /*TODO: weather icon fetch doesn't work currently
            String iconURL = weatherData.getString("icon_url");
            URL url = new URL(iconURL);
            Bitmap bmp = BitmapFactory.decodeStream(url.openConnection().getInputStream());*/



            //populates TextViews with parsed information
            MainActivity.cityTextView.setText(cityName);
            MainActivity.temperatureTextView.setText(intTemp + "°F");
            MainActivity.statusTextView.setText(weather);
            MainActivity.uvTextView.setText(uvMsg);

            //MainActivity.iconImageView.setImageBitmap(bmp);


        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
