package com.example.user.adminforquiz.mvp;

import com.arellomobile.mvp.MvpView;

public interface SignInView extends MvpView {

    void showError(String errorMessage);

    void enableButton(boolean enableButton);
}