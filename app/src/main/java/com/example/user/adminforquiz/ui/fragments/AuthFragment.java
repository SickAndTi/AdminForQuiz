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

import java.util.Objects;

import javax.inject.Inject;

import toothpick.Toothpick;

public class AuthFragment extends MvpAppCompatFragment implements AuthView {
    @InjectPresenter
    AuthPresenter authPresenter;
    @Inject
    MyPreferenceManager preferences;
    EditText etEnterLogin, etEnterPassword;
    Button btnOK, btnCancel;


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
        etEnterLogin = view.findViewById(R.id.etEnterLogin);
        etEnterPassword = view.findViewById(R.id.etEnterPassword);
        btnOK = view.findViewById(R.id.btnOK);
        btnOK.setOnClickListener(v -> authPresenter.authTry(etEnterLogin.getText().toString(), etEnterPassword.getText().toString()));
        btnCancel = view.findViewById(R.id.btnCancel);
        btnCancel.setOnClickListener(v -> authCancel());
//        btnUseOAuth = view.findViewById(R.id.btnUseOAuth);
//        btnUseOAuth.setOnClickListener(v -> useOAuth());
    }

    @Override
    public void showError(String errorMessage) {
        Toast.makeText(getContext(), errorMessage, Toast.LENGTH_LONG).show();
    }

    public void authCancel() {
        Objects.requireNonNull(getActivity()).finish();
    }
}
//    private void useOAuth() {
//        try {
//            if (preferences.getUserForAuth() != null && preferences.getPasswordForAuth() != null) {
//                etEnterLogin.setText(preferences.getUserForAuth());
//                etEnterPassword.setText(preferences.getPasswordForAuth());
//            }
//        } catch (NullPointerException ignored) {
//            Toast.makeText(getContext(), R.string.noAuthYet, Toast.LENGTH_LONG).show();
//        }
//    }

//    public void showSaveLogPassOption() {
//        LayoutInflater inflaterAuth = LayoutInflater.from(getContext());
//        @SuppressLint("InflateParams") View viewAuth = inflaterAuth.inflate(R.layout.dialog_oauth_save, null);
//        AlertDialog.Builder mDialogBuilderAuth = new AlertDialog.Builder(Objects.requireNonNull(getContext()));
//        mDialogBuilderAuth.setView(viewAuth);
//        mDialogBuilderAuth
//                .setCancelable(false)
//                .setPositiveButton("Save",
//                        (dialog, id) -> {
//                            authPresenter.saveLogPass(etEnterLogin.getText().toString(), etEnterPassword.getText().toString());
//                            authPresenter.goToAllQuizFragment();
//                            dialog.cancel();
//                        })
//                .setNegativeButton("Don't save",
//                        (dialog, id) -> {
//                            authPresenter.goToAllQuizFragment();
//                            dialog.cancel();
//                        });
//    }