package com.example.user.adminforquiz.ui.fragments;

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
import com.example.user.adminforquiz.Constants;
import com.example.user.adminforquiz.R;
import com.example.user.adminforquiz.mvp.AuthPresenter;
import com.example.user.adminforquiz.mvp.AuthView;
import com.example.user.adminforquiz.preference.MyPreferenceManager;
import com.jakewharton.rxbinding2.widget.RxTextView;

import java.util.Objects;

import javax.inject.Inject;

import io.reactivex.disposables.CompositeDisposable;
import toothpick.Toothpick;

public class AuthFragment extends MvpAppCompatFragment implements AuthView {
    @InjectPresenter
    AuthPresenter authPresenter;
    @Inject
    MyPreferenceManager preferences;
    EditText etEnterLogin, etEnterPassword;
    Button btnOK, btnCancel, btnRegistration;
    View progressBarAuth;
    CompositeDisposable compositeDisposable = new CompositeDisposable();

    public static AuthFragment newInstance() {
        return new AuthFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Toothpick.inject(this, Toothpick.openScope(Constants.APP_SCOPE));
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_auth, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        progressBarAuth = view.findViewById(R.id.flProgressBarAuth);
        etEnterLogin = view.findViewById(R.id.etEnterLogin);
        etEnterPassword = view.findViewById(R.id.etEnterPassword);
        btnOK = view.findViewById(R.id.btnOK);
        btnOK.setEnabled(false);
        btnOK.setOnClickListener(v -> authPresenter.authTry());
        btnCancel = view.findViewById(R.id.btnCancel);
        btnCancel.setOnClickListener(v -> authCancel());
        btnRegistration = view.findViewById(R.id.btnRegistration);
        btnRegistration.setOnClickListener(v -> authPresenter.goToRegistrationScreen());
        compositeDisposable.add(RxTextView.textChanges(etEnterLogin)
                .subscribe(charSequence -> authPresenter.onLoginChanged(charSequence.toString())));
        compositeDisposable.add(RxTextView.textChanges(etEnterPassword)
                .subscribe(charSequence -> authPresenter.onPasswordChanged(charSequence.toString())));
    }

    @Override
    public void showError(String errorMessage) {
        Toast.makeText(getContext(), errorMessage, Toast.LENGTH_LONG).show();
    }

    @Override
    public void showProgressBar(boolean showProgressBar) {
        progressBarAuth.setVisibility(showProgressBar ? View.VISIBLE : View.GONE);
    }

    @Override
    public void enableButton(boolean enableButton) {
        btnOK.setEnabled(enableButton);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        compositeDisposable.clear();
    }

    public void authCancel() {
        Objects.requireNonNull(getActivity()).finish();
    }
}