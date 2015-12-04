package com.example.kasia.projekt;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.location.Criteria;
import android.location.GpsSatellite;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
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
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends Activity implements LocationListener {

    // Progress Dialog
    private ProgressDialog pDialog;

    JSONParser jsonParser = new JSONParser();

    // url to create new product
    private static String url_create_incident = "http://mszulc.eu/create_incident.php";
    // JSON Node names
    private static final String TAG_SUCCESS = "success";

    TextView dlugosc, szerokosc, tekst1, tekst2;
    EditText opis;
    Button button2;
    RadioGroup rozmiar;
    RadioButton maly, sredni, duzy;
    String size, description, longitude, latitude;
    LocationManager lm;
    Criteria kr;
    Location loc;
    String dostawca = null;
    Context Context;

    public void reakcja() {
        Intent i = new Intent(this, TriageTypeActivity.class);
        //i.putExtra("isAdult",isAdult);
        startActivity(i);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Context = getApplicationContext();

        dlugosc = (TextView) findViewById(R.id.longitude);
        szerokosc = (TextView) findViewById(R.id.latitude);
        opis = (EditText) findViewById(R.id.description);
        description = opis.getText().toString();
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
                        reakcja();
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
            dlugosc.setText("długość geograficzna = " + loc.getLongitude());
            szerokosc.setText("szerokość geograficzna = " + loc.getLatitude());
        }

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
            case R.id.menu_dataBase:
                // Single menu item is selected do something
                // Ex: launching new activity/screen or show alert message
                Toast.makeText(MainActivity.this, "Bookmark is Selected", Toast.LENGTH_SHORT).show();
                return true;

            case R.id.menu_save:
                Toast.makeText(MainActivity.this, "Save is Selected", Toast.LENGTH_SHORT).show();
                return true;


            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onLocationChanged(Location location) {

        dostawca = lm.getBestProvider(kr, true);
        loc = lm.getLastKnownLocation(dostawca);

        tekst1.setText("najlepszy dostawca " + dostawca);
        tekst2.setText("" + lm.isProviderEnabled(dostawca));

        //if (loc!=null) {
        dlugosc.setText("długość geograficzna = " + loc.getLongitude());
        szerokosc.setText("szerokość geograficzna = " + loc.getLatitude());
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
            pDialog = new ProgressDialog(getApplicationContext());
            pDialog.setMessage("Creating Incident..");
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
            params.add(new BasicNameValuePair("longitude", dlugosc.getText().toString()));
            params.add(new BasicNameValuePair("latitude", szerokosc.getText().toString()));
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
        }

    }
}