package com.scp.adminforquiz.model;

import com.scp.adminforquiz.model.api.NwQuiz;
import com.scp.adminforquiz.model.api.NwQuizTranslation;
import com.scp.adminforquiz.model.api.NwQuizTranslationPhrase;
import com.scp.adminforquiz.model.api.NwUser;
import com.scp.adminforquiz.model.db.Quiz;
import com.scp.adminforquiz.model.db.QuizTranslation;
import com.scp.adminforquiz.model.db.QuizTranslationPhrase;
import com.scp.adminforquiz.model.db.User;

import org.jetbrains.annotations.NotNull;

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
            Quiz quiz = convert(nwQuiz);
            quizList.add(quiz);
        }
        return quizList;
    }

    public Quiz convert(NwQuiz nwQuiz) {
        Quiz quiz = new Quiz();
        quiz.id = nwQuiz.id;
        quiz.scpNumber = nwQuiz.scpNumber;
        quiz.imageUrl = nwQuiz.imageUrl;
        quiz.authorId = nwQuiz.authorId;
        quiz.approved = nwQuiz.approved;
        quiz.approverId = nwQuiz.approverId;
        quiz.created = nwQuiz.created;
        quiz.updated = nwQuiz.updated;
        quiz.quizTranslations = convertTranslation(nwQuiz.quizTranslations, nwQuiz.id);
        if (nwQuiz.author != null) {
            quiz.author = convertUser(nwQuiz.author);
        }
        if (nwQuiz.approver != null) {
            quiz.approver = convertUser(nwQuiz.approver);
        }
        return quiz;
    }

    private List<QuizTranslation> convertTranslation(List<NwQuizTranslation> nwQuizTranslationList, Long quizId) {
        List<QuizTranslation> quizTranslationList = new ArrayList<>();
        for (int i = 0; i < nwQuizTranslationList.size(); i++) {
            NwQuizTranslation nwQuizTranslation = nwQuizTranslationList.get(i);
            QuizTranslation quizTranslation = convertTranslation(nwQuizTranslation, quizId);
            quizTranslationList.add(quizTranslation);
        }
        return quizTranslationList;
    }

    public QuizTranslation convertTranslation(NwQuizTranslation nwQuizTranslation, Long quizId) {
        QuizTranslation quizTranslation = new QuizTranslation();
        quizTranslation.id = nwQuizTranslation.id;
        quizTranslation.langCode = nwQuizTranslation.langCode;
        quizTranslation.approved = nwQuizTranslation.approved;
        quizTranslation.approverId = nwQuizTranslation.approverId;
        quizTranslation.authorId = nwQuizTranslation.authorId;
        quizTranslation.created = nwQuizTranslation.created;
        quizTranslation.description = nwQuizTranslation.description;
        quizTranslation.updated = nwQuizTranslation.updated;
        quizTranslation.quizId = quizId;
        quizTranslation.translation = nwQuizTranslation.translation;
        quizTranslation.quizTranslationPhrases = convertTranslationPhrase(nwQuizTranslation.quizTranslationPhrases, nwQuizTranslation.id);
        if (nwQuizTranslation.author != null) {
            quizTranslation.author = convertUser(nwQuizTranslation.author);
        }
        if (nwQuizTranslation.approver != null) {
            quizTranslation.approver = convertUser(nwQuizTranslation.approver);
        }
        return quizTranslation;
    }

    private List<QuizTranslationPhrase> convertTranslationPhrase(List<NwQuizTranslationPhrase> nwQuizTranslationPhraseList, Long nwQuizTranslationId) {
        List<QuizTranslationPhrase> quizTranslationPhraseList = new ArrayList<>();
        for (int i = 0; i < nwQuizTranslationPhraseList.size(); i++) {
            NwQuizTranslationPhrase nwQuizTranslationPhrase = nwQuizTranslationPhraseList.get(i);
            QuizTranslationPhrase quizTranslationPhrase = convertTranslationPhrase(nwQuizTranslationPhrase, nwQuizTranslationId);
            quizTranslationPhraseList.add(quizTranslationPhrase);
        }
        return quizTranslationPhraseList;
    }

    public QuizTranslationPhrase convertTranslationPhrase(NwQuizTranslationPhrase nwQuizTranslationPhrase, Long nwQuizTranslationId) {
        QuizTranslationPhrase quizTranslationPhrase = new QuizTranslationPhrase();
        quizTranslationPhrase.id = nwQuizTranslationPhrase.id;
        quizTranslationPhrase.approved = nwQuizTranslationPhrase.approved;
        quizTranslationPhrase.approverId = nwQuizTranslationPhrase.approverId;
        quizTranslationPhrase.authorId = nwQuizTranslationPhrase.authorId;
        quizTranslationPhrase.created = nwQuizTranslationPhrase.created;
        quizTranslationPhrase.updated = nwQuizTranslationPhrase.updated;
        quizTranslationPhrase.quizTranslationId = nwQuizTranslationId;
        quizTranslationPhrase.translation = nwQuizTranslationPhrase.translation;
        if (nwQuizTranslationPhrase.author != null) {
            quizTranslationPhrase.author = convertUser(nwQuizTranslationPhrase.author);
        }
        if (nwQuizTranslationPhrase.approver != null) {
            quizTranslationPhrase.approver = convertUser(nwQuizTranslationPhrase.approver);
        }
        return quizTranslationPhrase;
    }

    private User convertUser(@NotNull NwUser nwUser) {
        User user = new User();
        user.id = nwUser.id;
        user.fullName = nwUser.fullName;
        user.avatar = nwUser.avatar;
        return user;
    }
}
