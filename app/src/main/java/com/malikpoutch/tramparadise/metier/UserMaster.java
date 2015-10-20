package com.malikpoutch.tramparadise.metier;

import com.google.android.gms.maps.model.LatLng;

/**
 * Created by Malik on 20/10/2015.
 */
public class UserMaster {
    LatLng positionUserMaster;

    int idUserMaster;



    //Getters and Setters

    public LatLng getPositionUserMaster() {
        return positionUserMaster;
    }

    public void setPositionUserMaster(LatLng positionUserMaster) {
        this.positionUserMaster = positionUserMaster;
    }

    public int getIdUserMaster() {
        return idUserMaster;
    }

    public void setIdUserMaster(int idUserMaster) {
        this.idUserMaster = idUserMaster;
    }
}
