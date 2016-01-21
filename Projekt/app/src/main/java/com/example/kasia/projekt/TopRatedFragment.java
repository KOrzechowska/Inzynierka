package com.example.kasia.projekt;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
//import android.support.design.widget.TextInputLayout;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.text.Editable;
//import android.text.TextWatcher;
import android.util.Base64;
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

import java.io.ByteArrayOutputStream;

/**
 * Created by kasia on 29.08.15.
 */
public class TopRatedFragment extends Fragment {

    private EditText edText1,edText2,edText3;
    private Button b;
    ImageView i;
    Bitmap bitmap = null;
    String bitmapAsString;
    private RadioGroup sex_rbt;
    String sex;


    //private TextInputLayout inputLayout1, inputLayout2, inputLayout3;
    String firstName, lastName, age;


    OnFragmentInteractionListener activityCallback;

    public interface OnFragmentInteractionListener {
        public void OnFragmentInteraction(String firstname, String lastname, String sex, String bitmapAsString);
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
        Log.i("e1","start-e1");
       // inputLayout1 = (TextInputLayout) rootView.findViewById(R.id.input_layout_name);
       // inputLayout2 = (TextInputLayout) rootView.findViewById(R.id.input_layout_lastname);
       // inputLayout3 = (TextInputLayout) rootView.findViewById(R.id.input_layout_age);
        b = (Button) rootView.findViewById(R.id.button3);
        i = (ImageView) rootView.findViewById(R.id.imageView);
        View.OnClickListener l = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(sex != null) {
                    Intent photo = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(photo, 1);
                }else
                    Toast.makeText(getActivity(), R.string.no_sex, Toast.LENGTH_SHORT).show();

            }
        };
        b.setOnClickListener(l);
        if(bitmap!=null)
        i.setImageBitmap(bitmap);
        edText1 = (EditText)rootView.findViewById(R.id.input_name);
        edText2 = (EditText)rootView.findViewById(R.id.input_lastname);
       // edText3 = (EditText)rootView.findViewById(R.id.input_age);

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
                        sex="F";
                        break;
                    case R.id.men_rbn:
                        sex="M";

                       break;
                    default:
                        break;
                }
            }
        });
        //edText1.addTextChangedListener(new MyTextWatcher(edText1));
        //edText2.addTextChangedListener(new MyTextWatcher(edText2));
       // edText3.addTextChangedListener(new MyTextWatcher(edText3));

        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        i.setImageBitmap(bitmap);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data){
        if(requestCode == 1 && resultCode == Activity.RESULT_OK){
            Bundle extras = data.getExtras();
            bitmap = (Bitmap) extras.get("data");
            i.setImageBitmap(bitmap);
            if(bitmap!=null){
                bitmapAsString = bitmapToBase64(bitmap); // zamiana bitmapy na string
            activityCallback.OnFragmentInteraction(edText1.getText().toString(),
                    edText2.getText().toString(),
                    sex, bitmapAsString);}
        }
    }


    public void buttonClicked (View view) {
        activityCallback.OnFragmentInteraction(edText1.getText().toString(),
                edText2.getText().toString(),
                sex, bitmapAsString);
                                       //edText3.getText().toString());
    }
    private String bitmapToBase64(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
        byte[] byteArray = byteArrayOutputStream .toByteArray();
        return Base64.encodeToString(byteArray, Base64.DEFAULT);
    }
    private Bitmap base64ToBitmap(String b64) {
        byte[] imageAsBytes = Base64.decode(b64.getBytes(), Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(imageAsBytes, 0, imageAsBytes.length);
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


