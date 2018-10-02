package com.example.user.adminforquiz.ui.activity;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.arellomobile.mvp.MvpAppCompatActivity;
import com.arellomobile.mvp.presenter.InjectPresenter;
import com.example.user.adminforquiz.App;
import com.example.user.adminforquiz.Constants;
import com.example.user.adminforquiz.R;
import com.example.user.adminforquiz.api.ApiClient;
import com.example.user.adminforquiz.model.db.Quiz;
import com.example.user.adminforquiz.mvp.MainPresenter;
import com.example.user.adminforquiz.mvp.MainView;
import com.example.user.adminforquiz.ui.fragments.AllQuizFragment;
import com.example.user.adminforquiz.ui.fragments.EditFragment;
import com.example.user.adminforquiz.ui.fragments.OneQuizFragment;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import ru.terrakok.cicerone.Navigator;
import ru.terrakok.cicerone.NavigatorHolder;
import ru.terrakok.cicerone.Router;
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
                case Constants.EDIT_SCREEN:
                    return EditFragment.newInstance((Long) data);
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

