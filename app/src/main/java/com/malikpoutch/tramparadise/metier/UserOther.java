package com.malikpoutch.tramparadise.metier;

import com.google.android.gms.maps.model.LatLng;

/**
 * Created by Malik on 20/10/2015.
 */
public class UserOther {

    LatLng positionUserOther;

    int idUserOther;



    //Getters and Setters

    public LatLng getPositionUserOther() {
        return positionUserOther;
    }

    public void setPositionUserOther(LatLng positionUserOther) {
        this.positionUserOther = positionUserOther;
    }

    public int getIdUserOther() {
        return idUserOther;
    }

    public void setIdUserOther(int idUserOther) {
        this.idUserOther = idUserOther;
    }
}
