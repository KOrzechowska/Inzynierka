package com.example.kasia.projekt;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by kasia on 29.08.15.
 */
public class DetailsFragment extends Fragment  implements View.OnTouchListener{

    private TextView tekst;
    ImageView img,imageView;
    Canvas canvas;
    Paint paint;
    private boolean glowaP = false, reka2P = false, reka1P = false, brzuch = false, glowaT = false, reka1T = false, reka2T = false,
        plecy=false, noga1T=false,noga2T=false;
   // public Context context;

    //private String napis;

  /*  @Override
    public void onAttach(Activity activity){
        super.onAttach(activity);
        context = getActivity();
        ((NewActivity)context).fragmentCommunicator = this;
    }*/

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_szczegoly, container, false);
        //napis = getArguments().getString("isAdult");
        //tekst = (TextView) rootView.findViewById(R.id.tekstCentrum);
        //tekst.setText(napis)





        imageView = (ImageView)rootView.findViewById(R.id.image);
        img = (ImageView) rootView.findViewById(R.id.image_areas);

        if (imageView != null) {
            imageView.setOnTouchListener(this);

        }
       // addOnClickListener(rootView);
        /*Bitmap canvasBitmap = Bitmap.createBitmap(50,50, Bitmap.Config.ARGB_8888);
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setColor(Color.BLUE);
        Canvas canvas = new Canvas(canvasBitmap);
        canvas.drawBitmap(canvasBitmap, 0, 0, paint);
        canvas.drawCircle(60, 50, 25, paint);
        /*ShapeDrawable sd = new ShapeDrawable(new OvalShape());
        sd.setIntrinsicHeight(100);
        sd.setIntrinsicWidth(100);
        sd.getPaint().setColor(Color.parseColor("#abcd123"));
        iv = (ImageView) rootView.findViewById(R.id.imageView);*/
        //iv.setBackground(sd);

        return rootView;
    }


    public void updateText(String newTekst){
       // tekst.setText(newTekst);
    }


    /**
     * Resume the activity.
     */

    @Override public void onResume() {
        super.onResume();

        /*View v  = findViewById (R.id.wglxy_bar);
        if (v != null) {
            Animation anim1 = AnimationUtils.loadAnimation(this, R.anim.fade_in);
            //anim1.setAnimationListener (new StartActivityAfterAnimation (i));
            v.startAnimation (anim1);
        }*/
    }



/**
 */
