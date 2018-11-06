package com.scp.adminforquiz.api;

import com.scp.adminforquiz.api.response.TokenResponse;

import io.reactivex.Single;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Header;
import retrofit2.http.POST;

public interface AuthApi {

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

    @FormUrlEncoded
    @POST("auth/socialLogin")
    Single<TokenResponse> socialLogin(
            @Field("provider") String provider,
            @Field("token") String tokenValue,
            @Field("clientId") String clientId,
            @Field("clientSecret") String clientSecret
    );

    @FormUrlEncoded
    @POST("auth/registration")
    Single<TokenResponse> signUp(
            @Field("email") String email,
            @Field("password") String password,
            @Field("fullName") String userFullName,
            @Field("avatarUrl") String avatarUrl,
            @Field("clientId") String clientId,
            @Field("clientSecret") String clientSecret
    );
}
