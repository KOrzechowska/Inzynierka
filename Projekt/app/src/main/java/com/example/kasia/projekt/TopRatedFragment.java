package com.example.kasia.projekt;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

/**
 * Created by kasia on 29.08.15.
 */
public class TopRatedFragment extends Fragment {

    private EditText edText1,edText2,edText3;
    private Button btn;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_top_rated, container, false);
        edText1 = (EditText)rootView.findViewById(R.id.editText);
        edText2 = (EditText)rootView.findViewById(R.id.editText2);
        edText3 = (EditText)rootView.findViewById(R.id.editText3);
        btn = (Button)rootView.findViewById(R.id.button);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String t1 = edText1.getText().toString();
                String t2 = edText2.getText().toString();
                String t3 = edText3.getText().toString();
                Log.i("tekst1",t1);
                Log.i("tekst2",t2);
                Log.i("tekst3",t3);

            }
        });
        return rootView;
    }

}
