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

    public void setUserSortFieldName(String sortFieldName) {
        preferences.edit().putString(Constants.USER_SORT_FIELD_NAME, sortFieldName).apply();
    }

    public String getUserSortFieldName() {
        return preferences.getString(Constants.USER_SORT_FIELD_NAME, null);
    }

    public void setUserId(Long userId) {
        preferences.edit().putLong(Constants.USER_ID, userId).apply();
    }

    public Long getUserId() {
        return preferences.getLong(Constants.USER_ID, 0);
    }

    public void setIsAdmin(boolean isAdmin) {
        preferences.edit().putBoolean(Constants.IS_ADMIN, isAdmin).apply();
    }

    public boolean getIsAdmin() {
        return preferences.getBoolean(Constants.IS_ADMIN, false);
    }
}


