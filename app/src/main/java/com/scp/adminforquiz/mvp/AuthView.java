package com.scp.adminforquiz.mvp;

import com.arellomobile.mvp.MvpView;

public interface AuthView extends MvpView {

    void showError(String errorMessage);

}
