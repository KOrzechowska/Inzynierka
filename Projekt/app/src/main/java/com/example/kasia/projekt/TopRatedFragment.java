package com.example.kasia.projekt;

import android.app.Activity;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by kasia on 29.08.15.
 */
public class TopRatedFragment extends Fragment {

    private EditText edText1,edText2,edText3;
    private Button btn;
    private TextInputLayout inputLayout1, inputLayout2, inputLayout3;
    String firstName, lastName, age;


    ToolbarListener activityCallback;

    public interface ToolbarListener {
        public void onButtonClick(String firstname, String lastname, String Age);
    }
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            activityCallback = (ToolbarListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement ToolbarListener");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_top_rated, container, false);
        inputLayout1 = (TextInputLayout) rootView.findViewById(R.id.input_layout_name);
        inputLayout2 = (TextInputLayout) rootView.findViewById(R.id.input_layout_lastname);
        inputLayout3 = (TextInputLayout) rootView.findViewById(R.id.input_layout_age);
        edText1 = (EditText)rootView.findViewById(R.id.input_name);
        edText2 = (EditText)rootView.findViewById(R.id.input_lastname);
        edText3 = (EditText)rootView.findViewById(R.id.input_age);
        btn = (Button)rootView.findViewById(R.id.button);
        edText1.addTextChangedListener(new MyTextWatcher(edText1));
        edText2.addTextChangedListener(new MyTextWatcher(edText2));
        edText3.addTextChangedListener(new MyTextWatcher(edText3));

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                buttonClicked(view);
                /*firstName = edText1.getText().toString();
                lastName = edText2.getText().toString();
                age = edText3.getText().toString();
                Log.i("tekst1",firstName);
                Log.i("tekst2",lastName);
                Log.i("tekst3",age);*/

            }
        });
        return rootView;
    }


    public void buttonClicked (View view) {
        activityCallback.onButtonClick(edText1.getText().toString(),
                                       edText2.getText().toString(),
                                       edText3.getText().toString());
    }
   /*public void setText (String url){
        TextView rootView = (TextView) getView().findViewById(R.id.detailsText);
        rootView.setText(url);
    }*/
   private class MyTextWatcher implements TextWatcher {

       private View view;

       private MyTextWatcher(View view) {
           this.view = view;
       }

       public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
       }

       public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
       }

       public void afterTextChanged(Editable editable) {
           switch (view.getId()) {
               case R.id.input_name:

                   break;
               case R.id.input_lastname:

                   break;
               case R.id.input_age:

                   break;
           }
       }
   }
}


