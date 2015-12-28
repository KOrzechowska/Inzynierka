package com.example.kasia.projekt;

import android.app.ActionBar;
import android.app.Activity;
import android.app.FragmentTransaction;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.nfc.NfcAdapter;
import android.nfc.tech.IsoDep;
import android.nfc.tech.MifareClassic;
import android.nfc.tech.MifareUltralight;
import android.nfc.tech.Ndef;
import android.nfc.tech.NfcA;
import android.nfc.tech.NfcB;
import android.nfc.tech.NfcF;
import android.nfc.tech.NfcV;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;


/**
 * Created by kasia on 30.08.15.
 */
public class NewActivity extends FragmentActivity implements ActionBar.TabListener, TopRatedFragment.OnFragmentInteractionListener,GamesFragment.ActivityCommunicator {

    String nfcID = UUID.randomUUID().toString(); // TODO: NFC tag UID

    Context fragment_context;
    // Progress Dialog
    private ProgressDialog pDialog;

    JSONParser jsonParser = new JSONParser();
    JSONObject patientDetails;
    JSONObject triageDetail; // triage ID

    // url to create new triage record
    private static String url_add_triage = "http://kasia.mszulc.eu/add_triage.php";
    // url to get triage record details
    private static String url_triage_details = "http://kasia.mszulc.eu/get_triage.php";
    // url to create new patient
    private static String url_create_product = "http://kasia.mszulc.eu/create_patient.php";
    private int AddPatientSuccess;
    // url to get patient details
    private static String url_patient_details = "http://kasia.mszulc.eu/get_patient.php";
    private String idPatient; // patient id from database
    private static final String TAG_TRIAGE = "triage";
    private static final String TAG_TRIAGE_ID = "triageID";
    private  static final String TAG_TRIAGE_RESCUER_ID = "rescuerID";
    private static final String TAG_TRIAGE_PATH_ID = "triage_pathID";
    String triageID;
    // JSON Node names
    private static  final String TAG_ID = "pid";
    private  static final String TAG_PATIENT = "patient";
    // JSON Node names
    private static final String TAG_SUCCESS = "success";
   // public FragmentCommunicator fragmentCommunicator;
    private ViewPager viewPager;
    private TabsPagerAdapter mAdapter;
    private ActionBar actionBar;
    private String sex ; // M- mężczyzna K- kobieta
    private String firstName, lastName;
    private boolean isAdultTriage; // czy poszkodowany jest dorosły czy jest dzieckiem
    private String triagePath;
    private String patientPhoto = null;
    private String incidentId, rescuerId;
    // Tab titles
    private String[] tabs = {"Dane osobowe", "pytanie TRIAGE", "Dodatkowe informacje"};





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_activity);
        Log.i("new activity", "start");

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            isAdultTriage = extras.getBoolean("isAdult");
            incidentId = extras.getString("incidentId");
            rescuerId = extras.getString("rescuerId");
            Log.i("rescuerID",rescuerId);
            Log.i("incidentId",incidentId);
        }
        //Bundle bundle = new Bundle();
        //bundle.putString("isAdult",String.valueOf(isPatientMen));
        // Initilization
        viewPager = (ViewPager) findViewById(R.id.pager);
        actionBar = getActionBar();
        mAdapter = new TabsPagerAdapter(getSupportFragmentManager(),isAdultTriage);
        //mAdapter.setArgument(bundle);

        viewPager.setAdapter(mAdapter);
        actionBar.setHomeButtonEnabled(false);
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

        // Adding Tabs
        for (String tab_name : tabs) {
            actionBar.addTab(actionBar.newTab().setText(tab_name)
                    .setTabListener(this));
        }

        /**
         * on swiping the viewpager make respective tab selected
         * */
        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageSelected(int position) {
                // on changing the page
                // make respected tab selected
                actionBar.setSelectedNavigationItem(position);
               // if(fragmentCommunicator != null)
                 //   fragmentCommunicator.passDataToFragment(firstName, lastName, sex, isAdultTriage);
            }

            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {
            }

            @Override
            public void onPageScrollStateChanged(int arg0) {
            }
        });

        
    }
    @Override
    public void onResume() {
        super.onResume();
      /*  // creating pending intent:
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, new Intent(this, getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0);
        // creating intent receiver for NFC events:
        IntentFilter filter = new IntentFilter();
        filter.addAction(NfcAdapter.ACTION_TAG_DISCOVERED);
        filter.addAction(NfcAdapter.ACTION_NDEF_DISCOVERED);
        filter.addAction(NfcAdapter.ACTION_TECH_DISCOVERED);
        // enabling foreground dispatch for getting intent from NFC event:
        NfcAdapter nfcAdapter = NfcAdapter.getDefaultAdapter(this);
        nfcAdapter.enableForegroundDispatch(this, pendingIntent, new IntentFilter[]{filter}, this.techList);*/
    }

    @Override
    public void onPause() {
        super.onPause();
        // disabling foreground dispatch:
       /* NfcAdapter nfcAdapter = NfcAdapter.getDefaultAdapter(this);
        nfcAdapter.disableForegroundDispatch(this);*/
    }

    /*@Override
    protected void onNewIntent(Intent intent) {
        if (intent.getAction().equals(NfcAdapter.ACTION_TAG_DISCOVERED)) {
            ((TextView)findViewById(R.id.text)).setText(
                    "NFC Tag\n" +
                            ByteArrayToHexString(intent.getByteArrayExtra(NfcAdapter.EXTRA_ID)));
        }
    }

    private String ByteArrayToHexString(byte [] inarray) {
        int i, j, in;
        String [] hex = {"0","1","2","3","4","5","6","7","8","9","A","B","C","D","E","F"};
        String out= "";

        for(j = 0 ; j < inarray.length ; ++j)
        {
            in = (int) inarray[j] & 0xff;
            i = (in >> 4) & 0x0f;
            out += hex[i];
            i = in & 0x0f;
            out += hex[i];
        }
        return out;
    }*/
    public void OnFragmentInteraction(String imie, String nazwisko, String plec, String bitmap) {
        TopRatedFragment textFragment =
                (TopRatedFragment)
                        getSupportFragmentManager().findFragmentById(R.id.fragment_top_rated);
        Log.i("tekst1", imie);
        Log.i("tekst2",nazwisko);
        Log.i("tekst3", plec);
        Log.i("tekst4",String.valueOf(bitmap.length()));
        firstName = imie;
        lastName = nazwisko;
        sex=plec; // kobieta lub mężczyzna
        patientPhoto = bitmap;

    }

    @Override
    public void onTabReselected(ActionBar.Tab tab, FragmentTransaction ft) {
    }

    @Override
    public void onTabSelected(ActionBar.Tab tab, FragmentTransaction ft) {
        // on tab selected
        // show respected fragment view
        viewPager.setCurrentItem(tab.getPosition());
    }

    @Override
    public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction ft) {
    }

   /* public interface FragmentCommunicator{
        public void passDataToFragment(String imie, String nazwisko,Boolean plec,Boolean someValue);
    }*/

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_main, menu);
        return true;
    }

    /**
     * Event Handling for Individual menu item selected
     * Identify single menu item by it's id
     * */
    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {

        switch (item.getItemId())
        {
            case R.id.menu_createNew:
                // Single menu item is selected do something
                // launching mainactivity screen - wybór dziecko czy dorosły
                Toast.makeText(NewActivity.this, "Create new patient", Toast.LENGTH_SHORT).show();
                createNextPatient();
                return true;

            case R.id.menu_updateOne:
                Toast.makeText(NewActivity.this, "Update triage status", Toast.LENGTH_SHORT).show();
                /*Intent intent = new Intent(this, NewActivity.class); // raz jeszcze ładuję new activity - powinno być czyste
                intent.putExtra("incidentId", incidentId);
                intent.putExtra("isAdult", isAdultTriage);
                intent.putExtra("rescuerId", rescuerId);
                startActivity(intent);*/
                TextView textFragment1 = (TextView)findViewById(R.id.input_name);
                getPatient();
                textFragment1.setText("Jarosław");
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void passDataToActivity(String someValue, Context fragmentContext) {
        triagePath = someValue;
        Log.i("tekst4",triagePath+firstName+lastName+sex);
        fragment_context = fragmentContext;
        //creating new product in background thread
        if (patientPhoto!=null){
            new CreateNewPatient().execute();
            Log.i("nfcID1:", nfcID);
            new GetPatientDetails().execute();
            Toast.makeText(fragment_context,idPatient,Toast.LENGTH_SHORT).show(); // prezentacja ID pacjenta
        }
        else Log.i("zdjecie","gdzie ono");

    }

    /**
     * Background Async Task to Create new product
     */
    class CreateNewPatient extends AsyncTask<String, String, String> {

        /**
         * Before starting background thread Show Progress Dialog
         */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(fragment_context);
            pDialog.setMessage("Creating Product..");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }
    /**
     * Creating product
     */
    @SuppressWarnings("deprecation")
    protected String doInBackground(String... args) {

        String age;
        if(isAdultTriage==true)
            age = "adult";
        else
            age = "child";
        Log.i("incidentID",String.valueOf(incidentId));
        Log.i("age", age);

        // Building Parameters
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("nfcID", nfcID));
        params.add(new BasicNameValuePair("firstName", firstName));
        params.add(new BasicNameValuePair("lastName", lastName));
        params.add(new BasicNameValuePair("sex", sex));
        params.add(new BasicNameValuePair("age", age));
        params.add(new BasicNameValuePair("patientPhoto", patientPhoto));
        params.add(new BasicNameValuePair("incidentId", incidentId));

        // getting JSON Object
        // Note that create product url accepts POST method
        JSONObject json = jsonParser.makeHttpRequest(url_create_product,
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
       // AddPatientSuccess=1;
        // dismiss the dialog once done
        pDialog.dismiss();
    }

}

    /**
     * Background Async Task to Get patient details like ID
     */
    class GetPatientDetails extends AsyncTask<String, String, String> {

        /**
         * Before starting background thread Show Progress Dialog
         */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
           /* pDialog = new ProgressDialog(fragment_context);
            pDialog.setMessage("Loading product details. Please wait...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();*/
        }

        /**
         * Getting patient details in background thread
         */
        protected String doInBackground(String... params) {

            // updating UI from Background Thread
            runOnUiThread(new Runnable() {
                public void run() {
                    // Check for success tag
                    int success;
                    try {
                        // Building Parameters
                        Log.i("nfcID", nfcID);
                        List<NameValuePair> params = new ArrayList<NameValuePair>();
                        params.add(new BasicNameValuePair("nfcID", nfcID));

                        // getting product details by making HTTP request
                        // Note that product details url will use GET request
                        JSONObject json = jsonParser.makeHttpRequest(
                                url_patient_details, "POST", params);

                        // check your log for json response
                        Log.d("Single Product Details", json.toString());

                        // json success tag
                        success = json.getInt(TAG_SUCCESS);
                        if (success == 1) {
                            // successfully received product details
                            JSONArray productObj = json
                                    .getJSONArray(TAG_PATIENT); // JSON Array

                            // get first product object from JSON Array
                            patientDetails = productObj.getJSONObject(0);

                            // product with this pid found
                            // Edit Text
                            //  idZdarzenieTextV.setText(product.getString(TAG_ID));
                            idPatient=patientDetails.getString(TAG_ID);

                            Log.i("idPatient",idPatient);


                        } else {
                            // product with pid not foundF
                            Log.i("idPatient","nie ma zdarzenia takiego");
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
           // pDialog.dismiss();
            Log.i("patientID from GET", idPatient);
            new AddNewTriageRecord().execute();
        }
    }

    /**
     * Background Async Task to Create new product
     */
    class AddNewTriageRecord extends AsyncTask<String, String, String> {

        /**
         * Before starting background thread Show Progress Dialog
         */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(fragment_context);
            pDialog.setMessage("Creating Triage Record..");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }
        /**
         * Creating product
         */
        @SuppressWarnings("deprecation")
        protected String doInBackground(String... args) {

            Log.i("triagePathT",triagePath);
            Log.i("pidT",idPatient);
            Log.i("rescuerIDT",rescuerId);
            // Building Parameters
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("pid", idPatient));
            params.add(new BasicNameValuePair("rescuerID", rescuerId));
            params.add(new BasicNameValuePair("triage_pathID", triagePath));

            // getting JSON Object
            // Note that create product url accepts POST method
            JSONObject json = jsonParser.makeHttpRequest(url_add_triage,
                    "POST", params);

            // check log cat fro response
            Log.d("Create Triage Response", json.toString());

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
            // AddPatientSuccess=1;
            // dismiss the dialog once done
            pDialog.dismiss();
            new GetLastTriageDetails().execute();
        }

    }

    /**
     * Background Async Task to Get patient details like ID
     */
    class GetLastTriageDetails extends AsyncTask<String, String, String> {

        /**
         * Before starting background thread Show Progress Dialog
         */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(fragment_context);
            pDialog.setMessage("Loading product details. Please wait...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }

        /**
         * Getting last triage details in background thread
         */
        protected String doInBackground(String... params) {

            // updating UI from Background Thread
            runOnUiThread(new Runnable() {
                public void run() {
                    // Check for success tag
                    int success;
                    try {
                        // Building Parameters
                        Log.i("nfcID", nfcID);
                        List<NameValuePair> params = new ArrayList<NameValuePair>();
                        params.add(new BasicNameValuePair("pid", idPatient));

                        // getting product details by making HTTP request
                        // Note that product details url will use GET request
                        JSONObject json = jsonParser.makeHttpRequest(
                                url_triage_details, "POST", params);

                        // check your log for json response
                        Log.d("Single Triage Details", json.toString());

                        // json success tag
                        success = json.getInt(TAG_SUCCESS);
                        if (success == 1) {
                            // successfully received product details
                            JSONArray productObj = json
                                    .getJSONArray(TAG_TRIAGE); // JSON Array

                            // get first product object from JSON Array
                            triageDetail = productObj.getJSONObject(0);

                            // product with this pid found
                            // Edit Text
                            //  idZdarzenieTextV.setText(product.getString(TAG_ID));
                            triageID=triageDetail.getString(TAG_TRIAGE_ID);

                            Log.i("triageID",triageID);


                        } else {
                            // product with pid not foundF
                            Log.i("triageID","nie ma zdarzenia takiego");
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

        }
    }


    /**
     * Logging out the user. Will set isLoggedIn flag to false in shared
     * preferences Clears the user data from sqlite users table
     * */
    private void createNextPatient() {

        // Launching the login activity
       // Intent intent = new Intent(NewActivity.this, TriageTypeActivity.class);
        //startActivity(intent); // można i tak ale trzeba by mu przesłać raz jeszcze parametry id
        finish(); // działa jak przycisk wstecz
    }

    private void getPatient(){

    }

}
