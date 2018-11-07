package com.scp.adminforquiz.mvp;

import com.arellomobile.mvp.MvpView;
import com.scp.adminforquiz.model.db.Quiz;

import java.util.List;

public interface AllQuizView extends MvpView {

    void showProgressBar(boolean showProgressBar);

    void showQuizList(List<Quiz> quizList);

    void showError(String errorMessage);

    void showSwipeRefresherBar(boolean showSwipeRefresherBar);

    void showBottomProgress(boolean showBottomProgress);

    void enableScrollListner(boolean enableScrollListener);

    void showBottomSheet(boolean showBottomSheet);
}
