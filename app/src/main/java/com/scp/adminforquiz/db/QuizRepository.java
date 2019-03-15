package com.scp.adminforquiz.db;

import com.scp.adminforquiz.model.db.Quiz;
import com.scp.adminforquiz.model.db.QuizTranslation;
import com.scp.adminforquiz.model.db.QuizTranslationPhrase;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Flowable;

public class QuizRepository {
    private QuizDao quizDao;

    @Inject
    public QuizRepository(QuizDao quizDao) {
        this.quizDao = quizDao;
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
