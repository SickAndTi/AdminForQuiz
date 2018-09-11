package com.example.user.adminforquiz.mvp;

import com.arellomobile.mvp.MvpView;
import com.example.user.adminforquiz.model.api.NwQuiz;

import java.util.List;

public interface AllQuizView extends MvpView {

    void showProgressBar(boolean showProgressBar);

    void showNwQuizList(List<NwQuiz> nwQuizList);

    void showError(String errorMessage);

    void showSwipeRefresherBar(boolean showSwipeRefresherBar);

}
