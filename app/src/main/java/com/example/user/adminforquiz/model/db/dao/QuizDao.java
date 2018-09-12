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

import java.util.List;

import io.reactivex.Flowable;
import io.reactivex.Single;

@Dao
public abstract class QuizDao {

    @Query("SELECT COUNT(*) FROM Quiz")
    abstract Flowable<Long> getCountFlowable();


    @Query("SELECT COUNT(*) FROM Quiz")
    abstract Long getCount();

    /**
     * returns all quizes, sorted by ID ASC
     */
    @Query("SELECT * FROM Quiz ORDER BY id ASC")
    abstract Flowable<List<Quiz>> getAll();

    @Query("SELECT * FROM Quiz ORDER BY RANDOM() LIMIT :count")
    abstract Flowable<List<Quiz>> getRandomQuizes(int count);

    @Query("SELECT * FROM Quiz WHERE id = :id")
    abstract Flowable<List<Quiz>> getByIdWithUpdates(Long id);

    @Query("SELECT * FROM Quiz WHERE id = :id")
    abstract Quiz getById(Long id);

    @Query("SELECT * FROM QuizTranslation WHERE quizId = :id")
    abstract List<QuizTranslation> getQuizTranslationsByQuizId(Long id);

    @Query("SELECT * FROM QuizTranslation WHERE quizId = :id AND langCode = :lang")
    abstract List<QuizTranslation> getQuizTranslationsByQuizIdAndLang(Long id, String lang);

    @Query("SELECT * FROM QuizTranslationPhrase WHERE quizTranslationId = :id")
    abstract List<QuizTranslationPhrase> getQuizTranslationPhrasesByQuizTranslationId(Long id);

    @Query("SELECT * FROM quiz WHERE id = :id")
    abstract Single<Quiz> getByIdOrErrorOnce(Long id);

    @Query("SELECT * FROM quiz ORDER BY id ASC LIMIT 1")
    abstract Single<Quiz> getFirst();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract Long insert(Quiz quiz);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract List<Long> insertQuizTranslations(List<QuizTranslation> list);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract List<Long> insertQuizTranslationPhrases(List<QuizTranslationPhrase> list);

    @Update
    abstract int update(Quiz quiz);

    @Delete
    abstract int delete(Quiz quiz);

    @Transaction
    public Long insertQuizWithQuizTranslations(Quiz quiz) {
        insert(quiz);
        insertQuizTranslations(quiz.quizTranslations);
        return insert(quiz);
    }
}


//    @Transaction
//    public List<Long> insertQuizesWithQuizTranslations(List<Quiz> quizes) {
//        quizes.stream().map(insertQuizWithQuizTranslations((Quiz) quizes));


//        @Transaction
//        public Quiz getQuizWithTranslationsAndPhrases (Long id)

//        {
//            val quiz = getById(id)
//            quiz.quizTranslations = getQuizTranslationsByQuizId(id)
//            quiz.quizTranslations ?.forEach {
//            quizTranslation ->
//                    quizTranslation.quizTranslationPhrases = getQuizTranslationPhrasesByQuizTranslationId(quizTranslation.id)
//        }
//            return quiz
//        }
//
//        @Transaction
//        fun getQuizWithTranslationsAndPhrases (id:Long, lang:String):Quiz
//
//        {
//            val quiz = getById(id)
//            quiz.quizTranslations = getQuizTranslationsByQuizIdAndLang(id, lang)
//            quiz.quizTranslations ?.forEach {
//            quizTranslation ->
//                    quizTranslation.quizTranslationPhrases = getQuizTranslationPhrasesByQuizTranslationId(quizTranslation.id)
//        }
//            return quiz
//        }
//
//        @Query("SELECT id FROM quiz WHERE id > :quizId ORDER BY id ASC LIMIT 1")
//        fun getNextQuizId (quizId:Long):Single<Long>
//    }
