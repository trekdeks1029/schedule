package com.domain.train.presentation.presenter.main;


import com.domain.train.models.City;
import com.domain.train.presentation.view.main.MainView;
import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;

@InjectViewState
public class MainPresenter extends MvpPresenter<MainView> {
    boolean isLoad = false;


    @Override
    protected void onFirstViewAttach() {
        super.onFirstViewAttach();

        if (new City().getCountAllCities() == 0){
            load();
        }

    }

    public void load(){
        setLoad(true);
        getViewState().startProgress();
        getViewState().loadData();
    }

    public boolean isLoad(){
        return isLoad;
    }

    public void setLoad(boolean isLoad){
        this.isLoad = isLoad;
    }
}
