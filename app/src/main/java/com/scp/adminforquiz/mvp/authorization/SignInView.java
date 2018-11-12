package com.scp.adminforquiz.mvp.authorization;

import com.arellomobile.mvp.MvpView;

public interface SignInView extends MvpView {

    void showError(String errorMessage);

    void enableButton(boolean enableButton);

    void setColorEnableButton(boolean isValid);
}
