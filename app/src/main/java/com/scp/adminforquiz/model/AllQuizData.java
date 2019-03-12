package com.scp.adminforquiz.model;

import com.scp.adminforquiz.model.db.Quiz;
import com.scp.adminforquiz.model.db.QuizTranslation;
import com.scp.adminforquiz.model.db.QuizTranslationPhrase;

import java.util.List;

public class AllQuizData {

    private List<Quiz> quizList;
    private List<QuizTranslation> quizTranslationList;
    private List<QuizTranslationPhrase> quizTranslationPhraseList;
    private Boolean userFilterAscending;
    private String userSortFieldName;

    public AllQuizData(
            List<Quiz> quizList,
            List<QuizTranslation> quizTranslationList,
            List<QuizTranslationPhrase> quizTranslationPhraseList,
            Boolean userFilterAscending,
            String userSortFieldName
    ) {
        this.quizList = quizList;
        this.quizTranslationList = quizTranslationList;
        this.quizTranslationPhraseList = quizTranslationPhraseList;
        this.userFilterAscending = userFilterAscending;
        this.userSortFieldName = userSortFieldName;
    }

    public List<Quiz> getQuizList() {
        return quizList;
    }

    public List<QuizTranslation> getQuizTranslationList() {
        return quizTranslationList;
    }

    public List<QuizTranslationPhrase> getQuizTranslationPhraseList() {
        return quizTranslationPhraseList;
    }

    public Boolean getUserFilterAscending() {
        return userFilterAscending;
    }

    public String getUserSortFieldName() {
        return userSortFieldName;
    }
}
