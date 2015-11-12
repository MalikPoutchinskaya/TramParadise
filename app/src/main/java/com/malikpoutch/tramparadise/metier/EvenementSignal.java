package com.malikpoutch.tramparadise.metier;


import java.util.Date;

/**
 * Created by mbouras on 08/07/2015.
 */
public class EvenementSignal implements IEvenementSignal {

    private String nom;
    private Integer image;
    private double longitude;
    private double latitude;
    private int noLigne;
    private Integer imageMarker;
    private Date date;

    public EvenementSignal() {
    }


    //Constructeur spécifique pour le menu

    public EvenementSignal(String nom, Integer image, double longitude, double latitude, int noLigne) {
        this.nom = nom;
        this.image = image;
        this.longitude = longitude;
        this.latitude = latitude;
        this.noLigne = noLigne;
    }

    //Constructeur pour la BDD
    public EvenementSignal(String nom, Integer image, Integer imageMarker, int noLigne) {
        this.nom = nom;
        this.image = image;
        this.noLigne = noLigne;
        this.imageMarker = imageMarker;


    }

    //Getter & Setters

    @Override
    public String getNom() {
        return nom;
    }

    @Override
    public void setNom(String nom) {
        this.nom = nom;
    }

    @Override
    public Integer getImage() {
        return image;
    }

    @Override
    public void setImage(Integer image) {
        this.image = image;
    }

    @Override
    public double getLongitude() {
        return longitude;
    }

    @Override
    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    @Override
    public double getLatitude() {
        return latitude;
    }

    @Override
    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public int getNoLigne() {
        return noLigne;
    }

    public void setNoLigne(int noLigne) {
        this.noLigne = noLigne;
    }

    public Integer getImageMarker() {
        return imageMarker;
    }

    public void setImageMarker(Integer imageMarker) {
        this.imageMarker = imageMarker;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}
