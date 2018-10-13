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

    public void setUserForAuth(String userForAuth) {
        preferences.edit().putString(Constants.USER_FOR_AUTH, userForAuth).apply();
    }

    public String getUserForAuth() {
        return preferences.getString(Constants.USER_FOR_AUTH, null);
    }

    public void setPasswordForAuth(String passwordForAuth) {
        preferences.edit().putString(Constants.PASSWORD_FOR_AUTH, passwordForAuth).apply();
    }

    public String getPasswordForAuth() {
        return preferences.getString(Constants.PASSWORD_FOR_AUTH, null);
    }
}


