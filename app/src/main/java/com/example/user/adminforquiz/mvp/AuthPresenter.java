package com.example.user.adminforquiz.mvp;

import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;
import com.example.user.adminforquiz.Constants;
import com.example.user.adminforquiz.api.ApiClient;
import com.example.user.adminforquiz.model.db.dao.QuizDao;
import com.example.user.adminforquiz.preference.MyPreferenceManager;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
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

    @Override
    protected void onFirstViewAttach() {
        super.onFirstViewAttach();
        Toothpick.inject(this, Toothpick.openScope(Constants.APP_SCOPE));
    }

    private void goToAllQuizFragment() {
        router.navigateTo(Constants.ALL_QUIZ_SCREEN);
    }

    public Disposable authTry(String user, String password) {
        return apiClient.getAccessToken(user, password)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(tokenResponse -> goToAllQuizFragment(),
                        error -> getViewState().showError(error.toString()));
    }
}
