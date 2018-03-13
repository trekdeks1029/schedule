package com.domain.train.presentation.view.schedule;

import com.arellomobile.mvp.MvpView;
import com.domain.train.models.City;
import com.domain.train.models.Station;

import java.util.LinkedList;

public interface SelectStationView extends MvpView {

    void startProgress();

    void endProgress();

    void showStations(LinkedList<Station> stations);

    void toast(String text);


}
