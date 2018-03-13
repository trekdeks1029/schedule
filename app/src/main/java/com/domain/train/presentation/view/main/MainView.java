package com.domain.train.presentation.view.main;

import com.arellomobile.mvp.MvpView;
import com.arellomobile.mvp.viewstate.strategy.SkipStrategy;
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType;

public interface MainView extends MvpView {


    @StateStrategyType(SkipStrategy.class)
    void loadData();

    @StateStrategyType(SkipStrategy.class)
    void startProgress();

    @StateStrategyType(SkipStrategy.class)
    void endProgress();

}
