package com.example.user.adminforquiz.mvp;

import android.text.TextUtils;

import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;
import com.example.user.adminforquiz.Constants;
import com.example.user.adminforquiz.api.ApiClient;
import com.jakewharton.rxrelay2.BehaviorRelay;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.disposables.CompositeDisposable;
import ru.terrakok.cicerone.Router;
import toothpick.Toothpick;

@InjectViewState
public class SignUpPresenter extends MvpPresenter<SignUpView> {

    @Inject
    Router router;
    @Inject
    ApiClient apiClient;
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
                .subscribe(isValid -> getViewState().enableButton(isValid))
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

    public void regUser() {
        // TODO server registration method
    }
}
