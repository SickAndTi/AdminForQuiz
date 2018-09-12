package com.example.user.adminforquiz.model;

import com.example.user.adminforquiz.model.api.NwQuiz;
import com.example.user.adminforquiz.model.db.Quiz;

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
            quizList.add(quiz);
        }

        return quizList;
    }
}
