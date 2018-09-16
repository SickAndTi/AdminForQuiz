package com.example.user.adminforquiz.mvp;

import com.arellomobile.mvp.MvpView;
import com.example.user.adminforquiz.model.db.Quiz;

import java.util.List;

public interface AllQuizView extends MvpView {

    void showProgressBar(boolean showProgressBar);

    void showQuizList(List<Quiz> quizList);

    void showError(String errorMessage);

    void showSwipeRefresherBar(boolean showSwipeRefresherBar);

}
