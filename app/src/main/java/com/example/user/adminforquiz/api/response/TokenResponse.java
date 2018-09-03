package com.example.user.adminforquiz.api.response;

public class TokenResponse {
    @JsonClass(generateAdapter = true)
    @Json(name = "access_token")
    val accessToken: String,
    @Json(name = "token_type")
    val tokenType: String,
    @Json(name = "expires_in")
    val expiresIn: Int,
    @Json(name = "scope")
    val scope: String
)
}
