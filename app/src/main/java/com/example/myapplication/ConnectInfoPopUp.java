package com.example.myapplication;
/**
 *
 * @title: the activity to create a popup if the device is connected
 * @author: Jolien Crum
 *
 */

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.DisplayMetrics;

public class ConnectInfoPopUp extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connect_info_pop_up);

        //sets difference in windows size from main window size (fullscreen)
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        int width = dm.widthPixels;
        int heigth = dm.heightPixels;
        //the main windows size width x0.8 and the height x 0.6
        getWindow().setLayout((int) (width*.8), (int) (heigth*.4));

    }
}