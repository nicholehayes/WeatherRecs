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

public class DownloadOWMData extends AsyncTask<String, Void, String> {

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

        try {
            JSONObject jsonObject = new JSONObject(result);

            String weatherInfo = jsonObject.getString("weather");
            String cityName = jsonObject.getString("name");

            JSONObject weatherData = new JSONObject(jsonObject.getString("main"));

            double tempInt = Double.parseDouble(weatherData.getString("temp"));
            int tempIn = (int) (tempInt*1.8-459.67);                            //TODO: change this based on user preferences

            MainActivity.temperatureTextView.setText(String.valueOf(tempIn) + "F");

            MainActivity.cityTextView.setText(cityName);
            JSONArray jsonArray = new JSONArray(weatherInfo);

            for (int i = 0; i < jsonArray.length(); i++){
                JSONObject jsonPart = jsonArray.getJSONObject(i);



            }


        } catch (Exception e) {
            e.printStackTrace();
        }


    }


}
