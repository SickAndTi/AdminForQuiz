package com.example.user.adminforquiz.ui.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.Toast;

import com.arellomobile.mvp.MvpAppCompatFragment;
import com.arellomobile.mvp.presenter.InjectPresenter;
import com.arellomobile.mvp.presenter.ProvidePresenter;
import com.arellomobile.mvp.presenter.ProvidePresenterTag;
import com.example.user.adminforquiz.R;
import com.example.user.adminforquiz.model.db.Quiz;
import com.example.user.adminforquiz.model.db.dao.QuizDao;
import com.example.user.adminforquiz.mvp.EditPresenter;
import com.example.user.adminforquiz.mvp.EditView;

import javax.inject.Inject;

import timber.log.Timber;

public class EditFragment extends MvpAppCompatFragment implements EditView {
    @InjectPresenter
    EditPresenter editPresenter;
    public final static String EXTRA_QUIZID = "EXTRA_QUIZID";
    @Inject
    QuizDao quizDao;
    Quiz quiz;

    public static EditFragment newInstance(Long quizId) {
        EditFragment fragment = new EditFragment();
        Bundle bundle = new Bundle();
        bundle.putLong(EXTRA_QUIZID, quizId);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_edit, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        EditText etScpNumber = view.findViewById(R.id.etScpNumber);
        Switch approved = view.findViewById(R.id.approved);
        ImageView imageView = view.findViewById(R.id.imageView);
        EditText etTranslation = view.findViewById(R.id.etTranslation);
        EditText etPhrases = view.findViewById(R.id.etPhrases);
        EditText etDateCreated = view.findViewById(R.id.etDateCreated);
        EditText etDateUpdated = view.findViewById(R.id.etDateUpdated);
        EditText etAuthorId = view.findViewById(R.id.etAuthorId);
        EditText etApproverId = view.findViewById(R.id.etApproverId);
    }

    @ProvidePresenter
    EditPresenter provideEditPresenter() {
        EditPresenter editPresenter = new EditPresenter();
        editPresenter.setQuizId(getArguments().getLong(EXTRA_QUIZID));
        return editPresenter;
    }

    @ProvidePresenterTag(presenterClass = EditPresenter.class)
    String provideEditPresenterTag() {
        return String.valueOf(getArguments().getLong(EXTRA_QUIZID));
    }

    @Override
    public void showError(String errorMessage) {
        Timber.e(errorMessage);
        Toast.makeText(getContext(), errorMessage, Toast.LENGTH_LONG).show();
    }

    @Override
    public void showQuiz(Quiz quiz) {

    }
}
