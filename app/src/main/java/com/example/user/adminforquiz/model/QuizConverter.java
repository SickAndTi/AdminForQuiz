package com.example.user.adminforquiz.model;

import com.example.user.adminforquiz.model.api.NwQuiz;
import com.example.user.adminforquiz.model.api.NwQuizTranslation;
import com.example.user.adminforquiz.model.api.NwQuizTranslationPhrase;
import com.example.user.adminforquiz.model.db.Quiz;
import com.example.user.adminforquiz.model.db.QuizTranslation;
import com.example.user.adminforquiz.model.db.QuizTranslationPhrase;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

public class QuizConverter {
    @Inject
    QuizConverter() {
    }

    public List<Quiz> convert(List<NwQuiz> nwQuizList) {
        List<Quiz> quizList = new ArrayList<>();
        for (int i = 0; i < nwQuizList.size(); i++) {
            NwQuiz nwQuiz = nwQuizList.get(i);
            Quiz quiz = new Quiz();
            quiz.id = nwQuiz.id;
            quiz.scpNumber = nwQuiz.scpNumber;
            quiz.imageUrl = nwQuiz.imageUrl;
            quiz.authorId = nwQuiz.authorId;
            quiz.approved = nwQuiz.approved;
            quiz.approverId = nwQuiz.approverId;
            quiz.created = nwQuiz.created;
            quiz.updated = nwQuiz.updated;
            quiz.quizTranslations = convert1(nwQuiz.quizTranslations);
            quizList.add(quiz);
        }

        return quizList;
    }

    private List<QuizTranslation> convert1(List<NwQuizTranslation> nwQuizTranslationList) {
        List<QuizTranslation> quizTranslationList = new ArrayList<>();
        for (int i = 0; i < nwQuizTranslationList.size(); i++) {
            NwQuizTranslation nwQuizTranslation = nwQuizTranslationList.get(i);
            QuizTranslation quizTranslation = new QuizTranslation();
            quizTranslation.id = nwQuizTranslation.id;
            quizTranslation.langCode = nwQuizTranslation.langCode;
            quizTranslation.approved = nwQuizTranslation.approved;
            quizTranslation.approverId = nwQuizTranslation.approverId;
            quizTranslation.authorId = nwQuizTranslation.authorId;
            quizTranslation.created = nwQuizTranslation.created;
            quizTranslation.description = nwQuizTranslation.description;
            quizTranslation.updated = nwQuizTranslation.updated;
            quizTranslation.quizId = null;
            quizTranslation.translation = nwQuizTranslation.translation;
            quizTranslation.quizTranslationPhrases = convert2(nwQuizTranslation.quizTranslationPhrases);
            quizTranslationList.add(quizTranslation);
        }

        return quizTranslationList;
    }

    private List<QuizTranslationPhrase> convert2(List<NwQuizTranslationPhrase> nwQuizTranslationPhraseList) {
        List<QuizTranslationPhrase> quizTranslationPhraseList = new ArrayList<>();
        for (int i = 0; i < nwQuizTranslationPhraseList.size(); i++) {
            NwQuizTranslationPhrase nwQuizTranslationPhrase = nwQuizTranslationPhraseList.get(i);
            QuizTranslationPhrase quizTranslationPhrase = new QuizTranslationPhrase();
            quizTranslationPhrase.id = nwQuizTranslationPhrase.id;
            quizTranslationPhrase.approved = nwQuizTranslationPhrase.approved;
            quizTranslationPhrase.approverId = nwQuizTranslationPhrase.approverId;
            quizTranslationPhrase.authorId = nwQuizTranslationPhrase.authorId;
            quizTranslationPhrase.created = nwQuizTranslationPhrase.created;
            quizTranslationPhrase.updated = nwQuizTranslationPhrase.updated;
            quizTranslationPhrase.quizTranslationId = null;
            quizTranslationPhrase.translation = nwQuizTranslationPhrase.translation;
            quizTranslationPhraseList.add(quizTranslationPhrase);
        }

        return quizTranslationPhraseList;
    }

}
