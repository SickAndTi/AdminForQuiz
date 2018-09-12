package com.example.user.adminforquiz.model.db;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;

import java.util.Date;
import java.util.List;

@Entity
public class QuizTranslation {
    //db
    @PrimaryKey
    public Long id;
    public Long quizId;
    //content
    public String langCode;
    public String translation;
    public String description;
    //status
    public Boolean approved;
    public Long authorId;
    public Long approverId;
    //dates
    public Date created;
    public Date updated;

    @Ignore
    List<QuizTranslationPhrase> quizTranslationPhrases;
}
