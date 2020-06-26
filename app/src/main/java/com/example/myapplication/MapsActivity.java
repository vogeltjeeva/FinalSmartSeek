/**
 *
 * @title: the class to create a map with the marker of the hider and seeker
 * @author: Eva Vogelezang
 * @reference:
 * Google Developers. https://developers.google.com/maps/documentation/android-sdk/marker#customize_a_marker
 *
 */
package com.example.myapplication;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;
import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.io.InputStream;
import java.util.Locale;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    //class variables
    private GoogleMap mMap;
    private LocationListener locationListener;
    private LocationManager locationManager;
    private final long MIN_TIME = 1000; //1 second
    private final long MIN_DIST = 5; //2 meters
    private LatLng latLng;
    public String strInput;
    public InputStream inputStream;

    //starting time of the game timer of 20 minutes (=1.200.000 miliseconds)
    private static final long START_GAMETIME_IN_MILLIS = 1200000;
    //starting time of the hider locations timer of 5 minutes (=300.000 miliseconds)
    private static final long START_LOCATIONTIME_IN_MILLIS = 5000;

    private TextView GameTimer;
    private TextView LocationTimer;
    private ImageButton StartTimerButton;
    private ImageButton ResetTimerButton;
    private ImageButton BReturnMenu;
    private ImageButton BInfoPopUp;

    //the timers
    private CountDownTimer GameCountDownTimer;
    private CountDownTimer LocationCountDownTimer;
    //tells us if timers are running
    private boolean TimerRunning;
    //time left in timers
    private long TimeLeftInMillisGame = START_GAMETIME_IN_MILLIS;
    private long TimeLeftInMillisLocation = START_LOCATIONTIME_IN_MILLIS;

    //standard code you get when creating a map activity in Android studios
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        //ask permission to get the location
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, PackageManager.PERMISSION_GRANTED);
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, PackageManager.PERMISSION_GRANTED);

        //initialize the timers and the start and reset timer symbol
        GameTimer = (TextView) findViewById(R.id.GameTimer);
        LocationTimer = (TextView) findViewById(R.id.LocationTimer);
        StartTimerButton = (ImageButton) findViewById(R.id.bStartTimer);
        ResetTimerButton = (ImageButton) findViewById(R.id.bResetTimer);

        //initialize house symbol button
        BReturnMenu = (ImageButton) findViewById(R.id.bHome);
        //if house symbol is clicked return to start menu
        BReturnMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MapsActivity.this, StartMainMenu.class));
            }
        });

        //initialize question mark symbol button
        BInfoPopUp = (ImageButton) findViewById(R.id.bInfoMap);
        //when the symbol is clicked it opens up the Map info popup
        BInfoPopUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(MapsActivity.this, MapInfoPopUp.class));
            }
        });

        //if the start timer symbol is clicked
        StartTimerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //if start timer symbol is clicked it changes to the pause timer symbol
                StartTimerButton.setBackgroundResource(R.drawable.ic_pause);
                //if time is running pause timer method is runned
                if (TimerRunning) {
                    pauseTimer();
                }
                //if timer is not running start timer method is runned
                else{
                    startTimer();
                }
            }
        });

        //if the reset timer symbol is clicked reset timer method is runned
        ResetTimerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetTimer();
            }
        });
        //updates the gametimer from 00:00 to actual time
        updateCountdownGame();
        //updates the locationtimer from 00:00 to actual time
        updateCountdownLocation();

    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     */

    @Override
    public void onMapReady(GoogleMap googleMap)  {
        mMap = googleMap;

        //The code to make the location of the seeker appear on the map with a marker
        locationListener = new LocationListener() {

            //Whenever the location of the seeker is changed it should capture that
            @Override
            public void onLocationChanged(Location location) {
                try {
                    latLng = new LatLng(location.getLatitude(), location.getLongitude());
                    mMap.addMarker(new MarkerOptions().position(latLng).title("Position seeker"));
                    mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
                } catch (SecurityException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(String provider) {

            }

            @Override
            public void onProviderDisabled(String provider) {

            }
        };

        //Let locationmanager get access to system service
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

        //Method to get locationupdates at a certain change of time or location
        try {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, MIN_TIME, MIN_DIST, locationListener);
        } catch (SecurityException e) {
            e.printStackTrace();
        }
    }

    private void startTimer() {

        GameCountDownTimer = new CountDownTimer(TimeLeftInMillisGame, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                TimeLeftInMillisGame = millisUntilFinished;
                //change symbol to an timer with a cross through it
                updateCountdownGame();
            }

            @Override
            public void onFinish() {
                //timer is not running
                TimerRunning = false;
                //changes pause symbol to start symbol
                StartTimerButton.setBackgroundResource(R.drawable.ic_timer);
                //makes start symbol invisible, because reset is needed before the timer can start again
                StartTimerButton.setVisibility(View.INVISIBLE);
                //makes reset symbol visible
                ResetTimerButton.setVisibility(View.VISIBLE);
                //creates popup saying the game has ended
                startActivity(new Intent(MapsActivity.this,GameEndPopUp.class));
                //stop the location timer
                LocationCountDownTimer.cancel();
            }
        }.start();

        LocationCountDownTimer = new CountDownTimer(TimeLeftInMillisLocation, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                TimeLeftInMillisLocation = millisUntilFinished;
                //change symbol to an timer with a cross through it
                updateCountdownLocation();
            }

            @Override
            public void onFinish() {
                //timer is not running
                TimerRunning = false;
                //resets itself to 5 minutes
                TimeLeftInMillisLocation = START_LOCATIONTIME_IN_MILLIS;
                updateCountdownLocation();
                //start countdown again
                LocationCountDownTimer.start();
                //show short message on the screen to clarify that the locations of the hiders are updated
                showToast("Locations of the hiders are updated");

                //!!!!!!!If the inputsream is available and gives the locations of the hider, if the gps module would work this part would be uncommented!!!!!
                // try {
                //if (inputStream.available() > 0){
                // conversion:
                //String lat_s = strInput.substring(0, strInput.indexOf(","));
                //String lng_s = strInput.substring(strInput.indexOf(" ") + 1);

                //double lath = Double.parseDouble(lat_s);
                //double lngh = Double.parseDouble(lng_s);
                //System.out.println("lat=" + lath + " lng=" + lngh);

                //Add a marker for the hider with the position of the hider, a title hider and a HUE_AZURE color
                //LatLng hider = new LatLng(lath, lngh);
                //mMap.addMarker(
                //new MarkerOptions()
                //                .position(hider)
                //                .title("Position hider")
                //                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE))
                //);
                //}
                //} catch (IOException e) {
                //e.printStackTrace();
                //}

                //Add a marker for the hider with the position of the hider, a title hider and a HUE_AZURE color
                LatLng hider = new LatLng(52.2462473, 6.8476649);
                mMap.addMarker(
                        new MarkerOptions()
                                .position(hider)
                                .title("Position hider")
                                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE))
                );

            }
        }.start();

        //if timer start the timer is running
        TimerRunning = true;
        //make reset symbol invisible if start symbol is running
        ResetTimerButton.setVisibility(View.INVISIBLE);
    }

    private void pauseTimer() {
        //cancel/pause the timers
        GameCountDownTimer.cancel();
        LocationCountDownTimer.cancel();
        //timer is not running
        TimerRunning = false;
        //changes pause symbol to start symbol
        StartTimerButton.setBackgroundResource(R.drawable.ic_timer);
        //make reset symbol visible
        ResetTimerButton.setVisibility(View.VISIBLE);

    }
    private void resetTimer() {
        TimeLeftInMillisGame = START_GAMETIME_IN_MILLIS;
        updateCountdownGame();
        TimeLeftInMillisLocation = START_LOCATIONTIME_IN_MILLIS;
        updateCountdownLocation();
        //after reset is pressed, reset symbol goes invisible
        ResetTimerButton.setVisibility(View.INVISIBLE);
        //after reset is pressed, start symbol goes visible
        StartTimerButton.setVisibility(View.VISIBLE);
    }

    private void updateCountdownGame() {
        //create minutes from the milli seconds
        int minutes = (int) (TimeLeftInMillisGame / 1000) / 60;
        //create seconds, returns what is left after calculating the minutes
        int seconds = (int) (TimeLeftInMillisGame / 1000) % 60;
        //make it a string and making it look like a timer
        String timeLeftFormattedGame = String.format(Locale.getDefault(),"%02d:%02d", minutes, seconds);
        //place string into the textview of the game timer
        GameTimer.setText(timeLeftFormattedGame);
    }

    private void updateCountdownLocation() {
        //create minutes from the milli seconds
        int minutes = (int) (TimeLeftInMillisLocation / 1000) / 60;
        //create seconds, returns what is left after calculating the minutes
        int seconds = (int) (TimeLeftInMillisLocation / 1000) % 60;
        //make it a string and making it look like a timer
        String timeLeftFormattedLocation = String.format(Locale.getDefault(),"%02d:%02d", minutes, seconds);
        //place string into the textview of the game timer
        LocationTimer.setText(timeLeftFormattedLocation);
    }

    //show msg string for short amount of time
    private void showToast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }
}
