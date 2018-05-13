package com.example.android.sunshine.data.network;

import android.app.IntentService;
import android.content.Intent;

import com.example.android.sunshine.utils.InjectorUtils;

public class SunshineSyncIntentService extends IntentService {
    public SunshineSyncIntentService() {
        super(SunshineSyncIntentService.class.getSimpleName());
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        WeatherNetworkDataSource networkDataSource = InjectorUtils.provideNetworkDataSource(this.getApplicationContext());
        networkDataSource.fetchWeather();
    }
}

