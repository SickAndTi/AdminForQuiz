package com.scp.adminforquiz.ui.fragments.authorization;

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
import com.scp.adminforquiz.mvp.authorization.SignUpPresenter;
import com.scp.adminforquiz.mvp.authorization.SignUpView;
import com.jakewharton.rxbinding2.widget.RxTextView;

import io.reactivex.disposables.CompositeDisposable;
import toothpick.Toothpick;

public class SignUpFragment extends MvpAppCompatFragment implements SignUpView {
    @InjectPresenter
    SignUpPresenter signUpPresenter;
    Button btnOK;
    EditText etLoginReg, etPasswordReg, etRepeatPasswordReg, etName;
    CompositeDisposable compositeDisposable = new CompositeDisposable();

    public static SignUpFragment newInstance() {
        return new SignUpFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Toothpick.inject(this, Toothpick.openScope(Constants.APP_SCOPE));
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_sign_up, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        btnOK = view.findViewById(R.id.btnOkReg);
        btnOK.setEnabled(false);
        btnOK.setOnClickListener(v -> signUpPresenter.regUser());
        etName = view.findViewById(R.id.etName);
        etLoginReg = view.findViewById(R.id.etEnterLoginReg);
        etPasswordReg = view.findViewById(R.id.etEnterPasswordReg);
        etRepeatPasswordReg = view.findViewById(R.id.etRepeatPasswordReg);
        compositeDisposable.add(RxTextView.textChanges(etName)
                .subscribe(charSequence -> signUpPresenter.onNameChanged(charSequence.toString())));
        compositeDisposable.add(RxTextView.textChanges(etLoginReg)
                .subscribe(charSequence -> signUpPresenter.onLoginRegChanged(charSequence.toString())));
        compositeDisposable.add(RxTextView.textChanges(etPasswordReg)
                .subscribe(charSequence -> signUpPresenter.onPasswordRegChanged(charSequence.toString())));
        compositeDisposable.add(RxTextView.textChanges(etRepeatPasswordReg)
                .subscribe(charSequence -> signUpPresenter.onPasswordRepeatRegChanged(charSequence.toString())));
    }

    @Override
    public void enableButton(boolean enableButton) {
        btnOK.setEnabled(enableButton);
    }

    @Override
    public void setColorEnableButton(boolean isValid) {
        btnOK.setTextColor(isValid ? getResources().getColor(R.color.backGroundColor) : getResources().getColor(R.color.selectTabColor));
        btnOK.setBackground(isValid ? getResources().getDrawable(R.drawable.button_auth_screen_enable) : getResources().getDrawable(R.drawable.button_auth_screen_disable));
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