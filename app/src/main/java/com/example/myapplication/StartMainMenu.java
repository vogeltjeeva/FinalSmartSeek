package com.example.myapplication;
/**
 *
 * @title: the activity to create a map with the marker of the hider and seeker
 * @author: Jolien Crum
 */

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

    public class StartMainMenu extends AppCompatActivity {
        private Button b_connect_bracelets;
        private Button b_the_map;
        private Button b_contact;


        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_start_menu);

            //initialize connect bracelets button
            b_connect_bracelets= (Button) findViewById(R.id.b_connect_bracelets);
            //when the bracelet button is clicked open the bracelet tab  
            b_connect_bracelets.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(StartMainMenu.this,ConnectBracelet.class));
                }
            });

            //initialize the map button
            b_the_map= (Button) findViewById(R.id.b_the_map);
            //when the map button is clicked open the map tab
            b_the_map.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(StartMainMenu.this,MapsActivity.class));
                }
            });

            //initialize contact button
            b_contact= (Button) findViewById(R.id.b_contact);
            //when contact button is clicked open contact tab
            b_contact.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(StartMainMenu.this,Contact.class));
                }
            });

        }
}
