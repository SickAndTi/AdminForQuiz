package com.example.user.adminforquiz.api;

import com.example.user.adminforquiz.BuildConfig;
import com.example.user.adminforquiz.api.response.TokenResponse;
import com.example.user.adminforquiz.model.api.NwQuiz;
import com.example.user.adminforquiz.model.api.NwQuizTranslation;
import com.example.user.adminforquiz.model.api.NwQuizTranslationPhrase;
import com.example.user.adminforquiz.model.db.dao.QuizDao;
import com.example.user.adminforquiz.preference.MyPreferenceManager;

import java.net.HttpURLConnection;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.Completable;
import io.reactivex.CompletableObserver;
import io.reactivex.Single;
import retrofit2.HttpException;
import retrofit2.Retrofit;

public class ApiClient {
    private QuizApi quizApi;
    @Inject
    MyPreferenceManager preferences;
    @Inject
    QuizDao quizDao;
    private NwQuiz nwQuiz;
    private NwQuizTranslation nwQuizTranslation;
    private NwQuizTranslationPhrase nwQuizTranslationPhrase;

    @Inject
    ApiClient(Retrofit retrofit) {
        quizApi = retrofit.create(QuizApi.class);
    }

    public Single<TokenResponse> getAccessToken() {
        return quizApi.getAccessToken(okhttp3.Credentials.basic(BuildConfig.CLIENT_ID, BuildConfig.CLIENT_SECRET),
                BuildConfig.GRANT_TYPE, BuildConfig.USER, BuildConfig.PASSWORD)
                .doOnSuccess(tokenResponse -> {
                    preferences.setAccessToken(tokenResponse.accessToken);
                    preferences.setRefreshToken(tokenResponse.refreshToken);
                });
    }

    public Single<TokenResponse> getAccessTokenByRefreshToken() {
        return quizApi.getAccessTokenByRefreshToken(okhttp3.Credentials.basic(BuildConfig.CLIENT_ID, BuildConfig.CLIENT_SECRET),
                BuildConfig.GRANT_TYPE_REFRESH_TOKEN, preferences.getRefreshToken())
                .doOnSuccess(tokenResponse -> {
                    preferences.setAccessToken(tokenResponse.accessToken);
                    preferences.setRefreshToken(tokenResponse.refreshToken);
                });
    }

    public Single<List<NwQuiz>> getNwQuizList() {
        return quizApi.getNwQuizList("Bearer" + preferences.getAccessToken())
                .doOnSuccess(nwQuizs -> quizDao.deleteAllTables())
                .onErrorResumeNext(error -> error instanceof HttpException && ((HttpException) error).code() == HttpURLConnection.HTTP_UNAUTHORIZED ?
                        getAccessTokenByRefreshToken().flatMap(tokenResponse -> getNwQuizList()) : Single.error(error));
    }

    public Single<NwQuiz> getNwQuizById(Long nwQuizId) {
        return quizApi.getNwQuizById("Bearer" + preferences.getAccessToken(), nwQuizId)
                .onErrorResumeNext(error -> error instanceof HttpException && ((HttpException) error).code() == HttpURLConnection.HTTP_UNAUTHORIZED ?
                        getAccessTokenByRefreshToken().flatMap(tokenResponse -> getNwQuizById(nwQuizId)) : Single.error(error));
    }

    public Single<NwQuiz> getNwQuizByQuizTranslationId() {
        return quizApi.getNwQuizByQuizTranslationId("Bearer" + preferences.getAccessToken(), nwQuizTranslation.id)
                .onErrorResumeNext(error -> error instanceof HttpException && ((HttpException) error).code() == HttpURLConnection.HTTP_UNAUTHORIZED ?
                        getAccessTokenByRefreshToken().flatMap(tokenResponse -> getNwQuizByQuizTranslationId()) : Single.error(error));
    }

