package com.example.user.adminforquiz.preference;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.example.user.adminforquiz.Constants;

import javax.inject.Inject;

public class MyPreferenceManager {

    private SharedPreferences preferences;

    @Inject
    public MyPreferenceManager(Context context) {
        this.preferences = PreferenceManager.getDefaultSharedPreferences(context);
    }


    public String getToken() {
        return preferences.getString(Constants.ACCESS_TOKEN, null);
    }

    public void setToken(String accessToken) {
        preferences.edit().putString(Constants.ACCESS_TOKEN, accessToken).apply();
    }


}


