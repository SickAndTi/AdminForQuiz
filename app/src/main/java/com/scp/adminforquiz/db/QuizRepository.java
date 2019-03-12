package com.scp.adminforquiz.db;

import com.jakewharton.rxrelay2.BehaviorRelay;
import com.scp.adminforquiz.model.db.Quiz;
import com.scp.adminforquiz.model.db.QuizTranslation;
import com.scp.adminforquiz.model.db.QuizTranslationPhrase;
import com.scp.adminforquiz.preference.MyPreferenceManager;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.Observable;

public class QuizRepository {
    private QuizDao quizDao;

    @NotNull
    private MyPreferenceManager preferences;

    private BehaviorRelay<Boolean> userFilterAscendingTypeRelay;

    private BehaviorRelay<String> userSortFieldNameRelay;


    @Inject
    public QuizRepository(QuizDao quizDao, @NotNull MyPreferenceManager preferences) {
        this.quizDao = quizDao;
        this.preferences = preferences;
        userFilterAscendingTypeRelay = BehaviorRelay.createDefault(this.preferences.getUserFilterAscending());
        userSortFieldNameRelay = BehaviorRelay.createDefault(this.preferences.getUserSortFieldName());
    }

    public Flowable<Boolean> getUserFilterAscendingType() {
        return userFilterAscendingTypeRelay.toFlowable(BackpressureStrategy.BUFFER);
    }

    public void setUserFilterAscendingType(boolean userFilterAscendingType) {
        preferences.setUserFilterAscending(userFilterAscendingType);
        userFilterAscendingTypeRelay.accept(userFilterAscendingType);
    }

    public Flowable<String> getUserSortFieldName() {
        return userSortFieldNameRelay.toFlowable(BackpressureStrategy.BUFFER);
    }

    public void setUserSortFieldName(String userSortFieldName) {
        preferences.setUserSortFieldName(userSortFieldName);
        userSortFieldNameRelay.accept(userSortFieldName);
    }

    public Quiz getFullQuizById(long quizId) {
        return quizDao.getQuizWithTranslationsAndPhrases(quizId);
    }

    public Flowable<Quiz> getQuizByIdFlowable(long quizId) {
        return quizDao.getQuizByIdOrErrorWithUpdates(quizId);
    }

    public Flowable<List<QuizTranslation>> getTranslationsByIdFlowable(long quizId) {
        return quizDao.getQuizTranslationsByQuizIdWithUpdates(quizId);
    }

    public Flowable<List<QuizTranslationPhrase>> getPhrasesByIdFlowable(long quizId) {
        return quizDao.getQuizTranslationPhrasesByQuizIdWithUpdates(quizId);
    }

    public List<Quiz> getQuizzesSorted(boolean ascending, String sortFieldName) {
        return quizDao.getAllQuizzesWithTranslationsAndPhrases(ascending, sortFieldName);
    }

    public Flowable<List<Quiz>> getAllQuizzes() {
        return quizDao.getAll();
    }

    public Flowable<List<QuizTranslation>> getAllTranslations() {
        return quizDao.getAllQuizTranslation();
    }

    public Flowable<List<QuizTranslationPhrase>> getAllPhrases() {
        return quizDao.getAllQuizTranslationPhrase();
    }

    public long getQuizIdByTranslationId(long quizTranslationId) {
        return quizDao.getQuizIdByQuizTranslationId(quizTranslationId);
    }

    public long insertQuiz(Quiz quiz) {
        return quizDao.insertQuizWithQuizTranslations(quiz);
    }

    public List<Long> insertQuizzes(List<Quiz> quizzes) {
        return quizDao.insertQuizesWithQuizTranslations(quizzes);
    }

    public long insertTranslation(QuizTranslation quizTranslation) {
        return quizDao.insertQuizTranslationWithPhrases(quizTranslation);
    }

    public long insertPhrase(QuizTranslationPhrase quizTranslationPhrase) {
        return quizDao.insertQuizTranslationPhrase(quizTranslationPhrase);
    }

    public int deleteQuiz(long quizId) {
        return quizDao.deleteQuizById(quizId);
    }

    public int deleteTranslation(long translationId) {
        return quizDao.deleteQuizTranslationById(translationId);
    }

    public int deletePhrase(long phraseId) {
        return quizDao.deleteQuizTranslationPhraseById(phraseId);
    }

    public void deleteAllQuizTables() {
        quizDao.deleteAllQuizTables();
    }
}
