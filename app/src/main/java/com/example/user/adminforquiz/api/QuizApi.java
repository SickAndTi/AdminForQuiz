package com.example.user.adminforquiz.api;

import com.example.user.adminforquiz.api.response.TokenResponseTest;
import com.example.user.adminforquiz.model.api.NwQuiz;

import java.util.List;

import io.reactivex.Single;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;

public interface QuizApi {
//    @FormUrlEncoded
//    @POST("oauth/token")
//    Single<TokenResponse> getAccessToken(
//            @Header("Authorization") String authorization,
//            @Field("grant_type") String grantType
//    );


    @GET("quiz/all")
    Single<List<NwQuiz>> getNwQuizList(
            @Header("Authorization") String authorization
    );

    @FormUrlEncoded
    @POST("oauth/token")
    Single<TokenResponseTest> getTestAccessToken(
            @Header("Authorization") String testAuthorization,
            @Field("grant_type") String testGrantType,
            @Field("username") String testUser,
            @Field("password") String testPassword
    );

    @FormUrlEncoded
    @POST("oauth/token")
    Single<TokenResponseTest> getTestAccessTokenByRefreshToken(
            @Header("Authorization") String testAuthorization,
            @Field("grant_type") String testRefreshToken,
            @Field("refresh_token") String testRefreshTokenValue
    );


}
