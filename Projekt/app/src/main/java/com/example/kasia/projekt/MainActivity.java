package com.example.kasia.projekt;

import android.app.Activity;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.location.Criteria;
import android.location.GpsSatellite;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.nfc.NfcAdapter;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;


public class MainActivity extends Activity implements LocationListener {

    /**
     * logowanie - zmienne
     * db - uchwyt do bazy danych SQLite session - zarządzanie sesją
     */
    private SQLiteHandler db;
    private SessionManager session;
    private TextView txtName;
    private TextView txtEmail;

    String pid, rescuerId, rescuerUID, name;

    // Progress Dialog
    private ProgressDialog pDialog;

    // JSON parser class
    JSONParser jsonParser = new JSONParser();
    JSONObject product, ratownik;

    // single product url
    private static final String url_rescuer_id = "http://kasia.mszulc.eu/get_rescuer.php";
    private static final String TAG_RESCUER = "ratownik";
    private static final String TAG_RESCUERID= "id";

    // single product url
    private static final String url_incident_details = "http://kasia.mszulc.eu/get_incident.php";

    // JSON Node names

    private static final String TAG_INCIDENT = "zdarzenie";
    private static final String TAG_LONGITUDE = "longitude";
    private static final String TAG_LATITUDE = "name";
    private static final String TAG_ID = "incidentId";
    private static final String TAG_DESCRIPTION = "description";


    // url to create new product
    private static String url_create_incident = "http://kasia.mszulc.eu/create_incident.php";
    // JSON Node names
    private static final String TAG_SUCCESS = "success";

    TextView dlugosc, szerokosc, tekst1, tekst2, idZdarzenieTextV;
    EditText opis;
    Button button2;
    RadioGroup rozmiar;
    RadioButton maly, sredni, duzy;
    String size, description, idZdarzenie;
    Double latitude, longitude;
    LocationManager lm;
    Criteria kr;
    Location loc;
    String dostawca = null;
    Context Context;
    Boolean incidentExist;
    ImageButton createIncident;
    Boolean isLeftHanded = false; // czy wybrano opcję leworęczności

    public void reakcja() {
        Intent i = new Intent(this, TriageTypeActivity.class);
        if(idZdarzenie!="0")
        i.putExtra("incidentId",idZdarzenie); // dane te są potrzebne do stworzenia rekordu w tabeli
        else
        Log.i("idZdarzenia2","gdzie te id");
        i.putExtra("rescuerId",rescuerId);   // patients oraz triage
        i.putExtra("isLeftHanded", isLeftHanded);
        startActivity(i);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        Context = getApplicationContext();

        // odebranie danych od aktywności logowanie - nr id ratownika - zalogowanego
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            rescuerId = extras.getString("id");
            isLeftHanded = extras.getBoolean("isLeftHanded");
        }


        txtName = (TextView) findViewById(R.id.name);
        txtEmail = (TextView) findViewById(R.id.email);

        // SqLite database handler
        db = new SQLiteHandler(getApplicationContext());

        // session manager
        session = new SessionManager(getApplicationContext());

        if (!session.isLoggedIn()) {
            logoutUser();
        }

        // Fetching user details from sqlite
        HashMap<String, String> user = db.getUserDetails();

        name = user.get("name");
        String email = user.get("email");
        rescuerUID = user.get("uid");
        if(rescuerId == null){
            new GetRescuerID().execute();
        }

        // Displaying the user details on the screen
        txtName.setText(name);
        txtEmail.setText(email);
       // Log.i("email", email);

        dlugosc = (TextView) findViewById(R.id.longitude);
        szerokosc = (TextView) findViewById(R.id.latitude);
        opis = (EditText) findViewById(R.id.description);
        idZdarzenieTextV = (TextView) findViewById(R.id.textView6);


