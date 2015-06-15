package com.lth.antonlundborg.antonsprojekt;

import android.annotation.TargetApi;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;

import static com.lth.antonlundborg.antonsprojekt.Constants.STATION_LATITUDE;
import static com.lth.antonlundborg.antonsprojekt.Constants.STATION_LONGITUDE;
import static com.lth.antonlundborg.antonsprojekt.Constants.STATION_NAME;
import static com.lth.antonlundborg.antonsprojekt.Constants.STATION_NUMBER;
import static com.lth.antonlundborg.antonsprojekt.Constants.STATION_WIND_DIRECTION;
import static com.lth.antonlundborg.antonsprojekt.Constants.STATION_WIND_SPEED;
import static com.lth.antonlundborg.antonsprojekt.Constants.TABLE_NAME;

public class SmhiMapFragment extends Fragment implements OnMapReadyCallback {

    private final static String TAG = "MapFragment";
    private static boolean firstTime = true;
    private GoogleMap mMap;
    private LatLngBounds swedenBounds;
    private ImageButton switchButton;
    private static final String PERIOD = "latest-hour";
    private static final String WIND_DIRECTION = "3";
    private static final String WIND_SPEED = "4";
    private static SMHIDatabase smhiDatabase;
    public static String[] FROM = {STATION_NAME, STATION_LATITUDE, STATION_LONGITUDE, STATION_WIND_SPEED, STATION_WIND_DIRECTION};
    public static ArrayList<String> smhiStations;

    public static SmhiMapFragment newInstance() {
        return new SmhiMapFragment();
    }

