package com.example.user.adminforquiz.preference;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.example.user.adminforquiz.Constants;

import javax.inject.Inject;

public class MyPreferenceManager {

    private SharedPreferences preferences;

    @Inject
    MyPreferenceManager(Context context) {
        this.preferences = PreferenceManager.getDefaultSharedPreferences(context);
    }

    public void setTestAccessToken(String testAccessToken) {
        preferences.edit().putString(Constants.ACCESS_TOKEN, testAccessToken).apply();
    }

    public String getTestAccessToken() {
        return preferences.getString(Constants.ACCESS_TOKEN, null);
    }

    public void setTestRefreshToken(String testRefreshToken) {
        preferences.edit().putString(Constants.REFRESH_TOKEN, testRefreshToken).apply();
    }

    public String getTestRefreshToken() {
        return preferences.getString(Constants.REFRESH_TOKEN, null);
    }
}


