package com.scp.adminforquiz.api;

import com.scp.adminforquiz.BuildConfig;
import com.scp.adminforquiz.Constants;
import com.scp.adminforquiz.api.response.TokenResponse;
import com.scp.adminforquiz.db.QuizDao;
import com.scp.adminforquiz.di.qualifier.AuthRetrofit;
import com.scp.adminforquiz.di.qualifier.QuizRetrofit;
import com.scp.adminforquiz.model.api.NwQuiz;
import com.scp.adminforquiz.model.api.NwQuizTranslation;
import com.scp.adminforquiz.model.api.NwQuizTranslationPhrase;
import com.scp.adminforquiz.model.api.NwUser;
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
                BuildConfig.CLIENT_SECRET,
                Constants.ADMIN
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

    public Single<List<NwQuiz>> getAllWithUser(int offset, int limit) {
        return quizApi.getAllWithUser(offset, limit);
    }

    public Single<NwUser> whoAreMe() {
        return quizApi.whoAreMe();
    }

    public Single<NwQuiz> createNwQuiz(NwQuiz nwQuiz) {
        return quizApi.createNwQuiz(nwQuiz);
    }

    public Single<NwQuiz> approveNwQuizById(Long nwQuizId, Boolean approve) {
        return quizApi.approveNwQuizById(nwQuizId, approve);
    }

    public Single<NwQuizTranslation> approveNwQuizTranslationById(Long nwQuizTranslationId, Boolean approve) {
        return quizApi.approveNwQuizTranslationById(nwQuizTranslationId, approve);
    }

    public Single<NwQuizTranslationPhrase> approveNwQuizTranslationPhraseById(Long nwQuizTranslationPhraseId, Boolean approve) {
        return quizApi.approveNwQuizTranslationPhraseById(nwQuizTranslationPhraseId, approve);
    }

    public Single<NwQuiz> addNwQuizTranslation(Long nwQuizId, String nwQuizTranslationLangCode, String nwQuizTranslationText, String nwQuizTranslationDescription) {
        return quizApi.addNwQuizTranslation(nwQuizId, nwQuizTranslationLangCode, nwQuizTranslationText, nwQuizTranslationDescription);
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