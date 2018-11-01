package com.scp.adminforquiz.model.db.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Transaction;
import android.arch.persistence.room.Update;

import com.scp.adminforquiz.model.db.Quiz;
import com.scp.adminforquiz.model.db.QuizTranslation;
import com.scp.adminforquiz.model.db.QuizTranslationPhrase;
import com.scp.adminforquiz.model.db.User;

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

    @Query("SELECT * FROM Quiz ORDER BY id DESC")
    public abstract Flowable<List<Quiz>> getAllDesc();

    @Query("SELECT * FROM Quiz ORDER BY created ASC")
    public abstract Flowable<List<Quiz>> getAllByDateCreatedAsc();

    @Query("SELECT * FROM Quiz ORDER BY created DESC")
    public abstract Flowable<List<Quiz>> getAllByDateCreatedDesc();

    @Query("SELECT * FROM Quiz ORDER BY updated ASC")
    public abstract Flowable<List<Quiz>> getAllByDateUpeatedAsc();

    @Query("SELECT * FROM Quiz ORDER BY updated DESC")
    public abstract Flowable<List<Quiz>> getAllByDateUpeatedDesc();

    @Query("SELECT * FROM Quiz ORDER BY approved ASC")
    public abstract Flowable<List<Quiz>> getAllByApprovedAsc();

    @Query("SELECT * FROM Quiz ORDER BY approved DESC")
    public abstract Flowable<List<Quiz>> getAllByApprovedDesc();

    @Query("SELECT id FROM Quiz ORDER BY id ASC")
    public abstract List<Long> getAllQuizIds();

    @Query("SELECT * FROM Quiz ORDER BY RANDOM() LIMIT :count")
    public abstract Flowable<List<Quiz>> getRandomQuizes(int count);

    @Query("SELECT * FROM Quiz WHERE id = :id")
    public abstract Flowable<List<Quiz>> getByIdWithUpdates(Long id);

    @Query("SELECT * FROM Quiz WHERE id = :id")
    public abstract Quiz getById(Long id);

    @Query("SELECT * FROM User WHERE id = :authorId")
    public abstract User getUserById(Long authorId);

    @Query("SELECT * FROM QuizTranslation WHERE quizId = :id")
    public abstract List<QuizTranslation> getQuizTranslationsByQuizId(Long id);

    @Query("SELECT quizId FROM QuizTranslation WHERE id =:quizTranslationId")
    public abstract Long getQuizIdByQuizTranslationId(Long quizTranslationId);

    @Query("SELECT description FROM QuizTranslation WHERE id = :quizTranslationId")
    public abstract Single<String> getQuizTranslationDescriptionByQuizTranslationId(Long quizTranslationId);

    @Query("SELECT * FROM QuizTranslation WHERE quizId = :id")
    public abstract Flowable<List<QuizTranslation>> getQuizTranslationsByQuizIdWithUpdates(Long id);

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

    @Query("SELECT * FROM QuizTranslationPhrase WHERE quizTranslationId IN (SELECT tr.id FROM QuizTranslation tr WHERE tr.quizId = :quizId)")
    public abstract Flowable<List<QuizTranslationPhrase>> getQuizTranslationPhrasesByQuizIdWithUpdates(Long quizId);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public abstract Long insert(Quiz quiz);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public abstract List<Long> insertQuizTranslations(List<QuizTranslation> list);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public abstract Long insertUser(User user);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public abstract Long insertQuizTranslation(QuizTranslation quizTranslation);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public abstract List<Long> insertQuizTranslationPhrases(List<QuizTranslationPhrase> list);

    @Update
    public abstract int update(Quiz quiz);

    @Delete
    public abstract int delete(Quiz quiz);

    @Query("DELETE FROM quiz WHERE id = :quizId")
    public abstract int deleteQuizById(Long quizId);

    @Query("DELETE FROM QuizTranslation WHERE id = :quizTranslationId")
    public abstract int deleteQuizTranslationById(Long quizTranslationId);

    @Query("DELETE FROM QuizTranslationPhrase WHERE id = :quizTranslationPhraseId")
    public abstract int deleteQuizTranslationPhraseById(Long quizTranslationPhraseId);

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
        insertUser(quiz.author);
        if (quiz.approver != null) {
            insertUser(quiz.approver);
        }
        if (quiz.quizTranslations != null) {
            for (QuizTranslation quizTranslation : quiz.quizTranslations) {
                quizTranslation.quizId = quiz.id;
                insertUser(quizTranslation.author);
                if (quizTranslation.approver != null) {
                    insertUser(quizTranslation.approver);
                }
                if (quizTranslation.quizTranslationPhrases != null) {
                    for (QuizTranslationPhrase quizTranslationPhrase : quizTranslation.quizTranslationPhrases) {
                        quizTranslationPhrase.quizTranslationId = quizTranslation.id;
                        insertUser(quizTranslationPhrase.author);
                        if (quizTranslationPhrase.approver != null) {
                            insertUser(quizTranslationPhrase.approver);
                        }
                    }
                    insertQuizTranslationPhrases(quizTranslation.quizTranslationPhrases);
                }
            }
            insertQuizTranslations(quiz.quizTranslations);
        }
        return insert(quiz);
    }

    @Transaction
    public Long insertQuizTranslationWithPhrases(QuizTranslation quizTranslation) {
        insertUser(quizTranslation.author);
        if (quizTranslation.approver != null) {
            insertUser(quizTranslation.approver);
        }
        for (QuizTranslationPhrase quizTranslationPhrase : quizTranslation.quizTranslationPhrases) {
            quizTranslationPhrase.quizTranslationId = quizTranslation.id;
            insertUser(quizTranslationPhrase.author);
            if (quizTranslationPhrase.approver != null) {
                insertUser(quizTranslationPhrase.approver);
            }
        }
        insertQuizTranslationPhrases(quizTranslation.quizTranslationPhrases);
        return insertQuizTranslation(quizTranslation);
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
        quiz.author = getUserById(quiz.authorId);
        quiz.approver = getUserById(quiz.approverId);
        for (int i = 0; i < quiz.quizTranslations.size(); i++) {
            QuizTranslation quizTranslation = quiz.quizTranslations.get(i);
            quizTranslation.author = getUserById(quizTranslation.authorId);
            quizTranslation.approver = getUserById(quizTranslation.approverId);
            quizTranslation.quizTranslationPhrases = getQuizTranslationPhrasesByQuizTranslationId(quizTranslation.id);
            for (QuizTranslationPhrase quizTranslationPhrase : quizTranslation.quizTranslationPhrases) {
                quizTranslationPhrase.author = getUserById(quizTranslationPhrase.authorId);
                quizTranslationPhrase.approver = getUserById(quizTranslationPhrase.approverId);
            }
        }
        return quiz;
    }

    @Transaction
    public List<Quiz> getAllQuizzesWithTranslationsAndPhrases() {
        List<Quiz> quizList = new ArrayList<>();
        for (Long quizId : getAllQuizIds()) {
            quizList.add(getQuizWithTranslationsAndPhrases(quizId));
        }
        return quizList;
    }
}