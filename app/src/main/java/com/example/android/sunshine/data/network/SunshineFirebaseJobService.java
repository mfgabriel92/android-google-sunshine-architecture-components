package com.example.android.sunshine.data.network;

import com.example.android.sunshine.utils.InjectorUtils;
import com.firebase.jobdispatcher.JobParameters;
import com.firebase.jobdispatcher.JobService;

public class SunshineFirebaseJobService extends JobService {
    private static final String LOG_TAG = SunshineFirebaseJobService.class.getSimpleName();

    @Override
    public boolean onStartJob(final JobParameters jobParameters) {
        WeatherNetworkDataSource networkDataSource = InjectorUtils.provideNetworkDataSource(this.getApplicationContext());
        networkDataSource.fetchWeather();

        jobFinished(jobParameters, false);

        return true;
    }

    @Override
    public boolean onStopJob(JobParameters jobParameters) {
        return true;
    }
}