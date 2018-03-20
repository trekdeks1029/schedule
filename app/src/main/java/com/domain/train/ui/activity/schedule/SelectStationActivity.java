package com.domain.train.ui.activity.schedule;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.domain.train.R;
import com.domain.train.adapters.StationAdapter;
import com.domain.train.models.Station;



import com.domain.train.ui.activity.main.MainActivity;
import com.domain.train.utils.DBHelper;

import java.util.LinkedList;

import butterknife.BindColor;
import butterknife.BindView;
import butterknife.ButterKnife;

public class SelectStationActivity extends AppCompatActivity implements StationAdapter.OnClickListenerDetail {
    public static final String TAG = "SelectStationActivity";

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.rvStation)
    RecyclerView rvStation;
    @BindView(R.id.progressBar)
    ProgressBar progressBar;

    @BindColor(R.color.colorPrimary)
    int primaryColor;
    @BindColor(R.color.grey)
    int greyColor;
    @BindColor(R.color.white)
    int whiteColor;


    StationAdapter mStationAdapter;

    boolean isRunning = false;

    LinkedList<Station> stations = new LinkedList<>();

    int count = 50, offset = 0, type = 1;

    String station = null;

    boolean end = false;


    public static Intent getIntent(final Context context, int type) {
        Intent intent = new Intent(context, SelectStationActivity.class);
        intent.putExtra("TYPE", type);
        return intent;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_station);
        ButterKnife.bind(this);
        type = getIntent().getIntExtra("TYPE", 1);
        createPage();
        getCities(null);
    }

    public void createPage() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        mStationAdapter = new StationAdapter(this);
        rvStation.setLayoutManager(layoutManager);
        rvStation.setAdapter(mStationAdapter);
        rvStation.addOnScrollListener(onScroll);

        /*closeKeyBoard();
        mSelectStationPresenter.newList();
        mStationAdapter.reload();
        mSelectStationPresenter.getCities(text.toString());*/

    }

    public void closeKeyBoard() {
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    @Override
    public void onClickSelect(View v, int position) {
        if (getStation(position) != null) {
            Intent intent = new Intent(this, MainActivity.class);
            intent.putExtra("STATION", getStation(position).getStationId());
            intent.putExtra("TYPE", type);
            setResult(RESULT_OK, intent);
            finish();

            return;
        }
    }

    @Override
    public void onBackPressed() {
        setResult(RESULT_CANCELED);
        finish();
    }


    public void startProgress() {
        progressBar.setVisibility(View.VISIBLE);
    }


    public void endProgress() {
        progressBar.setVisibility(View.GONE);
    }


    public void showStations(LinkedList<Station> stations) {
        mStationAdapter.setData(stations);
        mStationAdapter.reload();
        isRunning = false;
    }


    public void toast(String text) {
        Toast.makeText(this, text, Toast.LENGTH_LONG).show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.search_menu, menu);

        MenuItem myActionMenuItem = menu.findItem(R.id.action_search);
        final SearchView searchView = (SearchView) myActionMenuItem.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                closeKeyBoard();
                newList();
                mStationAdapter.reload();
                getCities(query.toString());
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        return true;
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

        Log.d("trekdeks", "getCities - "  + String.valueOf(type));

        stations.addAll(new Station().getAllStations(new DBHelper(this), station, count, offset, type));

        showStations(stations);

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


    private RecyclerView.OnScrollListener onScroll = new RecyclerView.OnScrollListener() {
        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);
            {
                LinearLayoutManager layoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();

                int visibleItemCount = layoutManager.getChildCount();
                int totalItemCount = layoutManager.getItemCount();
                int pastVisiblesItems = layoutManager.findFirstVisibleItemPosition();

                if ((visibleItemCount + pastVisiblesItems) >= totalItemCount - 10 && !isRunning) {
                    isRunning = true;
                    rvStation.post(new Runnable() {
                        public void run() {
                            getNextPage();
                        }
                    });
                }

            }
        }
    };
}
