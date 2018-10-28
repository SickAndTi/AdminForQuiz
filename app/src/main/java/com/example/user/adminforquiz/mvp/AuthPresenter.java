package com.example.user.adminforquiz.mvp;

import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;
import com.example.user.adminforquiz.Constants;
import com.example.user.adminforquiz.api.ApiClient;
import com.example.user.adminforquiz.model.db.dao.QuizDao;

import javax.inject.Inject;

import ru.terrakok.cicerone.Router;
import toothpick.Toothpick;

@InjectViewState
public class AuthPresenter extends MvpPresenter<AuthView> {
    @Inject
    ApiClient apiClient;
    @Inject
    QuizDao quizDao;
    @Inject
    Router router;


    @Override
    protected void onFirstViewAttach() {
        super.onFirstViewAttach();
        Toothpick.inject(this, Toothpick.openScope(Constants.APP_SCOPE));
    }

    public void regViaVk() {
        //TODO
    }

    public void regViaGoogle() {
        //TODO
    }

    public void regViaFacebook() {
        //TODO
    }
}
