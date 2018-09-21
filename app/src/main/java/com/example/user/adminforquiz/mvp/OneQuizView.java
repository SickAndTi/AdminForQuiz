package com.example.user.adminforquiz.mvp;

import com.arellomobile.mvp.MvpView;
import com.example.user.adminforquiz.model.db.Quiz;

public interface OneQuizView extends MvpView {

    void showQuiz(Quiz quiz);

    void showError(String errorMessage);
}
