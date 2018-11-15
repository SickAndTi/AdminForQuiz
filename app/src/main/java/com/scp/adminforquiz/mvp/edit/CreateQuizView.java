package com.scp.adminforquiz.mvp.edit;

import com.arellomobile.mvp.MvpView;
import com.arellomobile.mvp.viewstate.strategy.OneExecutionStateStrategy;
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType;

public interface CreateQuizView extends MvpView {

    void showProgressBar(boolean showProgressBar);

    void enableButton(boolean enableButton);

    @StateStrategyType(OneExecutionStateStrategy.class)
    void showError(String errorMessage);

    void setColorEnableButton(boolean isValid);
}