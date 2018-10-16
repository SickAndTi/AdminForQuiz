package com.example.user.adminforquiz.mvp;

import com.arellomobile.mvp.MvpView;

public interface AuthView extends MvpView {

    void showError(String errorMessage);

    void showProgressBar(boolean showProgressBar);

    void enableButton(boolean enableButton);
}
