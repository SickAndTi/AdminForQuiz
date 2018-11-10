
package com.scp.adminforquiz.model.api;

import com.google.gson.annotations.SerializedName;

public class NwUserAuthorities {
    @SerializedName("userId")
    public Integer userId;
    @SerializedName("authority")
    public String authority;
}