package com.scp.adminforquiz.model.db;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;

import java.util.List;

@Entity
public class User {
    //id = quiz.authorId
    @PrimaryKey
    public Long id;
    //content
    public String fullName;
    public String avatar;

    @Ignore
    public List<QuizTranslation> quizTranslations;
    @Ignore
    public List<QuizTranslationPhrase> quizTranslationPhraseList;
}
