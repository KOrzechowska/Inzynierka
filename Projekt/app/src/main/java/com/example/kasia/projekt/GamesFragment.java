package com.example.kasia.projekt;

/**
 * Created by kasia on 29.08.15.
 */

import android.app.Activity;
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
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class GamesFragment extends Fragment{ //implements NewActivity.FragmentCommunicator {

   public Context context;
   //interface via which we communicate to hosting Activity
   private ActivityCommunicator activityCommunicator;
    // Progress Dialog
    private ProgressDialog pDialog;

    JSONParser jsonParser = new JSONParser();


    // url to create new product
    private static String url_create_product = "http://kasia.mszulc.eu/create_patient.php";
    boolean Adult;
    private Bundle b;
    // JSON Node names
    private static final String TAG_SUCCESS = "success";

    TextView tekst1, tekst2, tekst3, tekst4, tekst5, tekst6, tekst7, tekst8, tekst9;
    TextView statusWiek, wynikTriage;
    Boolean triageKind, czyOddycha = true; // true - zwykły false - wersja pediatryczna
    ToggleButton tg1, tg2, tg3, tg4, tg5;
    Button b1;
    RadioGroup radioGroup1, radioGroup2, radioGroup3, radioGroup4, radioGroup5, radioGroup6, radioGroup7, radioGroup8;
    RadioButton rb1t, rb1n, rb2t, rb2n, rb3t, rb3n, rb4t, rb4n, rb5t, rb5n, rb6t, rb6n, rb7t, rb7n, rb8t, rb8n;
    String triageStatus; // [3]- zielony [2]- żółty [1]- czerwony [0]- czarny
    String triagePath;
    String imie, nazwisko;
    Boolean plec;



   @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        context = getActivity();

        activityCommunicator = (ActivityCommunicator)context;
    }

    public void widokWierszy(boolean w1, boolean w2, boolean w3, boolean w4, boolean w5, boolean w6, boolean w7, boolean w8) {
        if (w1) {
            tekst1.setVisibility(View.VISIBLE);
            radioGroup1.setVisibility(View.VISIBLE);
        } else {
            tekst1.setVisibility(View.INVISIBLE);
            radioGroup1.setVisibility(View.INVISIBLE);
            radioGroup1.clearCheck();
        }
        if (w2) {
            tekst2.setVisibility(View.VISIBLE);
            radioGroup2.setVisibility(View.VISIBLE);
        } else {
            tekst2.setVisibility(View.INVISIBLE);
            radioGroup2.setVisibility(View.INVISIBLE);
            radioGroup2.clearCheck();
        }
        if (w3) {
            tekst3.setVisibility(View.VISIBLE);
            radioGroup3.setVisibility(View.VISIBLE);
        } else {
            tekst3.setVisibility(View.INVISIBLE);
            radioGroup3.setVisibility(View.INVISIBLE);
            radioGroup3.clearCheck();
        }
        if (w4) {
            tekst4.setVisibility(View.VISIBLE);
            radioGroup4.setVisibility(View.VISIBLE);
        } else {
            tekst4.setVisibility(View.INVISIBLE);
            radioGroup4.setVisibility(View.INVISIBLE);
            radioGroup4.clearCheck();
        }
        if (w5) {
            tekst5.setVisibility(View.VISIBLE);
            radioGroup5.setVisibility(View.VISIBLE);
        } else {
            tekst5.setVisibility(View.INVISIBLE);
            radioGroup5.setVisibility(View.INVISIBLE);
            radioGroup5.clearCheck();
        }
       /* if (w6){
            tekst6.setVisibility(View.VISIBLE);
            radioGroup6.setVisibility(View.VISIBLE);
        }else{
            tekst6.setVisibility(View.INVISIBLE);
            radioGroup6.setVisibility(View.INVISIBLE);
        }
        if (w7){
            tekst7.setVisibility(View.VISIBLE);
            radioGroup7.setVisibility(View.VISIBLE);
        }else{
            tekst7.setVisibility(View.INVISIBLE);
            radioGroup7.setVisibility(View.INVISIBLE);
        }
        if (w8){
            tekst8.setVisibility(View.VISIBLE);
            radioGroup8.setVisibility(View.VISIBLE);
        }else{
            tekst8.setVisibility(View.INVISIBLE);
            radioGroup8.setVisibility(View.INVISIBLE);
        }*/
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_games, container, false);
        Log.i("e2","start-e2");
        b = getArguments();
        triageKind = b.getBoolean("iAT");
        if(b == null){
            Log.i("Received DATA", "gdzie ta dana");}
            //wynikTriage.setText("triage jest dla "+String.valueOf(Adult));}
        else Log.i("RECEIVED DATA",String.valueOf(triageKind));
        tekst1 = (TextView) rootView.findViewById(R.id.text1);
        tekst2 = (TextView) rootView.findViewById(R.id.text2);
        tekst3 = (TextView) rootView.findViewById(R.id.text3);
        tekst4 = (TextView) rootView.findViewById(R.id.text4);
        tekst5 = (TextView) rootView.findViewById(R.id.text5);

       /* tg1 = (ToggleButton) rootView.findViewById(R.id.button1);
        tg2 = (ToggleButton) rootView.findViewById(R.id.button2);
        tg3 = (ToggleButton) rootView.findViewById(R.id.button3);
        tg4 = (ToggleButton) rootView.findViewById(R.id.button4);
        tg5 = (ToggleButton) rootView.findViewById(R.id.button5);*/
       /* rb8t = (RadioButton) rootView.findViewById(R.id.question8rb1);
        rb8n = (RadioButton) rootView.findViewById(R.id.question8rb2);
        rb7t = (RadioButton) rootView.findViewById(R.id.question7rb1);
        rb7n = (RadioButton) rootView.findViewById(R.id.question7rb2);
        rb6t = (RadioButton) rootView.findViewById(R.id.question6rb1);
        rb6n = (RadioButton) rootView.findViewById(R.id.question6rb2);
        radioGroup8 = (RadioGroup) rootView.findViewById(R.id.rgQuestion8);
        radioGroup7 = (RadioGroup) rootView.findViewById(R.id.rgQuestion7);
        radioGroup6 = (RadioGroup) rootView.findViewById(R.id.rgQuestion6);*/
        radioGroup5 = (RadioGroup) rootView.findViewById(R.id.rgQuestion5);
        radioGroup3 = (RadioGroup) rootView.findViewById(R.id.rgQuestion3);
        radioGroup4 = (RadioGroup) rootView.findViewById(R.id.rgQuestion4);
        radioGroup2 = (RadioGroup) rootView.findViewById(R.id.rgCzyOddycha);
        radioGroup1 = (RadioGroup) rootView.findViewById(R.id.rgCzyChodzi);
        widokWierszy(true, false, false, false, false, false, false, false); //tylko pierwsze pytanie widoczne
        radioGroup1.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                // który radiobutton wybrany
                if (checkedId == R.id.question1rb1) {
                    //TAK - inne rzeczy niewidoczne
                    widokWierszy(true, false, false, false, false, false, false, false);
                    triageStatus = "GREEN"; // [3]- zielony
                    Toast.makeText(getActivity(), "chodzi- TRIAGE=zielony", Toast.LENGTH_SHORT).show();
                    wynikTriage.setText("TRIAGE = " + triageStatus);
                    triagePath = "1";
                    activityCommunicator.passDataToActivity(triagePath, context);
                    // creating new product in background thread
                    //new CreateNewProduct().execute();
                } else if (checkedId == R.id.question1rb2) {
                    //NIE
                    //tekst2.setVisibility(View.VISIBLE);
                    //radioGroup2.setVisibility(View.VISIBLE);
                    widokWierszy(true, true, false, false, false, false, false, false);
                    Toast.makeText(getActivity(), "nie chodzi", Toast.LENGTH_SHORT).show();

                }
            }
        });

        rb1t = (RadioButton) rootView.findViewById(R.id.question1rb1);
        rb1n = (RadioButton) rootView.findViewById(R.id.question1rb2);

        radioGroup2.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                // który radiobutton wybrany
                if (checkedId == R.id.question2rb1) {
                    //TAK - pojawia się kolejne pytanie
                    widokWierszy(true, true, true, false, false, false, false, false);
                    Toast.makeText(getActivity(), "oddycha", Toast.LENGTH_SHORT).show();
                } else if (checkedId == R.id.question2rb2) {
                    //NIE - udrożniej drogi jak nie to czarny kolor
                    czyOddycha = false;
                    if (!triageKind)  // dla Dzieci zmiana pytania 3 na powrót oddechu
                        tekst3.setText(getString(R.string.pytanie2cd)); // pytanie o polepszenie po udrożnieniu dróg
                    widokWierszy(true, true, true, false, false, false, false, false); // czy dorosły po udrożnieniu ma ok oddech
                    //TODO: zmienić toast na snackbar
                    Toast.makeText(getActivity(), "!udrożnij drogi oddechowe!", Toast.LENGTH_SHORT).show();

                }
            }
        });

        rb2t = (RadioButton) rootView.findViewById(R.id.question2rb1);
        rb2n = (RadioButton) rootView.findViewById(R.id.question2rb2);

        radioGroup3.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                // który radiobutton wybrany
                if (checkedId == R.id.question3rb1) {
                    //TAK - inne rzeczy niewidoczne
                    widokWierszy(true, true, true, false, false, false, false, false);
                    triageStatus = "RED"; // [1]- czerwony
                    if (!triageKind & !czyOddycha) { // jeśli dziecko i nie oddychało
                        triagePath = "7";
                        Toast.makeText(getActivity(), "oddech spontaniczny TRIAGE=czerwony", Toast.LENGTH_SHORT).show();
                    } else{
                        triagePath = "2";
                        Toast.makeText(getActivity(), "oddech < 8-30/min TRIAGE=czerwony", Toast.LENGTH_SHORT).show();}
                    wynikTriage.setText("TRIAGE = " + triageStatus);
                    activityCommunicator.passDataToActivity(triagePath, context);
                } else if (checkedId == R.id.question3rb2) {
                    //NIE- pojawia się kolejne pytanei o nawrót kapilarny
                    if (triageKind & !czyOddycha) { // jeśli dorosły i nie oddychał
                        triagePath = "6";
                        widokWierszy(true, true, true, false, false, false, false, false); // koniec gałęzi
                        triageStatus = "BLACK"; // [0]- czarny
                        Toast.makeText(getActivity(), "TRIAGE=czarny", Toast.LENGTH_SHORT).show();
                        wynikTriage.setText("TRIAGE = " + triageStatus);
                        activityCommunicator.passDataToActivity(triagePath, context);
                    } else if (!triageKind & !czyOddycha) {
                        tekst4.setText(R.string.pytanie3cd);
                        widokWierszy(true, true, true, true, false, false, false, false);
                    } else {
                        widokWierszy(true, true, true, true, false, false, false, false);
                        Toast.makeText(getActivity(), "następy krok ", Toast.LENGTH_SHORT).show();
                    }

                }
            }
        });

        rb3t = (RadioButton) rootView.findViewById(R.id.question3rb1);
        rb3n = (RadioButton) rootView.findViewById(R.id.question3rb2);

        radioGroup4.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                // który radiobutton wybrany
                if (checkedId == R.id.question4rb1) {
                    //TAK - inne rzeczy niewidoczne
                    if (!triageKind & !czyOddycha) { // dziecko nie oddychało i ma tętno
                        Toast.makeText(getActivity(), "Tętno jest, 5 wdechów", Toast.LENGTH_SHORT).show();
                        tekst5.setText(R.string.pytanie2cd);
                    } else
                        Toast.makeText(getActivity(), "nawrót <2sec", Toast.LENGTH_SHORT).show();
                    widokWierszy(true, true, true, true, true, false, false, false);
                } else if (checkedId == R.id.question4rb2) {
                    //NIE
                    widokWierszy(true, true, true, true, false, false, false, false);
                    if (!triageKind & !czyOddycha) {
                        triageStatus = "BLACK"; // [0]- czarny
                        triagePath = "8";
                        Toast.makeText(getActivity(), "brak tętna TRIAGE=czarny", Toast.LENGTH_SHORT).show();
                    } else {
                        triageStatus = "RED"; //[1]-czerwony
                        triagePath = "3";
                        Toast.makeText(getActivity(), "nawrót >2sec TRIAGE=czerwony", Toast.LENGTH_SHORT).show();
                    }

                    wynikTriage.setText("TRIAGE = " + triageStatus);
                    activityCommunicator.passDataToActivity(triagePath, context);
                }
            }
        });

        rb4t = (RadioButton) rootView.findViewById(R.id.question4rb1);
        rb4n = (RadioButton) rootView.findViewById(R.id.question4rb2);

        radioGroup5.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                // który radiobutton wybrany
                if (checkedId == R.id.question5rb1) {
                    //TAK - świadomy
                    widokWierszy(true, true, true, true, true, false, false, false);
                    if (!triageKind & !czyOddycha) {
                        triageStatus = "RED"; // [1]- czerwony
                        triagePath = "9";
                        Toast.makeText(getActivity(), "oddech spontaniczny, TRIAGE=czerwony", Toast.LENGTH_SHORT).show();
                    } else {
                        triageStatus = "YELLOW"; // [2]- zolty
                        triagePath = "4";
                        Toast.makeText(getActivity(), "świadomy, TRIAGE=żółty", Toast.LENGTH_SHORT).show();
                    }
                    wynikTriage.setText("TRIAGE = " + triageStatus);
                    activityCommunicator.passDataToActivity(triagePath, context);

                } else if (checkedId == R.id.question5rb2) {
                    //NIE- nieświadomy
                    widokWierszy(true, true, true, true, true, false, false, false);
                    if (!triageKind & !czyOddycha) {
                        triageStatus = "BLACK"; // [0]- czarny
                        triagePath = "10";
                        Toast.makeText(getActivity(), "brak tętna, TRIAGE=czarny", Toast.LENGTH_SHORT).show();
                    } else {
                        triageStatus = "RED"; // [1]- czerwony
                        triagePath = "5";
                        Toast.makeText(getActivity(), "nie świadomy, TRIAGE=czerwony", Toast.LENGTH_SHORT).show();

                    }
                    wynikTriage.setText("TRIAGE = " + triageStatus);
                    activityCommunicator.passDataToActivity(triagePath, context);

                }
            }
        });

        rb5t = (RadioButton) rootView.findViewById(R.id.question5rb1);
        rb5n = (RadioButton) rootView.findViewById(R.id.question5rb2);


        /*tg1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //setTitle("button1");
                //tg1.setVisibility(View.VISIBLE);
                // jeśli kliknięcie czy umie chodzić z tak na NIE
                if (((ToggleButton) v).isChecked()) {
                    tekst2.setVisibility(View.VISIBLE);
                    tg2.setVisibility(View.VISIBLE);
                }else {
                    // TRAIGE == zielony
                    Log.i("TRAIGE=", "zielony");
                }

            }
        });

        tg2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //tg2.setVisibility(View.INVISIBLE);
                //tg1.setVisibility(View.VISIBLE);
                // jeśli kliknięcie czy oddycha z nie na TAK
                if (((ToggleButton) v).isChecked()) {
                    tekst3.setVisibility(View.VISIBLE);
                    tg3.setVisibility(View.VISIBLE);
                }else {
                    // udrożnij drogi oddechowe TRIAGE = CZARNY
                    Log.i("TRIAGE=", "czarny");
                }
            }
        });

        tg3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //tg2.setVisibility(View.INVISIBLE);
                //tg1.setVisibility(View.VISIBLE);
                // jeśli kliknięcie czy oddycha z czestością 8-30/min z nie na TAK
                if (((ToggleButton) v).isChecked()) {
                    tekst4.setVisibility(View.VISIBLE);
                    tg4.setVisibility(View.VISIBLE);
                }else {
                    // udrożnij drogi oddechowe TRIAGE = CZERWONY
                    Log.i("TRIAGE=", "czerwony");
                }
            }
        });

        tg4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //tg2.setVisibility(View.INVISIBLE);
                //tg1.setVisibility(View.VISIBLE);
                // jeśli kliknięcie czy nawrót <2sec z nie na TAK
                if (((ToggleButton) v).isChecked()) {
                    tekst5.setVisibility(View.VISIBLE);
                    tg5.setVisibility(View.VISIBLE);
                }else {
                    // udrożnij drogi oddechowe TRIAGE = CZARNY
                    Log.i("TRIAGE=", "czerwony2");
                }
            }
        });

        tg5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //tg2.setVisibility(View.INVISIBLE);
                //tg1.setVisibility(View.VISIBLE);
                // jeśli kliknięcie czy swiadomy z nie na TAK
                if (((ToggleButton) v).isChecked()) {
                    Log.i("TRIAGE=", "zolty");
                }else {
                    // udrożnij drogi oddechowe TRIAGE = CZARNY
                    Log.i("TRIAGE=", "czerwony3");
                }
            }
        });*/
       /* tg1=(ToggleButton) rootView.findViewById(R.id.button1);
        tg2=(ToggleButton) rootView.findViewById(R.id.button2);

        b1=(Button) rootView.findViewById(R.id.button6);
        tg2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                StringBuffer result = new StringBuffer();
                result.append("Udrożnij drogi oddechowe ").append(tg2.getText());
                //result.append("You have clicked Second ON Button -:) ").append(tg2.getText());

                Toast.makeText(getActivity(), result.toString(), Toast.LENGTH_SHORT).show();
            }
        });*/
        statusWiek = (TextView) rootView.findViewById(R.id.textView);
        wynikTriage = (TextView) rootView.findViewById(R.id.textView2);


        return rootView;
    }



    public interface ActivityCommunicator{
        public void passDataToActivity(String someValue, Context fragmentContext);
    }
   /* @Override
    public void passDataToFragment(String firstName, String lastName, Boolean sex, Boolean someValue) {
        if (someValue)
            statusWiek.setText("Triage - dorosły" + imie + " " + nazwisko + " " + plec + " ");
        else statusWiek.setText("Triage - dziecko" + imie + " " + nazwisko + " " + plec + " ");
        imie = firstName;
        nazwisko = lastName;
        plec = sex;
       // triageKind = someValue;

    }*/

    /*public void onToggleButtonClicked(View view) {
        // Is the view now checked?
        boolean checked = ((ToggleButton) view).isChecked();

        // Check which checkbox was clicked
        switch(view.getId()) {
            case R.id.button1:
                if (checked){
                    Log.i("button1", "zmiana1");
                }
                // Put some meat on the sandwich
                else{}
                // Remove the meat
                break;
            case R.id.button2:
                if (checked){}
                // Cheese me
                else
                // I'm lactose intolerant
                break;
            case R.id.button3:
                if (checked){}
                // Cheese me
                else
                // I'm lactose intolerant
                break;
            case R.id.button4:
                if (checked){}
                // Cheese me
                else
                // I'm lactose intolerant
                break;
            case R.id.button5:
                if (checked){}
                // Cheese me
                else
                // I'm lactose intolerant
                break;
            // TODO: Veggie sandwich
        }
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
            pDialog = new ProgressDialog(getActivity());
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
            params.add(new BasicNameValuePair("firstName", imie));
            params.add(new BasicNameValuePair("lastName", nazwisko));
            params.add(new BasicNameValuePair("sex", plec.toString()));
            params.add(new BasicNameValuePair("triageStatus", String.valueOf(triageStatus)));

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


