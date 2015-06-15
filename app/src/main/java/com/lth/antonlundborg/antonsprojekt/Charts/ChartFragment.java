package com.lth.antonlundborg.antonsprojekt.Charts;


import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Spinner;

import com.lth.antonlundborg.antonsprojekt.R;


public abstract class ChartFragment extends ListFragment implements AbsListView.OnScrollListener, AdapterView.OnItemSelectedListener {
    protected BaseAdapter adapter;
    protected Spinner spinner;

    public ChartFragment() {
        // Required empty public constructor
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(getLayoutId(), container, false);
        spinner = (Spinner) v.findViewById(R.id.time_chart_spinner);

        ArrayAdapter<CharSequence> arrayAdapter = ArrayAdapter.createFromResource(getActivity(),
                getSpinnerArrayId(), android.R.layout.simple_spinner_item);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(arrayAdapter);
        spinner.setOnItemSelectedListener(this);

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
        Log.d("spinnerdebug", "onItemSelected called with position " + position);
        if(spinner.getTag(R.id.pos) != position){
            getListView().setSelection(position);
        }
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        spinner.setTag(R.id.pos, firstVisibleItem);
        spinner.setSelection(firstVisibleItem);
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) { }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {}


}
