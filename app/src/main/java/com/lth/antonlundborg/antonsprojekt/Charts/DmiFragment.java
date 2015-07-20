package com.lth.antonlundborg.antonsprojekt.Charts;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.SpinnerAdapter;

import com.lth.antonlundborg.antonsprojekt.R;
import com.nostra13.universalimageloader.core.ImageLoader;

public class DmiFragment extends ChartFragment {

    private int baltic = 0;
    private int westCoast = 1;
    private int place;
    private boolean firstTime = true;

    public DmiFragment() {

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        place = getArguments().getInt("place");

        SpinnerAdapter spinnerAdapter = ArrayAdapter.createFromResource(getActivity(),
                R.array.dmi_action_list, android.R.layout.simple_spinner_dropdown_item);

        ActionBar.OnNavigationListener onNavigationListener = new ActionBar.OnNavigationListener() {
            String[] strings = getResources().getStringArray(R.array.dmi_action_list);

            @Override
            public boolean onNavigationItemSelected(int position, long itemId) {
                if(firstTime == true || position == place){
                    firstTime = false;
                    return true;
                }
                ImageLoader.getInstance().clearDiskCache();
                Log.d("Navigation", "Navigation selected");
                Bundle args = new Bundle();
                args.putInt("place", position);
                Fragment fragment = new DmiFragment();
                fragment.setArguments(args);
                FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                ft.replace(R.id.fragment_container, fragment, strings[position]);
                ft.commit();
                return true;
            }
        };
        ActionBar actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_LIST);
        actionBar.setListNavigationCallbacks(spinnerAdapter, onNavigationListener);
        actionBar.setSelectedNavigationItem(place);
        return super.onCreateView(inflater, container, savedInstanceState);
    }
    @Override
    public void onDetach(){
        ((AppCompatActivity) getActivity()).getSupportActionBar().setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        super.onDetach();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_chart;
    }

    @Override
    protected int getSpinnerArrayId() {
        return R.array.dmi_chart_array;
    }

    @Override
    public String getUrlStart() {
        if (place == baltic) {
            return "http://ocean.dmi.dk/anim/plots/hs.ba.";
        } else if (place == westCoast) {
            return "http://ocean.dmi.dk/anim/plots/hs.idf.";
        } else {
            return "";
        }
    }

    @Override
    public String[] getUrlNumbers() {
        return new String[]{"1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13", "14", "15",
                "16", "17", "18", "19", "20", "21", "22", "23", "24", "25", "26", "27", "28", "29", "30", "31",
                "32", "33", "34", "35", "36", "37", "38", "39", "40", "41", "42", "43", "44", "45", "46", "47",
                "48", "49", "50", "51", "52", "53", "54", "55", "56", "57", "58", "59", "60", "61", "62", "63",
                "64", "65", "66", "67", "68", "69", "70", "71", "72", "73", "74", "75", "76", "77", "78", "79",
                "80", "81", "82", "83", "84", "85", "86", "87", "88", "89", "90", "91", "92", "93", "94", "95",
                "96", "97", "98", "99", "100", "101", "102", "103", "104", "105", "106", "107", "108", "109", "110",
                "111", "112", "113", "114", "115", "116", "117", "118", "119", "120"};
    }

    @Override
    public String getUrlEnd() {
        return ".png";
    }
}
