package com.domain.train.models;



import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;


/**
 * Created by Ilya on 08.03.2018.
 */

public class City implements Serializable {



    String countryTitle;

    Map<String, Float> point;

    String districtTitle;

    String cityTitle;

    String regionTitle;

    int cityId;
    LinkedList<Station> stations;

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
}
