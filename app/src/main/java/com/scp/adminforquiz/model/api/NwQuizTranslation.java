package com.scp.adminforquiz.model.api;

import com.google.gson.annotations.SerializedName;

import java.util.Date;
import java.util.List;

public class NwQuizTranslation {
    //db
    @SerializedName("id")
    public Long id;
    //content
    @SerializedName("langCode")
    public String langCode;
    @SerializedName("translation")
    public String translation;
    @SerializedName("description")
    public String description;
    @SerializedName("quizTranslationPhrases")
    public List<NwQuizTranslationPhrase> quizTranslationPhrases;
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
    @SerializedName("user")
    public NwUser nwUser;
}
