package com.scp.adminforquiz.db;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Transaction;
import android.arch.persistence.room.Update;
import android.support.annotation.NonNull;

import com.scp.adminforquiz.Constants;
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
    public abstract long getCount();

    /**
     * returns all quizes, sorted by ID ASC
     */
    @Query("SELECT * FROM Quiz ORDER BY id ASC")
    public abstract Flowable<List<Quiz>> getAll();

    @Query("SELECT * FROM QuizTranslation ORDER BY id ASC")
    public abstract Flowable<List<QuizTranslation>> getAllQuizTranslation();

    @Query("SELECT * FROM QuizTranslationPhrase ORDER BY id ASC")
    public abstract Flowable<List<QuizTranslationPhrase>> getAllQuizTranslationPhrase();

    @Query("SELECT id FROM Quiz ORDER BY id ASC")
    public abstract List<Long> getAllQuizIds();

    @Query("SELECT id FROM Quiz ORDER BY id DESC")
    public abstract List<Long> getAllQuizIdsDesc();

    @Query("SELECT id FROM Quiz ORDER BY created ASC")
    public abstract List<Long> getAllQuizIdsByDateCreatedAsc();

    @Query("SELECT id FROM Quiz ORDER BY created DESC")
    public abstract List<Long> getAllQuizIdsByDateCreatedDesc();

    @Query("SELECT id FROM Quiz ORDER BY updated ASC")
    public abstract List<Long> getAllQuizIdsByDateUpdatedAsc();

    @Query("SELECT id FROM Quiz ORDER BY updated DESC")
    public abstract List<Long> getAllQuizIdsByDateUpdatedDesc();

    @Query("SELECT id FROM Quiz ORDER BY approved ASC")
    public abstract List<Long> getAllQuizIdsByApprovedAsc();

    @Query("SELECT id FROM Quiz ORDER BY approved DESC")
    public abstract List<Long> getAllQuizIdsByApprovedDesc();

    @Query("SELECT * FROM Quiz ORDER BY RANDOM() LIMIT :count")
    public abstract Flowable<List<Quiz>> getRandomQuizes(int count);

    @Query("SELECT * FROM Quiz WHERE id = :id")
    public abstract Flowable<List<Quiz>> getByIdWithUpdates(long id);

    @Query("SELECT * FROM Quiz WHERE id = :id")
    abstract Quiz getById(long id);

    @Query("SELECT * FROM User WHERE id = :authorId")
    abstract User getUserById(Long authorId);

    @Query("SELECT * FROM QuizTranslation WHERE quizId = :id")
    abstract List<QuizTranslation> getQuizTranslationsByQuizId(long id);

    @Query("SELECT quizId FROM QuizTranslation WHERE id =:quizTranslationId")
    abstract long getQuizIdByQuizTranslationId(long quizTranslationId);

    @Query("SELECT description FROM QuizTranslation WHERE id = :quizTranslationId")
    public abstract Single<String> getQuizTranslationDescriptionByQuizTranslationId(long quizTranslationId);

    @Query("SELECT * FROM QuizTranslation WHERE quizId = :id")
    abstract Flowable<List<QuizTranslation>> getQuizTranslationsByQuizIdWithUpdates(long id);

    @Query("SELECT authorId FROM Quiz WHERE id = :quizId")
    public abstract long getAuthorIdByQuizId(long quizId);

    @Query("SELECT authorId FROM QuizTranslation WHERE id = :quizTranslationId")
    public abstract long getAuthorIdByQuizTranslationId(long quizTranslationId);

    @Query("SELECT authorId FROM QuizTranslationPhrase WHERE id = :quizTranslationPhraseId")
    public abstract long getAuthorIdByQuizTranslationPhraseId(long quizTranslationPhraseId);

    @Query("SELECT * FROM QuizTranslation WHERE quizId = :id AND langCode = :lang")
    public abstract List<QuizTranslation> getQuizTranslationsByQuizIdAndLang(long id, String lang);

    @Query("SELECT * FROM QuizTranslationPhrase WHERE quizTranslationId = :id")
    public abstract List<QuizTranslationPhrase> getQuizTranslationPhrasesByQuizTranslationId(long id);

    @Query("SELECT * FROM quiz WHERE id = :id")
    public abstract Single<Quiz> getByIdOrErrorOnce(long id);

    @Query("SELECT * FROM quiz WHERE id = :id")
    public abstract Flowable<Quiz> getQuizByIdOrErrorWithUpdates(long id);

    @Query("SELECT * FROM quiz ORDER BY id ASC LIMIT 1")
    public abstract Single<Quiz> getFirst();

    @Query("SELECT id FROM quiz WHERE id > :quizId ORDER BY id ASC LIMIT 1")
    public abstract Single<Long> getNextQuizId(long quizId);

    @Query("SELECT * FROM QuizTranslationPhrase WHERE quizTranslationId IN (SELECT tr.id FROM QuizTranslation tr WHERE tr.quizId = :quizId)")
    public abstract Flowable<List<QuizTranslationPhrase>> getQuizTranslationPhrasesByQuizIdWithUpdates(long quizId);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public abstract long insert(Quiz quiz);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public abstract List<Long> insertQuizTranslations(List<QuizTranslation> list);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public abstract long insertUser(@NonNull User user);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public abstract long insertQuizTranslation(QuizTranslation quizTranslation);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public abstract List<Long> insertQuizTranslationPhrases(List<QuizTranslationPhrase> list);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public abstract long insertQuizTranslationPhrase(QuizTranslationPhrase phrase);

    @Update
    public abstract int update(Quiz quiz);

    @Delete
    public abstract int delete(Quiz quiz);

    @Query("DELETE FROM quiz WHERE id = :quizId")
    public abstract int deleteQuizById(long quizId);

    @Query("DELETE FROM QuizTranslation WHERE id = :quizTranslationId")
    public abstract int deleteQuizTranslationById(long quizTranslationId);

    @Query("DELETE FROM QuizTranslationPhrase WHERE id = :quizTranslationPhraseId")
    public abstract int deleteQuizTranslationPhraseById(long quizTranslationPhraseId);

    @Query("DELETE FROM Quiz")
    public abstract void deleteQuizTable();

    @Query("DELETE FROM QuizTranslation")
    public abstract void deleteQuizTranslationTable();

    @Query("DELETE FROM QuizTranslationPhrase")
    public abstract void deleteQuizTranslationPhraseTable();

    @Transaction
    public void deleteAllQuizTables() {
        deleteQuizTable();
        deleteQuizTranslationTable();
        deleteQuizTranslationPhraseTable();
    }

    @Transaction
    public long insertQuizWithQuizTranslations(Quiz quiz) {
        if (quiz.author != null) {
            insertUser(quiz.author);
        }
        if (quiz.approver != null) {
            insertUser(quiz.approver);
        }
        if (quiz.quizTranslations != null) {
            for (QuizTranslation quizTranslation : quiz.quizTranslations) {
                quizTranslation.quizId = quiz.id;
                if (quizTranslation.author != null) {
                    insertUser(quizTranslation.author);
                }
                if (quizTranslation.approver != null) {
                    insertUser(quizTranslation.approver);
                }
                if (quizTranslation.quizTranslationPhrases != null) {
                    for (QuizTranslationPhrase quizTranslationPhrase : quizTranslation.quizTranslationPhrases) {
                        quizTranslationPhrase.quizTranslationId = quizTranslation.id;
                        if (quizTranslationPhrase.author != null) {
                            insertUser(quizTranslationPhrase.author);
                        }
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
    public long insertQuizTranslationWithPhrases(QuizTranslation quizTranslation) {
        if (quizTranslation.author != null) {
            insertUser(quizTranslation.author);
        }
        if (quizTranslation.approver != null) {
            insertUser(quizTranslation.approver);
        }
        for (QuizTranslationPhrase quizTranslationPhrase : quizTranslation.quizTranslationPhrases) {
            quizTranslationPhrase.quizTranslationId = quizTranslation.id;
            if (quizTranslationPhrase.author != null) {
                insertUser(quizTranslationPhrase.author);
            }
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
    Quiz getQuizWithTranslationsAndPhrases(long id) {
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
    public List<Quiz> getAllQuizzesWithTranslationsAndPhrases(boolean ascending, String sortFieldName) {
        List<Quiz> quizList = new ArrayList<>();
        if (sortFieldName != null) {
            if (ascending) {
                switch (sortFieldName) {
                    case Constants.CREATED:
                        for (Long quizId : getAllQuizIdsByDateCreatedAsc()) {
                            quizList.add(getQuizWithTranslationsAndPhrases(quizId));
                        }
                        break;
                    case Constants.UPDATED:
                        for (Long quizId : getAllQuizIdsByDateUpdatedAsc()) {
                            quizList.add(getQuizWithTranslationsAndPhrases(quizId));
                        }
                        break;
                    case Constants.APPROVE:
                        for (Long quizId : getAllQuizIdsByApprovedAsc()) {
                            quizList.add(getQuizWithTranslationsAndPhrases(quizId));
                        }
                        break;
                    case Constants.ID:
                        for (Long quizId : getAllQuizIds()) {
                            quizList.add(getQuizWithTranslationsAndPhrases(quizId));
                        }
                        break;
                }
            } else {
                switch (sortFieldName) {
                    case Constants.CREATED:
                        for (Long quizId : getAllQuizIdsByDateCreatedDesc()) {
                            quizList.add(getQuizWithTranslationsAndPhrases(quizId));
                        }
                        break;
                    case Constants.UPDATED:
                        for (Long quizId : getAllQuizIdsByDateUpdatedDesc()) {
                            quizList.add(getQuizWithTranslationsAndPhrases(quizId));
                        }
                        break;
                    case Constants.APPROVE:
                        for (Long quizId : getAllQuizIdsByApprovedDesc()) {
                            quizList.add(getQuizWithTranslationsAndPhrases(quizId));
                        }
                        break;
                    case Constants.ID:
                        for (Long quizId : getAllQuizIdsDesc()) {
                            quizList.add(getQuizWithTranslationsAndPhrases(quizId));
                        }
                        break;
                }
            }
        } else {
            for (Long quizId : getAllQuizIds()) {
                quizList.add(getQuizWithTranslationsAndPhrases(quizId));
            }
        }
        return quizList;
    }
}