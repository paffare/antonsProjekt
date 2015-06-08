package com.lth.antonlundborg.antonsprojekt;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import static com.lth.antonlundborg.antonsprojekt.Constants.*;


public class SMHIDatabase extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "smhi.db";
    private static final int DATABASE_VERSION = 1;


    private static final String DICTIONARY_TABLE_CREATE =
            "CREATE TABLE " + TABLE_NAME + "( " + DATABASE_ID + ", " + STATION_NAME + " TEXT, " + STATION_NUMBER + " INT, " +
                    STATION_LATITUDE + " REAL, " + STATION_LONGITUDE + " REAL, " + STATION_WIND_SPEED + " REAL, " +
                    STATION_WIND_DIRECTION + " REAL);";

    public SMHIDatabase(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(DICTIONARY_TABLE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

}
