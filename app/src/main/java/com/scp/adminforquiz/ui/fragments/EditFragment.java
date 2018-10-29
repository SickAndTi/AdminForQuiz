package com.scp.adminforquiz.ui.fragments;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.arellomobile.mvp.MvpAppCompatFragment;
import com.arellomobile.mvp.presenter.InjectPresenter;
import com.arellomobile.mvp.presenter.ProvidePresenter;
import com.arellomobile.mvp.presenter.ProvidePresenterTag;
import com.scp.adminforquiz.Constants;
import com.scp.adminforquiz.R;
import com.scp.adminforquiz.api.ApiClient;
import com.scp.adminforquiz.model.db.Quiz;
import com.scp.adminforquiz.model.db.QuizTranslation;
import com.scp.adminforquiz.model.db.QuizTranslationPhrase;
import com.scp.adminforquiz.model.db.dao.QuizDao;
import com.scp.adminforquiz.mvp.EditPresenter;
import com.scp.adminforquiz.mvp.EditView;
import com.scp.adminforquiz.ui.adapters.EditQuizRecyclerViewAdapter;

import java.util.Objects;

import javax.inject.Inject;

import ru.terrakok.cicerone.Router;
import timber.log.Timber;
import toothpick.Toothpick;

public class EditFragment extends MvpAppCompatFragment implements EditView, EditQuizRecyclerViewAdapter.EditInterface {
    public final static String EXTRA_QUIZ_ID = "EXTRA_QUIZ_ID";
    @InjectPresenter
    EditPresenter editPresenter;
    @Inject
    QuizDao quizDao;
    @Inject
    ApiClient apiClient;
    @Inject
    Router router;
    RecyclerView recyclerViewEditQuiz;
    View progressBarEdit;
    Toolbar toolbar;
    EditQuizRecyclerViewAdapter editQuizRecyclerViewAdapter;

    public static EditFragment newInstance(Long quizId) {
        EditFragment fragment = new EditFragment();
        Bundle bundle = new Bundle();
        bundle.putLong(EXTRA_QUIZ_ID, quizId);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_edit, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        toolbar = view.findViewById(R.id.toolbar);
        toolbar.inflateMenu(R.menu.edit_menu);
        toolbar.setOnMenuItemClickListener(menuItem -> {
            switch (menuItem.getItemId()) {
                case R.id.addTranslation:
                    editPresenter.goToAddTranslationFragment();
                    break;
                case R.id.deleteNwQuiz:
                    LayoutInflater inflaterDelete = LayoutInflater.from(getContext());
                    @SuppressLint("InflateParams")
                    View viewDelete = inflaterDelete.inflate(R.layout.dialog_delete, null);
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
            return super.onOptionsItemSelected(menuItem);
        });
        recyclerViewEditQuiz = view.findViewById(R.id.recyclerViewEditQuiz);
        progressBarEdit = view.findViewById(R.id.flProgressBarEdit);
        recyclerViewEditQuiz.setLayoutManager(new LinearLayoutManager(getContext()));
        editQuizRecyclerViewAdapter = new EditQuizRecyclerViewAdapter(this);
        recyclerViewEditQuiz.setAdapter(editQuizRecyclerViewAdapter);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Toothpick.inject(this, Toothpick.openScope(Constants.APP_SCOPE));
        setHasOptionsMenu(true);
    }

    @ProvidePresenter
    EditPresenter provideEditPresenter() {
        Timber.d("Provide Presenter");
        EditPresenter editPresenter = new EditPresenter();
        editPresenter.setQuizId(Objects.requireNonNull(getArguments()).getLong(EXTRA_QUIZ_ID));
        return editPresenter;
    }

    @ProvidePresenterTag(presenterClass = EditPresenter.class)
    String provideEditPresenterTag() {
        Timber.d("Provide Presenter Tag");
        return String.valueOf(Objects.requireNonNull(getArguments()).getLong(EXTRA_QUIZ_ID));
    }

    @Override
    public void showError(String errorMessage) {
        Timber.e(errorMessage);
        Toast.makeText(getContext(), errorMessage, Toast.LENGTH_LONG).show();
    }

    @Override
    public void showEditQuiz(Quiz quiz) {
        editQuizRecyclerViewAdapter.setEditQuiz(quiz);
    }

    @Override
    public void showProgress(boolean showProgress) {
        progressBarEdit.setVisibility(showProgress ? View.VISIBLE : View.GONE);
    }

    @Override
    public void onTranslationEditClicked(QuizTranslation quizTranslation) {
        editPresenter.goToUpdateTranslationDescriptionFragment(quizTranslation.id);
    }

    @Override
    public void onTranslationAddPhraseClicked(QuizTranslation quizTranslation) {
        editPresenter.goToAddPhraseFragment(quizTranslation.id);
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
    public void onApproveQuizClicked(Quiz quiz) {
        LayoutInflater inflaterApprove = LayoutInflater.from(getContext());
        @SuppressLint("InflateParams") View viewApprove = inflaterApprove.inflate(R.layout.dialog_approve_quiz, null);
        AlertDialog.Builder mDialogBuilderApprove = new AlertDialog.Builder(Objects.requireNonNull(getContext()));
        mDialogBuilderApprove.setView(viewApprove);
        mDialogBuilderApprove
                .setCancelable(false)
                .setPositiveButton("APPROVE",
                        (dialog, id) -> {
                            editPresenter.approveNwQuizById(quiz.id, true);
                            dialog.cancel();
                        })
                .setNegativeButton("DISAPPROVE",
                        (dialog, id) -> {
                            editPresenter.approveNwQuizById(quiz.id, false);
                            dialog.cancel();
                        });

        AlertDialog alertDialogApprove = mDialogBuilderApprove.create();
        alertDialogApprove.show();
    }
}