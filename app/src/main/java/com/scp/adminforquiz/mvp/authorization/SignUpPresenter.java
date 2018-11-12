package com.scp.adminforquiz.mvp.authorization;

import android.text.TextUtils;

import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;
import com.scp.adminforquiz.Constants;
import com.scp.adminforquiz.api.ApiClient;
import com.jakewharton.rxrelay2.BehaviorRelay;
import com.scp.adminforquiz.preference.MyPreferenceManager;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;
import ru.terrakok.cicerone.Router;
import toothpick.Toothpick;

@InjectViewState
public class SignUpPresenter extends MvpPresenter<SignUpView> {

    @Inject
    Router router;
    @Inject
    ApiClient apiClient;
    @Inject
    MyPreferenceManager preferences;
    private BehaviorRelay<String> nameRelayReg = BehaviorRelay.create();
    private BehaviorRelay<String> loginRelayReg = BehaviorRelay.create();
    private BehaviorRelay<String> passwordRelayReg = BehaviorRelay.create();
    private BehaviorRelay<String> passwordRepeatRelayReg = BehaviorRelay.create();
    private CompositeDisposable compositeDisposable = new CompositeDisposable();

    @Override
    protected void onFirstViewAttach() {
        super.onFirstViewAttach();
        Toothpick.inject(this, Toothpick.openScope(Constants.APP_SCOPE));
        compositeDisposable.add(Observable.combineLatest(
                nameRelayReg,
                loginRelayReg,
                passwordRelayReg,
                passwordRepeatRelayReg,
                (name, login, password, passwordRepeat) -> !TextUtils.isEmpty(name) && !TextUtils.isEmpty(login) && !TextUtils.isEmpty(password) && password.matches(passwordRepeat))
                .subscribe(isValid -> {
                    getViewState().enableButton(isValid);
                    getViewState().setColorEnableButton(isValid);
                })
        );
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        compositeDisposable.clear();
    }

    public void onNameChanged(String name) {
        nameRelayReg.accept(name);
    }

    public void onLoginRegChanged(String loginReg) {
        loginRelayReg.accept(loginReg);
    }

    public void onPasswordRegChanged(String passwordReg) {
        passwordRelayReg.accept(passwordReg);
    }

    public void onPasswordRepeatRegChanged(String passwordRepeatReg) {
        passwordRepeatRelayReg.accept(passwordRepeatReg);
    }

    private void goToAllQuizFragment() {
        router.newRootScreen(Constants.ALL_QUIZ_SCREEN);
    }

    public void regUser() {
        compositeDisposable.add(apiClient.signUp(loginRelayReg.getValue(), passwordRelayReg.getValue(), nameRelayReg.getValue(), null)
                .doOnSuccess(tokenResponse -> {
                    preferences.setAccessToken(tokenResponse.accessToken);
                    preferences.setRefreshToken(tokenResponse.refreshToken);
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(tokenResponse -> goToAllQuizFragment(),
                        error -> getViewState().showError(error.toString())));
    }
}
