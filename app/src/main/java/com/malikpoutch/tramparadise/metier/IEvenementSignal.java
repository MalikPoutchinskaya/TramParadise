package com.malikpoutch.tramparadise.metier;

/**
 * Created by mbouras on 10/07/2015.
 */
public interface IEvenementSignal {


    String getNom();

    int getNoLigne();

    public void setNoLigne(int noLigne);

    void setNom(String nom);

    Integer getImage();

    void setImage(Integer image);

    double getLongitude();

    void setLongitude(double longitude);

    double getLatitude();

    void setLatitude(double latitude);

    Integer getImageMarker();

    void setImageMarker(Integer imageMarker);
}
