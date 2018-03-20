package com.domain.train.ui.fragment.schedule;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;

import com.domain.train.R;
import com.domain.train.models.Station;

import com.domain.train.ui.activity.schedule.SelectStationActivity;
import com.domain.train.utils.DBHelper;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import butterknife.BindColor;
import butterknife.BindView;
import butterknife.ButterKnife;

import static android.content.Context.MODE_PRIVATE;

public class ScheduleFragment extends Fragment{
    public static final String TAG = "ScheduleFragment";

    AlertDialog dialog;

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




    Station departure, arrival;



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
                    openCalendar();
                return false;
            }
        });
        departureEditText.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (MotionEvent.ACTION_UP == event.getAction()) {
                    departureEditText.setFocusable(true);
                    startActivityForResult(SelectStationActivity.getIntent(getActivity(), 1), 91);
                }
                return false;
            }
        });
        arrivalEditText.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (MotionEvent.ACTION_UP == event.getAction()) {
                    arrivalEditText.setFocusable(true);
                    startActivityForResult(SelectStationActivity.getIntent(getActivity(), 2), 91);
                }
                return false;
            }
        });


        final View calendarView = getLayoutInflater().inflate(R.layout.calendar_dialog, null);

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(R.string.departureDateSelect)
                .setCancelable(false)
                .setView(calendarView)
                .setPositiveButton(R.string.select, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        DatePicker datePicker = (DatePicker) calendarView.findViewById(R.id.datePicker);
                        Date date = getDateFromDatePicker(datePicker);
                        setDate(new SimpleDateFormat("dd/MM/yy").format(date.getTime()));
                        closeCalendar();
                    }
                })
                .setNegativeButton(R.string.back, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
        dialog = builder.create();
    }


    public void openCalendar() {
        dialog.show();
    }


    public void closeCalendar() {
        if (dialog != null)
            dialog.cancel();
    }

    public static java.util.Date getDateFromDatePicker(DatePicker datePicker) {
        int day = datePicker.getDayOfMonth();
        int month = datePicker.getMonth();
        int year = datePicker.getYear();

        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month, day);

        return calendar.getTime();
    }


    public void changeDate(String date) {
        dateEditText.setText(date);
    }


    public void changeDepartue(String departure) {
        this.departureEditText.setText(departure);
    }


    public void changeArrival(String arrival) {
        arrivalEditText.setText(arrival);
    }


    public void setDate(String date) {
        changeDate(date);
    }

    public void setDeparture(Station station) {
        departure = station;
        changeDepartue(station.getCityTitle() + ", " + station.getStationTitle());
    }

    public void setArrival(Station station) {
        arrival = station;
        changeArrival(station.getCityTitle() + ", " + station.getStationTitle());
    }


    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (sharedPreferences.getInt("STATION", 0) != 0) {
            Station station = new Station().selectStationId(sharedPreferences.getInt("STATION", 0), new DBHelper(getActivity()));

            if (sharedPreferences.getInt("TYPE", 0) == 1) {
                setDeparture(station);
            } else if (sharedPreferences.getInt("TYPE", 0) == 2) {
                setArrival(station);
            }

            sharedPreferences.edit().remove("STATION").apply();
            sharedPreferences.edit().remove("TYPE").apply();
        }
    }
}