    public Single<List<NwQuiz>> getNwQuizListByLangCode() {
        return quizApi.getNwQuizListByLangCode("Bearer" + preferences.getAccessToken(), nwQuizTranslation.langCode)
                .onErrorResumeNext(error -> error instanceof HttpException && ((HttpException) error).code() == HttpURLConnection.HTTP_UNAUTHORIZED ?
                        getAccessTokenByRefreshToken().flatMap(tokenResponse -> getNwQuizListByLangCode()) : Single.error(error));
    }

    public Single<List<NwQuiz>> getNwQuizListSorted() {
        return quizApi.getNwQuizListSorted("Bearer" + preferences.getAccessToken(), "sortFieldName", true)
                .onErrorResumeNext(error -> error instanceof HttpException && ((HttpException) error).code() == HttpURLConnection.HTTP_UNAUTHORIZED ?
                        getAccessTokenByRefreshToken().flatMap(tokenResponse -> getNwQuizListSorted()) : Single.error(error));
    }

    public Single<List<NwQuizTranslation>> getNwQuizTranslationList() {
        return quizApi.getNwQuizTranslationList("Bearer" + preferences.getAccessToken())
                .onErrorResumeNext(error -> error instanceof HttpException && ((HttpException) error).code() == HttpURLConnection.HTTP_UNAUTHORIZED ?
                        getAccessTokenByRefreshToken().flatMap(tokenResponse -> getNwQuizTranslationList()) : Single.error(error));
    }

    public Single<NwQuizTranslation> getNwQuizTranslationById(Long nwQuizId) {
        return quizApi.getNwQuizTranslationById("Bearer" + preferences.getAccessToken(), nwQuizId)
                .onErrorResumeNext(error -> error instanceof HttpException && ((HttpException) error).code() == HttpURLConnection.HTTP_UNAUTHORIZED ?
                        getAccessTokenByRefreshToken().flatMap(tokenResponse -> getNwQuizTranslationById(nwQuizId)) : Single.error(error));
    }

    public Single<List<NwQuizTranslationPhrase>> getNwQuizTranslationPhraseList() {
        return quizApi.getNwQuizTranslationPhraseList("Bearer" + preferences.getAccessToken())
                .onErrorResumeNext(error -> error instanceof HttpException && ((HttpException) error).code() == HttpURLConnection.HTTP_UNAUTHORIZED ?
                        getAccessTokenByRefreshToken().flatMap(tokenResponse -> getNwQuizTranslationPhraseList()) : Single.error(error));
    }

    public Single<NwQuizTranslationPhrase> getNwQuizTranslationPhraseById() {
        return quizApi.getNwQuizTranslationPhraseById("Bearer" + preferences.getAccessToken(), nwQuiz.id)
                .onErrorResumeNext(error -> error instanceof HttpException && ((HttpException) error).code() == HttpURLConnection.HTTP_UNAUTHORIZED ?
                        getAccessTokenByRefreshToken().flatMap(tokenResponse -> getNwQuizTranslationPhraseById()) : Single.error(error));
    }

    public Single<NwQuiz> createNwQuiz() {
        return quizApi.createNwQuiz("Bearer" + preferences.getAccessToken(), nwQuiz)
                .onErrorResumeNext(error -> error instanceof HttpException && ((HttpException) error).code() == HttpURLConnection.HTTP_UNAUTHORIZED ?
                        getAccessTokenByRefreshToken().flatMap(tokenResponse -> createNwQuiz()) : Single.error(error));
    }

    public Single<NwQuiz> approveNwQuizById() {
        return quizApi.approveNwQuizById("Bearer" + preferences.getAccessToken(), nwQuiz.id, nwQuiz.approved)
                .onErrorResumeNext(error -> error instanceof HttpException && ((HttpException) error).code() == HttpURLConnection.HTTP_UNAUTHORIZED ?
                        getAccessTokenByRefreshToken().flatMap(tokenResponse -> approveNwQuizById()) : Single.error(error));
    }

