package com.example.android.sunshine.ui.main;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.preference.PreferenceManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.android.sunshine.R;
import com.example.android.sunshine.ui.settings.SettingsActivity;
import com.example.android.sunshine.data.SunshinePreferences;
import com.example.android.sunshine.ui.detail.DetailActivity;
import com.example.android.sunshine.utils.InjectorUtils;

import java.util.Date;

public class MainActivity
    extends AppCompatActivity
    implements  MainActivityAdapter.MainActivityAdapterOnClickHandler,
                SharedPreferences.OnSharedPreferenceChangeListener {

    private int mPosition = RecyclerView.NO_POSITION;
    private ProgressBar mPbLoading;
    private RecyclerView mRvMainActivity;
    private MainActivityAdapter mMainActivityAdapter;
    private MainActivityViewModel mViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mPbLoading = findViewById(R.id.pbLoading);
        mRvMainActivity = findViewById(R.id.rvMainActivity);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        mRvMainActivity.setLayoutManager(layoutManager);
        mRvMainActivity.setHasFixedSize(true);
        mMainActivityAdapter = new MainActivityAdapter(this, this);
        mRvMainActivity.setAdapter(mMainActivityAdapter);

        PreferenceManager.getDefaultSharedPreferences(this).registerOnSharedPreferenceChangeListener(this);

        loadWeatherData();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        PreferenceManager.getDefaultSharedPreferences(this).unregisterOnSharedPreferenceChangeListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.forecast, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.actionRefresh:
                loadWeatherData();
                return true;
            case R.id.actionMap:
                openLocationInMap();
                return true;
            case R.id.actionSettings:
                startActivity(new Intent(this, SettingsActivity.class));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }

    @Override
    public void onItemClick(Date date) {
        Intent weatherDetailIntent = new Intent(MainActivity.this, DetailActivity.class);
        long timestamp = date.getTime();
        weatherDetailIntent.putExtra(DetailActivity.WEATHER_ID_EXTRA, timestamp);

        startActivity(weatherDetailIntent);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
    }

    private void loadWeatherData() {
        MainViewModelFactory mFactory = InjectorUtils.provideMainActivityViewModelFactory(this.getApplicationContext());
        mViewModel = ViewModelProviders.of(this, mFactory).get(MainActivityViewModel.class);
        mViewModel.getForecast().observe(this, weatherEntries -> {
            mMainActivityAdapter.swapForecast(weatherEntries);

            if (mPosition == RecyclerView.NO_POSITION) {
                mPosition = 0;
            }

            mRvMainActivity.smoothScrollToPosition(mPosition);

            if (weatherEntries != null && weatherEntries.size() != 0) {
                mPbLoading.setVisibility(View.INVISIBLE);
                mRvMainActivity.setVisibility(View.VISIBLE);
            } else {
                mPbLoading.setVisibility(View.VISIBLE);
                mRvMainActivity.setVisibility(View.INVISIBLE);
            }
        });
    }

    private void openLocationInMap() {
        String location = SunshinePreferences.getPreferredWeatherLocation(this);
        Uri geoLocation = Uri.parse("geo:0,0?q=" + location);

        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(geoLocation);

        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        } else {
            Toast.makeText(this, getString(R.string.implicit_intent_app_not_found), Toast.LENGTH_LONG).show();
        }
    }
}
