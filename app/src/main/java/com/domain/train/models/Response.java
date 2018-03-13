package com.domain.train.models;

import com.activeandroid.Model;
import com.activeandroid.annotation.Table;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.LinkedList;

/**
 * Created by Ilya on 09.03.2018.
 */

public class Response implements Serializable{


    LinkedList<City> citiesFrom;

    LinkedList<City> citiesTo;


    public LinkedList<City> getCitiesFrom() {
        return citiesFrom;
    }

    public void setCitiesFrom(LinkedList<City> citiesFrom) {
        this.citiesFrom = citiesFrom;
    }

    public LinkedList<City> getCitiesTo() {
        return citiesTo;
    }

    public void setCitiesTo(LinkedList<City> citiesTo) {
        this.citiesTo = citiesTo;
    }
}
