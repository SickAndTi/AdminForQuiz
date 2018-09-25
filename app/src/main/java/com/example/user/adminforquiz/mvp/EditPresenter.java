package com.example.user.adminforquiz.mvp;

import com.arellomobile.mvp.MvpPresenter;
import com.example.user.adminforquiz.Constants;
import com.example.user.adminforquiz.model.db.dao.QuizDao;

import javax.inject.Inject;

import toothpick.Toothpick;

public class EditPresenter extends MvpPresenter<EditView> {
    @Inject
    QuizDao quizDao;
    private Long quizId;

    public Long getQuizId() {
        return quizId;
    }

    public void setQuizId(Long quizId) {
        this.quizId = quizId;
    }

    @Override
    protected void onFirstViewAttach() {
        super.onFirstViewAttach();
        Toothpick.inject(this, Toothpick.openScope(Constants.APP_SCOPE));
    }
}
