package com.lth.antonlundborg.antonsprojekt;

import android.app.ListActivity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;


public class ChartActivity extends ListActivity implements AdapterView.OnItemSelectedListener, AbsListView.OnScrollListener{
    private static final String chartUrl = "http://cdn.fmi.fi/marine-forecasts/products/wave-forecast-maps/wave";
    private static final String chartUrlEnd = ".gif";
    private String[] chartUrlNumber = {"03", "06", "09", "12", "15", "18", "21", "24", "27", "30", "33", "36", "39",
            "42", "45", "48", "51", "54", "57", "60", "63", "66", "69", "72"};
    private MyBitmapAdapter adapter;
    private Spinner spinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chart);

        ChartDownloader downloader;
        String[] urlAndIndex;
        for (int i = 0; i < chartUrlNumber.length; i++) {
            downloader = new ChartDownloader();
            urlAndIndex = new String[]{(chartUrl + chartUrlNumber[i] + chartUrlEnd), Integer.toString(i)};
            startAsyncTaskInParallel(downloader, urlAndIndex);
        }

        adapter = new MyBitmapAdapter(this, chartUrlNumber.length);
        setListAdapter(adapter);

        spinner = (Spinner) findViewById(R.id.time_chart_spinner);
        ArrayAdapter<CharSequence> arrayAdapter = ArrayAdapter.createFromResource(this,
                R.array.time_chart_array, android.R.layout.simple_spinner_item);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(arrayAdapter);

        spinner.setOnItemSelectedListener(this);
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
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_chart, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
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
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        adapter.addBitmap(bitmap, index);
                    }
                });

            } catch (FileNotFoundException e){
                e.printStackTrace();
                runOnUiThread(new Runnable(){
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
