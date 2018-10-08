package com.example.user.adminforquiz.model.db;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.TypeConverters;

import com.example.user.adminforquiz.model.db.dao.QuizDao;
import com.example.user.adminforquiz.model.db.dao.QuizTranslationDao;
import com.example.user.adminforquiz.util.DateTypeConverter;

@Database(version = 2,
        entities = {Quiz.class,
                QuizTranslation.class,
                QuizTranslationPhrase.class},
        exportSchema = false)

@TypeConverters(DateTypeConverter.class)

public abstract class DataBase extends RoomDatabase {
    abstract public QuizDao quizDao();

    abstract public QuizTranslationDao quizTranslationDao();

}