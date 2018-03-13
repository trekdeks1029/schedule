package com.domain.train.ui.fragment.schedule;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.arellomobile.mvp.MvpAppCompatFragment;
import com.domain.train.R;
import com.domain.train.models.Station;
import com.domain.train.presentation.view.schedule.ScheduleView;
import com.domain.train.presentation.presenter.schedule.SchedulePresenter;

import com.arellomobile.mvp.MvpFragment;
import com.arellomobile.mvp.presenter.InjectPresenter;
import com.domain.train.ui.activity.schedule.SelectStationActivity;
import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import butterknife.BindColor;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static android.content.Context.MODE_PRIVATE;

public class ScheduleFragment extends MvpAppCompatFragment implements ScheduleView {
    public static final String TAG = "ScheduleFragment";
    @InjectPresenter
    SchedulePresenter mSchedulePresenter;

    MaterialDialog dialog;

    SharedPreferences sharedPreferences;


    @BindColor(R.color.colorPrimary)
    int primaryColor;
    @BindColor(R.color.grey)
    int greyColor;

    @BindView(R.id.departure)
    EditText departureEditText;
    @BindView(R.id.arrival)
    EditText arrivalEditText;
    @BindView(R.id.date)
    EditText dateEditText;


    public static ScheduleFragment newInstance(int type) {
        ScheduleFragment fragment = new ScheduleFragment();

        Bundle args = new Bundle();
        args.putInt("TYPE", type);
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container,
                             final Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_schedule, container, false);
    }

    @Override
    public void onViewCreated(final View view, final Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
        sharedPreferences = getActivity().getSharedPreferences("APP", MODE_PRIVATE);
        createPage();
    }


    public void createPage() {
        dateEditText.setRawInputType(InputType.TYPE_CLASS_TEXT);
        dateEditText.setTextIsSelectable(true);
        departureEditText.setRawInputType(InputType.TYPE_CLASS_TEXT);
        departureEditText.setTextIsSelectable(true);
        arrivalEditText.setRawInputType(InputType.TYPE_CLASS_TEXT);
        arrivalEditText.setTextIsSelectable(true);

        dateEditText.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (MotionEvent.ACTION_UP == event.getAction())
                    mSchedulePresenter.openCalendar();
                return false;
            }
        });
        departureEditText.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (MotionEvent.ACTION_UP == event.getAction()) {
                    startActivityForResult(SelectStationActivity.getIntent(getActivity(), 1), 91);
                }
                return false;
            }
        });
        arrivalEditText.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (MotionEvent.ACTION_UP == event.getAction()) {
                    startActivityForResult(SelectStationActivity.getIntent(getActivity(), 2), 91);
                }
                return false;
            }
        });
    }

    @Override
    public void openCalendar() {
        MaterialDialog.Builder builder = new MaterialDialog.Builder(getActivity())
                .title(R.string.departureDateSelect)
                .customView(R.layout.calendar_dialog, false)
                .cancelable(true)
                .positiveText(R.string.select)
                .positiveColor(primaryColor)
                .negativeText(R.string.back)
                .negativeColor(greyColor)
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        DatePicker datePicker = (DatePicker) dialog.findViewById(R.id.datePicker);
                        Date date = getDateFromDatePicker(datePicker);
                        mSchedulePresenter.setDate(new SimpleDateFormat("dd/MM/yy").format(date.getTime()));
                        mSchedulePresenter.closeCalendar();
                    }
                })
                .onNegative(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        dialog.hide();
                    }
                });

        dialog = builder.build();
        dialog.show();
    }

    @Override
    public void closeCalendar() {
        if (dialog != null)
            dialog.hide();
    }

    public static java.util.Date getDateFromDatePicker(DatePicker datePicker) {
        int day = datePicker.getDayOfMonth();
        int month = datePicker.getMonth();
        int year = datePicker.getYear();

        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month, day);

        return calendar.getTime();
    }

    @Override
    public void changeDate(String date) {
        dateEditText.setText(date);
    }

    @Override
    public void changeDepartue(String departure) {
        this.departureEditText.setText(departure);
    }

    @Override
    public void changeArrival(String arrival) {
        arrivalEditText.setText(arrival);
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (!sharedPreferences.getString("STATION", "").isEmpty()) {
            Station station = getGson().fromJson(sharedPreferences.getString("STATION", ""), Station.class);

            Log.d("trekdeks", sharedPreferences.getString("STATION", ""));

            if (station.getType() == 1) {
                mSchedulePresenter.setDeparture(station);
            } else if (station.getType() == 2) {
                mSchedulePresenter.setArrival(station);
            }

            sharedPreferences.edit().remove("STATION").apply();
        }
    }

    public Gson getGson() {
        ExclusionStrategy exclusionStrategy = new ExclusionStrategy() {

            @Override
            public boolean shouldSkipField(FieldAttributes fieldAttributes) {
                return false;
            }

            @Override
            public boolean shouldSkipClass(Class<?> clazz) {
                return clazz == Field.class || clazz == Method.class;
            }
        };
        Gson gson = new GsonBuilder()
                .excludeFieldsWithModifiers(Modifier.FINAL, Modifier.TRANSIENT, Modifier.STATIC)
                .addSerializationExclusionStrategy(exclusionStrategy)
                .addDeserializationExclusionStrategy(exclusionStrategy)
                .create();
        return gson;
    }
}
