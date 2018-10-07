package com.example.user.adminforquiz.api;

import com.example.user.adminforquiz.api.response.TokenResponse;
import com.example.user.adminforquiz.model.api.NwQuiz;
import com.example.user.adminforquiz.model.api.NwQuizTranslation;
import com.example.user.adminforquiz.model.api.NwQuizTranslationPhrase;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Single;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface QuizApi {

    @FormUrlEncoded
    @POST("oauth/token")
    Single<TokenResponse> getAccessToken(
            @Header("Authorization") String testAuthorization,
            @Field("grant_type") String testGrantType,
            @Field("username") String testUser,
            @Field("password") String testPassword
    );

    @FormUrlEncoded
    @POST("oauth/token")
    Single<TokenResponse> getAccessTokenByRefreshToken(
            @Header("Authorization") String testAuthorization,
            @Field("grant_type") String testRefreshToken,
            @Field("refresh_token") String testRefreshTokenValue
    );

    @GET("quiz/all")
    Single<List<NwQuiz>> getNwQuizList(
            @Header("Authorization") String authorization
    );

    @GET("quiz/{id}")
    Single<NwQuiz> getNwQuizById(
            @Header("Authorization") String authorization,
            @Path("id") Long id
    );

    @GET("quiz/getQuizByQuizTranslationId")
    Single<NwQuiz> getNwQuizByQuizTranslationId(
            @Header("Authorization") String authorization,
            @Query("quizTranslationId") Long quizTranslationId
    );

    @GET("quiz/getByLangCode")
    Single<List<NwQuiz>> getNwQuizListByLangCode(
            @Header("Authorization") String authorization,
            @Query("langCode") String langCode
    );


    @GET("quiz/allSorted")
    Single<List<NwQuiz>> getNwQuizListSorted(
            @Header("Authorization") String authorization,
            @Query("sortFieldName") String sortFieldName,
            @Query("ascending") Boolean ascending
    );

    @GET("quiz/translations/all")
    Single<List<NwQuizTranslation>> getNwQuizTranslationList(
            @Header("Authorization") String authorization
    );

    @GET("quiz/translations/{id}")
    Single<NwQuizTranslation> getNwQuizTranslationById(
            @Header("Authorization") String authorization,
            @Path("id") Long id
    );

    @GET("quiz/translations/phrases/all")
    Single<List<NwQuizTranslationPhrase>> getNwQuizTranslationPhraseList(
            @Header("Authorization") String authorization
    );

    @GET("quiz/translations/phrases/{id}")
    Single<NwQuizTranslationPhrase> getNwQuizTranslationPhraseById(
            @Header("Authorization") String authorization,
            @Path("id") Long id
    );

    @POST("quiz/create")
    Single<NwQuiz> createNwQuiz(
            @Header("Authorization") String authorization,
            @Body NwQuiz nwQuiz
    );

    @POST("quiz/{id}/approve")
    Single<NwQuiz> approveNwQuizById(
            @Header("Authorization") String authorization,
            @Path("id") Long id,
            @Query("approve") Boolean approve
    );

    @POST("quiz/translations/add")
    Single<NwQuiz> addNwQuizTranslation(
            @Header("Authorization") String authorization,
            @Query("quizId") Long quizId,
            @Query("langCode") String langCode,
            @Query("text") String text,
            @Query("description") String description
    );

    @POST("quiz/translations/{quizTranslationId}/update")
    Single<NwQuizTranslation> updateNwQuizTranslationDescription(
            @Header("Authorization") String authorization,
            @Path("quizTranslationId") Long quizTranslationId,
            @Query("description") String description
    );

    @POST("quiz/translations/phrases/add")
    Single<NwQuizTranslation> addNwQuizTranslationPhrase(
            @Header("Authorization") String authorization,
            @Query("quizTranslationId") Long quizTranslationId,
            @Query("text") String text
    );

    @GET("quiz/delete/{id}")
    Single<Boolean> deleteNwQuizById(
            @Header("Authorization") String authorization,
            @Path("id") Long id
    );

    @GET("quiz/translations/delete/{id}")
    Single<Boolean>deleteNwQuizTranslationById(
            @Header("Authorization") String authorization,
            @Path("id") Long id
    );

    @GET("quiz/translations/phrases/delete/{id}")
    Single<Boolean> deleteNwQuizTranslationPhraseById(
            @Header("Authorization") String authorization,
            @Path("id") Long id
    );

}
