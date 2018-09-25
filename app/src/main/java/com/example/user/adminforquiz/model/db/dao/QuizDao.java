package com.example.user.adminforquiz.model.db.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Transaction;
import android.arch.persistence.room.Update;

import com.example.user.adminforquiz.model.db.Quiz;
import com.example.user.adminforquiz.model.db.QuizTranslation;
import com.example.user.adminforquiz.model.db.QuizTranslationPhrase;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Flowable;
import io.reactivex.Single;

@Dao
public abstract class QuizDao {

    @Query("SELECT COUNT(*) FROM Quiz")
    public abstract Flowable<Long> getCountFlowable();


    @Query("SELECT COUNT(*) FROM Quiz")
    public abstract Long getCount();

    /**
     * returns all quizes, sorted by ID ASC
     */
    @Query("SELECT * FROM Quiz ORDER BY id ASC")
    public abstract Flowable<List<Quiz>> getAll();

    @Query("SELECT * FROM Quiz ORDER BY RANDOM() LIMIT :count")
    public abstract Flowable<List<Quiz>> getRandomQuizes(int count);

    @Query("SELECT * FROM Quiz WHERE id = :id")
    public abstract Flowable<List<Quiz>> getByIdWithUpdates(Long id);

    @Query("SELECT * FROM Quiz WHERE id = :id")
    public abstract Quiz getById(Long id);

    @Query("SELECT * FROM QuizTranslation WHERE quizId = :id")
    public abstract List<QuizTranslation> getQuizTranslationsByQuizId(Long id);

    @Query("SELECT * FROM QuizTranslation WHERE quizId = :id AND langCode = :lang")
    public abstract List<QuizTranslation> getQuizTranslationsByQuizIdAndLang(Long id, String lang);

    @Query("SELECT * FROM QuizTranslationPhrase WHERE quizTranslationId = :id")
    public abstract List<QuizTranslationPhrase> getQuizTranslationPhrasesByQuizTranslationId(Long id);

    @Query("SELECT * FROM quiz WHERE id = :id")
    public abstract Single<Quiz> getByIdOrErrorOnce(Long id);

    @Query("SELECT * FROM quiz WHERE id = :id")
    public abstract Flowable<Quiz> getQuizByIdOrErrorWithUpdates(Long id);

    @Query("SELECT * FROM quiz ORDER BY id ASC LIMIT 1")
    public abstract Single<Quiz> getFirst();

    @Query("SELECT id FROM quiz WHERE id > :quizId ORDER BY id ASC LIMIT 1")
    public abstract Single<Long> getNextQuizId(Long quizId);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public abstract Long insert(Quiz quiz);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public abstract List<Long> insertQuizTranslations(List<QuizTranslation> list);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public abstract List<Long> insertQuizTranslationPhrases(List<QuizTranslationPhrase> list);

    @Update
    public abstract int update(Quiz quiz);

    @Delete
    public abstract int delete(Quiz quiz);

    @Query("DELETE FROM Quiz")
    public abstract void deleteQuizTable();

    @Query("DELETE FROM QuizTranslation")
    public abstract void deleteQuizTranslationTable();

    @Query("DELETE FROM QuizTranslationPhrase")
    public abstract void deleteQuizTranslationPhraseTable();

    @Transaction
    public void deleteAllTables() {
        deleteQuizTable();
        deleteQuizTranslationTable();
        deleteQuizTranslationPhraseTable();
    }

    @Transaction
    public Long insertQuizWithQuizTranslations(Quiz quiz) {

        if (quiz.quizTranslations != null) {

            for (int i = 0; i < quiz.quizTranslations.size(); i++) {
                quiz.quizTranslations.get(i).quizId = quiz.id;

                if (quiz.quizTranslations != null) {
                    QuizTranslation quizTranslation = quiz.quizTranslations.get(i);

                    for (int j = 0; j < quizTranslation.quizTranslationPhrases.size(); j++) {
                        quizTranslation.quizTranslationPhrases.get(j).quizTranslationId = quizTranslation.id;
                    }
                    insertQuizTranslationPhrases(quizTranslation.quizTranslationPhrases);
                }
                insertQuizTranslations(quiz.quizTranslations);
            }
        }
        return insert(quiz);
    }

    @Transaction
    public List<Long> insertQuizesWithQuizTranslations(List<Quiz> quizes) {
        List<Long> longList = new ArrayList<>();
        for (int i = 0; i < quizes.size(); i++) {
            longList.add(insertQuizWithQuizTranslations(quizes.get(i)));
        }
        return longList;
    }

    @Transaction
    public Quiz getQuizWithTranslationsAndPhrases(Long id) {
        Quiz quiz = getById(id);
        quiz.quizTranslations = getQuizTranslationsByQuizId(id);
        for (int i = 0; i < quiz.quizTranslations.size(); i++) {
            QuizTranslation quizTranslation = quiz.quizTranslations.get(i);
            quizTranslation.quizTranslationPhrases = getQuizTranslationPhrasesByQuizTranslationId(quizTranslation.id);
        }
        return quiz;
    }
}