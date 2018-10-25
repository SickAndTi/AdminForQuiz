package com.example.user.adminforquiz.mvp;

import com.arellomobile.mvp.MvpView;

public interface AddTranslationView extends MvpView {

    void showProgressBar(boolean showProgressBar);

    void enableButton(boolean enableButton);

    void showError(String errorMessage);
}
