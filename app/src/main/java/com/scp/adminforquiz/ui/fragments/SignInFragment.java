package com.scp.adminforquiz.ui.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.arellomobile.mvp.MvpAppCompatFragment;
import com.arellomobile.mvp.presenter.InjectPresenter;
import com.scp.adminforquiz.Constants;
import com.scp.adminforquiz.R;
import com.scp.adminforquiz.mvp.SignInPresenter;
import com.scp.adminforquiz.mvp.SignInView;
import com.scp.adminforquiz.preference.MyPreferenceManager;
import com.jakewharton.rxbinding2.widget.RxTextView;

import javax.inject.Inject;

import io.reactivex.disposables.CompositeDisposable;
import toothpick.Toothpick;

public class SignInFragment extends MvpAppCompatFragment implements SignInView {
    @InjectPresenter
    SignInPresenter signInPresenter;
    @Inject
    MyPreferenceManager preferences;
    EditText etEnterLogin, etEnterPassword;
    Button btnEnter;
    CompositeDisposable compositeDisposable = new CompositeDisposable();

    public static SignInFragment newInstance() {
        return new SignInFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Toothpick.inject(this, Toothpick.openScope(Constants.APP_SCOPE));
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_sign_in, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        etEnterLogin = view.findViewById(R.id.etEnterLogin);
        etEnterPassword = view.findViewById(R.id.etEnterPassword);
        btnEnter = view.findViewById(R.id.btnEnter);
        btnEnter.setEnabled(false);
        btnEnter.setOnClickListener(v -> signInPresenter.authTry());
        compositeDisposable.add(RxTextView.textChanges(etEnterLogin)
                .subscribe(charSequence -> signInPresenter.onLoginChanged(charSequence.toString())));
        compositeDisposable.add(RxTextView.textChanges(etEnterPassword)
                .subscribe(charSequence -> signInPresenter.onPasswordChanged(charSequence.toString())));
    }

    @Override
    public void showError(String errorMessage) {
        Toast.makeText(getContext(), errorMessage, Toast.LENGTH_LONG).show();
    }

    @Override
    public void enableButton(boolean enableButton) {
        btnEnter.setEnabled(enableButton);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        compositeDisposable.clear();
    }
}