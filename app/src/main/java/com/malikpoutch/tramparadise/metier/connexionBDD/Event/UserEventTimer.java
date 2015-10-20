package com.malikpoutch.tramparadise.metier.connexionBDD.Event;

import android.content.Context;
import android.util.Log;

import com.google.android.gms.maps.GoogleMap;
import com.malikpoutch.tramparadise.metier.connexionBDD.Event.AllEvent;
import com.malikpoutch.tramparadise.metier.connexionBDD.Users.UpdatePositionUser;
import com.malikpoutch.tramparadise.utils.VibrationTel;

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
    private String name = "hey babe";


    //Constructeur pour les vibrations si nouvel event et l'update de la position des users
    public UserEventTimer(Context mContext, GoogleMap mapi, VibrationTel vibrationTel, double longitude, double latitude) {
        this.mContext = mContext;
        this.mapi = mapi;
        this.vibrationTel = vibrationTel;
        this.latitude = latitude;
        this.longitude = longitude;
    }


    public void run() {
        Log.e("timer", "je passe");
        AllEvent allEvent = new AllEvent(mContext, mapi, vibrationTel);
        allEvent.execute();
        UpdatePositionUser updatePositionUser = new UpdatePositionUser(latitude, longitude, name, mContext);
        updatePositionUser.execute();

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
