package com.domain.train.utils;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Ilya on 19.03.2018.
 */

public class DBHelper extends SQLiteOpenHelper {

    private static final int DB_VERSION = 1;

    public DBHelper(Context context) {
        // конструктор суперкласса
        super(context, "trainDB", null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table stations (id integer primary key autoincrement," +
                "countryTitle text," +
                "districtTitle text," +
                "cityTitle text," +
                "regionTitle text," +
                "stationTitle text," +
                "stationId integer," +
                "cityId integer," +
                "type integer);");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if(newVersion > 1) {
            db.execSQL("DROP TABLE IF EXISTS stations");
            onCreate(db);
        }
    }
}
