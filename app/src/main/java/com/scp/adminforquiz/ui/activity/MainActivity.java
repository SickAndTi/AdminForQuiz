package com.scp.adminforquiz.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.widget.Toast;

import com.arellomobile.mvp.MvpAppCompatActivity;
import com.arellomobile.mvp.presenter.InjectPresenter;
import com.scp.adminforquiz.Constants;
import com.scp.adminforquiz.R;
import com.scp.adminforquiz.mvp.mainactivity.MainPresenter;
import com.scp.adminforquiz.mvp.mainactivity.MainView;
import com.scp.adminforquiz.ui.fragments.edit.AddPhraseFragment;
import com.scp.adminforquiz.ui.fragments.edit.AddTranslationFragment;
import com.scp.adminforquiz.ui.fragments.viewinfo.AllQuizFragment;
import com.scp.adminforquiz.ui.fragments.authorization.AuthFragment;
import com.scp.adminforquiz.ui.fragments.edit.CreateQuizFragment;
import com.scp.adminforquiz.ui.fragments.viewinfo.OneQuizFragment;
import com.vk.sdk.VKAccessToken;
import com.vk.sdk.VKCallback;
import com.vk.sdk.VKSdk;
import com.vk.sdk.api.VKError;

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
                case Constants.ONE_QUIZ_SCREEN:
                    return OneQuizFragment.newInstance((Long) data);
                case Constants.CREATE_QUIZ_SCREEN:
                    return CreateQuizFragment.newInstance();
                case Constants.ADD_TRANSLATION_SCREEN:
                    return AddTranslationFragment.newInstance((Long) data);
                case Constants.ADD_PHRASE_SCREEN:
                    return AddPhraseFragment.newInstance((Long) data);
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
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        VKSdk.onActivityResult(requestCode, resultCode, data, new VKCallback<VKAccessToken>() {
            @Override
            public void onResult(VKAccessToken res) {
                for (Fragment fragment : getSupportFragmentManager().getFragments()) {
                    fragment.onActivityResult(requestCode, resultCode, data);
                }
            }

            @Override
            public void onError(VKError error) {
            }
        });
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onPause() {
        super.onPause();
        navigatorHolder.removeNavigator();
    }
}