// More methods

    /**
     * Get the color from the hotspot image at point x-y.
     *
     */

    public int getHotspotColor ( int x, int y, View v) {
        if (img == null) {
            Log.d ("ImageAreasActivity", "Hot spot image not found");
            return 0;
        } else {
            img.setDrawingCacheEnabled(true);
            Bitmap hotspots = Bitmap.createBitmap(img.getDrawingCache());
            if (hotspots == null) {
                Log.d("ImageAreasActivity", "Hot spot bitmap was not created");
                return 0;
            } else {
                img.setDrawingCacheEnabled(false);
                return hotspots.getPixel(x, y);
            }
        }
    }

    @Override
    public boolean onTouch(View v, MotionEvent ev) {
        boolean handledHere = false;


        final int evX = (int) ev.getX();
        final int evY = (int) ev.getY();

        if(ev.getAction() == MotionEvent.ACTION_UP){
            //do something
            int touchColor = getHotspotColor (evX, evY,v);
            ColorTool ct = new ColorTool ();
            int tolerance = 25;

            if (ct.closeMatch (Color.RED, touchColor, tolerance)) {
                Toast.makeText(getActivity(),"czerwony",Toast.LENGTH_SHORT).show();
                if (!glowaP) { // jesli jeszcze głowy nie zaznaczono
                    rysuj(evX, evY);
                    glowaP = true;
                }
            }
            else if (ct.closeMatch (Color.BLUE, touchColor, tolerance)) {
                Toast.makeText(getActivity(),"ramie",Toast.LENGTH_SHORT).show();
                if (!reka2P) { // jesli jeszcze głowy nie zaznaczono
                    rysuj(evX, evY);
                    reka2P = true;
                }
            }
            else if (ct.closeMatch (Color.YELLOW, touchColor, tolerance)) {
                Toast.makeText(getActivity(),"plecy",Toast.LENGTH_SHORT).show();
                if (!plecy) { // jesli jeszcze głowy nie zaznaczono
                    rysuj(evX, evY);
                    plecy = true;
                }
            }
            else if (ct.closeMatch (Color.CYAN, touchColor, tolerance)) {
                Toast.makeText(getActivity(),"reka1t",Toast.LENGTH_SHORT).show();
                if (!reka1T) { // jesli jeszcze głowy nie zaznaczono
                    rysuj(evX, evY);
                    reka1T = true;
                }
            }
            else if (ct.closeMatch (Color.DKGRAY, touchColor, tolerance)) {
                Toast.makeText(getActivity(),"reka2t",Toast.LENGTH_SHORT).show();
                if (!reka2T) { // jesli jeszcze głowy nie zaznaczono
                    rysuj(evX, evY);
                    reka2T = true;
                }
            }
            else if (ct.closeMatch (Color.GRAY, touchColor, tolerance)) {
                Toast.makeText(getActivity(),"brzuch",Toast.LENGTH_SHORT).show();
                if (!brzuch) { // jesli jeszcze głowy nie zaznaczono
                    rysuj(evX, evY);
                    brzuch = true;
                }
            }
            else if (ct.closeMatch (Color.LTGRAY, touchColor, tolerance)) {
                Toast.makeText(getActivity(),"noga1t",Toast.LENGTH_SHORT).show();
                if (!noga1T) { // jesli jeszcze głowy nie zaznaczono
                    rysuj(evX, evY);
                    noga1T = true;
                }
            }
            else if (ct.closeMatch (Color.MAGENTA, touchColor, tolerance)) {
                Toast.makeText(getActivity(),"noga2t",Toast.LENGTH_SHORT).show();
                if (!noga2T) { // jesli jeszcze głowy nie zaznaczono
                    rysuj(evX, evY);
                    noga2T = true;
                }
            }
            else if (ct.closeMatch (Color.GREEN, touchColor, tolerance)) {
                Toast.makeText(getActivity(),"reka1p",Toast.LENGTH_SHORT).show();
                if (!reka1P) { // jesli jeszcze głowy nie zaznaczono
                    rysuj(evX, evY);
                    reka1P = true;
                }
            }
            else if (ct.closeMatch (Color.BLACK, touchColor, tolerance)) {
                Toast.makeText(getActivity(),"glowaT",Toast.LENGTH_SHORT).show();
                if (!glowaT) { // jesli jeszcze głowy nie zaznaczono
                    rysuj(evX, evY);
                    glowaT = true;
                }
            }
            else if (ct.closeMatch (Color.WHITE, touchColor, tolerance)) ;
        }
        return true;
    }

    public void rysuj(int x, int y){
        imageView.setDrawingCacheEnabled(true);
        Bitmap bitmapa = Bitmap.createBitmap(imageView.getDrawingCache());
        paint = new Paint();
        paint.setAntiAlias(true);
        paint.setColor(Color.RED);
        canvas = new Canvas(bitmapa);
        canvas.drawCircle(x, y, 40, paint);
        imageView.setDrawingCacheEnabled(false);
        Log.d("canvas", "draw a circle");
        imageView.setAdjustViewBounds(true);
        imageView.setImageBitmap(bitmapa);
    }




