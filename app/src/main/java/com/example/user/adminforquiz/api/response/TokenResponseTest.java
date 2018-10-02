package com.example.user.adminforquiz.api.response;

import com.google.gson.annotations.SerializedName;

public class TokenResponseTest {

    @SerializedName("access_token")
    public String accessToken;
    @SerializedName("token_type")
    public String tokenType;
    @SerializedName("refresh_token")
    public String refreshToken;
    @SerializedName("expires_in")
    public Integer expiresIn;
    @SerializedName("scope")
    public String scope;

}

