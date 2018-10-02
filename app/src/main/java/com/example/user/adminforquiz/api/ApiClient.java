package com.example.user.adminforquiz.api;

import com.example.user.adminforquiz.BuildConfig;
import com.example.user.adminforquiz.api.response.TokenResponseTest;
import com.example.user.adminforquiz.model.api.NwQuiz;
import com.example.user.adminforquiz.model.db.dao.QuizDao;
import com.example.user.adminforquiz.preference.MyPreferenceManager;

import java.net.HttpURLConnection;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.Single;
import retrofit2.HttpException;
import retrofit2.Retrofit;

public class ApiClient {
    private QuizApi quizApi;
    @Inject
    MyPreferenceManager preferences;
    @Inject
    QuizDao quizDao;

    @Inject
    ApiClient(Retrofit retrofit) {
        quizApi = retrofit.create(QuizApi.class);
    }

    //    private Single<TokenResponse> getAccessToken() {
//        return quizApi.getAccessToken(okhttp3.Credentials.basic(BuildConfig.USER, BuildConfig.PASSWORD), BuildConfig.GRANT_TYPE)
//                .doOnSuccess(tokenResponse -> preferences.setToken(tokenResponse.accessToken));
//    }
//
    public Single<List<NwQuiz>> getNwQuizList() {
        return quizApi.getNwQuizList("Bearer" + preferences.getTestAccessToken())
                .doOnSuccess(nwQuizs -> quizDao.deleteAllTables())
                .onErrorResumeNext(error -> error instanceof HttpException && ((HttpException) error).code() == HttpURLConnection.HTTP_UNAUTHORIZED ?
                        getTestAccessTokenByRefreshToken().flatMap(tokenResponseTest -> getNwQuizList()) : Single.error(error));
    }

    public Single<TokenResponseTest> getTestAccessToken() {
        return quizApi.getTestAccessToken(okhttp3.Credentials.basic(BuildConfig.CLIENT_ID, BuildConfig.CLIENT_SECRET),
                BuildConfig.GRANT_TYPE, BuildConfig.USER, BuildConfig.PASSWORD)
                .doOnSuccess(tokenResponseTest -> {
                    preferences.setTestAccessToken(tokenResponseTest.accessToken);
                    preferences.setTestRefreshToken(tokenResponseTest.refreshToken);
                });
    }

    private Single<TokenResponseTest> getTestAccessTokenByRefreshToken() {
        return quizApi.getTestAccessTokenByRefreshToken(okhttp3.Credentials.basic(BuildConfig.CLIENT_ID, BuildConfig.CLIENT_SECRET),
                BuildConfig.GRANT_TYPE_REFRESH_TOKEN, preferences.getTestRefreshToken())
                .doOnSuccess(tokenResponseTest -> {
                    preferences.setTestAccessToken(tokenResponseTest.accessToken);
                    preferences.setTestRefreshToken(tokenResponseTest.refreshToken);
                });
    }
}