    public SmhiMapFragment() {
        // Required empty public constructor
    }


    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_map, container, false);

        switchButton = (ImageButton) getActivity().findViewById(R.id.switch_button);
        if (firstTime) {
            new Thread(new Runnable() {
                public void run() {
                    smhiDatabase = new SMHIDatabase(getActivity());
                    addEvents();
                    Downloader downloader;
                    for (int i = 0; i < smhiStations.size(); i++) {
                        downloader = new Downloader();
                        startAsyncTaskInParallel(downloader, smhiStations.get(i));
                    }
                }
            }).start();
            firstTime = false;
            Log.d(TAG, "Downloading from internet");
        } else {
            DatabaseAccess dbAcess = new DatabaseAccess();
            dbAcess.execute();
            Log.d(TAG, "Getting from Database");
        }
        return view;
    }

    @Override
    public void onResume(){
        setUpMapIfNeeded();
        super.onResume();
    }

    private void addEvents() {
        smhiStations = new ArrayList<>();
        addEvent("Hallands Väderö A", "62260", 56.45, 12.55);
        addEvent("Väderöarna A", "81350", 58.58, 11.07);
        addEvent("Storön A", "163900", 65.70, 23.10);
        addEvent("Rödkallen A", "162790", 65.31, 22.37);
        addEvent("Rönnskär", "161710", 65.03, 21.57);
        addEvent("Bjuröklubb A", "151280", 64.48, 21.58);
        addEvent("Holmön A", "140460", 63.81, 20.87);
        addEvent("Järnäsklubb A", "139260", 63.44, 19.68);
        addEvent("Skagsudde A", "139120", 63.19, 19.02);
        addEvent("Lungö A", "128390", 62.64, 18.09);
        addEvent("Brämön A", "127130", 62.22, 17.74);
        addEvent("Kuggören A", "117430", 61.70, 17.52);
        addEvent("Eggegrund A", "107440", 60.73, 17.56);
        addEvent("Örskär A", "108320", 60.53, 18.38);
        addEvent("Söderarm A", "99450", 59.75, 19.41);
        addEvent("Svenska högarna A", "99280", 59.44, 19.51);
        addEvent("Stavsnäs A", "98160", 59.30, 18.70);
        addEvent("Landsort A", "87440", 58.74, 17.87);
        addEvent("Visingsö A", "84050", 58.10, 14.41);
        addEvent("Gotska Sandön A", "89230", 58.39, 19.20);
        addEvent("Harstena A", "87140", 58.25, 17.01);
        addEvent("Fårösund Ar A", "78550", 57.92, 18.96);
        addEvent("Östergarnsholm A", "78280", 57.44, 18.99);
        addEvent("Hoburg A", "68560", 56.92, 18.15);
        addEvent("Ölands Norra Udde A", "77210", 57.37, 17.10);
        addEvent("Ölands Södra Udde A", "66110", 56.20, 16.40);
        addEvent("Utklippan A", "55570", 55.95, 15.70);
        addEvent("Skillinge A", "54290", 55.49, 14.32);
        addEvent("Falsterbo A", "52240", 55.38, 12.82);
        addEvent("Nordkoster A", "81540", 58.89, 11.01);
        addEvent("Väderöarna A", "81350", 58.58, 11.07);
        addEvent("Måseskär A", "81050", 58.09, 11.33);
        addEvent("Vinga A", "71380", 57.63, 11.61);
        addEvent("Naven A", "83420", 58.70, 13.11);
        addEvent("Pålgrunden A", "83450", 58.76, 13.16);
        addEvent("Nidingen A", "71190", 57.30, 11.91);
        addEvent("Hallands Väderö A", "62260", 56.45, 12.55);
    }


    private void addEvent(String name, String number, double latitude, double longitude) {
        smhiStations.add(number);
        SQLiteDatabase db = smhiDatabase.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(STATION_NAME, name);
        values.put(STATION_NUMBER, number);
        values.put(STATION_LATITUDE, latitude);
        values.put(STATION_LONGITUDE, longitude);
        db.insertOrThrow(TABLE_NAME, null, values);
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    private void startAsyncTaskInParallel(Downloader task, String number) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, number);
        } else {
            task.execute(number);
        }
    }

    private void setUpMapIfNeeded() {
        // Do a null check to confirm that we have not already instantiated the map.
        if (mMap == null) {
            SupportMapFragment supportMapFragment;
            if (Build.VERSION.SDK_INT < 21) {
                supportMapFragment = (SupportMapFragment) getActivity().getSupportFragmentManager().findFragmentById(R.id.map);
            } else {
                supportMapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
            }
            supportMapFragment.getMapAsync(this);
        }

    }

    public void putAllValuesOnMap() {
        Cursor cursor = getAllEvents();
        Log.d(TAG, "Got cursor");
        cursor.moveToFirst();
        for (int i = 0; i < smhiStations.size(); i++) {
            String stationName = cursor.getString(0);
            LatLng latLng = new LatLng(cursor.getDouble(1), cursor.getDouble(2));
            double windSpeed = cursor.getDouble(3);
            double windDirection = cursor.getDouble(4);
            putValueOnMap(stationName, latLng, windSpeed, windDirection);
            Log.d(TAG, " published number " + i);
            cursor.moveToNext();
        }
    }

    public void putValueOnMap(final String stationName, LatLng latLng, double windSpeed, double windDirection) {
        final String finalStationName = stationName;
        final LatLng finalLatLng = latLng;
        final String snippet = "Wind direction: " + windDirection + " Wind Speed: " + windSpeed;
        final float rotation = (float) windDirection + 90;
        final Bitmap wind = getIcon(windSpeed);
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mMap.addMarker(new MarkerOptions().position(finalLatLng).title(finalStationName).snippet(snippet).rotation(rotation).icon(BitmapDescriptorFactory.fromBitmap(wind)));
            }
        });
    }

    public void putValueOnMap(String number) {
        Cursor cursor = getEvent(number);
        cursor.moveToFirst();
        String stationName = cursor.getString(0);
        LatLng latLng = new LatLng(cursor.getDouble(1), cursor.getDouble(2));
        double windSpeed = cursor.getDouble(3);
        double windDirection = cursor.getDouble(4);
        putValueOnMap(stationName, latLng, windSpeed, windDirection);
    }

    public Cursor getAllEvents() {
        SQLiteDatabase db = smhiDatabase.getReadableDatabase();
        return db.query(TABLE_NAME, FROM, null, null, null, null, null);
    }

    private Cursor getEvent(String number) {
        String WHERE = STATION_NUMBER + " = " + number;
        SQLiteDatabase db = smhiDatabase.getReadableDatabase();
        return db.query(TABLE_NAME, FROM, WHERE, null, null, null, null);
    }

    public Bitmap getIcon(double speed) {
        int id = 0;
        if (speed >= 0) {
            id = R.drawable.vindpil0;
        }
        if (speed >= 2) {
            id = R.drawable.vindpil2;
        }
        if (speed >= 5) {
            id = R.drawable.vindpil5;
        }
        if (speed >= 7) {
            id = R.drawable.vindpil7;
        }
        if (speed >= 10) {
            id = R.drawable.vindpil10;
        }
        if (speed >= 12) {
            id = R.drawable.vindpil12;
        }
        if (speed >= 15) {
            id = R.drawable.vindpil15;
        }
        if (speed >= 20) {
            id = R.drawable.vindpil20;
        }
        if (speed >= 25) {
            id = R.drawable.vindpil25;
        }
        if (speed >= 32) {
            id = R.drawable.vindpil32;
        }

        return BitmapFactory.decodeResource(getResources(), id);
    }

    @Override
    public void onMapReady(GoogleMap map) {
        mMap = map;
        swedenBounds = new LatLngBounds(new LatLng(55.001099, 11.10694), new LatLng(69.063141, 24.16707));
        map.setOnCameraChangeListener(new GoogleMap.OnCameraChangeListener() {
            @Override
            public void onCameraChange(CameraPosition arg0) {
                mMap.moveCamera(CameraUpdateFactory.newLatLngBounds(swedenBounds, 10));
                mMap.setOnCameraChangeListener(null);
            }
        });

    }

    private class DatabaseAccess extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... params) {
            putAllValuesOnMap();
            return null;
        }
    }

    private class Downloader extends AsyncTask<String, Void, Void> {

        @Override
        protected Void doInBackground(String... params) {
            SMHIDownloader parser = new SMHIDownloader();
            SQLiteDatabase db = smhiDatabase.getWritableDatabase();
            ContentValues values = new ContentValues();
            try {
                double direction = parser.getWindParameter(WIND_DIRECTION, params[0], PERIOD);
                double speed = parser.getWindParameter(WIND_SPEED, params[0], PERIOD);
                values.put(STATION_WIND_DIRECTION, direction);
                values.put(STATION_WIND_SPEED, speed);
                String WHERE = STATION_NUMBER + " = " + params[0];
                db.update(TABLE_NAME, values, WHERE, null);
                putValueOnMap(params[0]);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

    }
}
