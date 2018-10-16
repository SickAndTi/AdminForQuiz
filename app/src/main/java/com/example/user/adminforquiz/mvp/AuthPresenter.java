package com.example.user.adminforquiz.mvp;

import android.text.TextUtils;
import android.util.Patterns;
import android.widget.Button;
import android.widget.EditText;

import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;
import com.example.user.adminforquiz.Constants;
import com.example.user.adminforquiz.api.ApiClient;
import com.example.user.adminforquiz.model.db.dao.QuizDao;
import com.example.user.adminforquiz.preference.MyPreferenceManager;
import com.jakewharton.rxbinding2.widget.RxTextView;

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
    private CompositeDisposable compositeDisposable = new CompositeDisposable();

    @Override
    protected void onFirstViewAttach() {
        super.onFirstViewAttach();
        Toothpick.inject(this, Toothpick.openScope(Constants.APP_SCOPE));
    }

    private void goToAllQuizFragment() {
        router.newRootScreen(Constants.ALL_QUIZ_SCREEN);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        compositeDisposable.clear();
    }

    public void authTry(String user, String password) {
        compositeDisposable.add(apiClient.getAccessToken(user, password)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(tokenResponse -> goToAllQuizFragment(),
                        error -> getViewState().showError(error.toString())));
    }

    public void checkAuth(EditText etLogin, EditText etPassword, Button btnOK) {
        compositeDisposable.add(Observable.combineLatest(
                RxTextView.textChanges(etLogin),
                RxTextView.textChanges(etPassword),
                (login, password) -> !TextUtils.isEmpty(login)
                        && Patterns.EMAIL_ADDRESS.matcher(login.toString()).matches()
                        && !TextUtils.isEmpty(password)
        )
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(btnOK::setEnabled));
    }
}
