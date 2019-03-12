package com.scp.adminforquiz.di;

import android.arch.persistence.room.Room;
import android.content.Context;

import com.scp.adminforquiz.BuildConfig;
import com.scp.adminforquiz.Constants;
import com.scp.adminforquiz.api.ApiClient;
import com.scp.adminforquiz.api.AuthApi;
import com.scp.adminforquiz.api.response.TokenResponse;
import com.scp.adminforquiz.db.QuizRepository;
import com.scp.adminforquiz.di.qualifier.AuthRetrofit;
import com.scp.adminforquiz.di.qualifier.QuizRetrofit;
import com.scp.adminforquiz.model.QuizConverter;
import com.scp.adminforquiz.db.DataBase;
import com.scp.adminforquiz.db.QuizDao;
import com.scp.adminforquiz.preference.MyPreferenceManager;

import java.util.concurrent.TimeUnit;

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
                .connectTimeout(90, TimeUnit.SECONDS)
                .readTimeout(90, TimeUnit.SECONDS)
                .writeTimeout(90, TimeUnit.SECONDS)
                .addInterceptor(new HttpLoggingInterceptor(log -> Timber.d(log)).setLevel(HttpLoggingInterceptor.Level.BODY))
                .build();

        Retrofit authRetrofit = new Retrofit.Builder()
                .baseUrl(BuildConfig.API_URL)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .client(okHttpClientAuth)
                .build();

        bind(Retrofit.class).withName(AuthRetrofit.class).toInstance(authRetrofit);

        AuthApi authApi = authRetrofit.create(AuthApi.class);

        OkHttpClient okHttpClientQuiz = new OkHttpClient.Builder()
                .connectTimeout(90, TimeUnit.SECONDS)
                .readTimeout(90, TimeUnit.SECONDS)
                .writeTimeout(90, TimeUnit.SECONDS)
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
                                Constants.GRANT_TYPE_REFRESH_TOKEN,
                                myPreferenceManager.getRefreshToken()
                        )
                                .blockingGet();
                        myPreferenceManager.setAccessToken(tokenResponse.accessToken);
                        myPreferenceManager.setRefreshToken(tokenResponse.refreshToken);
                        request = request.newBuilder()
                                .header("Authorization", "Bearer" + tokenResponse.accessToken)
                                .method(request.method(), request.body()).url(request.url()).build();
                        response = chain.proceed(request);
                    }
                    return response;
                })
                .build();

        bind(Retrofit.class).withName(QuizRetrofit.class).toInstance(new Retrofit.Builder()
                .baseUrl(BuildConfig.API_URL)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .client(okHttpClientQuiz)
                .build());

        bind(ApiClient.class).singletonInScope();

        DataBase dataBase = Room.databaseBuilder(context, DataBase.class, "dataBase")
                .fallbackToDestructiveMigration()
                .build();
        bind(DataBase.class).toInstance(dataBase);

        bind(QuizRepository.class).singletonInScope();

        Cicerone<Router> cicerone = Cicerone.create();
        bind(Cicerone.class).toInstance(cicerone);
        bind(Router.class).toInstance(cicerone.getRouter());
        bind(NavigatorHolder.class).toInstance(cicerone.getNavigatorHolder());

        bind(Context.class).toInstance(context);

        bind(QuizDao.class).toInstance(dataBase.quizDao());

        bind(QuizConverter.class).singletonInScope();
    }
}