// end class


    /*@Override
    public void passDataToFragment(String someValue) {
        tekst.setText(someValue);
    }*/

   /* public void addOnClickListener(View view){

        glowaP = (CheckBox) view.findViewById(R.id.p1);
        ramieP1 = (CheckBox) view.findViewById(R.id.p2);
        ramieP2 = (CheckBox) view.findViewById(R.id.p3);
        brzuch = (CheckBox) view.findViewById(R.id.p4);
        rekaP1 = (CheckBox) view.findViewById(R.id.p5);
        rekaP2 = (CheckBox) view.findViewById(R.id.p6);
        udoP1 = (CheckBox) view.findViewById(R.id.p7);
        udoP2 = (CheckBox) view.findViewById(R.id.p8);
        nogaP1 = (CheckBox) view.findViewById(R.id.p9);
        nogaP2 = (CheckBox) view.findViewById(R.id.p10);

        glowaT = (CheckBox) view.findViewById(R.id.t1);
        ramieT1 = (CheckBox) view.findViewById(R.id.t2);
        ramieT2 = (CheckBox) view.findViewById(R.id.t3);
        plecy = (CheckBox) view.findViewById(R.id.t4);
        rekaT1 = (CheckBox) view.findViewById(R.id.t5);
        rekaT2 = (CheckBox) view.findViewById(R.id.t6);
        udoT1 = (CheckBox) view.findViewById(R.id.t7);
        udoT2 = (CheckBox) view.findViewById(R.id.t8);
        nogaT1 = (CheckBox) view.findViewById(R.id.t9);
        nogaT2 = (CheckBox) view.findViewById(R.id.t10);


        glowaP.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                //is chkIos checked?
                if (((CheckBox) v).isChecked()) {
                    Toast.makeText(getActivity(),
                            "głowa przód", Toast.LENGTH_LONG).show();
                }

            }
        });
        ramieP1.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                //is chkIos checked?
                if (((CheckBox) v).isChecked()) {
                    Toast.makeText(getActivity(),
                            "ramie przód", Toast.LENGTH_LONG).show();
                }

            }
        });
        ramieP2.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                //is chkIos checked?
                if (((CheckBox) v).isChecked()) {
                    Toast.makeText(getActivity(),
                            "ramie2 przód", Toast.LENGTH_LONG).show();
                }

            }
        });
        rekaP1.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                //is chkIos checked?
                if (((CheckBox) v).isChecked()) {
                    Toast.makeText(getActivity(),
                            "reka przód", Toast.LENGTH_LONG).show();
                }

            }
        });
        rekaP2.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                //is chkIos checked?
                if (((CheckBox) v).isChecked()) {
                    Toast.makeText(getActivity(),
                            "reka2 przód", Toast.LENGTH_LONG).show();
                }

            }
        });
        brzuch.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                //is chkIos checked?
                if (((CheckBox) v).isChecked()) {
                    Toast.makeText(getActivity(),
                            "brzuch", Toast.LENGTH_LONG).show();
                }

            }
        });
        udoP1.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                //is chkIos checked?
                if (((CheckBox) v).isChecked()) {
                    Toast.makeText(getActivity(),
                            "ramie przód", Toast.LENGTH_LONG).show();
                }

            }
        });
        udoP2.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                //is chkIos checked?
                if (((CheckBox) v).isChecked()) {
                    Toast.makeText(getActivity(),
                            "ramie przód", Toast.LENGTH_LONG).show();
                }

            }
        });
        nogaP1.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                //is chkIos checked?
                if (((CheckBox) v).isChecked()) {
                    Toast.makeText(getActivity(),
                            "ramie przód", Toast.LENGTH_LONG).show();
                }

            }
        });
        nogaP2.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                //is chkIos checked?
                if (((CheckBox) v).isChecked()) {
                    Toast.makeText(getActivity(),
                            "ramie przód", Toast.LENGTH_LONG).show();
                }

            }
        });

        glowaT.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                //is chkIos checked?
                if (((CheckBox) v).isChecked()) {
                    Toast.makeText(getActivity(),
                            "głowa przód", Toast.LENGTH_LONG).show();
                }

            }
        });
        ramieT1.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                //is chkIos checked?
                if (((CheckBox) v).isChecked()) {
                    Toast.makeText(getActivity(),
                            "ramie przód", Toast.LENGTH_LONG).show();
                }

            }
        });
        ramieT2.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                //is chkIos checked?
                if (((CheckBox) v).isChecked()) {
                    Toast.makeText(getActivity(),
                            "ramie2 przód", Toast.LENGTH_LONG).show();
                }

            }
        });
        rekaT1.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                //is chkIos checked?
                if (((CheckBox) v).isChecked()) {
                    Toast.makeText(getActivity(),
                            "reka przód", Toast.LENGTH_LONG).show();
                }

            }
        });
        rekaT2.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                //is chkIos checked?
                if (((CheckBox) v).isChecked()) {
                    Toast.makeText(getActivity(),
                            "reka2 przód", Toast.LENGTH_LONG).show();
                }

            }
        });
        plecy.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                //is chkIos checked?
                if (((CheckBox) v).isChecked()) {
                    Toast.makeText(getActivity(),
                            "brzuch", Toast.LENGTH_LONG).show();
                }

            }
        });
        udoT1.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                //is chkIos checked?
                if (((CheckBox) v).isChecked()) {
                    Toast.makeText(getActivity(),
                            "ramie przód", Toast.LENGTH_LONG).show();
                }

            }
        });
        udoT2.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                //is chkIos checked?
                if (((CheckBox) v).isChecked()) {
                    Toast.makeText(getActivity(),
                            "ramie przód", Toast.LENGTH_LONG).show();
                }

            }
        });
        nogaT1.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                //is chkIos checked?
                if (((CheckBox) v).isChecked()) {
                    Toast.makeText(getActivity(),
                            "ramie przód", Toast.LENGTH_LONG).show();
                }

            }
        });
        nogaT2.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                //is chkIos checked?
                if (((CheckBox) v).isChecked()) {
                    Toast.makeText(getActivity(),
                            "ramie przód", Toast.LENGTH_LONG).show();
                }

            }
        });
    }*/
}
