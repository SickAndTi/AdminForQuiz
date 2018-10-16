package com.example.user.adminforquiz.di;

import android.arch.persistence.room.Room;
import android.content.Context;

import com.example.user.adminforquiz.BuildConfig;
import com.example.user.adminforquiz.api.ApiClient;
import com.example.user.adminforquiz.api.AuthApi;
import com.example.user.adminforquiz.api.response.TokenResponse;
import com.example.user.adminforquiz.di.qualifier.AuthRetrofit;
import com.example.user.adminforquiz.di.qualifier.QuizRetrofit;
import com.example.user.adminforquiz.model.QuizConverter;
import com.example.user.adminforquiz.model.db.DataBase;
import com.example.user.adminforquiz.model.db.dao.QuizDao;
import com.example.user.adminforquiz.preference.MyPreferenceManager;

import okhttp3.Credentials;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import ru.terrakok.cicerone.Cicerone;
import ru.terrakok.cicerone.NavigatorHolder;
import ru.terrakok.cicerone.Router;
import timber.log.Timber;
import toothpick.config.Module;

public class AppModule extends Module {

    public AppModule(Context context) {

        MyPreferenceManager myPreferenceManager = new MyPreferenceManager(context);
        bind(MyPreferenceManager.class).toInstance(myPreferenceManager);

        OkHttpClient okHttpClientAuth = new OkHttpClient.Builder()
                .addInterceptor(new HttpLoggingInterceptor(log -> Timber.d(log)).setLevel(HttpLoggingInterceptor.Level.BODY))
                .build();

        Retrofit authRetrofit = new Retrofit.Builder()
                .baseUrl(BuildConfig.TEST_API_URL)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .client(okHttpClientAuth)
                .build();

        bind(Retrofit.class).withName(AuthRetrofit.class).toInstance(authRetrofit);

        AuthApi authApi = authRetrofit.create(AuthApi.class);

        OkHttpClient okHttpClientQuiz = new OkHttpClient.Builder()
                .addInterceptor(new HttpLoggingInterceptor(log -> Timber.d(log)).setLevel(HttpLoggingInterceptor.Level.BODY))
                .addInterceptor(chain -> {
                    Request request = chain.request();
                    request = request.newBuilder()
                            .header("Authorization", "Bearer" + myPreferenceManager.getAccessToken())
                            .method(request.method(), request.body()).url(request.url()).build();
                    return chain.proceed(request);
                })
                .addInterceptor(chain -> {
                    Request request = chain.request();
                    Response response = chain.proceed(request);
                    if (response.code() == 401) {
                        TokenResponse tokenResponse = authApi.getAccessTokenByRefreshToken(
                                Credentials.basic(BuildConfig.CLIENT_ID, BuildConfig.CLIENT_SECRET),
                                BuildConfig.GRANT_TYPE_REFRESH_TOKEN,
                                myPreferenceManager.getRefreshToken()
                        )
                                .blockingGet();
                        myPreferenceManager.setAccessToken(tokenResponse.accessToken);
                        myPreferenceManager.setRefreshToken(tokenResponse.refreshToken);
                        request = request.newBuilder()
                                .header("Authorization", "Bearer" + tokenResponse.accessToken)
                                .method(request.method(), request.body()).url(request.url()).build();
                        Timber.d("REQUEST %s", request);
                        response = chain.proceed(request);
                    }
                    return response;
                })

                .build();

        bind(Retrofit.class).withName(QuizRetrofit.class).toInstance(new Retrofit.Builder()
                .baseUrl(BuildConfig.TEST_API_URL)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .client(okHttpClientQuiz)
                .build());

        bind(ApiClient.class).singletonInScope();

        DataBase dataBase;
        dataBase = Room.databaseBuilder(context, DataBase.class, "dataBase")
                .fallbackToDestructiveMigration()
                .build();
        bind(DataBase.class).toInstance(dataBase);

        Cicerone<Router> cicerone = Cicerone.create();
        bind(Cicerone.class).toInstance(cicerone);
        bind(Router.class).toInstance(cicerone.getRouter());
        bind(NavigatorHolder.class).toInstance(cicerone.getNavigatorHolder());

        bind(Context.class).toInstance(context);

        bind(QuizDao.class).toInstance(dataBase.quizDao());


        bind(QuizConverter.class).singletonInScope();
    }
}
