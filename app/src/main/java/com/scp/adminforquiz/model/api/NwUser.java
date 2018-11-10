package com.scp.adminforquiz.model.api;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class NwUser {
    @SerializedName("id")
    public Long id;
    @SerializedName("fullName")
    public String fullName;
    @SerializedName("avatar")
    public String avatar;
    @SerializedName("authorities")
    public List<NwUserAuthorities> authorities = null;

}