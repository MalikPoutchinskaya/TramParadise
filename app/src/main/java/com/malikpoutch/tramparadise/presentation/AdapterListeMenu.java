package com.malikpoutch.tramparadise.presentation;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.malikpoutch.tramparadise.R;

import com.malikpoutch.tramparadise.metier.mock.EvenementSignalMock;

/**
 * Created by mbouras on 09/07/2015.
 */
public class AdapterListeMenu extends ArrayAdapter<String> {

    EvenementSignalMock eventMock = new EvenementSignalMock();

    private Integer[] tab_images_pour_la_liste = {
            eventMock.getControl().getImage(),
            eventMock.getAccident().getImage(),
            eventMock.getArret().getImage(),
            eventMock.getRetard().getImage(),
             };

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater)
                getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View rowView = inflater.inflate(R.layout.left_drawer, parent, false);

        TextView textView = (TextView) rowView.findViewById(R.id.titre_event);
        ImageView imageView = (ImageView) rowView.findViewById(R.id.icon);

        textView.setText(getItem(position));

        if(convertView == null )
            imageView.setImageResource(tab_images_pour_la_liste[position]);
        else
            rowView = (View)convertView;

        return rowView;
    }

    public AdapterListeMenu(Context context, String[] values) {
        super(context, R.layout.left_drawer, values);
    }
}
