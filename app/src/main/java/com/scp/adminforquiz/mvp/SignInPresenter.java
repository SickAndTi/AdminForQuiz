package com.scp.adminforquiz.mvp;

import android.text.TextUtils;
import android.util.Patterns;

import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;
import com.scp.adminforquiz.Constants;
import com.scp.adminforquiz.api.ApiClient;
import com.scp.adminforquiz.model.db.dao.QuizDao;
import com.scp.adminforquiz.preference.MyPreferenceManager;
import com.jakewharton.rxrelay2.BehaviorRelay;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;
import ru.terrakok.cicerone.Router;
import toothpick.Toothpick;

@InjectViewState
public class SignInPresenter extends MvpPresenter<SignInView> {
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
                (login, password) ->
                        !TextUtils.isEmpty(login) && Patterns.EMAIL_ADDRESS.matcher(login).matches() && !TextUtils.isEmpty(password)
        )
                .subscribe(isValid -> getViewState().enableButton(isValid)));
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
                .doOnSuccess(tokenResponse -> {
                    preferences.setAccessToken(tokenResponse.accessToken);
                    preferences.setRefreshToken(tokenResponse.refreshToken);
                    preferences.setUserForAuth(loginRelay.getValue());
                    preferences.setPasswordForAuth(passwordRelay.getValue());
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(tokenResponse ->
                                goToAllQuizFragment(),
                        error ->
                                getViewState().showError(error.toString())));
    }

    public void onLoginChanged(String login) {
        loginRelay.accept(login);
    }

    public void onPasswordChanged(String password) {
        passwordRelay.accept(password);
    }
}
