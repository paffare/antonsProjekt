package com.lth.antonlundborg.antonsprojekt.Charts;


import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Spinner;

import com.lth.antonlundborg.antonsprojekt.R;


public abstract class ChartFragment extends ListFragment implements AbsListView.OnScrollListener, AdapterView.OnItemSelectedListener, Spinner.OnTouchListener {
    protected BaseAdapter adapter;
    protected Spinner spinner;
    private Boolean clickedSpinner = false;
    protected Toolbar toolbar;

    public ChartFragment() {
        // Required empty public constructor
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(getLayoutId(), container, false);
        spinner = (Spinner) v.findViewById(R.id.time_chart_spinner);
        toolbar = (Toolbar) getActivity().findViewById(R.id.toolbar);



        ArrayAdapter<CharSequence> arrayAdapter = ArrayAdapter.createFromResource(getActivity(),
                getSpinnerArrayId(), R.layout.spinner_item);

        arrayAdapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
        spinner.setAdapter(arrayAdapter);
        spinner.setOnItemSelectedListener(this);
        spinner.setOnTouchListener(this);

        adapter = new ChartAdapter(getActivity(), getUrlStart() ,getUrlNumbers(), getUrlEnd());
        setListAdapter(adapter);
        adapter.notifyDataSetChanged();
        return v;
    }

    @Override
    public void onDetach(){
        super.onDetach();
    }

    protected abstract int getLayoutId();

    protected abstract int getSpinnerArrayId();

    protected abstract String[] getUrlNumbers();

    protected abstract String getUrlStart();

    protected abstract String getUrlEnd();

    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getListView().setOnScrollListener(this);
    }
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        if(clickedSpinner == true){
            Log.d("spinnerdebug", "onItemSelected called with position " + position);
            getListView().setSelection(position);
            clickedSpinner = false;
        }
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        spinner.setSelection(firstVisibleItem);
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        clickedSpinner = false;
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {}


    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        clickedSpinner = true;
        return false;
    }
}
