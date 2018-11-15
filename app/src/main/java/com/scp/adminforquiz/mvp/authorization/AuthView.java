package com.scp.adminforquiz.mvp.authorization;

import com.arellomobile.mvp.MvpView;
import com.arellomobile.mvp.viewstate.strategy.OneExecutionStateStrategy;
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType;

public interface AuthView extends MvpView {

    @StateStrategyType(OneExecutionStateStrategy.class)
    void showError(String errorMessage);

}
