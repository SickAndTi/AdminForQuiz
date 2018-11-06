package com.scp.adminforquiz.api;

import com.scp.adminforquiz.BuildConfig;
import com.scp.adminforquiz.Constants;
import com.scp.adminforquiz.api.response.TokenResponse;
import com.scp.adminforquiz.di.qualifier.AuthRetrofit;
import com.scp.adminforquiz.di.qualifier.QuizRetrofit;
import com.scp.adminforquiz.model.api.NwQuiz;
import com.scp.adminforquiz.model.api.NwQuizTranslation;
import com.scp.adminforquiz.model.api.NwQuizTranslationPhrase;
import com.scp.adminforquiz.model.db.dao.QuizDao;
import com.scp.adminforquiz.preference.MyPreferenceManager;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Single;
import retrofit2.Retrofit;

public class ApiClient {
    private QuizApi quizApi;
    private AuthApi authApi;
    @Inject
    MyPreferenceManager preferences;
    @Inject
    QuizDao quizDao;

    @Inject
    ApiClient(@QuizRetrofit Retrofit quizRetrofit, @AuthRetrofit Retrofit authRetrofit) {
        quizApi = quizRetrofit.create(QuizApi.class);
        authApi = authRetrofit.create(AuthApi.class);
    }

    public Single<TokenResponse> getAccessToken(String user, String password) {
        return authApi.getAccessToken(
                okhttp3.Credentials.basic(BuildConfig.CLIENT_ID, BuildConfig.CLIENT_SECRET),
                Constants.GRANT_TYPE,
                user,
                password
        );
    }

    public Single<TokenResponse> loginSocial(String provider, String tokenValue) {
        return authApi.socialLogin(
                provider,
                tokenValue,
                BuildConfig.CLIENT_ID,
                BuildConfig.CLIENT_SECRET
        );
    }

    public Single<TokenResponse> signUp(String email, String password, String userFullName, String avatarUrl) {
        return authApi.signUp(
                email,
                password,
                userFullName,
                avatarUrl,
                BuildConfig.CLIENT_ID,
                BuildConfig.CLIENT_SECRET
        );
    }

    public Single<List<NwQuiz>> getNwQuizList() {
        return quizApi.getNwQuizList();
    }

    public Single<List<NwQuiz>> getAllWithUser() {
        return quizApi.getAllWithUser();
    }

    public Single<NwQuiz> getNwQuizById(Long nwQuizId) {
        return quizApi.getNwQuizById(nwQuizId);
    }

    public Single<NwQuiz> getNwQuizByQuizTranslationId(Long nwQuizTranslationId) {
        return quizApi.getNwQuizByQuizTranslationId(nwQuizTranslationId);
    }

    public Single<List<NwQuiz>> getNwQuizListByLangCode(String langCode) {
        return quizApi.getNwQuizListByLangCode(langCode);
    }

    public Single<List<NwQuiz>> getNwQuizListSorted(String sortFieldName, Boolean ascending) {
        return quizApi.getNwQuizListSorted(sortFieldName, ascending);
    }

    public Single<List<NwQuizTranslation>> getNwQuizTranslationList() {
        return quizApi.getNwQuizTranslationList();
    }

    public Single<NwQuizTranslation> getNwQuizTranslationById(Long nwQuizId) {
        return quizApi.getNwQuizTranslationById(nwQuizId);
    }

    public Single<List<NwQuizTranslationPhrase>> getNwQuizTranslationPhraseList() {
        return quizApi.getNwQuizTranslationPhraseList();
    }

    public Single<NwQuizTranslationPhrase> getNwQuizTranslationPhraseById(Long nwQuizTranslationPhraseId) {
        return quizApi.getNwQuizTranslationPhraseById(nwQuizTranslationPhraseId);
    }

    public Single<NwQuiz> createNwQuiz(NwQuiz nwQuiz) {
        return quizApi.createNwQuiz(nwQuiz);
    }

    public Single<NwQuiz> approveNwQuizById(Long nwQuizId, Boolean approve) {
        return quizApi.approveNwQuizById(nwQuizId, approve);
    }

    public Single<NwQuiz> addNwQuizTranslation(Long nwQuizId, String nwQuizTranslationLangCode, String nwQuizTranslationText, String nwQuizTranslationDescription) {
        return quizApi.addNwQuizTranslation(nwQuizId, nwQuizTranslationLangCode, nwQuizTranslationText, nwQuizTranslationDescription);
    }

    public Single<NwQuizTranslation> updateNwQuizTranslationDescription(Long nwQuizTranslationId, String description) {
        return quizApi.updateNwQuizTranslationDescription(nwQuizTranslationId, description);
    }

    public Single<NwQuizTranslation> addNwQuizTranslationPhrase(Long nwQuizTranslationId, String nwQuizTranslationPhrase) {
        return quizApi.addNwQuizTranslationPhrase(nwQuizTranslationId, nwQuizTranslationPhrase);
    }

    public Single<Boolean> deleteNwQuizById(Long nwQuizId) {
        return quizApi.deleteNwQuizById(nwQuizId);
    }

    public Single<Boolean> deleteNwQuizTranslationById(Long nwQuizTranslationId) {
        return quizApi.deleteNwQuizTranslationById(nwQuizTranslationId);
    }

    public Single<Boolean> deleteNwQuizTranslationPhraseById(Long nwQuizTranslationPhraseId) {
        return quizApi.deleteNwQuizTranslationPhraseById(nwQuizTranslationPhraseId);
    }
}