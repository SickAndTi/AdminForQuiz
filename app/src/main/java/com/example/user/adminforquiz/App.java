package com.example.user.adminforquiz;

import android.app.Application;

import com.example.user.adminforquiz.di.AppModule;

import timber.log.Timber;
import toothpick.Toothpick;

public class App extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Timber.plant(new Timber.DebugTree());
        Toothpick.openScope(Constants.APP_SCOPE).installModules(new AppModule(this));
    }
}
