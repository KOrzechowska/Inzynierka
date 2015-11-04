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
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

public class GamesFragment extends Fragment {

    TextView tekst1, tekst2, tekst3, tekst4, tekst5;
    ToggleButton tg1, tg2, tg3, tg4, tg5;
    Button b1;
    int triageStatus = 1; // [3]- zielony [2]- żółty [1]- czerwony [0]- czarny
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_games, container, false);

        tekst1 = (TextView) rootView.findViewById(R.id.text1);
        tekst2 = (TextView) rootView.findViewById(R.id.text2);
        tekst3 = (TextView) rootView.findViewById(R.id.text3);
        tekst4 = (TextView) rootView.findViewById(R.id.text4);
        tekst5 = (TextView) rootView.findViewById(R.id.text5);

        tg1 = (ToggleButton) rootView.findViewById(R.id.button1);
        tg2 = (ToggleButton) rootView.findViewById(R.id.button2);
        tg3 = (ToggleButton) rootView.findViewById(R.id.button3);
        tg4 = (ToggleButton) rootView.findViewById(R.id.button4);
        tg5 = (ToggleButton) rootView.findViewById(R.id.button5);

        tg1.setOnClickListener(new View.OnClickListener() {
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
        });
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
