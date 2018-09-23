package com.example.user.adminforquiz.model.db;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.arch.persistence.room.TypeConverters;

import com.example.user.adminforquiz.util.DateTypeConverter;

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
    @TypeConverters(DateTypeConverter.class)
    public Date created;
    @TypeConverters(DateTypeConverter.class)
    public Date updated;

    @Override
    public String toString() {
        return "QuizTranslation{" +
                "id=" + id +
                ", quizId=" + quizId +
                ", langCode='" + langCode + '\'' +
                ", translation='" + translation + '\'' +
                ", description='" + description + '\'' +
                ", approved=" + approved +
                ", authorId=" + authorId +
                ", approverId=" + approverId +
                ", created=" + created +
                ", updated=" + updated +
                ", quizTranslationPhrases=" + quizTranslationPhrases +
                '}';
    }

    @Ignore
    public List<QuizTranslationPhrase> quizTranslationPhrases;
}
