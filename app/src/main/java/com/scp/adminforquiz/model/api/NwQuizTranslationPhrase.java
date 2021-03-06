package com.scp.adminforquiz.model.api;

import com.google.gson.annotations.SerializedName;

import org.jetbrains.annotations.Nullable;

import java.util.Date;

public class NwQuizTranslationPhrase {

    //db
    @SerializedName("id")
    public Long id;
    //content
    @SerializedName("translation")
    public String translation;
    //status
    @SerializedName("approved")
    public Boolean approved;
    @SerializedName("authorId")
    public Long authorId;
    @SerializedName("approverId")
    public Long approverId;
    //dates
    @SerializedName("created")
    public Date created;
    @SerializedName("updated")
    public Date updated;
    @SerializedName("author")
    @Nullable
    public NwUser author;
    @SerializedName("approver")
    @Nullable
    public NwUser approver;
}
