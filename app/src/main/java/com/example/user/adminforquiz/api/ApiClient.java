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

    public Single<TokenResponse> getAccessToken(String user, String password) {
        return quizApi.getAccessToken(
                okhttp3.Credentials.basic(BuildConfig.CLIENT_ID, BuildConfig.CLIENT_SECRET),
                BuildConfig.GRANT_TYPE,
                user,
                password
        )
                .doOnSuccess(tokenResponse -> {
                    preferences.setAccessToken(tokenResponse.accessToken);
                    preferences.setRefreshToken(tokenResponse.refreshToken);
                    preferences.setUserForAuth(user);
                    preferences.setPasswordForAuth(password);
                });
    }

    private Single<TokenResponse> getAccessTokenByRefreshToken() {
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

    public Single<NwQuiz> getNwQuizByQuizTranslationId(Long nwQuizTranslationId) {
        return quizApi.getNwQuizByQuizTranslationId("Bearer" + preferences.getAccessToken(), nwQuizTranslationId)
                .onErrorResumeNext(error -> error instanceof HttpException && ((HttpException) error).code() == HttpURLConnection.HTTP_UNAUTHORIZED ?
                        getAccessTokenByRefreshToken().flatMap(tokenResponse -> getNwQuizByQuizTranslationId(nwQuizTranslationId)) : Single.error(error));
    }

    public Single<List<NwQuiz>> getNwQuizListByLangCode(String langCode) {
        return quizApi.getNwQuizListByLangCode("Bearer" + preferences.getAccessToken(), langCode)
                .onErrorResumeNext(error -> error instanceof HttpException && ((HttpException) error).code() == HttpURLConnection.HTTP_UNAUTHORIZED ?
                        getAccessTokenByRefreshToken().flatMap(tokenResponse -> getNwQuizListByLangCode(langCode)) : Single.error(error));
    }

    public Single<List<NwQuiz>> getNwQuizListSorted(String sortFieldName, Boolean ascending) {
        return quizApi.getNwQuizListSorted("Bearer" + preferences.getAccessToken(), sortFieldName, ascending)
                .onErrorResumeNext(error -> error instanceof HttpException && ((HttpException) error).code() == HttpURLConnection.HTTP_UNAUTHORIZED ?
                        getAccessTokenByRefreshToken().flatMap(tokenResponse -> getNwQuizListSorted(sortFieldName, ascending)) : Single.error(error));
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

    public Single<NwQuizTranslationPhrase> getNwQuizTranslationPhraseById(Long nwQuizTranslationPhraseId) {
        return quizApi.getNwQuizTranslationPhraseById("Bearer" + preferences.getAccessToken(), nwQuizTranslationPhraseId)
                .onErrorResumeNext(error -> error instanceof HttpException && ((HttpException) error).code() == HttpURLConnection.HTTP_UNAUTHORIZED ?
                        getAccessTokenByRefreshToken().flatMap(tokenResponse -> getNwQuizTranslationPhraseById(nwQuizTranslationPhraseId)) : Single.error(error));
    }

    public Single<NwQuiz> createNwQuiz(NwQuiz nwQuiz) {
        return quizApi.createNwQuiz("Bearer" + preferences.getAccessToken(), nwQuiz)
                .onErrorResumeNext(error -> error instanceof HttpException && ((HttpException) error).code() == HttpURLConnection.HTTP_UNAUTHORIZED ?
                        getAccessTokenByRefreshToken().flatMap(tokenResponse -> createNwQuiz(nwQuiz)) : Single.error(error));
    }

    public Single<NwQuiz> approveNwQuizById(Long nwQuizId, Boolean approve) {
        return quizApi.approveNwQuizById("Bearer" + preferences.getAccessToken(), nwQuizId, approve)
                .onErrorResumeNext(error -> error instanceof HttpException && ((HttpException) error).code() == HttpURLConnection.HTTP_UNAUTHORIZED ?
                        getAccessTokenByRefreshToken().flatMap(tokenResponse -> approveNwQuizById(nwQuizId, approve)) : Single.error(error));
    }

    public Single<NwQuiz> addNwQuizTranslation(Long nwQuizId, String nwQuizTranslationLangCode, String nwQuizTranslationText, String nwQuizTranslationDescription) {
        return quizApi.addNwQuizTranslation("Bearer" + preferences.getAccessToken(),
                nwQuizId, nwQuizTranslationLangCode, nwQuizTranslationText, nwQuizTranslationDescription)
                .onErrorResumeNext(error -> error instanceof HttpException && ((HttpException) error).code() == HttpURLConnection.HTTP_UNAUTHORIZED ?
                        getAccessTokenByRefreshToken().flatMap(tokenResponse -> addNwQuizTranslation(nwQuizId, nwQuizTranslationLangCode, nwQuizTranslationText, nwQuizTranslationDescription)) : Single.error(error));
    }

    public Single<NwQuizTranslation> updateNwQuizTranslationDescription(Long nwQuizTranslationId, String description) {
        return quizApi.updateNwQuizTranslationDescription("Bearer" + preferences.getAccessToken(), nwQuizTranslationId, description)
                .onErrorResumeNext(error -> error instanceof HttpException && ((HttpException) error).code() == HttpURLConnection.HTTP_UNAUTHORIZED ?
                        getAccessTokenByRefreshToken().flatMap(tokenResponse -> updateNwQuizTranslationDescription(nwQuizTranslationId, description)) : Single.error(error));
    }

    public Single<NwQuizTranslation> addNwQuizTranslationPhrase(Long nwQuizTranslationId, String nwQuizTranslationPhrase) {
        return quizApi.addNwQuizTranslationPhrase("Bearer" + preferences.getAccessToken(), nwQuizTranslationId, nwQuizTranslationPhrase)
                .onErrorResumeNext(this::getAccessTokenOnError);
//                .onErrorResumeNext(error -> error instanceof HttpException && ((HttpException) error).code() == HttpURLConnection.HTTP_UNAUTHORIZED ?
//                        getAccessTokenByRefreshToken().flatMap(tokenResponse -> addNwQuizTranslationPhrase(nwQuizTranslationId, nwQuizTranslationPhrase)) : Single.error(error));
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

    private Single getAccessTokenOnError(Throwable error) {
        if (error instanceof HttpException && ((HttpException) error).code() == HttpURLConnection.HTTP_UNAUTHORIZED) {
            return getAccessTokenByRefreshToken();
        }
        return Single.error(error);
    }
}