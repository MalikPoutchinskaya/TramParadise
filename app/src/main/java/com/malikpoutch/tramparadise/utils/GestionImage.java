package com.malikpoutch.tramparadise.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;

/**
 * Created by Malik on 31/08/2015.
 */
public class GestionImage {

    private int div=4;

    Context mContext;

    public GestionImage(Context mContext) {
        this.mContext = mContext;
    }


    //Methode permettant de réduire la taille des markers dynamiquement sans perdre la qualité du png
    public Bitmap TailleMarker(Integer d){

        BitmapDrawable bd=(BitmapDrawable) mContext.getResources().getDrawable(d);
        Bitmap b=bd.getBitmap();
        Bitmap bhalfsize=Bitmap.createScaledBitmap(b, b.getWidth()/div,b.getHeight()/div, false);

        return bhalfsize;
    }
}
