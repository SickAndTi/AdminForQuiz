package com.scp.adminforquiz.api;

import com.scp.adminforquiz.model.api.NwQuiz;
import com.scp.adminforquiz.model.api.NwQuizTranslation;
import com.scp.adminforquiz.model.api.NwQuizTranslationPhrase;

import java.util.List;

import io.reactivex.Single;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface QuizApi {

    @GET("quiz/all")
    Single<List<NwQuiz>> getNwQuizList();

    // TODO all logic with User
    @GET("quiz/allWithUsers")
    Single<List<NwQuiz>> getAllWithUser();

    @GET("quiz/{id}")
    Single<NwQuiz> getNwQuizById(
            @Path("id") Long id
    );

    @GET("quiz/getQuizByQuizTranslationId")
    Single<NwQuiz> getNwQuizByQuizTranslationId(
            @Query("quizTranslationId") Long quizTranslationId
    );

    @GET("quiz/getByLangCode")
    Single<List<NwQuiz>> getNwQuizListByLangCode(
            @Query("langCode") String langCode
    );

    @GET("quiz/allSorted")
    Single<List<NwQuiz>> getNwQuizListSorted(
            @Query("sortFieldName") String sortFieldName,
            @Query("ascending") Boolean ascending
    );

    @GET("quiz/translations/all")
    Single<List<NwQuizTranslation>> getNwQuizTranslationList();

    @GET("quiz/translations/{id}")
    Single<NwQuizTranslation> getNwQuizTranslationById(
            @Path("id") Long id
    );

    @GET("quiz/translations/phrases/all")
    Single<List<NwQuizTranslationPhrase>> getNwQuizTranslationPhraseList(
    );

    @GET("quiz/translations/phrases/{id}")
    Single<NwQuizTranslationPhrase> getNwQuizTranslationPhraseById(
            @Path("id") Long id
    );

    @POST("quiz/create")
    Single<NwQuiz> createNwQuiz(
            @Body NwQuiz nwQuiz
    );

    @POST("quiz/{id}/approve")
    Single<NwQuiz> approveNwQuizById(
            @Path("id") Long id,
            @Query("approve") Boolean approve
    );

    @POST("quiz/translations/add")
    Single<NwQuiz> addNwQuizTranslation(
            @Query("quizId") Long quizId,
            @Query("langCode") String langCode,
            @Query("text") String text,
            @Query("description") String description
    );

    @POST("quiz/translations/{quizTranslationId}/update")
    Single<NwQuizTranslation> updateNwQuizTranslationDescription(
            @Path("quizTranslationId") Long quizTranslationId,
            @Query("description") String description
    );

    @POST("quiz/translations/phrases/add")
    Single<NwQuizTranslation> addNwQuizTranslationPhrase(
            @Query("quizTranslationId") Long quizTranslationId,
            @Query("text") String text
    );

    @GET("quiz/delete/{id}")
    Single<Boolean> deleteNwQuizById(
            @Path("id") Long id
    );

    @GET("quiz/translations/delete/{id}")
    Single<Boolean> deleteNwQuizTranslationById(
            @Path("id") Long id
    );

    @GET("quiz/translations/phrases/delete/{id}")
    Single<Boolean> deleteNwQuizTranslationPhraseById(
            @Path("id") Long id
    );
}