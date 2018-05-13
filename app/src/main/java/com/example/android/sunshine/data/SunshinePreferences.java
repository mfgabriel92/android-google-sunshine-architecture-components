package com.example.android.sunshine.data;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.example.android.sunshine.R;

public class SunshinePreferences {

    public static final String PREF_CITY_NAME = "city_name";
    public static final String PREF_COORD_LAT = "coord_lat";
    public static final String PREF_COORD_LONG = "coord_long";
    private static final String DEFAULT_WEATHER_LOCATION = "94043,USA";
    private static final double[] DEFAULT_WEATHER_COORDINATES = {37.4284, 122.0724};
    private static final String DEFAULT_MAP_LOCATION = "1600 Amphitheatre Parkway, Mountain View, CA 94043";

    static public void setLocationDetails(Context context, String cityName, double lat, double lon) {
    }

    static public void setLocation(Context context, String locationSetting, double lat, double lon) {
    }

    static public void resetLocationCoordinates(Context context) {
    }

    public static String getPreferredWeatherLocation(Context context) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);

        String keyForLocation = context.getString(R.string.pref_key_location);
        String defaultLocation = context.getString(R.string.pref_default_location);

        return sharedPreferences.getString(keyForLocation, defaultLocation);
    }

    public static boolean isLocationLatLonAvailable(Context context) {
        return false;
    }

    public static boolean isMetric(Context context) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);

        String keyUnits = context.getString(R.string.pref_key_unit);
        String units = context.getString(R.string.pref_default_unit);
        String preferredUnits = sharedPreferences.getString(keyUnits, units);

        return units.equals(preferredUnits);
    }

    public static double[] getLocationCoordinates(Context context) {
        return getDefaultWeatherCoordinates();
    }

    private static String getDefaultWeatherLocation() {
        return DEFAULT_WEATHER_LOCATION;
    }

    private static double[] getDefaultWeatherCoordinates() {
        return DEFAULT_WEATHER_COORDINATES;
    }
}