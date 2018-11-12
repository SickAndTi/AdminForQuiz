package com.scp.adminforquiz.mvp.authorization;

import com.arellomobile.mvp.MvpView;

public interface AuthView extends MvpView {

    void showError(String errorMessage);

}
