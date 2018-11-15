package com.scp.adminforquiz.mvp.viewinfo;

import com.arellomobile.mvp.MvpView;
import com.arellomobile.mvp.viewstate.strategy.OneExecutionStateStrategy;
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType;
import com.scp.adminforquiz.model.db.Quiz;

import java.util.List;

public interface AllQuizView extends MvpView {

    void showProgressBar(boolean showProgressBar);

    void showQuizList(List<Quiz> quizList);

    @StateStrategyType(OneExecutionStateStrategy.class)
    void showError(String errorMessage);

    void showSwipeRefresherBar(boolean showSwipeRefresherBar);

    void showBottomProgress(boolean showBottomProgress);

    void enableScrollListner(boolean enableScrollListener);

    void showBottomSheet(boolean showBottomSheet);
}
