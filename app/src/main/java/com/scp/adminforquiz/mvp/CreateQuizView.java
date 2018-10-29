package com.scp.adminforquiz.mvp;

import com.arellomobile.mvp.MvpView;

public interface CreateQuizView extends MvpView {

    void showProgressBar(boolean showProgressBar);

    void enableButton(boolean enableButton);

    void showError(String errorMessage);
}