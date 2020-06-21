/**
 *
 * @title: the class to create a map with the marker of the hider and seeker
 * @author: Eva Vogelezang
 */
package com.example.myapplication;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;
import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.io.InputStream;

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
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     */

    @Override
    public void onMapReady(GoogleMap googleMap)  {
        mMap = googleMap;

        //If the inputsream is available and gives the locations of the hider
       // try {
            //if (inputStream.available() > 0){
                // conversion:
                //String lat_s = strInput.substring(0, strInput.indexOf(","));
                //String lng_s = strInput.substring(strInput.indexOf(" ") + 1);

                //double lath = Double.parseDouble(lat_s);
                //double lngh = Double.parseDouble(lng_s);
                //System.out.println("lat=" + lath + " lng=" + lngh);

                // Add a marker for the hider with the position of the hider, a title hider and a HUE_AZURE color
                //LatLng hider = new LatLng(lath, lngh);
                //mMap.addMarker(
                        //new MarkerOptions()
                                //.position(hider)
                                //.title("Position hider")
                                //.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE))
                //);
            //}
        //} catch (IOException e) {
            //e.printStackTrace();
        //}

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
}