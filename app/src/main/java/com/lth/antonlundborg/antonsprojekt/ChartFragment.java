package com.lth.antonlundborg.antonsprojekt;


import android.app.Fragment;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;


/**
 * A simple {@link Fragment} subclass.
 */
public class ChartFragment extends ListFragment implements AdapterView.OnItemSelectedListener, AbsListView.OnScrollListener {
    private static final String chartUrl = "http://cdn.fmi.fi/marine-forecasts/products/wave-forecast-maps/wave";
    private static final String chartUrlEnd = ".gif";
    private String[] chartUrlNumber = {"03", "06", "09", "12", "15", "18", "21", "24", "27", "30", "33", "36", "39",
            "42", "45", "48", "51", "54", "57", "60", "63", "66", "69", "72"};
    private MyBitmapAdapter adapter;
    private Spinner spinner;

    public ChartFragment() {

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_chart, container, false);
        ChartDownloader downloader;
        String[] urlAndIndex;
        for (int i = 0; i < chartUrlNumber.length; i++) {
            downloader = new ChartDownloader();
            urlAndIndex = new String[]{(chartUrl + chartUrlNumber[i] + chartUrlEnd), Integer.toString(i)};
            startAsyncTaskInParallel(downloader, urlAndIndex);
        }

        adapter = new MyBitmapAdapter(getActivity(), chartUrlNumber.length);
        setListAdapter(adapter);

        spinner = (Spinner) v.findViewById(R.id.time_chart_spinner);
        ArrayAdapter<CharSequence> arrayAdapter = ArrayAdapter.createFromResource(getActivity(),
                R.array.time_chart_array, android.R.layout.simple_spinner_item);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(arrayAdapter);

        spinner.setOnItemSelectedListener(this);

        return v;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getListView().setOnScrollListener(this);
    }

    private void startAsyncTaskInParallel(ChartDownloader task, String[] url) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, url);
        } else {
            task.execute(url);
        }
    }
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        Log.d("spinnerdebug", "onItemSelected called with position " + position);
        if(spinner.getTag(R.id.pos) != position){
            getListView().setSelection(position);

        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) { }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {}

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        spinner.setTag(R.id.pos, firstVisibleItem);
        spinner.setSelection(firstVisibleItem);
    }


    private class ChartDownloader extends AsyncTask<String, Void, Void> {

        @Override
        protected Void doInBackground(String... params) {
            InputStream inputStream;
            try {
                URL url = new java.net.URL(params[0]);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.connect();
                inputStream = connection.getInputStream();
                final Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                final int index = Integer.parseInt(params[1]);
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        adapter.addBitmap(bitmap, index);
                    }
                });

            } catch (FileNotFoundException e){
                e.printStackTrace();
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        adapter.decreseSize();
                    }
                });
            } catch (IOException ex) {
                ex.printStackTrace();
            }
            return null;
        }
    }

}
