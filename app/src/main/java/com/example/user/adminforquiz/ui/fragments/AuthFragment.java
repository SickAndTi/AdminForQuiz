package com.example.user.adminforquiz.ui.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Patterns;
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

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;
import toothpick.Toothpick;

public class AuthFragment extends MvpAppCompatFragment implements AuthView {
    @InjectPresenter
    AuthPresenter authPresenter;
    @Inject
    MyPreferenceManager preferences;
    EditText etEnterLogin, etEnterPassword;
    Button btnOK, btnCancel;
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
        btnOK.setOnClickListener(v -> authPresenter.authTry(etEnterLogin.getText().toString(), etEnterPassword.getText().toString()));
        btnCancel = view.findViewById(R.id.btnCancel);
        btnCancel.setOnClickListener(v -> authCancel());
        authPresenter.checkAuth(etEnterLogin, etEnterPassword, btnOK);
    }

//    private void checkAuth() {
//        compositeDisposable.add(Observable.combineLatest(
//                RxTextView.textChanges(etEnterLogin),
//                RxTextView.textChanges(etEnterPassword),
//                (login, password) -> !TextUtils.isEmpty(login)
//                        && Patterns.EMAIL_ADDRESS.matcher(login.toString()).matches()
//                        && !TextUtils.isEmpty(password)
//        )
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(r -> btnOK.setEnabled(r)));
//    }

    @Override
    public void showError(String errorMessage) {
        Toast.makeText(getContext(), errorMessage, Toast.LENGTH_LONG).show();
    }

    @Override
    public void showProgressBar(boolean showProgressBar) {
        progressBarAuth.setVisibility(showProgressBar ? View.VISIBLE : View.GONE);
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