package com.example.user.adminforquiz.mvp;

import com.arellomobile.mvp.MvpView;
import com.example.user.adminforquiz.model.db.Quiz;

public interface EditView extends MvpView {
    void showError(String errorMessage);

    void showQuiz(Quiz quiz);
}
