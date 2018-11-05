package com.scp.adminforquiz.preference;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.scp.adminforquiz.Constants;

import javax.inject.Inject;

public class MyPreferenceManager {

    private SharedPreferences preferences;

    @Inject
    public MyPreferenceManager(Context context) {
        this.preferences = PreferenceManager.getDefaultSharedPreferences(context);
    }

    public void setAccessToken(String testAccessToken) {
        preferences.edit().putString(Constants.ACCESS_TOKEN, testAccessToken).apply();
    }

    public String getAccessToken() {
        return preferences.getString(Constants.ACCESS_TOKEN, null);
    }

    public void setRefreshToken(String testRefreshToken) {
        preferences.edit().putString(Constants.REFRESH_TOKEN, testRefreshToken).apply();
    }

    public String getRefreshToken() {
        return preferences.getString(Constants.REFRESH_TOKEN, null);
    }

    public void setUserFilterAscending(boolean ascending) {
        preferences.edit().putBoolean(Constants.USER_FILTER_ASCENDING, ascending).apply();
    }

    public boolean getUserFilterAscending() {
        return preferences.getBoolean(Constants.USER_FILTER_ASCENDING, true);
    }

    public void setUserFilterBy(int filterItem) {
        preferences.edit().putInt(Constants.USER_FILTER_BY, filterItem).apply();
    }

    public int getUserFilterBy() {
        return preferences.getInt(Constants.USER_FILTER_BY, 0);
    }
}


