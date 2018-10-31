package com.scp.adminforquiz.mvp;

import com.arellomobile.mvp.MvpView;
import com.scp.adminforquiz.model.db.Quiz;

public interface EditView extends MvpView {
    void showError(String errorMessage);

    void showEditQuiz(Quiz quiz);

    void showProgress(boolean showProgress);
}
