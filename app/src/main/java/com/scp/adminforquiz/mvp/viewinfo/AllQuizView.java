package com.scp.adminforquiz.mvp.viewinfo;

import com.arellomobile.mvp.MvpView;
import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy;
import com.arellomobile.mvp.viewstate.strategy.OneExecutionStateStrategy;
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType;
import com.scp.adminforquiz.model.db.Quiz;

import java.util.List;

public interface AllQuizView extends MvpView {

    void showProgressBar(boolean showProgressBar);

    @StateStrategyType(AddToEndSingleStrategy.class)
    void showQuizList(List<Quiz> quizList);

    @StateStrategyType(AddToEndSingleStrategy.class)
    void filterQueryText(String queryText);

    @StateStrategyType(OneExecutionStateStrategy.class)
    void showError(String errorMessage);

    void showSwipeProgressBar(boolean showSwipeRefresherBar);

    void showBottomProgress(boolean showBottomProgress);

    void enableScrollListener(boolean enableScrollListener);

    void showBottomSheet(boolean showBottomSheet);

    void setUserFilterAscendingType(boolean userFilterAscendingType);

    void setUserSortFieldName(String userSortFieldName);
}
