package com.example.user.adminforquiz.api;

import com.example.user.adminforquiz.BuildConfig;
import com.example.user.adminforquiz.Constants;
import com.example.user.adminforquiz.api.response.TokenResponse;
import com.example.user.adminforquiz.di.qualifier.AuthRetrofit;
import com.example.user.adminforquiz.di.qualifier.QuizRetrofit;
import com.example.user.adminforquiz.model.api.NwQuiz;
import com.example.user.adminforquiz.model.api.NwQuizTranslation;
import com.example.user.adminforquiz.model.api.NwQuizTranslationPhrase;
import com.example.user.adminforquiz.model.db.dao.QuizDao;
import com.example.user.adminforquiz.preference.MyPreferenceManager;

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

    public Single<List<NwQuiz>> getNwQuizList() {
        return quizApi.getNwQuizList();
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