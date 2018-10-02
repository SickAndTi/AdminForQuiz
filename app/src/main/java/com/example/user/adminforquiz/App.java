package com.example.user.adminforquiz;

import android.app.Application;

import com.example.user.adminforquiz.api.ApiClient;
import com.example.user.adminforquiz.di.AppModule;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import ru.terrakok.cicerone.Cicerone;
import ru.terrakok.cicerone.NavigatorHolder;
import ru.terrakok.cicerone.Router;
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