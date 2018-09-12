package com.example.user.adminforquiz.model.db.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.example.user.adminforquiz.model.db.QuizTranslation;

import java.util.List;

import io.reactivex.Flowable;
import io.reactivex.Single;

@Dao
public interface QuizTranslationDao {
    @Query("SELECT * FROM quizTranslation")
    Flowable<List<QuizTranslation>> getAll();

    @Query("SELECT * FROM quizTranslation WHERE id = :id")
    Flowable<List<QuizTranslation>> getByIdWithUpdates(Long id);

    @Query("SELECT * FROM quizTranslation WHERE id = :id")
    Single<QuizTranslation> getByIdOrErrorOnce(Long id);

    @Query("SELECT langCode FROM quizTranslation GROUP BY langCode")
    Single<List<String>> getAllLangsSignle();

    @Query("SELECT langCode FROM quizTranslation GROUP BY langCode")
    List<String> getAllLangs();

    @Insert
    Long insert(QuizTranslation quiz);

    @Update
    int update(QuizTranslation quiz);

    @Delete
    int delete(QuizTranslation quiz);
}
