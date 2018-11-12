package com.scp.adminforquiz.mvp.edit;

import com.arellomobile.mvp.MvpView;

public interface AddPhraseView extends MvpView {

    void showProgressBar(boolean showProgressBar);

    void enableButton(boolean enableButton);

    void showError(String errorMessage);

    void setColorEnableButton(boolean isValid);
}
