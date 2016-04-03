package com.malikpoutch.tramparadise.metier.connexionBDD.UsersBDD;

import android.app.ProgressDialog;
import android.media.Image;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.maps.model.LatLng;
import com.malikpoutch.tramparadise.metier.GestionLigneTram;
import com.malikpoutch.tramparadise.metier.mock.EvenementSignalMock;
import com.malikpoutch.tramparadise.utils.JSONParser;

import org.apache.http.NameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Malik on 28/08/2015.
 * <p/>
 * <p/>
 * Background Async Task to Load all product by making HTTP Request
 * <p/>
 * Todo: plein d'infos inutiles faire le ménage, 'name' par exemple
 */
public class GetAllUserOther extends AsyncTask<String, String, String> {



    //GetAllUserOther pour renvoyer en Metier
    GestionAffichageUserOther gestionAffichageUserOther = new GestionAffichageUserOther();

    //Init des TextView
    TextView est;
    TextView ouest;
    TextView sud;
    TextView nord;
    //Comptage du nombre d'utilisateur;
    Boolean firstTime;
    private int counter = 0;
    private int totalP; // the total number
    TextView totalPeople;
    ImageView icon;


    public GetAllUserOther(GestionAffichageUserOther gestionAffichageUserOther, TextView est, TextView ouest, TextView sud, TextView nord) {
        this.gestionAffichageUserOther = gestionAffichageUserOther;
        this.est=est;
        this.sud=sud;
        this.nord=nord;
        this.ouest=ouest;
    }

    public GetAllUserOther(GestionAffichageUserOther gestionAffichageUserOther, TextView totalPeople, ImageView icon, Boolean firstTime) {
        this.gestionAffichageUserOther = gestionAffichageUserOther;
        this.totalPeople =totalPeople;
        this.icon = icon;
        this.firstTime = firstTime;
    }

    // Progress Dialog
    private ProgressDialog pDialog;

    // Creating JSON Parser object
    JSONParser jParser = new JSONParser();

    ArrayList<HashMap<String, String>> eventList = new ArrayList<HashMap<String, String>>();

    //Test--> marche, on laisse
    ArrayList<LatLng> listUserOther = new ArrayList<LatLng>();


    // creating new HashMap
    HashMap<String, String> map = new HashMap<String, String>();

    // url to get all products list
    private static String url_all_products = "http://tramp.hol.es/AccesBDDTramParadise/Users/getAllUserOther.php";

    //Récupération de la list d'event
    EvenementSignalMock eventMock = new EvenementSignalMock();


    // JSON Node names
    //TODO:remplacer PRODCUT et PID par Event et id
    private static final String TAG_SUCCESS = "success";
    private static final String TAG_PRODUCTS = "tbusers";
    private static final String TAG_PID = "id";
    private static final String TAG_NAME = "name";
    private static final String TAG_LAT = "lat";
    private static final String TAG_LONG = "long";


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
                    LatLng userOther = new LatLng(lat, longi);

                    //On met tout dans une liste de UserOther qui sera renvoye pour traitement
                    listUserOther.add(userOther);


                    // adding each child node to HashMap key => value
                    map.put(TAG_PID, id);
                    map.put(TAG_NAME, name);
                    //map.put(TAG_LAT, lat);
                    //map.put(TAG_LONG, longi);


                    // adding HashList to ArrayList
                    eventList.add(map);
                    Log.d("taille liste", "" + eventList.size());
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

        //J'envoi ma liste pour traitemetb
        gestionAffichageUserOther.setLstUserOthers(listUserOther);
        gestionAffichageUserOther.tchekWhereIsOther();

        //Je les affichent

        Log.e("nbEst", gestionAffichageUserOther.getNbOtherEst() + "");

        //est.setText(gestionAffichageUserOther.getNbOtherEst()+"");
        //ouest.setText(gestionAffichageUserOther.getNbOtherOuest()+"");
        //sud.setText(gestionAffichageUserOther.getNbOtherSud() + "");
        //nord.setText(gestionAffichageUserOther.getNbOtherNorth() + "");

        totalP = gestionAffichageUserOther.getLstUserOthers().size();
        //Init
        //Ajuste la taille du chiffre
        //float tailleIcon= icon.getHeight();
        //totalPeople.setTextScaleX(tailleIcon);

        totalPeople.setText("" + counter);


        //effet d'un compteur
        if (firstTime) {
            new Thread(new Runnable() {

                public void run() {
                    while (counter < totalP) {
                        try {
                            Thread.sleep(200);
                        } catch (InterruptedException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                        totalPeople.post(new Runnable() {

                            public void run() {
                                totalPeople.setText("" + counter);

                            }

                        });
                        counter++;
                    }

                }

            }).start();

        }else{
            totalPeople.setText("" + totalP);
        }

    }
}








