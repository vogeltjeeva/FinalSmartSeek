package com.example.myapplication;
/**
 *
 * @title: the class with the useragreements
 * @author: Jolien Crum
 */

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class StartUp extends AppCompatActivity {
    private Button b_agree_useragreement;
    private Button b_disagree_useragreement;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_startup);

        //initialize button agree (of the useragreement)
        b_agree_useragreement = (Button) findViewById(R.id.b_agree_useragreement);
        b_agree_useragreement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openStartMenu();
            }
        });

        //when button disagreeagree (of the useragreement) is pressed the application closes
        b_disagree_useragreement = (Button) findViewById(R.id.b_disagree_useragreement);
        b_disagree_useragreement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                System.exit(0);
            }
        });
    }
    //opens up the new page, main start menu if the button agree of the useragreement is pressed
    public void openStartMenu() {
        Intent intent = new Intent(this, StartMainMenu.class);
        startActivity(intent);
    }

}

