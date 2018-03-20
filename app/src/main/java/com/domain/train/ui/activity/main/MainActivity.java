package com.domain.train.ui.activity.main;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.domain.train.R;
import com.domain.train.models.City;
import com.domain.train.models.Response;
import com.domain.train.models.Station;


import com.domain.train.ui.fragment.schedule.ScheduleFragment;
import com.domain.train.ui.fragment.settings.AboutFragment;
import com.domain.train.utils.DBHelper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.LinkedList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {
    public static final String TAG = "MainActivity";


    @BindView(R.id.progressBar)
    ProgressBar progressBar;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.drawer_layout)
    DrawerLayout drawer;
    @BindView(R.id.nav_view)
    NavigationView navigationView;


    SharedPreferences sharedPreferences;

    Context context;

    boolean isLoad = false;

    AsyncTask task;


    public static Intent getIntent(final Context context) {
        Intent intent = new Intent(context, MainActivity.class);

        return intent;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        context = this;
        ButterKnife.bind(this);
        sharedPreferences = getSharedPreferences("APP", MODE_PRIVATE);
        if (new Station().conutStations(new DBHelper(this)) > 0) {
            if (savedInstanceState == null)
                openFragment(1);

        } else {
            load();
        }
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

                if (!isLoad()) {
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
        if (resultCode == RESULT_OK) {
            sharedPreferences.edit().putInt("STATION", data.getIntExtra("STATION", 0)).apply();
            sharedPreferences.edit().putInt("TYPE", data.getIntExtra("TYPE", 0)).apply();
        } else
            super.onActivityResult(requestCode, resultCode, data);
    }


    public void loadData() {
        task = new Task().execute();
    }


    public void startProgress() {
        progressBar.setVisibility(View.VISIBLE);
    }


    public void endProgress() {
        progressBar.setVisibility(View.GONE);
    }

    public Response getResponseFromJson(String json) {
        Response response = new Response();

        if (json != null) {
            //response = getGson().fromJson(json, Response.class);
            JSONObject jsonObject = null;
            try {
                jsonObject = new JSONObject(json);
                response.setCitiesFrom(getIterationJSON(jsonObject.getJSONArray("citiesFrom")));
                response.setCitiesTo(getIterationJSON(jsonObject.getJSONArray("citiesTo")));
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return response;
        }
        return null;
    }

    public LinkedList<City> getIterationJSON(JSONArray array) {
        try {
            LinkedList<City> cities = new LinkedList<>();

            for (int i = 0; i < array.length(); i++) {
                JSONObject cityJson = (JSONObject) array.get(i);

                City city = new City();
                LinkedList<Station> stations = new LinkedList<>();

                city.setType(1);
                city.setCityId(cityJson.getInt("cityId"));
                city.setCityTitle(cityJson.getString("cityTitle"));
                city.setCountryTitle(cityJson.getString("countryTitle"));
                city.setDistrictTitle(cityJson.getString("districtTitle"));
                city.setRegionTitle(cityJson.getString("regionTitle"));

                for (int b = 0; b < cityJson.getJSONArray("stations").length(); b++) {
                    Station station = new Station();
                    JSONObject stationJson = (JSONObject) cityJson.getJSONArray("stations").get(b);

                    station.setType(1);
                    station.setCityId(stationJson.getInt("cityId"));
                    station.setCityTitle(stationJson.getString("cityTitle"));
                    station.setCountryTitle(stationJson.getString("countryTitle"));
                    station.setDistrictTitle(stationJson.getString("districtTitle"));
                    station.setRegionTitle(stationJson.getString("regionTitle"));
                    station.setStationId(stationJson.getInt("stationId"));
                    station.setStationTitle(stationJson.getString("stationTitle"));

                    stations.add(station);
                }

                city.setStations(stations);

                cities.add(city);
            }

            return cities;

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    public int saveDb(LinkedList<City> cities, int type) {
        if (cities == null)
            return 0;

        DBHelper helper = new DBHelper(this);
        SQLiteDatabase db = helper.getWritableDatabase();
        db.beginTransaction();
        try {
            for (int b = 0; b < cities.size(); b++) {
                City city = cities.get(b);

                LinkedList<Station> stations = city.getStations();
                for (int i = 0; i < stations.size(); i++) {
                    Station station = stations.get(i);
                    station.setType(type);

                    station.insertStation(db, type);
                    stations.set(i, station);
                }
                city.setType(type);
                city.setStations(stations);
            }

            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
        }
        return 1;
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

    public void load() {
        setLoad(true);
        startProgress();
        loadData();
    }

    public boolean isLoad() {
        return isLoad;
    }

    public void setLoad(boolean isLoad) {
        this.isLoad = isLoad;
    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }


    class Task extends AsyncTask<Void, Void, Integer> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            startProgress();
        }

        @Override
        protected Integer doInBackground(Void... params) {
            int count = 0;
            String json = loadJSONFromAsset();
            Response response = getResponseFromJson(json);
            if (response != null) {
                count += saveDb(response.getCitiesFrom(), 1);
                count += saveDb(response.getCitiesTo(), 2);
            }
            return count;
        }

        @Override
        protected void onPostExecute(Integer result) {
            super.onPostExecute(result);

            if (result > 0) {
                setLoad(false);
                endProgress();
                openFragment(1);
            } else {
                Toast.makeText(MainActivity.this, "Ошибка сохранения станций", Toast.LENGTH_LONG).show();
                setLoad(false);
                endProgress();
            }


        }
    }

    @Override
    protected void onPause() {
        if (task != null)
            task.cancel(true);
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
