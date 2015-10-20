package com.malikpoutch.tramparadise.metier.mock;

import com.malikpoutch.tramparadise.R;
import com.malikpoutch.tramparadise.metier.EvenementSignal;
import com.malikpoutch.tramparadise.metier.IEvenementSignal;

import java.util.ArrayList;

/**
 * Created by mbouras on 10/07/2015.
 *
 * Classe servant de bouchonnage pour le menu de droite et pour les markers
 *
 */
public class EvenementSignalMock {


        IEvenementSignal control = new EvenementSignal("Control", R.drawable.police_icon, R.drawable.marker_cops, 0);
        IEvenementSignal accident = new EvenementSignal("Accident", R.drawable.break_icon,  R.drawable.marker_accident, 1);
        IEvenementSignal arret = new EvenementSignal("Arret", R.drawable.arret_icon,  R.drawable.marker_arret, 2);
        IEvenementSignal retard = new EvenementSignal("Retard", R.drawable.retard_icon,  R.drawable.marker_retard, 3);


    ArrayList<IEvenementSignal> listEvent = new ArrayList<IEvenementSignal>();

    public ArrayList<IEvenementSignal> getListEvent() {
        listEvent.add(control);
        listEvent.add(accident);
        listEvent.add(arret);
        listEvent.add(retard);

        return listEvent;
    }

    public void setListEvent(ArrayList<IEvenementSignal> listEvent) {
        this.listEvent = listEvent;
    }

    public IEvenementSignal getControl() {
        return control;
    }

    public void setControl(IEvenementSignal control) {
        this.control = control;
    }

    public IEvenementSignal getAccident() {
        return accident;
    }

    public void setAccident(IEvenementSignal accident) {
        this.accident = accident;
    }

    public IEvenementSignal getArret() {
        return arret;
    }

    public void setArret(IEvenementSignal arret) {
        this.arret = arret;
    }

    public IEvenementSignal getRetard() {
        return retard;
    }

    public void setRetard(IEvenementSignal retard) {
        this.retard = retard;
    }

}
