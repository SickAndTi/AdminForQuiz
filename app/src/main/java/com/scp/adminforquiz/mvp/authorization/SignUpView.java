package com.scp.adminforquiz.mvp.authorization;

import com.arellomobile.mvp.MvpView;
import com.arellomobile.mvp.viewstate.strategy.OneExecutionStateStrategy;
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType;

public interface SignUpView extends MvpView {

    @StateStrategyType(OneExecutionStateStrategy.class)
    void showError(String errorMessage);

    void enableButton(boolean enableButton);

    void setColorEnableButton(boolean isValid);
}
