package com.example.groupproject.weatherrecs;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.InputStream;

public class MainActivity extends AppCompatActivity {

    static TextView cityTextView;
    static TextView temperatureTextView;
    static TextView statusTextView;
    static TextView uvTextView;
    static TextView clothesTextView;
    static ImageView iconImageView;


    // gives access to preferences in non-activity classes
    public static Context contextOfApplication;
    public static Context getContextOfApplication(){
        return contextOfApplication;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        contextOfApplication = getApplicationContext();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        cityTextView = (TextView) findViewById(R.id.cityTextView);
        temperatureTextView = (TextView) findViewById(R.id.temperatureTextView);
        statusTextView = (TextView) findViewById(R.id.statusTextView);
        uvTextView = (TextView) findViewById(R.id.uvTextView);
        clothesTextView = (TextView) findViewById(R.id.clothesTextView);
        iconImageView = (ImageView) findViewById(R.id.iconImageView);


        /*
        //This part gets user location via app permissions
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        String provider = locationManager.getBestProvider(new Criteria(), false);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Call this when the user doesn't grant permission! e.g. the user will pick a location, etc.
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        Location location = locationManager.getLastKnownLocation(provider);

        Double lat = location.getLatitude();
        Double lng = location.getLongitude();
        */

        //The location data (latitude/longitude) is then used to generate an accurate weather JSON
        DownloadAPIData task = new DownloadAPIData();
        task.execute("http://api.wunderground.com/api/43f3a903f5e333e9/conditions/q/LA/Baton_Rouge.json");

        //DownloadHourlyData hourlyTask = new DownloadHourlyData();
        //hourlyTask.execute("http://api.wunderground.com/api/43f3a903f5e333e9/hourly/q/LA/Baton_Rouge.json");

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Intent i = new Intent(this,SettingsActivity.class);
            this.startActivity(i);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

}


