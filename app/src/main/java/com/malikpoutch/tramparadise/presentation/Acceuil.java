package com.malikpoutch.tramparadise.presentation;

/**
 * Created by Malik on 24/08/2015.
 */
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;

import com.malikpoutch.tramparadise.R;

public class Acceuil extends Activity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.popup_refus);

        final Button loginButton = (Button) findViewById(R.id.buttonNantes);
        loginButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Acceuil.this, HomeActivity.class);
                startActivity(intent);
            }
        });
    }

}