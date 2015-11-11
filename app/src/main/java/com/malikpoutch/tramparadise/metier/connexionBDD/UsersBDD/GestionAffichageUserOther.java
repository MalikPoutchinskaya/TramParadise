package com.malikpoutch.tramparadise.metier.connexionBDD.UsersBDD;

import com.google.android.gms.maps.model.LatLng;
import com.malikpoutch.tramparadise.metier.UserMaster;

import java.util.ArrayList;

/**
 * Created by Malik on 22/10/2015.
 */
public class GestionAffichageUserOther {


    UserMaster userMaster = new UserMaster();


    ArrayList<LatLng> lstUserOthers = new ArrayList<>();

    int nbOtherEst = 0;
    int nbOtherOuest = 0;
    int nbOtherSud = 0;
    int nbOtherNorth = 0;


    public void tchekWhereIsOther() {
        for (LatLng userOther : lstUserOthers
                ) {
            if (isNorth(userOther) && isOuest(userOther)) {
                double compareLat = Math.abs(userOther.latitude - userMaster.getPositionUserMaster().latitude);
                double compareLong = Math.abs(userOther.longitude - userMaster.getPositionUserMaster().longitude);

                if (compareLat > compareLong)
                    nbOtherNorth++;
                else {
                    nbOtherOuest++;
                }
            } else if (isNorth(userOther) && !isOuest(userOther)) {
                double compareLat = Math.abs(userOther.latitude - userMaster.getPositionUserMaster().latitude);
                double compareLong = Math.abs(userOther.longitude - userMaster.getPositionUserMaster().longitude);

                if (compareLat > compareLong)
                    nbOtherNorth++;
                else {
                    nbOtherEst++;
                }
            } else if (!isNorth(userOther) && !isOuest(userOther)) {
                double compareLat = Math.abs(userOther.latitude - userMaster.getPositionUserMaster().latitude);
                double compareLong = Math.abs(userOther.longitude - userMaster.getPositionUserMaster().longitude);

                if (compareLat > compareLong)
                    nbOtherSud++;
                else if (compareLat < compareLong) {
                    nbOtherEst++;
                }
            } else if (!isNorth(userOther) && isOuest(userOther)) {
                double compareLat = Math.abs(userOther.latitude - userMaster.getPositionUserMaster().latitude);
                double compareLong = Math.abs(userOther.longitude - userMaster.getPositionUserMaster().longitude);

                if (compareLat > compareLong)
                    nbOtherSud++;
                else {
                    nbOtherOuest++;
                }


            }

        }
    }

    public boolean isOuest(LatLng userOther) {
        if (userOther.longitude > userMaster.getPositionUserMaster().longitude) {
            return true;
        }
        return false;
    }

    public boolean isNorth(LatLng userOther) {
        if (userOther.latitude > userMaster.getPositionUserMaster().latitude) {
            return true;
        }
        return false;
    }


    //getters and setters

    public UserMaster getUserMaster() {
        return userMaster;
    }

    public void setUserMaster(UserMaster userMaster) {
        this.userMaster = userMaster;
    }

    public ArrayList<LatLng> getLstUserOthers() {
        return lstUserOthers;
    }

    public void setLstUserOthers(ArrayList<LatLng> lstUserOthers) {
        this.lstUserOthers = lstUserOthers;
    }

    public int getNbOtherEst() {
        return nbOtherEst;
    }

    public void setNbOtherEst(int nbOtherEst) {
        this.nbOtherEst = nbOtherEst;
    }

    public int getNbOtherOuest() {
        return nbOtherOuest;
    }

    public void setNbOtherOuest(int nbOtherOuest) {
        this.nbOtherOuest = nbOtherOuest;
    }

    public int getNbOtherSud() {
        return nbOtherSud;
    }

    public void setNbOtherSud(int nbOtherSud) {
        this.nbOtherSud = nbOtherSud;
    }

    public int getNbOtherNorth() {
        return nbOtherNorth;
    }

    public void setNbOtherNorth(int nbOtherNorth) {
        this.nbOtherNorth = nbOtherNorth;
    }
}
