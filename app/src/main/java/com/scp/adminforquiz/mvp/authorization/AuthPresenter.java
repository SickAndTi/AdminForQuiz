package com.scp.adminforquiz.mvp.authorization;

import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;
import com.scp.adminforquiz.Constants;
import com.scp.adminforquiz.api.ApiClient;
import com.scp.adminforquiz.db.QuizDao;
import com.scp.adminforquiz.preference.MyPreferenceManager;

import javax.inject.Inject;

import io.reactivex.disposables.CompositeDisposable;
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
    @Inject
    MyPreferenceManager preferences;
    private CompositeDisposable compositeDisposable = new CompositeDisposable();

    @Override
    protected void onFirstViewAttach() {
        super.onFirstViewAttach();
        Toothpick.inject(this, Toothpick.openScope(Constants.APP_SCOPE));
    }

    public void regViaVk() {
        //TODO получить email ФИО и ссылку на аватарку
    }

    public void regViaGoogle() {

    }

    public void regViaFacebook() {
        //TODO получить токен
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        compositeDisposable.clear();
    }
}