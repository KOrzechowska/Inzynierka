package com.example.kasia.projekt;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.util.Log;

/**
 * Created by kasia on 30.08.15.
 */
public class TabsPagerAdapter extends FragmentPagerAdapter {


    private Bundle bundleF;
    private boolean isAdult, isLeftHanded;
    public TabsPagerAdapter(FragmentManager fm, Boolean dane, Boolean option) {
        super(fm);
        isAdult = dane;
        isLeftHanded = option;
        Log.i("przesyłam dane", String.valueOf(isAdult));
    }

    @Override
    public Fragment getItem(int index) {

        switch (index) {
            case 0:
                // Top Rated fragment activity
                Log.i("przesyłam 1", "1");
               return new TopRatedFragment();
            case 1:
                // Games fragment activity
                Log.i("przesyłam 1", "2");
                Bundle bundle = new Bundle();
                bundle.putBoolean("iAT",isAdult);
                bundle.putBoolean("iLH", isLeftHanded);
                TriageQuestionFragment TriageQuestionFragment = new TriageQuestionFragment();
                TriageQuestionFragment.setArguments(bundle);
                return TriageQuestionFragment;
            case 2:
                Log.i("przesyłam 1", "3");
                // Movies fragment activity
                DetailsFragment detailsFragment = new DetailsFragment();
                //moviesFragment.setArguments(bundleF);
                return detailsFragment;
        }

        return null;
    }

    @Override
    public int getCount() {
        // get item count - equal to number of tabs
        return 3;
    }


}
