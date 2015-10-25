package com.malikpoutch.tramparadise.presentation;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.malikpoutch.tramparadise.R;
import com.malikpoutch.tramparadise.metier.EvenementSignal;
import com.malikpoutch.tramparadise.metier.GestionLigneTram;
import com.malikpoutch.tramparadise.metier.IEvenementSignal;
import com.malikpoutch.tramparadise.metier.UserMaster;
import com.malikpoutch.tramparadise.metier.connexionBDD.EventBDD.AddEvent;
import com.malikpoutch.tramparadise.metier.connexionBDD.UserEventTimer;
import com.malikpoutch.tramparadise.metier.connexionBDD.UsersBDD.DeleteUserMaster;
import com.malikpoutch.tramparadise.metier.connexionBDD.UsersBDD.GestionAffichageUserOther;
import com.malikpoutch.tramparadise.metier.connexionBDD.UsersBDD.GetAllUserOther;
import com.malikpoutch.tramparadise.metier.connexionBDD.UsersBDD.PutPositionUser;
import com.malikpoutch.tramparadise.metier.connexionBDD.UsersBDD.UpdatePositionUser;
import com.malikpoutch.tramparadise.metier.mock.EvenementSignalMock;
import com.malikpoutch.tramparadise.utils.GestionImage;
import com.malikpoutch.tramparadise.utils.VibrationTel;

import java.util.Timer;
import java.util.UUID;


