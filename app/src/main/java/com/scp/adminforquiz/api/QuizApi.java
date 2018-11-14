package com.scp.adminforquiz.api;

import com.scp.adminforquiz.model.api.NwQuiz;
import com.scp.adminforquiz.model.api.NwQuizTranslation;
import com.scp.adminforquiz.model.api.NwQuizTranslationPhrase;
import com.scp.adminforquiz.model.api.NwUser;

import java.util.List;

import io.reactivex.Single;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface QuizApi {

    @GET("quiz/allWithUsers")
    Single<List<NwQuiz>> getAllWithUser();

    @GET("user/me?showFull=true")
    Single<NwUser> whoAreMe(
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

    @POST("quiz/translations/{id}/approve")
    Single<NwQuizTranslation> approveNwQuizTranslationById(
            @Path("id") Long id,
            @Query("approve") Boolean approve
    );

    @POST("quiz/translations/phrases/{id}/approve")
    Single<NwQuizTranslationPhrase> approveNwQuizTranslationPhraseById(
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

    @POST("quiz/translations/phrases/add")
    Single<NwQuizTranslation> addNwQuizTranslationPhrase(
            @Query("quizTranslationId") Long quizTranslationId,
            @Query("text") String text
    );

    @DELETE("quiz/delete/{id}")
    Single<Boolean> deleteNwQuizById(
            @Path("id") Long id
    );

    @DELETE("quiz/translations/delete/{id}")
    Single<Boolean> deleteNwQuizTranslationById(
            @Path("id") Long id
    );

    @DELETE("quiz/translations/phrases/delete/{id}")
    Single<Boolean> deleteNwQuizTranslationPhraseById(
            @Path("id") Long id
    );
}