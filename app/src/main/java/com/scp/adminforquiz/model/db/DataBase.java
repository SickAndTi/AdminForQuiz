package com.scp.adminforquiz.model.db;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.TypeConverters;

import com.scp.adminforquiz.model.db.dao.QuizDao;
import com.scp.adminforquiz.model.db.dao.QuizTranslationDao;
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