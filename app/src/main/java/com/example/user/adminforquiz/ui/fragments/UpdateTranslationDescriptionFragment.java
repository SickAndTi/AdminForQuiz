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
import com.arellomobile.mvp.presenter.ProvidePresenter;
import com.arellomobile.mvp.presenter.ProvidePresenterTag;
import com.example.user.adminforquiz.R;
import com.example.user.adminforquiz.mvp.UpdateTranslationDescriptionPresenter;
import com.example.user.adminforquiz.mvp.UpdateTranslationDescriptionView;
import com.jakewharton.rxbinding2.widget.RxTextView;

import java.util.Objects;

import io.reactivex.disposables.CompositeDisposable;

public class UpdateTranslationDescriptionFragment extends MvpAppCompatFragment implements UpdateTranslationDescriptionView {

    public static final String EXTRA_QUIZ_TRANSLATION_ID = "EXTRA_QUIZ_TRANSLATION_ID";
    @InjectPresenter
    UpdateTranslationDescriptionPresenter updateTranslationDescriptionPresenter;
    CompositeDisposable compositeDisposable = new CompositeDisposable();
    EditText etTranslationDescription;
    Button btnOK, btnCancel;
    View progressBar;

    public static UpdateTranslationDescriptionFragment newInstance(Long quizTranslationId) {
        UpdateTranslationDescriptionFragment fragment = new UpdateTranslationDescriptionFragment();
        Bundle bundle = new Bundle();
        bundle.putLong(EXTRA_QUIZ_TRANSLATION_ID, quizTranslationId);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_update_translation_description, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        etTranslationDescription = view.findViewById(R.id.etTranslationDescription);
        btnOK = view.findViewById(R.id.btnOK);
        btnOK.setOnClickListener(v -> updateTranslationDescriptionPresenter.updateDescription());
        btnCancel = view.findViewById(R.id.btnCancel);
        btnCancel.setOnClickListener(v -> updateTranslationDescriptionPresenter.cancel());
        progressBar = view.findViewById(R.id.flProgressBarUpdateDescription);
        compositeDisposable.add(RxTextView.textChanges(etTranslationDescription)
                .subscribe(charSequence -> updateTranslationDescriptionPresenter.onDescriptionChanged(charSequence.toString())));
    }

    @Override
    public void showProgressBar(boolean showProgressBar) {
        progressBar.setVisibility(showProgressBar ? View.VISIBLE : View.GONE);
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
    public void setTranslationDescription(String descriptionText) {
        etTranslationDescription.setText(descriptionText);
    }

    @ProvidePresenter
    UpdateTranslationDescriptionPresenter provideUpdateTranslationDescription() {
        UpdateTranslationDescriptionPresenter updateTranslationDescriptionPresenter = new UpdateTranslationDescriptionPresenter();
        updateTranslationDescriptionPresenter.setQuizTranslationId(Objects.requireNonNull(getArguments()).getLong(EXTRA_QUIZ_TRANSLATION_ID));
        return updateTranslationDescriptionPresenter;
    }

    @ProvidePresenterTag(presenterClass = UpdateTranslationDescriptionPresenter.class)
    String provideUpdateTranslationDescriptionTag() {
        return String.valueOf(Objects.requireNonNull(getArguments()).getLong(EXTRA_QUIZ_TRANSLATION_ID));
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        compositeDisposable.clear();
    }
}
