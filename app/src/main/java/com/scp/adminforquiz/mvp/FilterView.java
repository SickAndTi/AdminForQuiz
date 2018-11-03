package com.scp.adminforquiz.mvp;

import com.arellomobile.mvp.MvpView;

public interface FilterView extends MvpView {

    void showProgress(boolean showProgress);

    void showError(String errorMessage);

    void isChecked(boolean isChecked);

    void getSwitchIdButton(long buttonId);
}