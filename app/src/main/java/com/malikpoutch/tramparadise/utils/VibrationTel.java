package com.malikpoutch.tramparadise.utils;

import android.annotation.TargetApi;
import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.media.AudioAttributes;
import android.os.Build;
import android.os.Vibrator;
import android.util.Log;

/**
 * Created by Malik on 07/09/2015.
 * Gere les paramètre de vibration du tel
 */
@TargetApi(Build.VERSION_CODES.LOLLIPOP)
public class VibrationTel {
    int nbrePassage = 0;
    int modeVibration = 2;
    int sizeBddBefore; //Permet de stocker la taille de la Bdd pour comparaison
    int repeatVib = 500; // 0.5secondes
    Context context;


    //Constructeur, recupere le context du thread principal
    public VibrationTel(Context context) {
        this.context = context;
    }


    //Methode peremttant de faire vibrer et sonner
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public void doVibration() {
        Vibrator vib = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);


        AudioAttributes.Builder attrs = new AudioAttributes.Builder();
        attrs.setUsage(AudioAttributes.USAGE_ALARM);





        //vib.vibrate(repeatVib, attributes.build());
        vib.vibrate(repeatVib, attrs.build());


        Log.e("sonnoooo", vib.hasVibrator()+"");

    }



    //Getter and Setters
    public int getNbrePassage() {
        return nbrePassage;
    }

    public void setNbrePassage(int nbrePassage) {
        this.nbrePassage = nbrePassage;
    }

    public int getModeVibration() {
        return modeVibration;
    }

    public void setModeVibration(int modeVibration) {
        this.modeVibration = modeVibration;
    }

    public int getSizeBddBefore() {
        return sizeBddBefore;
    }

    public void setSizeBddBefore(int sizeBddBefore) {
        this.sizeBddBefore = sizeBddBefore;
    }


}
