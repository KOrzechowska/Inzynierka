package com.example.kasia.projekt;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
//import android.support.design.widget.TextInputLayout;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.text.Editable;
//import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by kasia on 29.08.15.
 */
public class TopRatedFragment extends Fragment {

    private EditText edText1,edText2,edText3;
    private Button btn, b;
    ImageView i;
    Bitmap bitmap;
    private RadioGroup sex_rbt;
    String sex;

    //private TextInputLayout inputLayout1, inputLayout2, inputLayout3;
    String firstName, lastName, age;


    OnFragmentInteractionListener activityCallback;

    public interface OnFragmentInteractionListener {
        public void OnFragmentInteraction(String firstname, String lastname, String sex);
    }
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            activityCallback = (OnFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement ToolbarListener");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_top_rated, container, false);
       // inputLayout1 = (TextInputLayout) rootView.findViewById(R.id.input_layout_name);
       // inputLayout2 = (TextInputLayout) rootView.findViewById(R.id.input_layout_lastname);
       // inputLayout3 = (TextInputLayout) rootView.findViewById(R.id.input_layout_age);
        b = (Button) rootView.findViewById(R.id.button3);
        i = (ImageView) rootView.findViewById(R.id.imageView);
        View.OnClickListener l = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent photo = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(photo,1);
                i.setImageBitmap(bitmap);
            }
        };
        b.setOnClickListener(l);

        edText1 = (EditText)rootView.findViewById(R.id.input_name);
        edText2 = (EditText)rootView.findViewById(R.id.input_lastname);
       // edText3 = (EditText)rootView.findViewById(R.id.input_age);
        btn = (Button)rootView.findViewById(R.id.button);
        sex_rbt = (RadioGroup) rootView.findViewById(R.id.sex);

        // Checked change Listener for RadioGroup 1
        sex_rbt.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId)
            {
                switch (checkedId)
                {
                    case R.id.women_rbn:
                        sex="W";
                        activityCallback.OnFragmentInteraction(edText1.getText().toString(),
                                edText2.getText().toString(),
                                sex);
                        break;
                    case R.id.men_rbn:
                        sex="M";
                        activityCallback.OnFragmentInteraction(edText1.getText().toString(),
                                edText2.getText().toString(),
                                sex);
                       break;
                    default:
                        break;
                }
            }
        });
        //edText1.addTextChangedListener(new MyTextWatcher(edText1));
        //edText2.addTextChangedListener(new MyTextWatcher(edText2));
       // edText3.addTextChangedListener(new MyTextWatcher(edText3));

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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data){
        if(requestCode == 1 && resultCode == Activity.RESULT_OK){
            Bundle extras = data.getExtras();
            bitmap = (Bitmap) extras.get("data");

        }
    }

    public void buttonClicked (View view) {
        activityCallback.OnFragmentInteraction(edText1.getText().toString(),
                edText2.getText().toString(),
                sex);
                                       //edText3.getText().toString());
    }
   /*public void setText (String url){
        TextView rootView = (TextView) getView().findViewById(R.id.detailsText);
        rootView.setText(url);
    }*/
  /* private class MyTextWatcher implements TextWatcher {

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
   }*/
}


