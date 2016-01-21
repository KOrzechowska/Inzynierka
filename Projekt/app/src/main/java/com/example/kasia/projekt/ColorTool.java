package com.example.kasia.projekt;

/**
 * Created by kasia on 29.11.15.
 */
import android.graphics.Color;
import android.util.Log;
import android.widget.Toast;

/**
 * A class with methods to help with colors.
 * (Only one method so far.)
 *
 */

public class ColorTool {

    /**
     * Zwraca prawda jeśli dopasowano kolory
     * Aby się dopasować wszystkie trzy kolory (RGB) muszą znaleźć się w zakresie tolerancji
     *
     * @param color1 int
     * @param color2 int
     * @param tolerance int - tolerancja
     * @return boolean
     */

    public boolean closeMatch (int color1, int color2, int tolerance) {
        if ((int) Math.abs (Color.red (color1) - Color.red (color2)) > tolerance ) return false;
        if ((int) Math.abs (Color.green(color1) - Color.green(color2)) > tolerance ) return false;
        if ((int) Math.abs (Color.blue(color1) - Color.blue(color2)) > tolerance ) return false;
        return true;
    }

}
