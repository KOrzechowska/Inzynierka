package com.example.kasia.projekt;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.nfc.NfcAdapter;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by kasia on 02.12.15.
 */
public class TriageTypeActivity extends Activity  {
    private boolean isAdult = true; // domyślnie aplikacja dla dorosłego pacjenta
    TextView tekst1, tekst2, tekst3;
    String incidentDBid, rescuerDBid;
    private Boolean CreateFlag;
   /* LocationManager lm;
    Criteria kr;
    Location loc;
    String dostawca=null;*/
    android.content.Context Context;

    public void reakcja(){
        CreateFlag = true; // flag to create new patient
        Intent i = new Intent(this, NewActivity.class);
        i.putExtra("isAdult",isAdult);
        i.putExtra("incidentId",incidentDBid);
        i.putExtra("rescuerId",rescuerDBid);
        i.putExtra("createFlag", CreateFlag);
        startActivity(i);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.triage_type_activity);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            incidentDBid = extras.getString("incidentId");
            rescuerDBid = extras.getString("rescuerId");

            Log.i("index",String.valueOf(incidentDBid));}

            ImageButton b = (ImageButton) findViewById(R.id.imageButton);
            ImageButton b2 = (ImageButton) findViewById(R.id.imageButton2);
            Context = getApplicationContext();

       /* tekst1 = (TextView) findViewById(R.id.textView3);
        tekst2 = (TextView) findViewById(R.id.textView4);
        tekst3 = (TextView) findViewById(R.id.textView5);

        kr=new Criteria();
        lm=(LocationManager) getSystemService(LOCATION_SERVICE);
        dostawca=lm.getBestProvider(kr,true);
        loc=lm.getLastKnownLocation(dostawca);
        lm.requestLocationUpdates(dostawca,1000,1,this);
        tekst1.setText("najlepszy dostawca " + dostawca);
        tekst2.setText(""+lm.isProviderEnabled(dostawca));

        if (loc!=null) {
            tekst2.setText("długość geograficzna = " + loc.getLongitude());
            tekst3.setText("szerokość geograficzna = " + loc.getLatitude());
        }*/

            b.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    isAdult = false; // dziecko
                    reakcja();

                }
            });
            b2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    isAdult = true; // dziecko
                    reakcja();

                }
            });

    }

   /* @Override
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
    /*@Override
    public boolean onOptionsItemSelected(MenuItem item)
    {

        switch (item.getItemId())
        {
            case R.id.menu_dataBase:
                // Single menu item is selected do something
                // Ex: launching new activity/screen or show alert message
                Toast.makeText(TriageTypeActivity.this, "Bookmark is Selected", Toast.LENGTH_SHORT).show();
                return true;

            case R.id.menu_save:
                Toast.makeText(TriageTypeActivity.this, "Save is Selected", Toast.LENGTH_SHORT).show();
                return true;


            default:
                return super.onOptionsItemSelected(item);
        }
    }*/

   /* @Override
    public void onLocationChanged(Location location) {

        dostawca=lm.getBestProvider(kr,true);
        loc=lm.getLastKnownLocation(dostawca);

        tekst1.setText("najlepszy dostawca " + dostawca);
        tekst2.setText(""+lm.isProviderEnabled(dostawca));

        //if (loc!=null) {
        tekst2.setText("długość geograficzna = " + loc.getLongitude());
        tekst3.setText("szerokość geograficzna = " + loc.getLatitude());
        //}
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }*/
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
}
