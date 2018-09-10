package com.example.user.adminforquiz.model.api;

import com.google.gson.annotations.SerializedName;

import java.util.Date;

public class NwQuizTranslationPhrase {

    //db
    @SerializedName("id")
    Long id;
    //content
    @SerializedName("translation")
    String translation;
    //status
    @SerializedName("approved")
    Boolean approved;
    @SerializedName("authorId")
    Long authorId;
    @SerializedName("approverId")
    Long approverId;
    //dates
    @SerializedName("created")
    Date created;
    @SerializedName("updated")
    Date updated;
}
