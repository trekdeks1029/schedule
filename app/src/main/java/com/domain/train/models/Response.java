package com.domain.train.models;


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
