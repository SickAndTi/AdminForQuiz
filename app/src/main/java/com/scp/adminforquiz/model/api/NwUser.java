package com.scp.adminforquiz.model.api;

import com.google.gson.annotations.SerializedName;

public class NwUser {
    @SerializedName("id")
    public Integer id;
    @SerializedName("fullName")
    public String fullName;
    @SerializedName("avatar")
    public String avatar;

}