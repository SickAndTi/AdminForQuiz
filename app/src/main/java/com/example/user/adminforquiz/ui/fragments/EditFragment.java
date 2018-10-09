package com.example.user.adminforquiz.ui.fragments;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.arellomobile.mvp.MvpAppCompatFragment;
import com.arellomobile.mvp.presenter.InjectPresenter;
import com.arellomobile.mvp.presenter.ProvidePresenter;
import com.arellomobile.mvp.presenter.ProvidePresenterTag;
import com.example.user.adminforquiz.R;
import com.example.user.adminforquiz.api.ApiClient;
import com.example.user.adminforquiz.model.db.Quiz;
import com.example.user.adminforquiz.model.db.QuizTranslation;
import com.example.user.adminforquiz.model.db.QuizTranslationPhrase;
import com.example.user.adminforquiz.model.db.dao.QuizDao;
import com.example.user.adminforquiz.mvp.EditPresenter;
import com.example.user.adminforquiz.mvp.EditView;
import com.example.user.adminforquiz.ui.adapters.EditQuizRecyclerViewAdapter;

import java.util.Objects;

import javax.inject.Inject;

import ru.terrakok.cicerone.Router;
import timber.log.Timber;

public class EditFragment extends MvpAppCompatFragment implements EditView, EditQuizRecyclerViewAdapter.EditInterface {
    public final static String EXTRA_QUIZID = "EXTRA_QUIZID";
    @InjectPresenter
    EditPresenter editPresenter;
    @Inject
    QuizDao quizDao;
    @Inject
    ApiClient apiClient;
    @Inject
    Router router;
    RecyclerView recyclerViewEditQuiz;
    View progressView;
    EditQuizRecyclerViewAdapter editQuizRecyclerViewAdapter;

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
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.addTranslation:
                LayoutInflater inflater = LayoutInflater.from(getContext());
                @SuppressLint("InflateParams") View view = inflater.inflate(R.layout.dialog_add_translation, null);
                AlertDialog.Builder mDialogBuilder = new AlertDialog.Builder(Objects.requireNonNull(getContext()));
                mDialogBuilder.setView(view);
                final EditText etEnterLangCode = view.findViewById(R.id.etEnterLangCode);
                final EditText etEnterText = view.findViewById(R.id.etEnterText);
                final EditText etEnterDescription = view.findViewById(R.id.etEnterDescription);
                mDialogBuilder
                        .setCancelable(false)
                        .setPositiveButton("OK",
                                (dialog, id) -> {
                                    editPresenter.addTranslation(etEnterLangCode.getText().toString(), etEnterText.getText().toString(), etEnterDescription.getText().toString());
                                    dialog.cancel();
                                })
                        .setNegativeButton("Cancel",
                                (dialog, id) -> dialog.cancel());

                AlertDialog alertDialog = mDialogBuilder.create();
                alertDialog.show();
                break;

            case R.id.deleteNwQuiz:
                LayoutInflater inflaterDelete = LayoutInflater.from(getContext());
                @SuppressLint("InflateParams") View viewDelete = inflaterDelete.inflate(R.layout.dialog_delete, null);
                AlertDialog.Builder mDialogBuilderDelete = new AlertDialog.Builder(Objects.requireNonNull(getContext()));
                mDialogBuilderDelete.setView(viewDelete);

                mDialogBuilderDelete
                        .setCancelable(false)
                        .setPositiveButton("OK",
                                (dialog, id) -> {
                                    editPresenter.deleteNwQuizById();
                                    dialog.cancel();
                                })
                        .setNegativeButton("Cancel",
                                (dialog, id) -> dialog.cancel());

