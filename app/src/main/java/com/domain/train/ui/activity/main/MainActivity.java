package com.domain.train.ui.activity.main;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.activeandroid.ActiveAndroid;
import com.activeandroid.query.Select;
import com.arellomobile.mvp.MvpAppCompatActivity;
import com.domain.train.App;
import com.domain.train.R;
import com.domain.train.models.City;
import com.domain.train.models.Response;
import com.domain.train.models.Station;
import com.domain.train.presentation.view.main.MainView;
import com.domain.train.presentation.presenter.main.MainPresenter;

import com.arellomobile.mvp.MvpActivity;


import com.arellomobile.mvp.presenter.InjectPresenter;
import com.domain.train.ui.fragment.schedule.ScheduleFragment;
import com.domain.train.ui.fragment.settings.AboutFragment;
import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.LinkedList;
import java.util.concurrent.Callable;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

public class MainActivity extends MvpAppCompatActivity implements MainView {
    public static final String TAG = "MainActivity";
    @InjectPresenter
    MainPresenter mMainPresenter;


    @BindView(R.id.progressBar)
    ProgressBar progressBar;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.drawer_layout)
    DrawerLayout drawer;
    @BindView(R.id.nav_view)
    NavigationView navigationView;


    SharedPreferences sharedPreferences;


    public static Intent getIntent(final Context context) {
        Intent intent = new Intent(context, MainActivity.class);

        return intent;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        sharedPreferences = getSharedPreferences("APP", MODE_PRIVATE);
        if (savedInstanceState == null && new City().getCountAllCities() > 0)
            openFragment(1);
        createPage();
    }

    public void createPage() {
        setSupportActionBar(toolbar);
        createDrawable();
    }

    public void createDrawable() {

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                if (!mMainPresenter.isLoad()) {
                    item.setChecked(true);
                    Log.d(TAG, String.valueOf(item.getItemId()));
                    switch (item.getItemId()) {
                        case R.id.schedule:
                            openFragment(1);
                            break;
                        case R.id.about:
                            openFragment(2);
                            break;
                        default:
                            openFragment(1);
                            break;
                    }
                    drawer.closeDrawer(GravityCompat.START);
                } else
                    toast("Пожалуйста подождите, идет загрузка данных");

                return false;
            }
        });
    }

    public void openFragment(int position) {

        for (int i = 0; i < getSupportFragmentManager().getFragments().size(); i++) {
            Fragment fragment = getSupportFragmentManager().getFragments().get(i);
            getSupportFragmentManager().beginTransaction().hide(fragment).commit();
        }

        if (position == 1) {
            toolbar.setTitle(R.string.app_name);
            if (getSupportFragmentManager().findFragmentByTag("SCHEDULE") == null)
                getSupportFragmentManager().beginTransaction()
                        .add(R.id.fragment, new ScheduleFragment().newInstance(1), "SCHEDULE")
                        .commit();
            else {
                Fragment fragment = getSupportFragmentManager().findFragmentByTag("SCHEDULE");
                if (fragment != null)
                    getSupportFragmentManager().beginTransaction().show(fragment).commit();
            }
        } else if (position == 2) {
            toolbar.setTitle(R.string.about);
            if (getSupportFragmentManager().findFragmentByTag("ABOUT") == null)
                getSupportFragmentManager().beginTransaction()
                        .add(R.id.fragment, new AboutFragment().newInstance(), "ABOUT")
                        .commit();
            else {
                Fragment fragment = getSupportFragmentManager().findFragmentByTag("ABOUT");
                if (fragment != null)
                    getSupportFragmentManager().beginTransaction().show(fragment).commit();
            }
        }
    }

    public void toast(String text) {
        Toast.makeText(this, text, Toast.LENGTH_LONG).show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK)
            sharedPreferences.edit().putString("STATION", getGson().toJson(data.getSerializableExtra("STATION"))).apply();
        else
            super.onActivityResult(requestCode, resultCode, data);
    }


    @Override
    public void loadData() {
        Observable.fromCallable(new Callable<String>() {
            @Override
            public String call() throws Exception {
                return loadJSONFromAsset();
            }
        }).subscribeOn(Schedulers.io())
                .map(new Function<String, String>() {
                    @Override
                    public String apply(String s) throws Exception {
                        int count = 0;
                        Response response = getResponseFromJson(s);
                        count += saveDb(response.getCitiesFrom(), 1);
                        count += saveDb(response.getCitiesTo(), 2);
                        return "cities saved - " + count;
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<String>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(String s) {
                        Log.d("trekdeks", s);
                    }

                    @Override
                    public void onError(Throwable e) {
                        Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                        mMainPresenter.setLoad(false);
                        endProgress();
                    }

                    @Override
                    public void onComplete() {
                        mMainPresenter.setLoad(false);
                        endProgress();
                        openFragment(1);
                    }
                });
    }

    @Override
    public void startProgress() {
        progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void endProgress() {
        progressBar.setVisibility(View.GONE);
    }

    public Response getResponseFromJson(String json) {
        Response response;

        if (json != null) {
            //необходимо для сериализации данных если модель наследуется от ORM

            response = getGson().fromJson(json, Response.class);

            return response;
        }

        return null;

    }


    public int saveDb(LinkedList<City> cities, int type) {
        if (cities == null)
            return 0;
        //транзакция для сохранения в БД
        ActiveAndroid.beginTransaction();
        try {
            for (int b = 0; b < cities.size(); b++) {

                City city = cities.get(b);

                //по правилам ORM вначале сохраняем вложенные объекты
                LinkedList<Station> stations = city.getStations();
                for (int i = 0; i < stations.size(); i++) {
                    Station station = stations.get(i);
                    station.setType(type);
                    station.save();
                    stations.set(i, station);
                }
                city.setType(type);
                city.setStations(stations);
                city.save();
            }
            ActiveAndroid.setTransactionSuccessful();
        } finally {
            ActiveAndroid.endTransaction();
        }
        int count = new Select().from(City.class).where("id > 0").count();
        return count;
    }

    public String loadJSONFromAsset() {
        String json = null;
        try {
            InputStream is = getAssets().open("allStations.json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return json;
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

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }
}
