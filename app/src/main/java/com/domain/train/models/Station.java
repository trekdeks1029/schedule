package com.domain.train.models;

import android.util.Log;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.activeandroid.query.Select;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by Ilya on 08.03.2018.
 */

@Table(name = "Station")
public class Station extends Model implements Serializable {

    @SerializedName("countryTitle")
    @Expose
    @Column(name = "countryTitle")
    String countryTitle;
    @SerializedName("districtTitle")
    @Expose
    @Column(name = "districtTitle")
    String districtTitle;
    @SerializedName("cityTitle")
    @Expose
    @Column(name = "cityTitle")
    String cityTitle;
    @SerializedName("regionTitle")
    @Expose
    @Column(name = "regionTitle")
    String regionTitle;
    @SerializedName("stationTitle")
    @Expose
    @Column(name = "stationTitle")
    String stationTitle;
    @SerializedName("stationId")
    @Expose
    @Column(name = "stationId")
    int stationId;
    @SerializedName("point")
    @Expose
    @Column(name = "point")
    Map<String, Float> point;
    @Expose
    @Column(name = "type")
    int type;


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

    public Observable<List<Station>> getAllStations(final int count, final int offset, final int type) {

        return Observable.fromCallable(new Callable<List<Station>>() {
            @Override
            public List<Station> call() throws Exception {
                return new Select().from(Station.class)
                        .where("type = ?", type)
                        .orderBy("cityTitle ASC")
                        .limit(count)
                        .offset(offset)
                        .execute();
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());

    }

    public Observable<List<Station>> getSearchStations(final String stationTitle, final int count, final int offset, final int type) {

        return Observable.fromCallable(new Callable<List<Station>>() {
            @Override
            public List<Station> call() throws Exception {
                return new Select().from(Station.class)
                        .where("stationTitle LIKE ?", "%" + stationTitle.trim() + "%")
                        .where("type = ?", type)
                        .orderBy("cityTitle ASC")
                        .limit(count)
                        .offset(offset)
                        .execute();
            }
        })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public int getCountAllStations() {
        return new Select().from(Station.class).count();
    }
}
