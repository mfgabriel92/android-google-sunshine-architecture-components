package com.example.android.sunshine.data.network;

import android.support.annotation.NonNull;

import com.example.android.sunshine.data.database.WeatherEntry;

public class WeatherResponse {

    @NonNull
    private final WeatherEntry[] mWeatherForecast;

    public WeatherResponse(@NonNull final WeatherEntry[] weatherForecast) {
        mWeatherForecast = weatherForecast;
    }

    public WeatherEntry[] getWeatherForecast() {
        return mWeatherForecast;
    }
}
