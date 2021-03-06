package com.example.kasia.projekt;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.FragmentTransaction;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
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
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
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
public class NewActivity extends AppCompatActivity implements TopRatedFragment.OnFragmentInteractionListener,TriageQuestionFragment.ActivityCommunicator, DetailsFragment.ActivityCommunicator {


    private SessionManager session; // session - przechowywanie na zew informacji o użytkowniku
    private SQLiteHandler db; // android baza SQLite

    private Boolean hasNFCmodul; // czy smartphone wspiera technologię NFC
    Button nfcButton; EditText nfcText;
    String nfcID = null;

    Context fragment_context, detailsFragment_context;
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
    // url to update triage record
    private static String url_update_triage = "http://kasia.mszulc.eu/update_triage.php";

    private static final String TAG_TRIAGE = "triage";
    private static final String TAG_TRIAGE_ID = "triageID";
    private static final String TAG_TRIAGE_PHOTO = "details";
    private static final String TAG_TRIAGE_COMMENT = "comment";
    private  static final String TAG_TRIAGE_RESCUER_ID = "rescuerID";
    private static final String TAG_TRIAGE_PATH_ID = "triage_pathID";
    String triageID;
    // JSON Node names
    private static  final String TAG_ID = "pid";
    private  static final String TAG_PATIENT = "patient";
    private static final String TAG_FIRSTNAME = "firstName";
    private static final String TAG_LASTNAME = "lastName";
    private static final String TAG_SEX = "sex";
    private static final String TAG_PHOTO = "patientPhoto";

    private Boolean EditFlag = false, CreateFlag, EditTriageStatus = false, triageCompleted = false;
    private String idPatient, namePatient, lastNamePatient, sexPatient, photoPatient; // patient id from database

    private Boolean dodanoPacjenta = false;
    // JSON Node names
    private static final String TAG_SUCCESS = "success";
   // public FragmentCommunicator fragmentCommunicator;
    private ViewPager viewPager;
    private TabsPagerAdapter mAdapter;
    private ActionBar actionBar;
    private String sex ; // M- mężczyzna K- kobieta
    private String firstName, lastName;
    private boolean isAdultTriage; // czy poszkodowany jest dorosły czy jest dzieckiem
    private String triagePath, detailsPhoto, detailsComment;
    private String patientPhoto = null;
    private String incidentId, rescuerId;
    // Tab titles
    private String[] tabs = {"Dane osobowe", "pytanie TRIAGE", "Dodatkowe informacje"};

    // list of NFC technologies detected:
    private final String[][] techList = new String[][] {
            new String[] {
                    NfcA.class.getName(),
                    NfcB.class.getName(),
                    NfcF.class.getName(),
                    NfcV.class.getName(),
                    IsoDep.class.getName(),
                    MifareClassic.class.getName(),
                    MifareUltralight.class.getName(), Ndef.class.getName()
            }
    };

