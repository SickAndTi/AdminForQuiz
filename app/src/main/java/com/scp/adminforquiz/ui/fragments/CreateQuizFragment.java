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
import com.scp.adminforquiz.R;
import com.scp.adminforquiz.mvp.CreateQuizPresenter;
import com.scp.adminforquiz.mvp.CreateQuizView;
import com.jakewharton.rxbinding2.widget.RxTextView;

import io.reactivex.disposables.CompositeDisposable;

public class CreateQuizFragment extends MvpAppCompatFragment implements CreateQuizView {
    @InjectPresenter
    CreateQuizPresenter createQuizPresenter;
    Button btnOK, btnCancel;
    EditText etEnterScpNumber, etEnterImageUrl;
    View progressBarCreateQuiz;
    CompositeDisposable compositeDisposable = new CompositeDisposable();

    public static CreateQuizFragment newInstance() {
        return new CreateQuizFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_create_quiz, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        btnOK = view.findViewById(R.id.btnOK);
        btnOK.setOnClickListener(v -> createQuizPresenter.createQuiz());
        btnCancel = view.findViewById(R.id.btnCancel);
        btnCancel.setOnClickListener(v -> createQuizPresenter.cancel());
        etEnterScpNumber = view.findViewById(R.id.etEnterScpNumber);
        etEnterImageUrl = view.findViewById(R.id.etEnterImageUrl);
        progressBarCreateQuiz = view.findViewById(R.id.flProgressBarCreateQuiz);
        compositeDisposable.add(RxTextView.textChanges(etEnterScpNumber)
                .subscribe(charSequence -> createQuizPresenter.onNumberChanged(charSequence.toString())));
        compositeDisposable.add(RxTextView.textChanges(etEnterImageUrl)
                .subscribe(charSequence -> createQuizPresenter.onImageChanged(charSequence.toString())));
    }

    @Override
    public void showProgressBar(boolean showProgressBar) {
        progressBarCreateQuiz.setVisibility(showProgressBar ? View.VISIBLE : View.GONE);
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
    public void onDestroyView() {
        super.onDestroyView();
        compositeDisposable.clear();
    }
}
