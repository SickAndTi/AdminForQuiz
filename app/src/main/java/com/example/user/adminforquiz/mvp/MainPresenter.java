package com.example.user.adminforquiz.mvp;

import android.text.TextUtils;

import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;
import com.example.user.adminforquiz.Constants;
import com.example.user.adminforquiz.preference.MyPreferenceManager;

import javax.inject.Inject;

import ru.terrakok.cicerone.Router;
import toothpick.Toothpick;

@InjectViewState
public class MainPresenter extends MvpPresenter<MainView> {
    @Inject
    Router router;
    @Inject
    MyPreferenceManager preferenceManager;

    @Override
    protected void onFirstViewAttach() {
        super.onFirstViewAttach();
        Toothpick.inject(this, Toothpick.openScope(Constants.APP_SCOPE));
        if (TextUtils.isEmpty(preferenceManager.getAccessToken())) {
            router.newRootScreen(Constants.AUTH_SCREEN);
        } else {
            router.newRootScreen(Constants.ALL_QUIZ_SCREEN);
        }
    }
}

