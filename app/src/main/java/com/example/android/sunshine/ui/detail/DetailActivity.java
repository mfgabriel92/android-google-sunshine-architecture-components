package com.example.android.sunshine.ui.detail;

import android.arch.lifecycle.ViewModelProviders;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.example.android.sunshine.R;
import com.example.android.sunshine.data.database.WeatherEntry;
import com.example.android.sunshine.databinding.ActivityDetailBinding;
import com.example.android.sunshine.utils.InjectorUtils;
import com.example.android.sunshine.utils.SunshineDateUtils;
import com.example.android.sunshine.utils.SunshineWeatherUtils;

import java.util.Date;

public class DetailActivity extends AppCompatActivity {

    public static final String WEATHER_ID_EXTRA = "WEATHER_ID_EXTRA";
    public static final int DEFAULT_WEATHER_ID_EXTRA = -1;
    private ActivityDetailBinding mDetailBinding;
    private DetailActivityViewModel mViewModel;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mDetailBinding = DataBindingUtil.setContentView(this, R.layout.activity_detail);
        long timestamp = getIntent().getLongExtra(WEATHER_ID_EXTRA, DEFAULT_WEATHER_ID_EXTRA);
        Date date = new Date(timestamp);

        DetailViewModelFactory mFactory = InjectorUtils.provideDetailViewModelFactory(this.getApplicationContext(), date);
        mViewModel = ViewModelProviders.of(this, mFactory).get(DetailActivityViewModel.class);
        mViewModel.getWeather().observe(this, weatherEntry -> {
            if (weatherEntry != null) {
                bindWeatherToUI(weatherEntry);
            }
        });
    }

    private void bindWeatherToUI(WeatherEntry weatherEntry) {
        int weatherId = weatherEntry.getWeatherIconId();
        int weatherImageId = SunshineWeatherUtils.getLargeArtResourceIdForWeatherCondition(weatherId);

        mDetailBinding.primaryInfo.weatherIcon.setImageResource(weatherImageId);

        long localDateMidnightGmt = weatherEntry.getDate().getTime();
        String dateText = SunshineDateUtils.getFriendlyDateString(DetailActivity.this, localDateMidnightGmt, true);
        mDetailBinding.primaryInfo.date.setText(dateText);

        String description = SunshineWeatherUtils.getStringForWeatherCondition(DetailActivity.this, weatherId);
        String descriptionA11y = getString(R.string.a11y_forecast, description);
        mDetailBinding.primaryInfo.weatherDescription.setText(description);
        mDetailBinding.primaryInfo.weatherDescription.setContentDescription(descriptionA11y);
        mDetailBinding.primaryInfo.weatherIcon.setContentDescription(descriptionA11y);

        double maxInCelsius = weatherEntry.getMax();
        String highString = SunshineWeatherUtils.formatTemperature(DetailActivity.this, maxInCelsius);
        String highA11y = getString(R.string.a11y_high_temp, highString);
        mDetailBinding.primaryInfo.highTemperature.setText(highString);
        mDetailBinding.primaryInfo.highTemperature.setContentDescription(highA11y);

        double minInCelsius = weatherEntry.getMin();
        String lowString = SunshineWeatherUtils.formatTemperature(DetailActivity.this, minInCelsius);
        String lowA11y = getString(R.string.a11y_low_temp, lowString);
        mDetailBinding.primaryInfo.lowTemperature.setText(lowString);
        mDetailBinding.primaryInfo.lowTemperature.setContentDescription(lowA11y);

        double humidity = weatherEntry.getHumidity();
        String humidityString = getString(R.string.format_humidity, humidity);
        String humidityA11y = getString(R.string.a11y_humidity, humidityString);
        mDetailBinding.extraDetails.humidity.setText(humidityString);
        mDetailBinding.extraDetails.humidity.setContentDescription(humidityA11y);
        mDetailBinding.extraDetails.humidityLabel.setContentDescription(humidityA11y);

        double windSpeed = weatherEntry.getWind();
        double windDirection = weatherEntry.getDegrees();
        String windString = SunshineWeatherUtils.getFormattedWind(DetailActivity.this, windSpeed, windDirection);
        String windA11y = getString(R.string.a11y_wind, windString);
        mDetailBinding.extraDetails.windMeasurement.setText(windString);
        mDetailBinding.extraDetails.windMeasurement.setContentDescription(windA11y);
        mDetailBinding.extraDetails.windLabel.setContentDescription(windA11y);

        double pressure = weatherEntry.getPressure();
        String pressureString = getString(R.string.format_pressure, pressure);
        String pressureA11y = getString(R.string.a11y_pressure, pressureString);
        mDetailBinding.extraDetails.pressure.setText(pressureString);
        mDetailBinding.extraDetails.pressure.setContentDescription(pressureA11y);
        mDetailBinding.extraDetails.pressureLabel.setContentDescription(pressureA11y);
    }
}
