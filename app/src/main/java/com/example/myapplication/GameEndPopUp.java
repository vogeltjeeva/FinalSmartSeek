package com.example.myapplication;

/**
 *
 * @title: the page that pops up when the game ends
 * @author: Jolien Crum
 *
 */

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;

public class GameEndPopUp extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_end_pop_up);

        //sets difference in windows size from main window size (fullscreen)
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        int width = dm.widthPixels;
        int heigth = dm.heightPixels;
        //the main windows size width x0.8 and the height x 0.6
        getWindow().setLayout((int) (width*.5), (int) (heigth*.2));
    }
}