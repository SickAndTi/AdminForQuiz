package com.scp.adminforquiz.mvp.viewinfo;

import com.arellomobile.mvp.MvpView;
import com.arellomobile.mvp.viewstate.strategy.OneExecutionStateStrategy;
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType;
import com.scp.adminforquiz.model.db.Quiz;

public interface OneQuizView extends MvpView {

    void showQuiz(Quiz quiz);

    @StateStrategyType(OneExecutionStateStrategy.class)
    void showError(String errorMessage);

    @StateStrategyType(OneExecutionStateStrategy.class)
    void showError(int stringResource);

    void showProgressBar(boolean showProgressBar);
}
