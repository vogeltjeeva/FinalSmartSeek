package com.example.myapplication;
/**
 *
 * @title: the class that creates the timer on the phone of the seeker
 * @author: Jolien Crum
 */
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.provider.ContactsContract;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.util.Locale;
import java.util.Timer;

public class Time extends AppCompatActivity{

        //starting time of the game timer of 20 minutes (=1.200.000 miliseconds)
        private static final long START_GAMETIME_IN_MILLIS = 15000;
        //starting time of the hider locations timer of 5 minutes (=300.000 miliseconds)
        private static final long START_LOCATIONTIME_IN_MILLIS = 5000;

        private TextView GameTimer;
        private TextView LocationTimer;
        private ImageButton StartTimerButton;
        private ImageButton ResetTimerButton;
        //the timers
        private CountDownTimer GameCountDownTimer;
        private CountDownTimer LocationCountDownTimer;
        //tells us if timers are running
        private boolean TimerRunning;
        //time left in timers
        private long TimeLeftInMillisGame = START_GAMETIME_IN_MILLIS;
        private long TimeLeftInMillisLocation = START_LOCATIONTIME_IN_MILLIS;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_maps);

            //initialize the timers and the start and reset timer symbol
            GameTimer = (TextView) findViewById(R.id.GameTimer);
            LocationTimer = (TextView) findViewById(R.id.LocationTimer);
            StartTimerButton = (ImageButton) findViewById(R.id.bStartTimer);
            ResetTimerButton = (ImageButton) findViewById(R.id.bResetTimer);

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
                    startActivity(new Intent(Time.this,GameEndPopUp.class));
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
