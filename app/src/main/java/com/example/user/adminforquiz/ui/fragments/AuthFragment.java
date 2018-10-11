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
import com.example.user.adminforquiz.BuildConfig;
import com.example.user.adminforquiz.R;
import com.example.user.adminforquiz.mvp.AuthPresenter;
import com.example.user.adminforquiz.mvp.AuthView;

import java.util.Objects;

public class AuthFragment extends MvpAppCompatFragment implements AuthView {
    @InjectPresenter
    AuthPresenter authPresenter;
    EditText etEnterLogin, etEnterPassword;
    Button btnOK, btnCancel;


    public static AuthFragment newInstance() {
        return new AuthFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_auth, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        etEnterLogin = view.findViewById(R.id.etEnterLogin);
        etEnterPassword = view.findViewById(R.id.etEnterPassword);
        btnOK = view.findViewById(R.id.btnOK);
        btnOK.setOnClickListener(v -> authTry());
        btnCancel = view.findViewById(R.id.btnCancel);
        btnCancel.setOnClickListener(v -> authCancel());

    }

    @Override
    public void showError(String errorMessage) {
        Toast.makeText(getContext(), errorMessage, Toast.LENGTH_LONG).show();
    }

    public void authTry() {
        if (etEnterLogin.getText().toString().equals(BuildConfig.USER) && etEnterPassword.getText().toString().equals(BuildConfig.PASSWORD)) {
            authPresenter.goToAllQuizFragment();
        } else {
            Toast.makeText(getContext(), R.string.wrongLogPass, Toast.LENGTH_LONG).show();
        }
    }

    public void authCancel() {
        Objects.requireNonNull(getActivity()).finish();
    }
}
