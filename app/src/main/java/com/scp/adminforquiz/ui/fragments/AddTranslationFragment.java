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
import com.arellomobile.mvp.presenter.ProvidePresenter;
import com.arellomobile.mvp.presenter.ProvidePresenterTag;
import com.scp.adminforquiz.R;
import com.scp.adminforquiz.mvp.AddTranslationPresenter;
import com.scp.adminforquiz.mvp.AddTranslationView;
import com.jakewharton.rxbinding2.widget.RxTextView;
import java.util.Objects;
import io.reactivex.disposables.CompositeDisposable;

public class AddTranslationFragment extends MvpAppCompatFragment implements AddTranslationView {
    public final static String EXTRA_QUIZ_ID = "EXTRA_QUIZ_ID";
    @InjectPresenter
    AddTranslationPresenter addTranslationPresenter;
    EditText etEnterLangCode, etEnterTitle, etEnterDescription;
    Button btnOK, btnCancel;
    View progressBarAddTranslation;
    CompositeDisposable compositeDisposable = new CompositeDisposable();

    public static AddTranslationFragment newInstance(Long quizId) {
        AddTranslationFragment addTranslationFragment = new AddTranslationFragment();
        Bundle bundle = new Bundle();
        bundle.putLong(EXTRA_QUIZ_ID, quizId);
        addTranslationFragment.setArguments(bundle);
        return addTranslationFragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_add_translation, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        etEnterLangCode = view.findViewById(R.id.etEnterLangCode);
        etEnterTitle = view.findViewById(R.id.etEnterTitle);
        etEnterDescription = view.findViewById(R.id.etEnterDescription);
        btnOK = view.findViewById(R.id.btnOK);
        btnOK.setOnClickListener(v -> addTranslationPresenter.addTranslation());
        btnCancel = view.findViewById(R.id.btnCancel);
        btnCancel.setOnClickListener(v -> addTranslationPresenter.cancel());
        progressBarAddTranslation = view.findViewById(R.id.flProgressBarAddTranslation);
        compositeDisposable.add(RxTextView.textChanges(etEnterLangCode)
                .subscribe(charSequence -> addTranslationPresenter.onLangCodeChanged(charSequence.toString())));
        compositeDisposable.add(RxTextView.textChanges(etEnterTitle)
                .subscribe(charSequence -> addTranslationPresenter.onTitleChanged(charSequence.toString())));
        compositeDisposable.add(RxTextView.textChanges(etEnterDescription)
                .subscribe(charSequence -> addTranslationPresenter.onDescriptionChanged(charSequence.toString())));
    }

    @Override
    public void showProgressBar(boolean showProgressBar) {
        progressBarAddTranslation.setVisibility(showProgressBar ? View.VISIBLE : View.GONE);
    }

    @Override
    public void enableButton(boolean enableButton) {
        btnOK.setEnabled(enableButton);
    }

    @Override
    public void showError(String errorMessage) {
        Toast.makeText(getContext(), errorMessage, Toast.LENGTH_LONG).show();
    }

    @ProvidePresenter
    AddTranslationPresenter provideAddTranslationPresenter() {
        AddTranslationPresenter addTranslationPresenter = new AddTranslationPresenter();
        addTranslationPresenter.setQuizId(Objects.requireNonNull(getArguments()).getLong(EXTRA_QUIZ_ID));
        return addTranslationPresenter;
    }

    @ProvidePresenterTag(presenterClass = AddTranslationPresenter.class)
    String provideAddTranslationPresenterTag() {
        return String.valueOf(Objects.requireNonNull(getArguments()).getLong(EXTRA_QUIZ_ID));
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        compositeDisposable.clear();
    }
}
