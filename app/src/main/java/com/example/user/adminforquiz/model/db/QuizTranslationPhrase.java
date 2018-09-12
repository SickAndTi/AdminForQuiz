package com.example.user.adminforquiz.model.db;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

import java.util.Date;

@Entity
public class QuizTranslationPhrase {
    //db
    @PrimaryKey
    public Long id;
    public Long quizTranslationId;
    //content
    public String translation;
    //status
    public Boolean approved;
    public Long authorId;
    public Long approverId;
    //dates
    public Date created;
    public Date updated;
}