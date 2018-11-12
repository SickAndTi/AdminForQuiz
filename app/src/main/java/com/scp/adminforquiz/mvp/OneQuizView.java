package com.scp.adminforquiz.mvp;

import com.arellomobile.mvp.MvpView;
import com.scp.adminforquiz.model.db.Quiz;

public interface OneQuizView extends MvpView {

    void showQuiz(Quiz quiz);

    void showError(String errorMessage);

    void showError(int stringResource);

    void showProgressBar(boolean showProgressBar);

}
