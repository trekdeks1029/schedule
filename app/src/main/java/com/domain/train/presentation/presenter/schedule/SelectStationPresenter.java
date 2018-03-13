package com.domain.train.presentation.presenter.schedule;


import android.util.Log;

import com.domain.train.models.City;
import com.domain.train.models.Station;
import com.domain.train.presentation.view.schedule.SelectStationView;
import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;
import com.domain.train.ui.activity.schedule.SelectStationActivity;

import java.util.LinkedList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

@InjectViewState
public class SelectStationPresenter extends MvpPresenter<SelectStationView> {

    LinkedList<Station> stations = new LinkedList<>();

    int count = 50, offset = 0, type = 1;

    String station = null;

    boolean end = false;

    public SelectStationPresenter(int type){
        this.type = type;
    }

    @Override
    protected void onFirstViewAttach() {
        super.onFirstViewAttach();
        getCities(null);
    }

    public void newList() {
        end = false;
        stations.clear();
        offset = 0;
    }

    public void getCities(final String station) {
        if (end)
            return;
        this.station = station;

        Observable<List<Station>> observer;
        if (station == null)
            observer = new Station().getAllStations(count, offset, type);
        else
            observer = new Station().getSearchStations(station, count, offset, type);


        observer.subscribe(new Observer<List<Station>>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(List<Station> stationAr) {
                if (stationAr.size() == 0)
                    end = true;
                stations.addAll(stationAr);
            }

            @Override
            public void onError(Throwable e) {
                getViewState().toast(e.getMessage());
            }

            @Override
            public void onComplete() {
                getViewState().showStations(stations);
            }
        });
    }


    public Station getStation(int position) {
        if (position >= 0 && stations.size() > position)
            return stations.get(position);
        return null;
    }

    public void getNextPage() {
        offset = offset + count;
        getCities(station);
    }


}
