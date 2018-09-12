package com.example.user.adminforquiz.model.db;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

import com.example.user.adminforquiz.model.db.dao.QuizDao;
import com.example.user.adminforquiz.model.db.dao.QuizTranslationDao;

@Database(version = 2,
        entities = {Quiz.class,
                QuizTranslation.class,
                QuizTranslationPhrase.class},
        exportSchema = false)

public abstract class DataBase extends RoomDatabase {
    abstract public QuizDao quizDao();

    abstract public QuizTranslationDao quizTranslationDao();

}