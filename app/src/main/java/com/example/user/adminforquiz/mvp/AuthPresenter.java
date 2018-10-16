package com.example.user.adminforquiz.mvp;

import android.text.TextUtils;
import android.util.Patterns;

import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;
import com.example.user.adminforquiz.Constants;
import com.example.user.adminforquiz.api.ApiClient;
import com.example.user.adminforquiz.model.db.dao.QuizDao;
import com.example.user.adminforquiz.preference.MyPreferenceManager;
import com.jakewharton.rxrelay2.BehaviorRelay;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;
import ru.terrakok.cicerone.Router;
import toothpick.Toothpick;

@InjectViewState
public class AuthPresenter extends MvpPresenter<AuthView> {
    @Inject
    Router router;
    @Inject
    QuizDao quizDao;
    @Inject
    MyPreferenceManager preferences;
    @Inject
    ApiClient apiClient;
    private BehaviorRelay<String> loginRelay = BehaviorRelay.create();
    private BehaviorRelay<String> passwordRelay = BehaviorRelay.create();
    private CompositeDisposable compositeDisposable = new CompositeDisposable();

    @Override
    protected void onFirstViewAttach() {
        super.onFirstViewAttach();
        Toothpick.inject(this, Toothpick.openScope(Constants.APP_SCOPE));
        compositeDisposable.add(Observable.combineLatest(
                loginRelay,
                passwordRelay,
                (login, password) -> !TextUtils.isEmpty(login) && Patterns.EMAIL_ADDRESS.matcher(login).matches() && !TextUtils.isEmpty(password)
        )
                .subscribe(aBoolean -> getViewState().enableButton(aBoolean)));
    }

    private void goToAllQuizFragment() {
        router.newRootScreen(Constants.ALL_QUIZ_SCREEN);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        compositeDisposable.clear();
    }

    public void authTry() {
        compositeDisposable.add(apiClient.getAccessToken(loginRelay.getValue(), passwordRelay.getValue())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(tokenResponse -> goToAllQuizFragment(),
                        error -> getViewState().showError(error.toString())));
    }

    public void onLoginChanged(String login) {
        loginRelay.accept(login);
    }

    public void onPasswordChanged(String password) {
        passwordRelay.accept(password);
    }
}
