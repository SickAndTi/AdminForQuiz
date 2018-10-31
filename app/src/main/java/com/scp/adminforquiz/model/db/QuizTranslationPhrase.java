package com.scp.adminforquiz.model.db;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.arch.persistence.room.TypeConverters;

import com.scp.adminforquiz.util.DateTypeConverter;

import java.util.Date;
import java.util.List;

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

    @Override
    public String toString() {
        return "QuizTranslationPhrase{" +
                "id=" + id +
                ", quizTranslationId=" + quizTranslationId +
                ", translation='" + translation + '\'' +
//                ", approved=" + approved +
//                ", authorId=" + authorId +
//                ", approverId=" + approverId +
//                ", created=" + created +
//                ", updated=" + updated +
                '}';
    }
    @Ignore
    public User author;
    @Ignore
    public User approver;
}