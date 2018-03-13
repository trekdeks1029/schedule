package com.domain.train.presentation.view.schedule;

import com.arellomobile.mvp.MvpView;
import com.arellomobile.mvp.viewstate.strategy.SingleStateStrategy;
import com.arellomobile.mvp.viewstate.strategy.SkipStrategy;
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType;

public interface ScheduleView extends MvpView {


    @StateStrategyType(SkipStrategy.class)
    void openCalendar();

    @StateStrategyType(SingleStateStrategy.class)
    void closeCalendar();

    @StateStrategyType(SingleStateStrategy.class)
    void changeDate(String date);

    @StateStrategyType(SingleStateStrategy.class)
    void changeDepartue(String departure);

    @StateStrategyType(SingleStateStrategy.class)
    void changeArrival(String arrival);

}
