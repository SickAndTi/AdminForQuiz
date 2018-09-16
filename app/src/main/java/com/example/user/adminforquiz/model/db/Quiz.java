package com.example.user.adminforquiz.model.db;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.arch.persistence.room.TypeConverters;

import com.example.user.adminforquiz.util.DateTypeConverter;

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
    @TypeConverters(DateTypeConverter.class)
    public Date created;
    @TypeConverters(DateTypeConverter.class)
    public Date updated;

    @Ignore
    public List<QuizTranslation> quizTranslations;
}




