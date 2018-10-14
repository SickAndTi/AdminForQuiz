package com.example.user.adminforquiz.mvp;

import android.text.TextUtils;
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

import io.reactivex.Flowable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import kotlin.jvm.functions.Function2;
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

    @Override
    protected void onFirstViewAttach() {
        super.onFirstViewAttach();
        Toothpick.inject(this, Toothpick.openScope(Constants.APP_SCOPE));
    }

    private void goToAllQuizFragment() {
        router.newRootScreen(Constants.ALL_QUIZ_SCREEN);
    }

    public Disposable authTry(String user, String password) {
        return apiClient.getAccessToken(user, password)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(tokenResponse -> goToAllQuizFragment(),
                        error -> getViewState().showError(error.toString()));
    }

//    public void checkAuth(EditText etLogin, EditText etPassword, Button btnOK, String login, String password) {
//        Flowable.combineLatest(RxTextView.textChanges(etLogin),
//                RxTextView.textChanges(etPassword),
//                (Function2<String, String, Double>)
//                        (log, pass) -> Boolean.valueOf(!TextUtils.isEmpty(log) && !TextUtils.isEmpty(pass))
//                                .subscribeOn(Schedulers.io())
//                                .observeOn(AndroidSchedulers.mainThread())
//                                .subscribe(r -> btnOK.setEnabled(true)));
//    }
}
