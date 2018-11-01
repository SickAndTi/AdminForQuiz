package com.scp.adminforquiz.mvp;

import android.view.View;

import com.arellomobile.mvp.MvpView;

public interface FilterView extends MvpView {

    void showProgress(boolean showProgress);

    void showError(String errorMessage);

    void isChecked(View view, boolean isChecked);
}