    public Single<NwQuiz> addNwQuizTranslation(Long nwQuizTranslationId, String nwQuizTranslationLangCode, String nwQuizTranslationText, String nwQuizTranslationDescription) {
        return quizApi.addNwQuizTranslation("Bearer" + preferences.getAccessToken(),
                nwQuizTranslationId, nwQuizTranslationLangCode, nwQuizTranslationText, nwQuizTranslationDescription)
                .onErrorResumeNext(error -> error instanceof HttpException && ((HttpException) error).code() == HttpURLConnection.HTTP_UNAUTHORIZED ?
                        getAccessTokenByRefreshToken().flatMap(tokenResponse -> addNwQuizTranslation(nwQuizTranslationId, nwQuizTranslationLangCode, nwQuizTranslationText, nwQuizTranslationDescription)) : Single.error(error));
    }

    public Single<NwQuizTranslation> updateNwQuizTranslationDescription(Long nwQuizTranslationId, String description) {
        return quizApi.updateNwQuizTranslationDescription("Bearer" + preferences.getAccessToken(), nwQuizTranslationId, description)
                .onErrorResumeNext(error -> error instanceof HttpException && ((HttpException) error).code() == HttpURLConnection.HTTP_UNAUTHORIZED ?
                        getAccessTokenByRefreshToken().flatMap(tokenResponse -> updateNwQuizTranslationDescription(nwQuizTranslationId, description)) : Single.error(error));
    }

    public Single<NwQuizTranslation> addNwQuizTranslationPhrase(Long nwQuizTranslationId, String nwQuizTranslationPhrase) {
        return quizApi.addNwQuizTranslationPhrase("Bearer" + preferences.getAccessToken(), nwQuizTranslationId, nwQuizTranslationPhrase)
                .onErrorResumeNext(error -> error instanceof HttpException && ((HttpException) error).code() == HttpURLConnection.HTTP_UNAUTHORIZED ?
                        getAccessTokenByRefreshToken().flatMap(tokenResponse -> addNwQuizTranslationPhrase(nwQuizTranslationId, nwQuizTranslationPhrase)) : Single.error(error));
    }

    public Single<Boolean> deleteNwQuizById(Long nwQuizId) {
        return quizApi.deleteNwQuizById("Bearer" + preferences.getAccessToken(), nwQuizId)
                .onErrorResumeNext(error -> error instanceof HttpException && ((HttpException) error).code() == HttpURLConnection.HTTP_UNAUTHORIZED ?
                        getAccessTokenByRefreshToken().flatMap(tokenResponse -> deleteNwQuizById(nwQuizId)) : Single.error(error));

    }

    public Single<Boolean> deleteNwQuizTranslationById(Long nwQuizTranslationId) {
        return quizApi.deleteNwQuizTranslationById("Bearer" + preferences.getAccessToken(), nwQuizTranslationId)
                .onErrorResumeNext(error -> error instanceof HttpException && ((HttpException) error).code() == HttpURLConnection.HTTP_UNAUTHORIZED ?
                        getAccessTokenByRefreshToken().flatMap(tokenResponse -> deleteNwQuizTranslationById(nwQuizTranslationId)) : Single.error(error));
    }

    public Single<Boolean> deleteNwQuizTranslationPhraseById(Long nwQuizTranslationPhraseId) {
        return quizApi.deleteNwQuizTranslationPhraseById("Bearer" + preferences.getAccessToken(), nwQuizTranslationPhraseId)
                .onErrorResumeNext(error -> error instanceof HttpException && ((HttpException) error).code() == HttpURLConnection.HTTP_UNAUTHORIZED ?
                        getAccessTokenByRefreshToken().flatMap(tokenResponse -> deleteNwQuizTranslationPhraseById(nwQuizTranslationPhraseId)) : Single.error(error));
    }
}



