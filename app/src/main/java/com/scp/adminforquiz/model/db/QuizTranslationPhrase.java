package com.scp.adminforquiz.model.db;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.arch.persistence.room.TypeConverters;

import com.scp.adminforquiz.util.DateTypeConverter;

import org.jetbrains.annotations.Nullable;

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
    @TypeConverters(DateTypeConverter.class)
    public Date created;
    @TypeConverters(DateTypeConverter.class)
    public Date updated;

    @Ignore
    @Nullable
    public User author;
    @Ignore
    @Nullable
    public User approver;
}