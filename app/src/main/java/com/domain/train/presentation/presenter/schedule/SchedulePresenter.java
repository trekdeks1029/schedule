package com.domain.train.presentation.presenter.schedule;


import com.domain.train.models.Station;
import com.domain.train.presentation.view.schedule.ScheduleView;
import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;

@InjectViewState
public class SchedulePresenter extends MvpPresenter<ScheduleView> {

    Station departure, arrival;


    public void openCalendar() {
        getViewState().openCalendar();
    }

    public void setDate(String date) {
        getViewState().changeDate(date);
    }

    public void setDeparture(Station station) {
        departure = station;
        getViewState().changeDepartue(station.getCityTitle() + ", " + station.getStationTitle());
    }

    public void setArrival(Station station) {
        arrival = station;
        getViewState().changeArrival(station.getCityTitle() + ", " + station.getStationTitle());
    }

    public void closeCalendar(){
        getViewState().closeCalendar();
    }
}
