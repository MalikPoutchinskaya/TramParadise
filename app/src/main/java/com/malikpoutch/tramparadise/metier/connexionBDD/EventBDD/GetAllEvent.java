package com.malikpoutch.tramparadise.metier.connexionBDD.EventBDD;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.util.Log;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.malikpoutch.tramparadise.metier.GestionLigneTram;
import com.malikpoutch.tramparadise.metier.IEvenementSignal;
import com.malikpoutch.tramparadise.metier.mock.EvenementSignalMock;
import com.malikpoutch.tramparadise.utils.GestionImage;
import com.malikpoutch.tramparadise.utils.JSONParser;
import com.malikpoutch.tramparadise.utils.VibrationTel;

import org.apache.http.NameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

/**
 * Created by Malik on 28/08/2015.
 * <p/>
 * <p/>
 * Background Async Task to Load all product by making HTTP Request
 */
public class GetAllEvent extends AsyncTask<String, String, String> {



    //Context + GoogleMap +  constructeur
    private Context mContext;
    private GoogleMap mapi;
    private VibrationTel vibrationTel;




    public GetAllEvent(Context context, GoogleMap map, VibrationTel vibrationTel) {
        this.mContext = context;
        this.mapi = map;
        this.vibrationTel = vibrationTel;

    }


    //Insiation de la classe dessin des lignes de tram
    GestionLigneTram gestionLigneTram = new GestionLigneTram(mContext);


    // Progress Dialog
    private ProgressDialog pDialog;

    // Creating JSON Parser object
    JSONParser jParser = new JSONParser();

    ArrayList<HashMap<String, String>> eventList = new ArrayList<HashMap<String, String>>();

    //Test--> marche, on laisse
    ArrayList<String> listName = new ArrayList<String>();
    ArrayList<Double> listLat = new ArrayList<Double>();
    ArrayList<Double> listLong = new ArrayList<Double>();
    ArrayList<String> listDate = new ArrayList<String>();



    // creating new HashMap
    HashMap<String, String> map = new HashMap<String, String>();

    // url to get all products list
    private static String url_all_products = "http://tramp.hol.es/AccesBDDTramParadise/Events/SelectAllEvent.php";

    //Récupération de la list d'event
    EvenementSignalMock eventMock = new EvenementSignalMock();
    //Je défini une nouvelle arrayList d'event pour eviter de faire des add Event dans mes boucles
    ArrayList<IEvenementSignal> lstEventMock = eventMock.getListEvent();


    // JSON Node names
    //TODO:remplacer PRODCUT et PID par Event et id
    private static final String TAG_SUCCESS = "success";
    private static final String TAG_PRODUCTS = "tbevent";
    private static final String TAG_PID = "id";
    private static final String TAG_NAME = "name";
    private static final String TAG_LAT = "lat";
    private static final String TAG_LONG = "long";
    private static final String TAG_DATE = "date";



    // products JSONArray
    JSONArray products = null;


    /**
     * Before starting background thread Show Progress Dialog
     */
    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    /**
     * getting All products from url
     */
    protected String doInBackground(String... args) {
        // Building Parameters
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        // getting JSON string from URL
        JSONObject json = jParser.makeHttpRequest(url_all_products, "GET", params);

        // Check your log cat for JSON reponse
        //Log.d("All Products: ", "je passe");

        try {
            // Checking for SUCCESS TAG
            int success = json.getInt(TAG_SUCCESS);

            if (success == 1) {
                // products found
                // Getting Array of Products
                products = json.getJSONArray(TAG_PRODUCTS);

                // looping through All Products
                for (int i = 0; i < products.length(); i++) {
                    JSONObject c = products.getJSONObject(i);

                    // Storing each json item in variable
                    String id = c.getString(TAG_PID);
                    String name = c.getString(TAG_NAME);
                    Double lat = Double.parseDouble(c.getString(TAG_LAT)); //Parse du string de la bdd en double
                    Double longi = Double.parseDouble(c.getString(TAG_LONG)); //Parse du string de la bdd en double
                    String date = c.getString(TAG_DATE);


                    listName.add(name);
                    listLat.add(lat);
                    listLong.add(longi);
                    listDate.add(date);




                    // adding each child node to HashMap key => value
                    map.put(TAG_PID, id);
                    map.put(TAG_NAME, name);
                    // map.put(TAG_LAT, lat);
                    //map.put(TAG_LONG, longi);


                    // adding HashList to ArrayList
                    eventList.add(map);
                    //Log.d("taille liste", "" + eventList.size());
                }
            } else {
                // no products found
                Log.d("check", "Pas d event trouve..");
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return null;
    }

    protected void onPostExecute(String file_url) {
        //Enlève les markers présent sur la map
        mapi.clear();


        //On parcour l'arrayList
        for (int a = 0; a < eventList.size(); a++) {
            //Todo: renvoyer juste la liste dans une classe GetAllEvent et traiter la liste dans une autre classe dédié

            for (IEvenementSignal e : lstEventMock
                    ) {
                if (listName.get(a).equals(e.getNom())) {
                    //Log.e("name", listName.get(a));

                    //Size du marker
                    GestionImage gImg = new GestionImage(mContext);
                    Bitmap marker = gImg.TailleMarker(e.getImageMarker());


                    //Ajoute tout les marker de la base à la map
                    mapi.addMarker(new MarkerOptions()
                            .icon(BitmapDescriptorFactory.fromBitmap(marker))
                            .title(listDate.get(a))
                            .position(new LatLng(listLat.get(a), listLong.get(a))));

                    //Remet les lignes de Tram!
                    mapi.addPolyline(gestionLigneTram.dessinLigne1());
                    mapi.addPolyline(gestionLigneTram.dessinLigne2());
                    mapi.addPolyline(gestionLigneTram.dessinLigne3());


                    //Si ajout dans base de donnee, le tel vibre et si pas a l'ouverture de l'appli
                    if (hasNewEvent(vibrationTel.getSizeBddBefore(), eventList.size()) == true && vibrationTel.getNbrePassage() != 0) {
                        //Le telephone vibre
                        vibrationTel.doVibration();
                       // Log.e("Viiibre!! befor",vibrationTel.getSizeBddBefore()+"");
                       // Log.e("after", eventList.size()+"");



                    }

                    //On valorise la nouveau nombre d'event dans la BDD
                    vibrationTel.setSizeBddBefore(eventList.size());
                    //On incrément le nombre de passage pour le test de l'ouverture de l'appli
                    //Si l'appli se lance pour la premiere fois, pas vibration
                    vibrationTel.setNbrePassage(vibrationTel.getNbrePassage() + 1);
                }
            }

        }

    }

    //Teste le nombre d'event dans la base par rapport au dernier passage
    private boolean hasNewEvent(int sizeBefore, int sizeAfter) {
        if (sizeAfter > sizeBefore) {
            return true;
        } else {
            return false;
        }
    }
}

