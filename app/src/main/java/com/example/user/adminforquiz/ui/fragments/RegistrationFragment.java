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
import com.example.user.adminforquiz.mvp.RegistrationPresenter;
import com.example.user.adminforquiz.mvp.RegistrationView;
import com.jakewharton.rxbinding2.widget.RxTextView;

import io.reactivex.disposables.CompositeDisposable;
import toothpick.Toothpick;

public class RegistrationFragment extends MvpAppCompatFragment implements RegistrationView {
    @InjectPresenter
    RegistrationPresenter registrationPresenter;
    View flProgressBarRegistration;
    Button btnOK, btnCancel;
    EditText etLoginReg, etPasswordReg, etRepeatPasswordReg;
    CompositeDisposable compositeDisposable = new CompositeDisposable();


    public static RegistrationFragment newInstance() {
        return new RegistrationFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Toothpick.inject(this, Toothpick.openScope(Constants.APP_SCOPE));
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_registration, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        flProgressBarRegistration = view.findViewById(R.id.flProgressBarRegistration);
        btnOK = view.findViewById(R.id.btnOkReg);
        btnOK.setEnabled(false);
        btnOK.setOnClickListener(v -> registrationPresenter.regUser());
        btnCancel = view.findViewById(R.id.btnCancelReg);
        btnCancel.setOnClickListener(v -> registrationPresenter.backToAuthScreen());
        etLoginReg = view.findViewById(R.id.etEnterLoginReg);
        etPasswordReg = view.findViewById(R.id.etEnterPasswordReg);
        etRepeatPasswordReg = view.findViewById(R.id.etRepeatPasswordReg);
        compositeDisposable.add(RxTextView.textChanges(etLoginReg)
                .subscribe(charSequence -> registrationPresenter.onLoginRegChanged(charSequence.toString())));
        compositeDisposable.add(RxTextView.textChanges(etPasswordReg)
                .subscribe(charSequence -> registrationPresenter.onPasswordRegChanged(charSequence.toString())));
        compositeDisposable.add(RxTextView.textChanges(etRepeatPasswordReg)
                .subscribe(charSequence -> registrationPresenter.onPasswordRepeatRegChanged(charSequence.toString())));
    }

    @Override
    public void showProgressBar(boolean showProgressBar) {
        flProgressBarRegistration.setVisibility(showProgressBar ? View.VISIBLE : View.GONE);
    }

    @Override
    public void enableButton(boolean enableButton) {
        btnOK.setEnabled(enableButton);
    }

    @Override
    public void showError(String errorMessage) {
        Toast.makeText(getContext(), errorMessage, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        compositeDisposable.clear();
    }
}