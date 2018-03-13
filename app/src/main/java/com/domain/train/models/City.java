package com.domain.train.models;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.activeandroid.query.Select;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by Ilya on 08.03.2018.
 */

@Table(name = "City")
public class City extends Model implements Serializable {


    @SerializedName("countryTitle")
    @Expose
    @Column(name = "countryTitle")
    String countryTitle;
    @SerializedName("point")
    @Expose
    @Column(name = "point")
    Map<String, Float> point;
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
    @SerializedName("cityId")
    @Expose
    @Column(name = "cityId")
    int cityId;
    @SerializedName("stations")
    @Expose
    @Column(name = "stations")
    LinkedList<Station> stations;
    @Expose
    @Column(name = "type")
    int type;

    public String getCountryTitle() {
        return countryTitle;
    }

    public void setCountryTitle(String countryTitle) {
        this.countryTitle = countryTitle;
    }

    public Map<String, Float> getPoint() {
        return point;
    }

    public void setPoint(Map<String, Float> point) {
        this.point = point;
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

    public int getCityId() {
        return cityId;
    }

    public void setCityId(int cityId) {
        this.cityId = cityId;
    }

    public LinkedList<Station> getStations() {
        return stations;
    }

    public void setStations(LinkedList<Station> stations) {
        this.stations = stations;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public List<City> getAllCities(){
        return new Select().from(City.class).execute();
    }

    public Observable<List<City>> getSearchCities(final String city){
        return Observable.fromCallable(new Callable<List<City>>() {
            @Override
            public List<City> call() throws Exception {
                return new Select().from(City.class).where("cityTitle = ?", city).execute();
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());

    }

    public int getCountAllCities(){
        return new Select().from(City.class).count();
    }
}