                AlertDialog alertDialogDelete = mDialogBuilderDelete.create();
                alertDialogDelete.show();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.edit_menu, menu);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recyclerViewEditQuiz = view.findViewById(R.id.recyclerViewEditQuiz);
        recyclerViewEditQuiz.setLayoutManager(new LinearLayoutManager(getContext()));
        editQuizRecyclerViewAdapter = new EditQuizRecyclerViewAdapter(this);
        recyclerViewEditQuiz.setAdapter(editQuizRecyclerViewAdapter);

        progressView = view.findViewById(R.id.flProgressBar);
    }

    @ProvidePresenter
    EditPresenter provideEditPresenter() {
        Timber.d("Provide Presenter");
        EditPresenter editPresenter = new EditPresenter();
        editPresenter.setQuizId(getArguments().getLong(EXTRA_QUIZID));
        return editPresenter;
    }

    @ProvidePresenterTag(presenterClass = EditPresenter.class)
    String provideEditPresenterTag() {
        Timber.d("Provide Presenter Tag");
        return String.valueOf(getArguments().getLong(EXTRA_QUIZID));
    }

    @Override
    public void showError(String errorMessage) {
        Timber.e(errorMessage);
        Toast.makeText(getContext(), errorMessage, Toast.LENGTH_LONG).show();
    }

    @Override
    public void showEditQuiz(Quiz quiz) {
        Timber.d("show editQuiz %s", quiz);
        editQuizRecyclerViewAdapter.setEditQuiz(quiz);
    }


    @Override
    public void showProgress(boolean showProgress) {
        progressView.setVisibility(showProgress ? View.VISIBLE : View.GONE);
    }


    @Override
    public void onTranslationEditClicked(QuizTranslation quizTranslation) {

        LayoutInflater inflaterUpDescription = LayoutInflater.from(getContext());
        @SuppressLint("InflateParams") View viewUpDescription = inflaterUpDescription.inflate(R.layout.dialog_edit_translation_description, null);
        AlertDialog.Builder mDialogBuilderUpDescription = new AlertDialog.Builder(Objects.requireNonNull(getContext()));
        mDialogBuilderUpDescription.setView(viewUpDescription);
        final EditText etUpdateDescription = viewUpDescription.findViewById(R.id.etAddingText);
        etUpdateDescription.setText(quizTranslation.description);
        mDialogBuilderUpDescription
                .setCancelable(false)
                .setPositiveButton("OK",
                        (dialog, id) -> {
                            editPresenter.updateTranslationDescription(quizTranslation.id, etUpdateDescription.getText().toString());
                            dialog.cancel();
                        })
                .setNegativeButton("Cancel",
                        (dialog, id) -> dialog.cancel());

        AlertDialog alertDialogUpDescription = mDialogBuilderUpDescription.create();
        alertDialogUpDescription.show();
    }

    @Override
    public void onTranslationDeleteClicked(QuizTranslation quizTranslation) {
        LayoutInflater inflaterDeleteTranslation = LayoutInflater.from(getContext());
        @SuppressLint("InflateParams") View viewDeleteTranslation = inflaterDeleteTranslation.inflate(R.layout.dialog_delete, null);
        AlertDialog.Builder mDialogBuilderDeleteTranslation = new AlertDialog.Builder(Objects.requireNonNull(getContext()));
        mDialogBuilderDeleteTranslation.setView(viewDeleteTranslation);
        mDialogBuilderDeleteTranslation
                .setCancelable(false)
                .setPositiveButton("OK",
                        (dialog, id) -> {
                            editPresenter.deleteNwQuizTranslationById(quizTranslation.id);
                            dialog.cancel();
                        })
                .setNegativeButton("Cancel",
                        (dialog, id) -> dialog.cancel());

        AlertDialog alertDialogDeleteTranslation = mDialogBuilderDeleteTranslation.create();
        alertDialogDeleteTranslation.show();
    }

    @Override
    public void onTranslationPhraseDeleteClicked(QuizTranslationPhrase quizTranslationPhrase) {
        LayoutInflater inflaterDeleteTranslationPhrase = LayoutInflater.from(getContext());
        @SuppressLint("InflateParams") View viewDeleteTranslationPhrase = inflaterDeleteTranslationPhrase.inflate(R.layout.dialog_delete, null);
        AlertDialog.Builder mDialogBuilderDeleteTranslationPhrase = new AlertDialog.Builder(Objects.requireNonNull(getContext()));
        mDialogBuilderDeleteTranslationPhrase.setView(viewDeleteTranslationPhrase);
        mDialogBuilderDeleteTranslationPhrase
                .setCancelable(false)
                .setPositiveButton("OK",
                        (dialog, id) -> {
                            editPresenter.deleteNwQuizTranslationPhraseById(quizTranslationPhrase.id);
                            dialog.cancel();
                        })
                .setNegativeButton("Cancel",
                        (dialog, id) -> dialog.cancel());

        AlertDialog alertDialogDeleteTranslationPhrase = mDialogBuilderDeleteTranslationPhrase.create();
        alertDialogDeleteTranslationPhrase.show();
    }

    @Override
    public void onTranslationAddPhraseClicked(QuizTranslation quizTranslation) {
        LayoutInflater inflaterPhrase = LayoutInflater.from(getContext());
        @SuppressLint("InflateParams") View viewPhrase = inflaterPhrase.inflate(R.layout.dialog_add_translation_phrase, null);
        AlertDialog.Builder mDialogBuilderPhrase = new AlertDialog.Builder(Objects.requireNonNull(getContext()));
        mDialogBuilderPhrase.setView(viewPhrase);
        final EditText etAddingTextPhrase = (EditText) viewPhrase.findViewById(R.id.etAddingText);

        mDialogBuilderPhrase
                .setCancelable(false)
                .setPositiveButton("OK",
                        (dialog, id) -> {
                            editPresenter.addTranslationPhrase(quizTranslation.id, etAddingTextPhrase.getText().toString());
                            dialog.cancel();
                        })
                .setNegativeButton("Cancel",
                        (dialog, id) -> dialog.cancel());

        AlertDialog alertDialogPhrase = mDialogBuilderPhrase.create();
        alertDialogPhrase.show();
    }
}
