package com.example.user.adminforquiz.ui.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.widget.Toast;

import com.arellomobile.mvp.MvpAppCompatActivity;
import com.arellomobile.mvp.presenter.InjectPresenter;
import com.example.user.adminforquiz.Constants;
import com.example.user.adminforquiz.R;
import com.example.user.adminforquiz.mvp.MainPresenter;
import com.example.user.adminforquiz.mvp.MainView;
import com.example.user.adminforquiz.ui.fragments.AddPhraseFragment;
import com.example.user.adminforquiz.ui.fragments.AddTranslationFragment;
import com.example.user.adminforquiz.ui.fragments.AllQuizFragment;
import com.example.user.adminforquiz.ui.fragments.AuthFragment;
import com.example.user.adminforquiz.ui.fragments.LoginFragment;
import com.example.user.adminforquiz.ui.fragments.CreateQuizFragment;
import com.example.user.adminforquiz.ui.fragments.EditFragment;
import com.example.user.adminforquiz.ui.fragments.OneQuizFragment;
import com.example.user.adminforquiz.ui.fragments.RegistrationFragment;
import com.example.user.adminforquiz.ui.fragments.UpdateTranslationDescriptionFragment;

import javax.inject.Inject;

import ru.terrakok.cicerone.Navigator;
import ru.terrakok.cicerone.NavigatorHolder;
import ru.terrakok.cicerone.android.SupportFragmentNavigator;
import toothpick.Toothpick;

public class MainActivity extends MvpAppCompatActivity implements MainView {

    @Inject
    NavigatorHolder navigatorHolder;
    @InjectPresenter
    MainPresenter mainPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toothpick.inject(this, Toothpick.openScope(Constants.APP_SCOPE));
    }

    private Navigator navigator = new SupportFragmentNavigator(getSupportFragmentManager(), R.id.container) {
        @Override
        protected Fragment createFragment(String screenKey, Object data) {
            switch (screenKey) {
                case Constants.ALL_QUIZ_SCREEN:
                    return AllQuizFragment.newInstance();
                case Constants.LOGIN_SCREEN:
                    return LoginFragment.newInstance();
                case Constants.REGISTRATION_SCREEN:
                    return RegistrationFragment.newInstance();
                case Constants.ONE_QUIZ_SCREEN:
                    return OneQuizFragment.newInstance((Long) data);
                case Constants.EDIT_SCREEN:
                    return EditFragment.newInstance((Long) data);
                case Constants.CREATE_QUIZ_SCREEN:
                    return CreateQuizFragment.newInstance();
                case Constants.ADD_TRANSLATION_SCREEN:
                    return AddTranslationFragment.newInstance((Long) data);
                case Constants.ADD_PHRASE_SCREEN:
                    return AddPhraseFragment.newInstance((Long) data);
                case Constants.UPDATE_TRANSLATION_DESCRIPTION_SCREEN:
                    return UpdateTranslationDescriptionFragment.newInstance((Long) data);
                case Constants.AUTH_SCREEN:
                    return AuthFragment.newInstance();
                default:
                    throw new RuntimeException("Unknown screen key !");
            }
        }

        @Override
        protected void showSystemMessage(String message) {
            Toast.makeText(MainActivity.this, message, Toast.LENGTH_SHORT).show();
        }

        @Override
        protected void exit() {
            finish();
        }
    };

    @Override
    protected void onResume() {
        super.onResume();
        navigatorHolder.setNavigator(navigator);
    }

    @Override
    protected void onPause() {
        super.onPause();
        navigatorHolder.removeNavigator();
    }
}