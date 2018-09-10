package com.example.user.adminforquiz.model.api;

import com.google.gson.annotations.SerializedName;

import java.util.Date;
import java.util.List;

public class NwQuizTranslation {
    //db
    @SerializedName("id")
    Long id;
    //content
    @SerializedName("langCode")
    String langCode;
    @SerializedName("translation")
    String translation;
    @SerializedName("description")
    String description;
    @SerializedName("quizTranslationPhrases")
    List<NwQuizTranslationPhrase> quizTranslationPhrases;
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
