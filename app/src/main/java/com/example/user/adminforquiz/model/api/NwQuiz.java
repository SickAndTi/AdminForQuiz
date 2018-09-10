package com.example.user.adminforquiz.model.api;

import com.google.gson.annotations.SerializedName;

import java.util.Date;
import java.util.List;

public class NwQuiz {
    //db
    @SerializedName("id")
    Long id;
    //content
    @SerializedName("scpNumber")
    public String scpNumber;
    @SerializedName("imageUrl")
    public String imageUrl;
    @SerializedName("quizTranslations")
    public List<NwQuizTranslation> quizTranslations;
    //status
    @SerializedName("authorId")
    public Long authorId;
    @SerializedName("approved")
    public Boolean approved;
    @SerializedName("approverId")
    public Long approverId;
    //dates
    @SerializedName("created")
    public Date created;
    @SerializedName("updated")
    public Date updated;
}
