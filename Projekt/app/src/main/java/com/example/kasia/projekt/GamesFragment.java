package com.example.kasia.projekt;

/**
 * Created by kasia on 29.08.15.
 */

import android.os.Bundle;
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

public class GamesFragment extends Fragment {

    TextView tekst1, tekst2, tekst3, tekst4, tekst5;
    ToggleButton tg1, tg2, tg3, tg4, tg5;
    Button b1;
    RadioGroup radioGroup1, radioGroup2, radioGroup3, radioGroup4, radioGroup5;
    RadioButton rb1t, rb1n, rb2t, rb2n, rb3t, rb3n, rb4t, rb4n, rb5t, rb5n;
    int triageStatus = 1; // [3]- zielony [2]- żółty [1]- czerwony [0]- czarny
    public void widokWierszy (boolean w1, boolean w2, boolean w3, boolean w4, boolean w5){
        if (w1){
            tekst1.setVisibility(View.VISIBLE);
            radioGroup1.setVisibility(View.VISIBLE);
        }else{
            tekst1.setVisibility(View.INVISIBLE);
            radioGroup1.setVisibility(View.INVISIBLE);
        }
        if (w2){
            tekst2.setVisibility(View.VISIBLE);
            radioGroup2.setVisibility(View.VISIBLE);
        }else{
            tekst2.setVisibility(View.INVISIBLE);
            radioGroup2.setVisibility(View.INVISIBLE);
        }
        if (w3){
            tekst3.setVisibility(View.VISIBLE);
            radioGroup3.setVisibility(View.VISIBLE);
        }else{
            tekst3.setVisibility(View.INVISIBLE);
            radioGroup3.setVisibility(View.INVISIBLE);
        }
        if (w4){
            tekst4.setVisibility(View.VISIBLE);
            radioGroup4.setVisibility(View.VISIBLE);
        }else{
            tekst4.setVisibility(View.INVISIBLE);
            radioGroup4.setVisibility(View.INVISIBLE);
        }
        if (w5){
            tekst5.setVisibility(View.VISIBLE);
            radioGroup5.setVisibility(View.VISIBLE);
        }else{
            tekst5.setVisibility(View.INVISIBLE);
            radioGroup5.setVisibility(View.INVISIBLE);
        }
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_games, container, false);

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
        radioGroup5 = (RadioGroup) rootView.findViewById(R.id.rgQuestion5);
        radioGroup3 = (RadioGroup) rootView.findViewById(R.id.rgQuestion3);
        radioGroup4 = (RadioGroup) rootView.findViewById(R.id.rgQuestion4);
        radioGroup2 = (RadioGroup) rootView.findViewById(R.id.rgCzyOddycha);
        radioGroup1 = (RadioGroup) rootView.findViewById(R.id.rgCzyChodzi);
        widokWierszy(true,false,false,false,false); //tylko pierwsze pytanie widoczne
        radioGroup1.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                // który radiobutton wybrany
                if(checkedId==R.id.question1rb1){
                    //TAK - inne rzeczy niewidoczne
                    widokWierszy(true,false,false,false,false);
                    Toast.makeText(getActivity(),"chodzi- TRIAGE=zielony",Toast.LENGTH_SHORT).show();
                }else if(checkedId==R.id.question1rb2){
                    //NIE
                    //tekst2.setVisibility(View.VISIBLE);
                    //radioGroup2.setVisibility(View.VISIBLE);
                    widokWierszy(true,true,false,false,false);
                    Toast.makeText(getActivity(),"nie chodzi",Toast.LENGTH_SHORT).show();

                }
            }
        });

        rb1t = (RadioButton) rootView.findViewById(R.id.question1rb1);
        rb1n = (RadioButton) rootView.findViewById(R.id.question1rb2);

        radioGroup2.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                // który radiobutton wybrany
                if(checkedId==R.id.question2rb1){
                    //TAK - pojawia się kolejne pytanie
                    widokWierszy(true,true,true,false,false);
                    Toast.makeText(getActivity(),"oddycha",Toast.LENGTH_SHORT).show();
                }else if(checkedId==R.id.question2rb2){
                    //NIE - udrożniej drogi jak nie to czarny kolor
                    widokWierszy(true,true,false,false,false);
                    //TODO: zmienić toast na snackbar
                    Toast.makeText(getActivity(),"!udrożnij drogi oddechowe!, TRAIGE=czarny",Toast.LENGTH_LONG).show();
                }
            }
        });

        rb2t = (RadioButton) rootView.findViewById(R.id.question2rb1);
        rb2n = (RadioButton) rootView.findViewById(R.id.question2rb2);

        radioGroup3.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                // który radiobutton wybrany
                if(checkedId==R.id.question3rb1){
                    //TAK - inne rzeczy niewidoczne
                    widokWierszy(true,true,true,false,false);
                    Toast.makeText(getActivity(),"oddech < 8-30/min TRIAGE=czerwony",Toast.LENGTH_SHORT).show();
                }else if(checkedId==R.id.question3rb2){
                    //NIE- pojawia się kolejne pytanei o nawrót kapilarny
                    widokWierszy(true,true,true,true,false);
                    Toast.makeText(getActivity(),"następy krok ",Toast.LENGTH_SHORT).show();

                }
            }
        });

        rb3t = (RadioButton) rootView.findViewById(R.id.question3rb1);
        rb3n = (RadioButton) rootView.findViewById(R.id.question3rb2);

        radioGroup4.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                // który radiobutton wybrany
                if(checkedId==R.id.question4rb1){
                    //TAK - inne rzeczy niewidoczne
                    widokWierszy(true,true,true,true,true);
                    Toast.makeText(getActivity(),"nawrót <2sec",Toast.LENGTH_SHORT).show();
                }else if(checkedId==R.id.question4rb2){
                    //NIE
                    widokWierszy(true,true,true,true,false);
                    Toast.makeText(getActivity(),"nawrót >2sec TRIAGE=czerwony",Toast.LENGTH_SHORT).show();

                }
            }
        });

        rb4t = (RadioButton) rootView.findViewById(R.id.question4rb1);
        rb4n = (RadioButton) rootView.findViewById(R.id.question4rb2);

        radioGroup5.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                // który radiobutton wybrany
                if(checkedId==R.id.question5rb1){
                    //TAK - świadomy
                    widokWierszy(true,true,true,true,true);
                    Toast.makeText(getActivity(),"świadomy, TRIAGE=żółty",Toast.LENGTH_SHORT).show();
                }else if(checkedId==R.id.question5rb2){
                    //NIE- nieświadomy
                    widokWierszy(true,true,true,true,true);
                    Toast.makeText(getActivity(),"nieświadomy, TRIAGE=czerwony",Toast.LENGTH_SHORT).show();

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


        return rootView;
    }

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
}