    /**
     * funkcja wywoływana przy tworzenie tej aktywności
     * session, db - w celu obsługi wylogowania (wylogowanie z sesji i czyszczenie db)
     * extras - zawiera parametry odebrane z poprzednich aktywności dla fragmentów NewActivity
     *          i dla insertów do bazy
     * @param savedInstanceState
     */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_triage);

        Log.i("new activity", "start");
        Boolean isLeftHanded; // wykorzystywane tylko do przesłania dalej do fragmentu z pytaniami
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            isAdultTriage = extras.getBoolean("isAdult");
            isLeftHanded = extras.getBoolean("isLeft");
            incidentId = extras.getString("incidentId");
            rescuerId = extras.getString("rescuerId");
            CreateFlag = extras.getBoolean("createFlag");
            Log.i("rescuerID",rescuerId);
            Log.i("incidentId",incidentId);
        }
        else isLeftHanded = false; // domyślnie jest praworęczny uzytkownik
        // session manager
        session = new SessionManager(getApplicationContext());
        // SqLite database handler
        db = new SQLiteHandler(getApplicationContext());


        ///////////////////////////////////////////
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tab_layout);
        tabLayout.addTab(tabLayout.newTab().setText(R.string.danePersonalne));
        tabLayout.addTab(tabLayout.newTab().setText(R.string.triageFormularz));
        tabLayout.addTab(tabLayout.newTab().setText(R.string.szczegolyMed));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        final ViewPager viewPager = (ViewPager) findViewById(R.id.pager);
        final TabsPagerAdapter adapter = new TabsPagerAdapter
                (getSupportFragmentManager(),isAdultTriage, isLeftHanded);

        viewPager.setAdapter(adapter);

        viewPager.setOffscreenPageLimit(2);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }
            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }
            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });
        //////////////////////////////////////////////

        //Bundle bundle = new Bundle();
        //bundle.putString("isAdult",String.valueOf(isPatientMen));
        // Initilization
       /* viewPager = (ViewPager) findViewById(R.id.pager);
        viewPager.setOffscreenPageLimit(2);
        actionBar = getActionBar();
        mAdapter = new TabsPagerAdapter(getSupportFragmentManager(),isAdultTriage, isLeftHanded);
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
        /*viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

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
        });*/

        hasNFCmodul = getPackageManager().hasSystemFeature(PackageManager.FEATURE_NFC);

    }
    @Override
    public void onResume() {
        super.onResume();
        if(hasNFCmodul) {
            // creating pending intent:
            PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, new
                    Intent(this, getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0);
            // creating intent receiver for NFC events:
            IntentFilter filter = new IntentFilter();
            filter.addAction(NfcAdapter.ACTION_TAG_DISCOVERED);
            filter.addAction(NfcAdapter.ACTION_NDEF_DISCOVERED);
            filter.addAction(NfcAdapter.ACTION_TECH_DISCOVERED);
            // enabling foreground dispatch for getting intent from NFC event:
            NfcAdapter nfcAdapter = NfcAdapter.getDefaultAdapter(this);


            nfcAdapter.enableForegroundDispatch(this, pendingIntent,null, null);


        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if(hasNFCmodul) {
            // disabling foreground dispatch:
            NfcAdapter nfcAdapter = NfcAdapter.getDefaultAdapter(this);
            nfcAdapter.disableForegroundDispatch(this);
        }


    }

    @Override
    protected void onNewIntent(Intent intent) {
        if(hasNFCmodul) {
            if (patientPhoto != null & CreateFlag &
                    intent.getAction().equals(NfcAdapter.ACTION_TAG_DISCOVERED)) {
                nfcID = ByteArrayToHexString(intent.getByteArrayExtra(NfcAdapter.EXTRA_ID));
                ((TextView) findViewById(R.id.textView2)).setText(
                        "NFC Tag\n" + nfcID);
                // rejestracja nowego pacjenta
                new CreateNewPatient().execute();
                Log.i("nfcID1:", nfcID);
            }
            if (EditFlag & intent.getAction().equals(NfcAdapter.ACTION_TAG_DISCOVERED)) {

                nfcID = ByteArrayToHexString(intent.getByteArrayExtra(NfcAdapter.EXTRA_ID));
                ((TextView) findViewById(R.id.textView2)).setText(
                        "NFC Tag\n" + nfcID);
                // pobranie parametrów pacjenta o danym NFCID
                new GetPatientDetails().execute();
            }
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
    }
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

   /* @Override
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
    }*/

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
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(NewActivity.this);

        switch (item.getItemId()) {
            case R.id.menu_createNew:
                // Single menu item is selected do something
                // launching mainactivity screen - wybór dziecko czy dorosły
                Toast.makeText(NewActivity.this, "Create new patient", Toast.LENGTH_SHORT).show();

                alertDialog.setTitle(R.string.for_create);
                alertDialog.setMessage(R.string.create_message);
                alertDialog.setIcon(R.mipmap.ic_add_patient);
                alertDialog.setPositiveButton(R.string.add, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        EditFlag = false; // gdy dodajemy pacjenta to nie jest to edytowanie
                        createNextPatient();
                    }
                });
                alertDialog.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener(){
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                alertDialog.show();
                return true;

            case R.id.menu_updateOne:
                Toast.makeText(NewActivity.this, "Update triage status", Toast.LENGTH_SHORT).show();
                /*Intent intent = new Intent(this, NewActivity.class); // raz jeszcze ładuję new activity - powinno być czyste
                intent.putExtra("incidentId", incidentId);
                intent.putExtra("isAdult", isAdultTriage);
                intent.putExtra("rescuerId", rescuerId);
                startActivity(intent);*/
                alertDialog.setTitle(R.string.for_edit);
                alertDialog.setMessage(R.string.edit_message);
                alertDialog.setIcon(R.mipmap.ic_edit_patient);
                alertDialog.setPositiveButton(R.string.edit, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        CreateFlag = false; // gdy edytujemy pacjenta to nie dodajemy pacjenta
                        getPatient();
                    }
                });
                alertDialog.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener(){
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                alertDialog.show();
                return true;
            case R.id.menu_logout:
                Toast.makeText(NewActivity.this, "Logout", Toast.LENGTH_SHORT).show();
                logoutUser();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    /**
     *
     * @param someValue
     * @param priority
     * @param fragmentContext
     */
    @Override
    public void passDataToActivity(String someValue, String priority, Context fragmentContext) {
        triagePath = someValue;
        Log.i("tekst4",triagePath+firstName+lastName+sex);
        fragment_context = fragmentContext;
        // jest wynik i w zalezności od obecności moduł NFC
        Log.i("nfcmodul",hasNFCmodul.toString());

       /* if( hasNFCmodul == false & EditFlag == true){
            Dialog dialog = createInputDialog();
            dialog.show();
       }*/

        //creating new patient in background thread
        // conditions: photo, createFlag == true, and TODO: NFC tag ID
        if( hasNFCmodul == false)
        if (patientPhoto!=null & CreateFlag == true){
            Dialog dialog = createInputDialog();
            dialog.show();
            Toast.makeText(NewActivity.this,"Priorytet poszkodowanego: "+priority + "\n"+R.string.nfcTag,Toast.LENGTH_LONG).show();
            // new CreateNewPatient().execute();
            //Log.i("nfcID1:", nfcID);
            //new GetPatientDetails().execute();
        }
        else Log.i("zdjecie","gdzie ono");
        if(EditTriageStatus == true){
            Toast.makeText(fragment_context,"stwórz rekord",Toast.LENGTH_SHORT).show();
            new AddNewTriageRecord().execute();
        }

    }

    /**
     * Dane przesłane z fragmentu details fragment
     * @param someValue - zdjęcie szczegółów
     * @param comment - komentarz ratownika
     * @param fragmentContext - context fragmentu
     */
    @Override
    public void passDataToNewActivity(String someValue,String comment,  Context fragmentContext) {
        detailsFragment_context = fragmentContext;
        detailsPhoto = someValue;
        detailsComment = comment;
        Log.i("detailsPhoto", detailsPhoto);
        if(triageCompleted)
            new SavePatientDetails().execute();
        else // nie ma jeszcze triage do tych szczegółów
            Toast.makeText(detailsFragment_context,"Nie nadano jeszcze priorytetu TRIAGE", Toast.LENGTH_SHORT);

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
           /* pDialog = new ProgressDialog(fragment_context);
            pDialog.setMessage("Creating Product..");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();*/
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
                dodanoPacjenta = true;

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
        //pDialog.dismiss();
        if(dodanoPacjenta)
        new GetPatientDetails().execute();
    }

}

    /**
     * Background Async Task to Get patient details like ID
     */
    class GetPatientDetails extends AsyncTask<String, String, String> {

        int pobrano = 0;
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
        @SuppressWarnings("deprecation")
        protected String doInBackground(String... args) {


            // updating UI from Background Thread
            //runOnUiThread(new Runnable() {
              //public void run() {
                    // Check for success tag
                    int success;
                    try {
                        // Building Parameters
                        List<NameValuePair> params = new ArrayList<NameValuePair>();
                        params.add(new BasicNameValuePair("nfcID", nfcID));
                        params.add(new BasicNameValuePair("incidentId", incidentId));

                        // getting product details by making HTTP request
                        JSONObject json = jsonParser.makeHttpRequest(
                                url_patient_details, "POST", params);
                        // check your log for json response
                        Log.d("Single Product Details", json.toString());
                        // json success tag
                        success = json.getInt(TAG_SUCCESS);
                        if (success == 1) {
                            // successfully received patient details
                            JSONArray productObj = json
                                    .getJSONArray(TAG_PATIENT); // JSON Array

                            // get first patient object from JSON Array
                            patientDetails = productObj.getJSONObject(0);
                            // patient details extracted from DB
                            idPatient = patientDetails.getString(TAG_ID);
                            namePatient = patientDetails.getString(TAG_FIRSTNAME);
                            lastNamePatient = patientDetails.getString(TAG_LASTNAME);
                            sexPatient = patientDetails.getString(TAG_SEX);
                            photoPatient = patientDetails.getString(TAG_PHOTO);
                            pobrano = 1;
                        } else {
                            // patient found
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

               // }
            //});

            return null;
        }
        /**
         * After completing background task Dismiss the progress dialog
         * *
         */
        protected void onPostExecute(String file_url) {
            nfcID = null;
            // dismiss the dialog once done

           // pDialog.dismiss();
            if(pobrano==1 & EditFlag){
                Toast.makeText(getApplication(), R.string.pacjentZnaleziony, Toast.LENGTH_SHORT).show();
            Log.i("patientID from GET", idPatient);}
            else
                Toast.makeText(getApplication(), R.string.pacjentNieZnaleziony, Toast.LENGTH_SHORT).show();
            Log.i("create flag", CreateFlag.toString());
            if(EditFlag & pobrano == 1){ // jeśli nie poprano to nie ma co wypisać
                TextView firstName_textFragment1 = (TextView)findViewById(R.id.input_name);
                TextView lastName_textFragment1 = (TextView)findViewById(R.id.input_lastname);
                firstName_textFragment1.setText(namePatient);
                lastName_textFragment1.setText(lastNamePatient);
                RadioButton women_fragment1 = (RadioButton)findViewById(R.id.women_rbn);
                RadioButton men_fragment1 = (RadioButton)findViewById(R.id.men_rbn);
                if(sexPatient == "M")
                    men_fragment1.setChecked(true);
                else if(sexPatient == "F")
                    women_fragment1.setChecked(true);
                ImageView i_fragment1 = (ImageView)findViewById(R.id.imageView);
                Bitmap bitmapa = base64ToBitmap(photoPatient);
                i_fragment1.setImageBitmap(bitmapa);
                RadioGroup rgQ1_Fragment2 = (RadioGroup)findViewById(R.id.rgCzyChodzi);
                RadioGroup rgQ2_Fragment2 = (RadioGroup)findViewById(R.id.rgCzyOddycha);
                RadioGroup rgQ3_Fragment2 = (RadioGroup)findViewById(R.id.rgQuestion3);
                RadioGroup rgQ4_Fragment2 = (RadioGroup)findViewById(R.id.rgQuestion4);
                RadioGroup rgQ5_Fragment2 = (RadioGroup)findViewById(R.id.rgQuestion5);
                rgQ1_Fragment2.clearCheck();
                rgQ2_Fragment2.clearCheck(); rgQ2_Fragment2.setVisibility(View.INVISIBLE);
                rgQ3_Fragment2.clearCheck(); rgQ3_Fragment2.setVisibility(View.INVISIBLE);
                rgQ4_Fragment2.clearCheck(); rgQ4_Fragment2.setVisibility(View.INVISIBLE);
                rgQ5_Fragment2.clearCheck(); rgQ5_Fragment2.setVisibility(View.INVISIBLE);
                TextView wynikTriage_fragment2 = (TextView) findViewById(R.id.textView);
                wynikTriage_fragment2.setText(""); wynikTriage_fragment2.setBackgroundColor(Color.WHITE);
                ImageView  imageView = (ImageView)findViewById(R.id.image);
                imageView.setImageResource(R.mipmap.ic_checkboxmen);
                TextView detailsComment = (TextView)findViewById(R.id.comment);
                detailsComment.setText("");
                EditTriageStatus = true;
            }else if(CreateFlag){
                new AddNewTriageRecord().execute();
            }
        }


    }

    public Bitmap base64ToBitmap(String b64) {
        byte[] imageAsBytes = Base64.decode(b64.getBytes(), Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(imageAsBytes, 0, imageAsBytes.length);
    }
    /**
     * Background Async Task to Create new product
     */
    class AddNewTriageRecord extends AsyncTask<String, String, String> {
        int dodano = 0;

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
                    dodano = 1;

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
            if(dodano == 1)
                Toast.makeText(fragment_context,R.string.dodanoTriage, Toast.LENGTH_SHORT ).show();
            else
                Toast.makeText(fragment_context,R.string.niedodanoTriage, Toast.LENGTH_SHORT ).show();
            CreateFlag = false; // utworzono pacjenta - aby wybrać kolejnego nalezy skorzystać z menu
            EditFlag = false; // dodano nowy rekod dla pacjenta
            EditTriageStatus = false;
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
        @SuppressWarnings("deprecation")
        protected String doInBackground(String... args) {

            // updating UI from Background Thread
           // runOnUiThread(new Runnable() {
             //   public void run() {
                    // Check for success tag
                    int success;
                    try {
                        // Building Parameters
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
               // }
            //});

            return null;
        }
        /**
         * After completing background task Dismiss the progress dialog
         * *
         */
        protected void onPostExecute(String file_url) {
            // dismiss the dialog once done
             pDialog.dismiss();
            triageCompleted = true;

        }
    }

    /**
     * Background Async Task to  Save product Details
     * */
    class SavePatientDetails extends AsyncTask<String, String, String> {

        int zapisano = 0;
        /**
         * Before starting background thread Show Progress Dialog
         * */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(detailsFragment_context);
            pDialog.setMessage("Saving product ...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }

        /**
         * Saving product
         * */
        protected String doInBackground(String... args) {

            // Building Parameters
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair(TAG_TRIAGE_ID, triageID));
            params.add(new BasicNameValuePair(TAG_TRIAGE_PHOTO, detailsPhoto));
            params.add(new BasicNameValuePair(TAG_TRIAGE_COMMENT, detailsComment));

            // sending modified data through http request
            // Notice that update product url accepts POST method
            JSONObject json = jsonParser.makeHttpRequest(url_update_triage,
                    "POST", params);

            // check json success tag
            try {
                int success = json.getInt(TAG_SUCCESS);

                if (success == 1) {
                    Log.i("dodano update", "OK!!!");
                    zapisano = 1;
                    // successfully updated
                    //Intent i = getIntent();
                    // send result code 100 to notify about product update
                    //setResult(100, i);
                    //finish();
                } else {
                    // failed to update product
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return null;
        }

        /**
         * After completing background task Dismiss the progress dialog
         * **/
        protected void onPostExecute(String file_url) {
            // dismiss the dialog once product uupdated
            pDialog.dismiss();
            if(zapisano==1)
                Toast.makeText(getApplication(), R.string.dodanoSzczegol, Toast.LENGTH_SHORT).show();
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

            EditFlag = true; // flag to edit patient triage status
            //new GetPatientDetails().execute(); // pobranie parametrów pacjenta o danym NFCID
        Toast.makeText(NewActivity.this,R.string.nfcTag,Toast.LENGTH_LONG).show();
        if( hasNFCmodul == false & EditFlag == true){
            Dialog dialog = createInputDialog();
            dialog.show();
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
        Intent intent = new Intent(NewActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

    private Dialog createInputDialog() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("NFC ID");
        builder.setMessage("Wprowadź ID tagu NFC:");

        // Use an EditText view to get user input.
        final EditText input = new EditText(this);
        input.setId(0);
        builder.setView(input);

        builder.setPositiveButton("Wprowadź", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int whichButton) {
                nfcID = input.getText().toString();
                if(patientPhoto!=null & CreateFlag == true)
                    new CreateNewPatient().execute();
                if(EditFlag)
                    new GetPatientDetails().execute();
                return;
            }
        });

        builder.setNegativeButton("Anuluj", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                return;
            }
        });

        return builder.create();
    }

}