public class HomeActivity extends AppCompatActivity implements
        LocationListener, OnMapReadyCallback {

    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    private ActionBarDrawerToggle mDrawerToggle;




    //Variable de positionnement
    private LocationManager lm;

    private double latitude;
    private double longitude;
    private double altitude;
    private float accuracy;


    //Instance du Mock
    EvenementSignalMock eventMock = new EvenementSignalMock();

    //Insiation de la classe dessin des lignes de tram
    GestionLigneTram gestionLigneTram = new GestionLigneTram();

    //Chargement de la liste du menu de gauche
    String[] values = new String[]{eventMock.getControl().getNom(), eventMock.getAccident().getNom(), eventMock.getArret().getNom(), eventMock.getRetard().getNom()}; //charge le nombre d'option du menu, ici 4

    // Creation de la vue
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);


        //Creation de la googleMap
        final MapFragment mapFragment = (MapFragment) getFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);



        //Dessin du menu de gauche
        AdapterListeMenu adaptateur = new AdapterListeMenu(this, values);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerList = (ListView) findViewById(R.id.left_drawer);
        mDrawerList.setAdapter(adaptateur);

        //On insert la position du user dans la BDD
        //en cherchant son identifiant unique
        String name = getDeviceId();
        PutPositionUser putPositionUser = new PutPositionUser(latitude, longitude,name,getApplicationContext());
        putPositionUser.execute();

        // Gestion de l'ouverture du menu de gauche, fait appel a une Classe interne
        mDrawerList.setOnItemClickListener(new DrawerItemClickListener());

        mDrawerToggle = new ActionBarDrawerToggle(
                this, mDrawerLayout,
                R.string.open_drawer,
                R.string.close_drawer
        ) {
            public void onDrawerClosed(View view) {

                Log.d("HomeActivity", "onDrawerClosed");
            }

            public void onDrawerOpened(View drawerView) {
                Log.d("HomeActivity", "onDrawerOpened");
            }
        };
        mDrawerLayout.setDrawerListener(mDrawerToggle);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        //Instance du mode vibration
        VibrationTel vibrationTel = new VibrationTel(getApplicationContext());

        //Actualisation de la page!
        //Timer pour actualiser les markers grace a la BDD
        final int debut = 0; // démarre direct
        final int interval = 10000; //10 secondes

        //Instance de vibration si nouvel event et update position
        UserEventTimer userEventTimer = new UserEventTimer(getApplicationContext(),mapFragment.getMap(), vibrationTel, longitude, latitude);
        new Timer().scheduleAtFixedRate(userEventTimer, debut, interval);

    }


    //Creation du Menu(la toolbar à droite)
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_home, menu);

        return true;
    }


    //Pour l'effet visuel du menu de gauche
    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mDrawerToggle.syncState();
    }

    //Gestoin du clic de la toolbar (menu de droite)
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        //icon de gauche (menu de gauche)
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        // icon de droite
        switch (item.getItemId()) {
            case R.id.action_apropos:
                openAPropos();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void openAPropos() {
        Intent intent = new Intent(HomeActivity.this, APropos.class);
        startActivity(intent);
    }

    //on positionne la map sur Nantes
    @Override
    public void onMapReady(GoogleMap map) {
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(
                new LatLng(47.2184, -1.55362), 11));

        // Other supported types include: MAP_TYPE_NORMAL,
        // MAP_TYPE_TERRAIN, MAP_TYPE_HYBRID and MAP_TYPE_NONE
        map.setMapType(GoogleMap.MAP_TYPE_NORMAL);


        // Dessin de la ligne1 du tram NANTES.
        //TODO: voir inside la méthode

        map.addPolyline(gestionLigneTram.dessinLigne1());
        map.addPolyline(gestionLigneTram.dessinLigne2());
        map.addPolyline(gestionLigneTram.dessinLigne3());


    }


    //Classe interne permettant le Listener du menu
    public class DrawerItemClickListener implements ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView parent, View view, int position, long id) {
            selectItem(position);
        }
    }


    /**
     * On viens ici realiser les actions de la touch
     * Le paramètre est la position dans la liste du menu de gauche
     */
    private void selectItem(int position) {

        BoiteDeConfirmation(position);

        //une fois l'evenement selectionne, on ferme le menu
        mDrawerList.setItemChecked(position, true);
        mDrawerLayout.closeDrawer(mDrawerList);

    }



    //Ouvre une boite de dialogue permettant de confirmer la selection
    //Prend en parametre la position dans le menu des Evenements
    private int i = 0;
    private int j = 0;
    public void BoiteDeConfirmation(final int position) {
        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case DialogInterface.BUTTON_POSITIVE:
                        try {
                            //Instanciation de la googleMap
                            GoogleMap map = ((MapFragment) getFragmentManager()
                                    .findFragmentById(R.id.map)).getMap();

                            //On vide la liste d'event
                            EvenementSignalMock eventClic = new EvenementSignalMock();

                            //On test la position dans le menu pour afficher le bon icon
                            //Test d'une arraylist
                            for (IEvenementSignal e : eventClic.getListEvent()
                                    ) {
                                if (e.getNoLigne() == position) {

                                    //Transfert de lat et long dans EvenementSignal pour être récupéré dans httpConnexion
                                    EvenementSignal event = new EvenementSignal();
                                    event.setLongitude(longitude);
                                    event.setLatitude(latitude);
                                    event.setNom(e.getNom());

                                    //Appel de l'Async pour inserer dans la bdd
                                    AddEvent con = new AddEvent(latitude, longitude, e.getNom(), getApplicationContext());
                                    con.execute();

                                    //Redimensionnement du bimap (ImageMArker)
                                    //Size du marker
                                    GestionImage gImg = new GestionImage(getApplicationContext());
                                    Bitmap marker = gImg.TailleMarker(e.getImageMarker());

                                    //Placement du marqueur
                                    map.addMarker(new MarkerOptions()
                                            .icon(BitmapDescriptorFactory.fromBitmap(marker))
                                            .position(new LatLng(latitude, longitude)));

                                }
                            }
                        } catch (Exception e) {
                            Toast.makeText(getApplicationContext(), "Oops!.. une erreur s'est produite!", Toast.LENGTH_LONG);
                        }
                        break;
                    case DialogInterface.BUTTON_NEGATIVE:
                        //No button clicked
                        Log.v("test", "boutton non");
                        break;
                }
            }
        };

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Confirmer la presence d'un " + values[position].toUpperCase() + " ?").setPositiveButton("OUI!", dialogClickListener)
                .setNegativeButton("Nan..", dialogClickListener).show();
    }

    //Methodes liee a l'implementation de la Class Location Listener
    //Localisation google map
    @Override
    public void onLocationChanged(Location location) {
        latitude = location.getLatitude();
        longitude = location.getLongitude();
        altitude = location.getAltitude();
        accuracy = location.getAccuracy();

        //Met a jour la BDD pourla position du Master
        UpdatePositionUser updatePositionUser = new UpdatePositionUser(latitude, longitude, getDeviceId(), getApplicationContext());
        updatePositionUser.execute();

        //Création d'un Master avec position. Envoi du Master au metier GestionAff pour traitement
        LatLng latLng = new LatLng(latitude,longitude);
        UserMaster userMaster = new UserMaster();
        userMaster.setPositionUserMaster(latLng);


        //J'envoi mon Master avec sa location et je valorise la localisation des Others
        GestionAffichageUserOther gestionAffichageUserOther = new GestionAffichageUserOther();
        gestionAffichageUserOther.setUserMaster(userMaster);
        TextView est = (TextView)findViewById(R.id.nbEst);
        TextView ouest = (TextView)findViewById(R.id.nbOuest);
        TextView sud = (TextView)findViewById(R.id.nbSud);
        TextView nord = (TextView)findViewById(R.id.nbNord);
        GetAllUserOther getAllUserOther = new GetAllUserOther(gestionAffichageUserOther, est, ouest, sud, nord);
        getAllUserOther.execute();



    }


    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        String newStatus = "";
        switch (status) {
            case LocationProvider.OUT_OF_SERVICE:
                newStatus = "OUT_OF_SERVICE";
                break;
            case LocationProvider.TEMPORARILY_UNAVAILABLE:
                newStatus = "TEMPORARILY_UNAVAILABLE";
                break;
            case LocationProvider.AVAILABLE:
                newStatus = "AVAILABLE";
                break;
        }
        String msg = String.format(getResources().getString(R.string.provider_new_status), provider, newStatus);
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();


    }


    @Override
    public void onProviderEnabled(String provider) {
        String msg = String.format(getResources().getString(R.string.provider_enabled), provider);
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onProviderDisabled(String provider) {
        String msg = String.format(getResources().getString(R.string.provider_disabled), provider);
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onResume() {
        super.onResume();
        lm = (LocationManager) this.getSystemService(LOCATION_SERVICE);
        if (lm.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 10000, 0, this);
        }
        if (lm.getAllProviders().contains(LocationManager.NETWORK_PROVIDER)) {
            lm.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 10000, 0, this);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        lm.removeUpdates(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        //On vient ici supprimer l'user dans la bdd quand l'app se ferme
        //TODO: retirer lat, long , context.. juste inutile
        DeleteUserMaster deleteUserMaster = new DeleteUserMaster(latitude, longitude, getDeviceId(), getApplicationContext());
        deleteUserMaster.execute();
        Log.e("onStop", "supp en table ok !");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        //On insert la position du user dans la BDD again car delete dans le onPause (voir cycle de vie d'une app)
        //en cherchant son identifiant unique
        String name = getDeviceId();
        PutPositionUser putPositionUser = new PutPositionUser(latitude, longitude,name,getApplicationContext());
        putPositionUser.execute();
        Log.e("onRestart", "remis en table ok !");
    }


    public String getDeviceId(){
        //Connaitre l'ID du telephone. Afin de mettre en table
        final TelephonyManager tm = (TelephonyManager) getBaseContext().getSystemService(Context.TELEPHONY_SERVICE);

        final String tmDevice;
        final String tmSerial;
        final String androidId;

        //On recherche l'ID unique du telephone
        tmDevice = "" + tm.getDeviceId();
        tmSerial = "" + tm.getSimSerialNumber();
        androidId = "" + android.provider.Settings.Secure.getString(getContentResolver(), android.provider.Settings.Secure.ANDROID_ID);

        UUID deviceUuid = new UUID(androidId.hashCode(), ((long)tmDevice.hashCode() << 32) | tmSerial.hashCode());
        String deviceId = deviceUuid.toString();

        return deviceId;
    }



}





