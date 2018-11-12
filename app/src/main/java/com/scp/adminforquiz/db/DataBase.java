package com.scp.adminforquiz.db;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.TypeConverters;

import com.scp.adminforquiz.model.db.Quiz;
import com.scp.adminforquiz.model.db.QuizTranslation;
import com.scp.adminforquiz.model.db.QuizTranslationPhrase;
import com.scp.adminforquiz.model.db.User;
import com.scp.adminforquiz.util.DateTypeConverter;

@Database(version = 2,
        entities = {Quiz.class,
                QuizTranslation.class,
                QuizTranslationPhrase.class,
                User.class},
        exportSchema = false)

@TypeConverters(DateTypeConverter.class)

public abstract class DataBase extends RoomDatabase {
    abstract public QuizDao quizDao();
}