package com.lth.antonlundborg.antonsprojekt;

import android.provider.BaseColumns;

/**
 * Created by antonlundborg on 15-06-04.
 */
public interface Constants extends BaseColumns {

    static String TABLE_NAME = "Wind";
    public static String DATABASE_ID = "_id integer primary key";

    //Columns in smhi database
    public static final String STATION_NAME = "name";
    public static final String STATION_NUMBER = "number";
    public static final String STATION_LATITUDE = "latitude";
    public static final String STATION_LONGITUDE = "longitude";
    public static final String STATION_WIND_SPEED = "windspeed";
    public static final String STATION_WIND_DIRECTION = "winddirection";
}
