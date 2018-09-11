package com.example.user.adminforquiz.api;

import com.example.user.adminforquiz.BuildConfig;
import com.example.user.adminforquiz.api.response.TokenResponse;
import com.example.user.adminforquiz.model.api.NwQuiz;
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
    ApiClient(Retrofit retrofit) {
        quizApi = retrofit.create(QuizApi.class);
    }

    public Single<TokenResponse> getAccessToken() {
        return quizApi.getAccessToken(okhttp3.Credentials.basic(BuildConfig.USER, BuildConfig.PASSWORD), BuildConfig.GRANT_TYPE)
                .doOnSuccess(accessToken -> preferences.setToken(String.valueOf(accessToken)));
    }

    public Single<List<NwQuiz>> getNwQuizList() {
        return quizApi.getNwQuizList("Bearer" + preferences.getToken())
                .onErrorResumeNext(error -> error instanceof HttpException && ((HttpException) error).code() == HttpURLConnection.HTTP_UNAUTHORIZED ?
                        getAccessToken().flatMap(tokenResponse -> getNwQuizList()) : Single.error(error));
    }
}



