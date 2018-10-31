package com.scp.adminforquiz.model.api;

import com.google.gson.annotations.SerializedName;

import java.util.Date;
import java.util.List;

public class NwQuiz {
    @SerializedName("id")
    public Long id;
    @SerializedName("scpNumber")
    public String scpNumber;
    @SerializedName("imageUrl")
    public String imageUrl;
    @SerializedName("quizTranslations")
    public List<NwQuizTranslation> quizTranslations;
    @SerializedName("authorId")
    public Long authorId;
    @SerializedName("approved")
    public Boolean approved;
    @SerializedName("approverId")
    public Long approverId;
    @SerializedName("created")
    public Date created;
    @SerializedName("updated")
    public Date updated;
    @SerializedName("author")
    public NwUser author;
    @SerializedName("approver")
    public NwUser approver;
}
