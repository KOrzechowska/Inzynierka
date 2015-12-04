package com.example.kasia.projekt;

import android.app.ActionBar;
import android.app.Activity;
import android.app.FragmentTransaction;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
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
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by kasia on 30.08.15.
 */
public class NewActivity extends FragmentActivity implements ActionBar.TabListener, TopRatedFragment.OnFragmentInteractionListener,GamesFragment.ActivityCommunicator {

    Context fragment_context;
    // Progress Dialog
    private ProgressDialog pDialog;

    JSONParser jsonParser = new JSONParser();

    // url to create new product
    private static String url_create_product = "http://mszulc.eu/create_patient.php";
    // JSON Node names
    private static final String TAG_SUCCESS = "success";
   // public FragmentCommunicator fragmentCommunicator;
    private ViewPager viewPager;
    private TabsPagerAdapter mAdapter;
    private ActionBar actionBar;
    private String sex ; // M- mężczyzna K- kobieta
    private String firstName, lastName;
    private boolean isAdultTriage;
    private String triageStatus;
    // Tab titles
    private String[] tabs = {"Dane osobowe", "pytanie TRIAGE", "Dodatkowe informacje"};





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_activity);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            isAdultTriage = extras.getBoolean("isAdult");
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
    public void OnFragmentInteraction(String imie, String nazwisko, String plec) {
        TopRatedFragment textFragment =
                (TopRatedFragment)
                        getSupportFragmentManager().findFragmentById(R.id.fragment_top_rated);
        Log.i("tekst1", imie);
        Log.i("tekst2",nazwisko);
        Log.i("tekst3", plec);
        firstName = imie;
        lastName = nazwisko;
        sex=plec; // kobieta lub mężczyzna

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
            case R.id.menu_dataBase:
                // Single menu item is selected do something
                // Ex: launching new activity/screen or show alert message
                Toast.makeText(NewActivity.this, "Bookmark is Selected", Toast.LENGTH_SHORT).show();
                return true;

            case R.id.menu_save:
                Toast.makeText(NewActivity.this, "Save is Selected", Toast.LENGTH_SHORT).show();
                return true;


            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void passDataToActivity(String someValue, Context fragmentContext) {
        triageStatus = someValue;
        Log.i("tekst4",triageStatus+firstName+lastName+sex);
        fragment_context = fragmentContext;
        //creating new product in background thread
        new CreateNewProduct().execute();
    }

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
            pDialog = new ProgressDialog(fragment_context);
            pDialog.setMessage("Creating Product..");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }
    /**
     * Creating product
     */
    protected String doInBackground(String... args) {

        // Building Parameters
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("firstName", firstName));
        params.add(new BasicNameValuePair("lastName", lastName));
        params.add(new BasicNameValuePair("sex", sex));
        params.add(new BasicNameValuePair("triageStatus", triageStatus));

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
        // dismiss the dialog once done
        pDialog.dismiss();
    }

}

}