        tekst1 = (TextView) findViewById(R.id.tekst1);
        tekst2 = (TextView) findViewById(R.id.tekst2);
        rozmiar = (RadioGroup) findViewById(R.id.incidentSize);
        maly = (RadioButton) findViewById(R.id.small);
        sredni = (RadioButton) findViewById(R.id.middle);
        duzy = (RadioButton) findViewById(R.id.big);
        rozmiar.setOnCheckedChangeListener(
                new RadioGroup.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(RadioGroup group, int checkedId) {
                        if (checkedId == R.id.small) {
                            size = "small";
                        } else if (checkedId == R.id.middle) {
                            size = "middle";
                        } else if (checkedId == R.id.big) {
                            size = "big";
                        }
                    }
                }
        );

        button2 = (Button) findViewById(R.id.button2);
        button2.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(idZdarzenie != null)
                            reakcja();
                        else
                            Toast.makeText(MainActivity.this, R.string.NoIncident, Toast.LENGTH_SHORT).show();
                    }
                }
        );

        kr = new Criteria();
        lm = (LocationManager) getSystemService(LOCATION_SERVICE);
        dostawca = lm.getBestProvider(kr, true);
        loc = lm.getLastKnownLocation(dostawca);
        lm.requestLocationUpdates(dostawca, 1000, 1, this);
        tekst1.setText("najlepszy dostawca " + dostawca);
        tekst2.setText("" + lm.isProviderEnabled(dostawca));

        if (loc != null) {
            dlugosc.setText(""+loc.getLongitude());
            szerokosc.setText(""+loc.getLatitude());
            longitude = loc.getLongitude();
            latitude = loc.getLatitude();
        } else dlugosc.setText("nie ma wartosci!!");

        createIncident = (ImageButton) findViewById(R.id.createIncident);
        // getting product details from intent
        //Intent i = getIntent();

        // getting product id (pid) from intent
        //pid = i.getStringExtra(TAG_ID);

        // Getting complete product details in background thread
        new GetProductDetails().execute();
        idZdarzenieTextV.setText(idZdarzenie);
        /**
         * obsługa guzika createIncident
         * jeśli nie ma takie zdarzenia w bazie i rozmiar zdarzenia jest uzupełniony
         * znana jest długość i szerokość geograficzna
         * to stwórz zdarzenie
         */
        createIncident.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (idZdarzenie==null && size!=null && longitude!=null && latitude!=null){
                    description = opis.getText().toString(); // pobranie opisu wydarzenia
                    new CreateNewProduct().execute();
                    idZdarzenieTextV.setText(idZdarzenie);}
                else {
                    if(size == null)
                        Toast.makeText(MainActivity.this, R.string.no_size, Toast.LENGTH_SHORT).show();
                    else if(longitude == null)
                        Toast.makeText(MainActivity.this, R.string.no_longitude, Toast.LENGTH_SHORT).show();
                    else
                    Toast.makeText(MainActivity.this, R.string.IncidentExist, Toast.LENGTH_SHORT).show();

                }

            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_main, menu);
        return true;
    }

    /**
     * Event Handling for Individual menu item selected
     * Identify single menu item by it's id
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.menu_createNew:
                // Single menu item is selected do something
                // Ex: launching new activity/screen or show alert message
                Toast.makeText(MainActivity.this, "Bookmark is Selected", Toast.LENGTH_SHORT).show();
                return true;

            case R.id.menu_updateOne:
                Toast.makeText(MainActivity.this, "Save is Selected", Toast.LENGTH_SHORT).show();
                return true;

            case R.id.menu_logout:
                Toast.makeText(MainActivity.this, "Logout", Toast.LENGTH_SHORT).show();
                logoutUser();
                return true;


            default:
                return super.onOptionsItemSelected(item);
        }
    }

    /**
     * Disable NFC for this activity
     */
    public void onResume() {
        super.onResume();
        NfcAdapter nfcAdapter = NfcAdapter.getDefaultAdapter(this);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, new Intent(this, getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0);
        nfcAdapter.enableForegroundDispatch(this, pendingIntent, null, null);
    }

    public void onPause() {
        super.onPause();
        NfcAdapter nfcAdapter = NfcAdapter.getDefaultAdapter(this);
        nfcAdapter.disableForegroundDispatch(this);
    }

    public void onNewIntent(Intent intent) {
        if (NfcAdapter.ACTION_TAG_DISCOVERED.equals(intent.getAction())) {
            // drop NFC events
        }
    }

    @Override
    public void onLocationChanged(Location location) {

        Log.i("lokalizacja", "jestem gps");
        dostawca = lm.getBestProvider(kr, true);
        loc = lm.getLastKnownLocation(dostawca);

        tekst1.setText("najlepszy dostawca " + dostawca);
        tekst2.setText("" + lm.isProviderEnabled(dostawca));

        if (loc != null) {
            dlugosc.setText(String.valueOf(loc.getLongitude()));
            szerokosc.setText(String.valueOf(loc.getLatitude()));
            longitude = loc.getLongitude();
            latitude = loc.getLatitude();
        } else dlugosc.setText("wciaz nie ma");
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }
   /* @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu.xml; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }*/

    /**
     * Background Async Task to Create new product
     */
    class CreateNewProduct extends AsyncTask<String, String, String> {

        /**
         * Before starting background thread Show Progress Dialog
         */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(MainActivity.this);
            pDialog.setMessage("Creating Incident..");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }

        /**
         * Creating product
         */
        protected String doInBackground(String... args) {

            Log.i("opis",description);
            // Building Parameters
            Log.i("longitudeDB",String.format(Locale.UK,"%.4f",longitude));
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("longitude", String.format(Locale.UK,"%.4f",longitude)));
            params.add(new BasicNameValuePair("latitude", String.format(Locale.UK,"%.4f",latitude)));
            params.add(new BasicNameValuePair("size", size));
            params.add(new BasicNameValuePair("description", description));

            // getting JSON Object
            // Note that create product url accepts POST method
            JSONObject json = jsonParser.makeHttpRequest(url_create_incident,
                    "POST", params);

            // check log cat fro response
            Log.d("Create Response", json.toString());

            // check for success tag
            try {
                int success = json.getInt(TAG_SUCCESS);

                if (success == 1) {
                    // successfully created product
                    Log.i("wynik", "dodano");

                    // closing this screen

                } else {
                    Log.i("wynik", "nie dodano");
                    // failed to create product
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return null;
        }

        /**
         * After completing background task Dismiss the progress dialog
         * *
         */
        protected void onPostExecute(String file_url) {
            // dismiss the dialog once done
            pDialog.dismiss();
            // po każdym dodaniu zdarzenia select po jego id
            new GetProductDetails().execute();
        }

    }

    /**
     * Background Async Task to Get complete product details
     */
    class GetProductDetails extends AsyncTask<String, String, String> {

        /**
         * Before starting background thread Show Progress Dialog
         */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(MainActivity.this);
            pDialog.setMessage("Loading product details. Please wait...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }

        /**
         * Getting product details in background thread
         */
        protected String doInBackground(String... params) {

            // updating UI from Background Thread
            runOnUiThread(new Runnable() {
                public void run() {
                    // Check for success tag
                    int success;
                    try {
                        // Building Parameters
                        Log.i("longitude",String.format(Locale.UK,"%.4f", longitude));
                        List<NameValuePair> params = new ArrayList<NameValuePair>();
                        params.add(new BasicNameValuePair("longitude", String.format(Locale.UK,"%.4f", longitude)));
                        params.add(new BasicNameValuePair("latitude", String.format(Locale.UK,"%.4f", latitude)));

                        // getting product details by making HTTP request
                        // Note that product details url will use GET request
                        JSONObject json = jsonParser.makeHttpRequest(
                                url_incident_details, "POST", params);

                        // check your log for json response
                        Log.d("Single Product Details", json.toString());

                        // json success tag
                        success = json.getInt(TAG_SUCCESS);
                        if (success == 1) {
                            // successfully received product details
                            JSONArray productObj = json
                                    .getJSONArray(TAG_INCIDENT); // JSON Array

                            // get first product object from JSON Array
                            product = productObj.getJSONObject(0);

                            // product with this pid found
                            // Edit Text
                               //  idZdarzenieTextV.setText(product.getString(TAG_ID));
                            idZdarzenie=product.getString(TAG_ID);

                                Log.i("idZdarznie",idZdarzenie);


                        } else {
                            // product with pid not foundF
                            Log.i("idZdarznie","nie ma zdarzenia takiego");
                            //incidentExist=false;
                            //new CreateNewProduct().execute();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });

            return null;
        }
        /**
     * After completing background task Dismiss the progress dialog
     * *
     */
    protected void onPostExecute(String file_url) {
        // dismiss the dialog once done
        pDialog.dismiss();
        if(idZdarzenie==null){
            Toast.makeText(MainActivity.this, "To zdarzenie nie jest zarejestrowane", Toast.LENGTH_SHORT).show();
        }
        else{
            Toast.makeText(MainActivity.this,"idZdarzenia = "+idZdarzenie,Toast.LENGTH_SHORT ).show();
        }
    }
    }

    /**
     * Background Async Task to Get complete product details
     */
    class GetRescuerID extends AsyncTask<String, String, String> {

        /**
         * Before starting background thread Show Progress Dialog
         */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
           /* pDialog = new ProgressDialog(MainActivity.this);
            pDialog.setMessage("Pobieranie id użytkownika");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();*/
        }

        /**
         * Getting product details in background thread
         */
        protected String doInBackground(String... params) {

            // updating UI from Background Thread
            runOnUiThread(new Runnable() {
                public void run() {
                    // Check for success tag
                    int success;
                    try {
                        // Building Parameters
                        List<NameValuePair> params = new ArrayList<NameValuePair>();
                        params.add(new BasicNameValuePair("unique_id", rescuerUID));

                        // getting product details by making HTTP request
                        // Note that product details url will use GET request
                        JSONObject json = jsonParser.makeHttpRequest(
                                url_rescuer_id, "POST", params);

                        // check your log for json response
                        Log.d("Single Product Details", json.toString());

                        // json success tag
                        success = json.getInt(TAG_SUCCESS);
                        if (success == 1) {
                            // successfully received product details
                            JSONArray productObj = json
                                    .getJSONArray(TAG_RESCUER); // JSON Array

                            // get first product object from JSON Array
                            ratownik = productObj.getJSONObject(0);

                            // product with this pid found
                            // Edit Text
                            //  idZdarzenieTextV.setText(product.getString(TAG_ID));
                            rescuerId=ratownik.getString(TAG_RESCUERID);

                            Log.i("idZdarznie",rescuerId);


                        } else {
                            // product with pid not foundF
                            Log.i("idZdarznie","nie ma zdarzenia takiego");
                            //incidentExist=false;
                            //new CreateNewProduct().execute();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });

            return null;
        }
        /**
         * After completing background task Dismiss the progress dialog
         * *
         */
        protected void onPostExecute(String file_url) {
            // dismiss the dialog once done
            //pDialog.dismiss();
            Toast.makeText(MainActivity.this,"Witaj"+name,  Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Logging out the user. Will set isLoggedIn flag to false in shared
     * preferences Clears the user data from sqlite users table
     * */
    private void logoutUser() {
        session.setLogin(false);

        db.deleteUsers();

        // Launching the login activity
        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }
}


