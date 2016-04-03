package com.malikpoutch.tramparadise.metier.connexionBDD;

import android.content.Context;
import android.util.Log;
import android.widget.ProgressBar;

import com.google.android.gms.maps.GoogleMap;
import com.malikpoutch.tramparadise.R;
import com.malikpoutch.tramparadise.metier.connexionBDD.EventBDD.GetAllEvent;
import com.malikpoutch.tramparadise.utils.CustomProgressDialog;
import com.malikpoutch.tramparadise.utils.VibrationTel;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.TimerTask;

/**
 * Created by Malik on 01/09/2015.
 */
public class UserEventTimer extends TimerTask {

    private Context mContext;
    private GoogleMap mapi;
    private VibrationTel vibrationTel;
    private double latitude;
    private double longitude;
    private String name;
    private ProgressBar pbar;

    //Constructeur pour les vibrations si nouvel event et l'update de la position des users
    public UserEventTimer(Context mContext, GoogleMap mapi, VibrationTel vibrationTel, double longitude, double latitude,ProgressBar pbar) {
        this.mContext = mContext;
        this.mapi = mapi;
        this.vibrationTel = vibrationTel;
        this.latitude = latitude;
        this.longitude = longitude;
        this.pbar = pbar;
    }


    public void run() {
        Log.e("timer", "je passe");

        GetAllEvent getAllEvent = new GetAllEvent(mContext, mapi, vibrationTel, pbar);
        getAllEvent.execute();


    }

    public GoogleMap getMapi() {
        return mapi;
    }

    public void setMapi(GoogleMap mapi) {
        this.mapi = mapi;
    }

    public VibrationTel getVibrationTel() {
        return vibrationTel;
    }

    public void setVibrationTel(VibrationTel vibrationTel) {
        this.vibrationTel = vibrationTel;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Context getmContext() {
        return mContext;
    }

    public void setmContext(Context mContext) {
        this.mContext = mContext;
    }
}
