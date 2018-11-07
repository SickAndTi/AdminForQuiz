package com.scp.adminforquiz.ui.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
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
import com.scp.adminforquiz.R;
import com.scp.adminforquiz.mvp.AddPhrasePresenter;
import com.scp.adminforquiz.mvp.AddPhraseView;
import com.jakewharton.rxbinding2.widget.RxTextView;

import java.util.Objects;

import io.reactivex.disposables.CompositeDisposable;
import timber.log.Timber;

public class AddPhraseFragment extends MvpAppCompatFragment implements AddPhraseView {

    public static final String EXTRA_QUIZ_TRANSLATION_ID = "EXTRA_QUIZ_TRANSLATION_ID";
    CompositeDisposable compositeDisposable = new CompositeDisposable();
    @InjectPresenter
    AddPhrasePresenter addPhrasePresenter;
    EditText etAddPhrase;
    Button btnOK, btnCancel;
    View progressBarAddPhrase;

    public static Fragment newInstance(Long translationId) {
        AddPhraseFragment addPhraseFragment = new AddPhraseFragment();
        Bundle bundle = new Bundle();
        bundle.putLong(EXTRA_QUIZ_TRANSLATION_ID, translationId);
        addPhraseFragment.setArguments(bundle);
        return addPhraseFragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_add_phrase, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        etAddPhrase = view.findViewById(R.id.etAddPhrase);
        btnOK = view.findViewById(R.id.btnOK);
        btnOK.setOnClickListener(v -> addPhrasePresenter.addPhrase());
        btnCancel = view.findViewById(R.id.btnCancel);
        btnCancel.setOnClickListener(v -> addPhrasePresenter.cancel());
        progressBarAddPhrase = view.findViewById(R.id.flProgressBarAddPhrase);
        compositeDisposable.add(RxTextView.textChanges(etAddPhrase)
                .subscribe(charSequence -> addPhrasePresenter.onPhraseChanged(charSequence.toString())));
    }

    @ProvidePresenter
    AddPhrasePresenter provideAddPhrasePresenter() {
        AddPhrasePresenter addPhrasePresenter = new AddPhrasePresenter();
        addPhrasePresenter.setQuizTranslationId(Objects.requireNonNull(getArguments()).getLong(EXTRA_QUIZ_TRANSLATION_ID));
        return addPhrasePresenter;
    }

    @ProvidePresenterTag(presenterClass = AddPhrasePresenter.class)
    String provideAddPhrasePresenterTag() {
        return String.valueOf(Objects.requireNonNull(getArguments()).getLong(EXTRA_QUIZ_TRANSLATION_ID));
    }

    @Override
    public void showProgressBar(boolean showProgressBar) {
        progressBarAddPhrase.setVisibility(showProgressBar ? View.VISIBLE : View.GONE);
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
    public void setColorEnableButton(boolean isValid) {
        btnOK.setTextColor(isValid ? getResources().getColor(R.color.buttonAuthColor) : getResources().getColor(R.color.backGroundColor));
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        compositeDisposable.clear();
    }
}
