package com.domain.train.models;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.domain.train.utils.DBHelper;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;


/**
 * Created by Ilya on 08.03.2018.
 */

public class Station implements Serializable {


    String countryTitle;

    String districtTitle;

    String cityTitle;

    String regionTitle;

    String stationTitle;

    int stationId;

    Map<String, Float> point;

    int type;

    int cityId;
    int id;


    public String getCountryTitle() {
        return countryTitle;
    }

    public void setCountryTitle(String countryTitle) {
        this.countryTitle = countryTitle;
    }

    public String getDistrictTitle() {
        return districtTitle;
    }

    public void setDistrictTitle(String districtTitle) {
        this.districtTitle = districtTitle;
    }

    public String getCityTitle() {
        return cityTitle;
    }

    public void setCityTitle(String cityTitle) {
        this.cityTitle = cityTitle;
    }

    public String getRegionTitle() {
        return regionTitle;
    }

    public void setRegionTitle(String regionTitle) {
        this.regionTitle = regionTitle;
    }

    public String getStationTitle() {
        return stationTitle;
    }

    public void setStationTitle(String stationTitle) {
        this.stationTitle = stationTitle;
    }

    public int getStationId() {
        return stationId;
    }

    public void setStationId(int stationId) {
        this.stationId = stationId;
    }

    public Map<String, Float> getPoint() {
        return point;
    }

    public void setPoint(Map<String, Float> point) {
        this.point = point;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getCityId() {
        return cityId;
    }

    public void setCityId(int cityId) {
        this.cityId = cityId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFullAddress() {
        String address = getCountryTitle() + ", ";

        if (!getRegionTitle().isEmpty())
            address += getRegionTitle() + ", ";

        if (!getDistrictTitle().isEmpty())
            address += getDistrictTitle() + ", ";

        if (!getCityTitle().isEmpty())
            address += getCityTitle();


        return address;
    }


    public LinkedList<Station> getAllStations(DBHelper helper, final String stationTitle, final int count, final int offset, final int type) {
        Log.d("trekdeks", "getAllStations - "  + String.valueOf(type));

        LinkedList<Station> stations = new LinkedList<>();
        Cursor cursor;
        SQLiteDatabase db = helper.getWritableDatabase();

        if (stationTitle == null) {
            cursor = db.query("stations",
                    new String[]{"countryTitle",
                            "districtTitle",
                            "cityTitle",
                            "regionTitle",
                            "stationTitle",
                            "stationId",
                            "cityId",
                            "type"
                    },
                    "type = ?",
                    new String[]{type + ""},
                    null,
                    null,
                    "cityTitle ASC",
                    offset + "," + count);
        } else {
            cursor = db.query("stations",
                    new String[]{"countryTitle",
                            "districtTitle",
                            "cityTitle",
                            "regionTitle",
                            "stationTitle",
                            "stationId",
                            "cityId",
                            "type"
                    },
                    "type = ? AND stationTitle LIKE ?",
                    new String[]{type + "", "%" + stationTitle.trim() + "%"},
                    null,
                    null,
                    "cityTitle ASC",
                    offset + "," + count);
        }

        if (cursor.getCount() == 0)
            return stations;

        if (cursor.moveToFirst()) {
            do {
                Station station = new Station();
                station.setCountryTitle(cursor.getString(cursor.getColumnIndex("countryTitle")));
                station.setDistrictTitle(cursor.getString(cursor.getColumnIndex("districtTitle")));
                station.setCityTitle(cursor.getString(cursor.getColumnIndex("cityTitle")));
                station.setRegionTitle(cursor.getString(cursor.getColumnIndex("regionTitle")));
                station.setStationTitle(cursor.getString(cursor.getColumnIndex("stationTitle")));
                station.setStationId(cursor.getInt(cursor.getColumnIndex("stationId")));
                station.setCityId(cursor.getInt(cursor.getColumnIndex("cityId")));
                station.setType(cursor.getInt(cursor.getColumnIndex("type")));

                stations.add(station);

            } while (cursor.moveToNext());
        }

        cursor.close();
        helper.close();

        return stations;
    }

    public Station selectStationId(int id, DBHelper helper) {
        Station station = new Station();
        Cursor cursor;
        SQLiteDatabase db = helper.getWritableDatabase();


        cursor = db.query("stations",
                new String[]{"countryTitle",
                        "districtTitle",
                        "cityTitle",
                        "regionTitle",
                        "stationTitle",
                        "stationId",
                        "cityId",
                        "type"
                },
                "stationId = ?",
                new String[]{id + ""},
                null,
                null,
                null,
                "0,1");


        if (cursor.getCount() == 0)
            return null;

        if (cursor.moveToFirst()) {

            //do {

                station.setCountryTitle(cursor.getString(cursor.getColumnIndex("countryTitle")));
                station.setDistrictTitle(cursor.getString(cursor.getColumnIndex("districtTitle")));
                station.setCityTitle(cursor.getString(cursor.getColumnIndex("cityTitle")));
                station.setRegionTitle(cursor.getString(cursor.getColumnIndex("regionTitle")));
                station.setStationTitle(cursor.getString(cursor.getColumnIndex("stationTitle")));
                station.setStationId(cursor.getInt(cursor.getColumnIndex("stationId")));
                station.setCityId(cursor.getInt(cursor.getColumnIndex("cityId")));
                station.setType(cursor.getInt(cursor.getColumnIndex("type")));

            //} while (cursor.moveToNext());
        }

        cursor.close();
        helper.close();

        return station;
    }

    public int conutStations(DBHelper helper) {

        Cursor cursor;
        SQLiteDatabase db = helper.getWritableDatabase();

        cursor = db.query("stations",
                new String[]{"countryTitle",
                        "districtTitle",
                        "cityTitle",
                        "regionTitle",
                        "stationTitle",
                        "stationId",
                        "cityId"
                },
                null,
                null,
                null,
                null,
                null);

        int count = cursor.getCount();

        cursor.close();
        helper.close();

        return count;
    }

    public long insertStation(SQLiteDatabase db, int type) {

        ContentValues station = new ContentValues();

        station.put("countryTitle", this.countryTitle);
        station.put("districtTitle", this.districtTitle);
        station.put("cityTitle", this.cityTitle);
        station.put("regionTitle", this.regionTitle);
        station.put("stationTitle", this.stationTitle);
        station.put("stationId", this.stationId);
        station.put("cityId", this.cityId);
        station.put("type", type);

        return db.insert("stations", null, station);
    }
}
