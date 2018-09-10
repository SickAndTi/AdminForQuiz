package com.example.user.adminforquiz.di;

import android.content.Context;

import com.example.user.adminforquiz.api.ApiClient;
import com.example.user.adminforquiz.preference.MyPreferenceManager;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import timber.log.Timber;
import toothpick.config.Module;

public class AppModule extends Module {

    public AppModule(Context context) {
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .addInterceptor(new HttpLoggingInterceptor(log -> Timber.d(log)).setLevel(HttpLoggingInterceptor.Level.BODY))
                .build();

        bind(Retrofit.class).toInstance(new Retrofit.Builder()
                .baseUrl("http://37.143.14.68:8080/")
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .client(okHttpClient)
                .build());
        bind(ApiClient.class).singletonInScope();

        bind(MyPreferenceManager.class).singletonInScope();

    }
}
