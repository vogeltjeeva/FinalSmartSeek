package com.example.myapplication;

/**
 *
 * @title: the page that shows our contact information
 * @author: Jolien Crum
 *
 */

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

public class Contact extends AppCompatActivity {
    private ImageButton HomeSymbol;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact);

        //activates the website link and redirects you to chrome
        TextView website = (TextView) findViewById(R.id.textwebsite);
        website.setMovementMethod(LinkMovementMethod.getInstance());

        //email link
        TextView email = (TextView) findViewById(R.id.textEmail);
        email.setText(Html.fromHtml("<a href=\"mailto:InfoSmartSeek@gmail.com\">Our email:InfoSmartSeek@gmail.com</a>"));
        email.setMovementMethod(LinkMovementMethod.getInstance());

        //activates the phone number ink and redirects you to call method on phone
        TextView PhoneNumber = (TextView) findViewById(R.id.textPhoneNumber);
        PhoneNumber.setMovementMethod(LinkMovementMethod.getInstance());

        //initialize home symbol button
        HomeSymbol= (ImageButton) findViewById(R.id.HomeSymbol);
        //when the home symbol button is clicked open the main menu tab
        HomeSymbol.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Contact.this,StartMainMenu.class));
            }
        });
    }
}
