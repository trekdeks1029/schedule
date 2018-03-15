package com.domain.train.ui.activity.schedule;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.arellomobile.mvp.MvpAppCompatActivity;
import com.arellomobile.mvp.presenter.ProvidePresenter;
import com.domain.train.R;
import com.domain.train.adapters.StationAdapter;
import com.domain.train.models.City;
import com.domain.train.models.Station;
import com.domain.train.presentation.view.schedule.SelectStationView;
import com.domain.train.presentation.presenter.schedule.SelectStationPresenter;

import com.arellomobile.mvp.MvpActivity;


import com.arellomobile.mvp.presenter.InjectPresenter;
import com.domain.train.ui.activity.main.MainActivity;
import com.mancj.materialsearchbar.MaterialSearchBar;

import java.util.LinkedList;

import butterknife.BindColor;
import butterknife.BindView;
import butterknife.ButterKnife;

public class SelectStationActivity extends MvpAppCompatActivity implements SelectStationView, StationAdapter.OnClickListenerDetail {
    public static final String TAG = "SelectStationActivity";
    @InjectPresenter
    SelectStationPresenter mSelectStationPresenter;


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


    @ProvidePresenter
    SelectStationPresenter getmSelectStationPresenter() {
        return new SelectStationPresenter(getIntent().getIntExtra("TYPE", 1));
    }


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
        createPage();
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
        if (mSelectStationPresenter.getStation(position) != null) {
            Intent intent = new Intent(this, MainActivity.class);
            intent.putExtra("STATION", mSelectStationPresenter.getStation(position));
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

    @Override
    public void startProgress() {
        progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void endProgress() {
        progressBar.setVisibility(View.GONE);
    }

    @Override
    public void showStations(LinkedList<Station> stations) {
        mStationAdapter.setData(stations);
        mStationAdapter.reload();
        isRunning = false;
    }

    @Override
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
                mSelectStationPresenter.newList();
                mStationAdapter.reload();
                mSelectStationPresenter.getCities(query.toString());
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        return true;
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
                            mSelectStationPresenter.getNextPage();
                        }
                    });
                }

            }
        }
    };
}
