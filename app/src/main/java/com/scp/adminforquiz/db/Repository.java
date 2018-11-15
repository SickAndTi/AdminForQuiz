package com.scp.adminforquiz.db;

import com.scp.adminforquiz.model.db.Quiz;
import com.scp.adminforquiz.model.db.QuizTranslation;
import com.scp.adminforquiz.model.db.QuizTranslationPhrase;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Flowable;

public class Repository {
    private QuizDao quizDao;

    @Inject
    public Repository(QuizDao quizDao) {
        this.quizDao = quizDao;
    }

    public Quiz getFullQuizById(Long quizId) {
        return quizDao.getQuizWithTranslationsAndPhrases(quizId);
    }

    public Flowable<Quiz> getQuizByIdFlowable(Long quizId) {
        return quizDao.getQuizByIdOrErrorWithUpdates(quizId);
    }

    public Flowable<List<QuizTranslation>> getTranslationsByIdFlowable(Long quizId) {
        return quizDao.getQuizTranslationsByQuizIdWithUpdates(quizId);
    }

    public Flowable<List<QuizTranslationPhrase>> getPhrasesByIdFlowable(Long quizId) {
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

    public Long getQuizIdByTranslationId(Long quizTranslationId) {
        return quizDao.getQuizIdByQuizTranslationId(quizTranslationId);
    }

    public Long insertQuiz(Quiz quiz) {
        return quizDao.insertQuizWithQuizTranslations(quiz);
    }

    public List<Long> insertQuizzes(List<Quiz> quizzes) {
        return quizDao.insertQuizesWithQuizTranslations(quizzes);
    }

    public Long insertTranslation(QuizTranslation quizTranslation) {
        return quizDao.insertQuizTranslationWithPhrases(quizTranslation);
    }

    public Long insertPhrase(QuizTranslationPhrase quizTranslationPhrase) {
        return quizDao.insertQuizTranslationPhrase(quizTranslationPhrase);
    }

    public int deleteQuiz(Long quizId) {
        return quizDao.deleteQuizById(quizId);
    }

    public int deleteTranslation(Long translationId) {
        return quizDao.deleteQuizTranslationById(translationId);
    }

    public int deletePhrase(Long phraseId) {
        return quizDao.deleteQuizTranslationPhraseById(phraseId);
    }

    public void deleteAllTables() {
        quizDao.deleteAllTables();
    }
}
