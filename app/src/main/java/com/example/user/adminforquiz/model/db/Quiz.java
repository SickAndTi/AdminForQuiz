package com.example.user.adminforquiz.model.db;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;

import java.util.Date;
import java.util.List;

@Entity
public class Quiz {

    //db
    @PrimaryKey
    public Long id;
    //content
    public String scpNumber;
    public String imageUrl;
    //status
    public Long authorId;
    public boolean approved;
    public Long approverId;
    //dates
    public Date created;
    public Date updated;

    @Ignore
    public List<QuizTranslation> quizTranslations;
}




