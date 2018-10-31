package com.scp.adminforquiz;

import android.app.Application;

import com.scp.adminforquiz.di.AppModule;
import com.vk.sdk.VKSdk;

import timber.log.Timber;
import toothpick.Toothpick;

public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        Timber.plant(new Timber.DebugTree());
        Toothpick.openScope(Constants.APP_SCOPE).installModules(new AppModule(this));
        VKSdk.initialize(this);
    }
}