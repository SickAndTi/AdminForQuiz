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
import com.scp.adminforquiz.R;
import com.scp.adminforquiz.model.db.Quiz;
import com.scp.adminforquiz.model.db.QuizTranslation;
import com.scp.adminforquiz.model.db.QuizTranslationPhrase;
import com.scp.adminforquiz.mvp.OneQuizPresenter;
import com.scp.adminforquiz.mvp.OneQuizView;
import com.scp.adminforquiz.ui.adapters.OneQuizRecyclerViewAdapter;

import java.util.Objects;

import timber.log.Timber;

public class OneQuizFragment extends MvpAppCompatFragment implements OneQuizView, OneQuizRecyclerViewAdapter.EditInterface {
    @InjectPresenter
    OneQuizPresenter oneQuizPresenter;
    RecyclerView recyclerViewOneQuiz;
    OneQuizRecyclerViewAdapter oneQuizRecyclerViewAdapter;
    View flProgressBar;
    Toolbar toolbar;
    public final static String EXTRA_QUIZ_ID = "EXTRA_QUIZ_ID";

    public static OneQuizFragment newInstance(Long quizId) {
        OneQuizFragment fragment = new OneQuizFragment();
        Bundle bundle = new Bundle();
        bundle.putLong(EXTRA_QUIZ_ID, quizId);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_one_quiz, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        toolbar = view.findViewById(R.id.toolbar);
        toolbar.inflateMenu(R.menu.onequiz_menu);
        toolbar.setOnMenuItemClickListener(menuItem -> {
            switch (menuItem.getItemId()) {
                case R.id.addTranslation:
                    oneQuizPresenter.goToAddTranslationFragment();
                    break;
                case R.id.deleteQuiz:
                    showDeleteQuizDialog();
                    break;
            }
            return super.onOptionsItemSelected(menuItem);
        });
        flProgressBar = view.findViewById(R.id.flProgressBar);
        recyclerViewOneQuiz = view.findViewById(R.id.recyclerViewOneQuiz);
        recyclerViewOneQuiz.setLayoutManager(new LinearLayoutManager(getContext()));
        oneQuizRecyclerViewAdapter = new OneQuizRecyclerViewAdapter(this);
        recyclerViewOneQuiz.setAdapter(oneQuizRecyclerViewAdapter);
    }

    @ProvidePresenterTag(presenterClass = OneQuizPresenter.class)
    String provideQuizPresenterTag() {
        return String.valueOf(Objects.requireNonNull(getArguments()).getLong(EXTRA_QUIZ_ID));
    }

    @ProvidePresenter
    OneQuizPresenter provideQuizPresenter() {
        OneQuizPresenter oneQuizPresenter = new OneQuizPresenter();
        oneQuizPresenter.setQuizId(Objects.requireNonNull(getArguments()).getLong(EXTRA_QUIZ_ID));
        return oneQuizPresenter;
    }

    @Override
    public void showQuiz(Quiz quiz) {
        oneQuizRecyclerViewAdapter.setQuiz(quiz);
    }

    @Override
    public void showError(String errorMessage) {
        Timber.e(errorMessage);
        Toast.makeText(getContext(), errorMessage, Toast.LENGTH_LONG).show();
    }

    @Override
    public void showProgressBar(boolean showProgressBar) {
        flProgressBar.setVisibility(showProgressBar ? View.VISIBLE : View.GONE);
    }

    @Override
    public void onTranslationEditClicked(QuizTranslation quizTranslation) {
        oneQuizPresenter.goToUpdateTranslationDescriptionFragment(quizTranslation.id);
    }

    private void showDeleteQuizDialog() {
        LayoutInflater inflaterDelete = LayoutInflater.from(getContext());
        @SuppressLint("InflateParams")
        View viewDelete = inflaterDelete.inflate(R.layout.dialog_delete, null);
        AlertDialog.Builder mDialogBuilderDelete = new AlertDialog.Builder(Objects.requireNonNull(getContext()));
        mDialogBuilderDelete.setView(viewDelete);
        mDialogBuilderDelete
                .setCancelable(false)
                .setPositiveButton("OK",
                        (dialog, id) -> {
                            oneQuizPresenter.deleteQuiz();
                            dialog.cancel();
                        })
                .setNegativeButton("Cancel",
                        (dialog, id) -> dialog.cancel());

        AlertDialog alertDialogDelete = mDialogBuilderDelete.create();
        alertDialogDelete.show();
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
                            oneQuizPresenter.deleteTranslationById(quizTranslation.id);
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
                            oneQuizPresenter.deletePhraseById(quizTranslationPhrase.id);
                            dialog.cancel();
                        })
                .setNegativeButton("Cancel",
                        (dialog, id) -> dialog.cancel());
        AlertDialog alertDialogDeleteTranslationPhrase = mDialogBuilderDeleteTranslationPhrase.create();
        alertDialogDeleteTranslationPhrase.show();
    }

    @Override
    public void onTranslationAddPhraseClicked(QuizTranslation quizTranslation) {
        oneQuizPresenter.goToAddPhraseFragment(quizTranslation.id);
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
                            oneQuizPresenter.approveQuizById(quiz.id, true);
                            dialog.cancel();
                        })
                .setNegativeButton("DISAPPROVE",
                        (dialog, id) -> {
                            oneQuizPresenter.approveQuizById(quiz.id, false);
                            dialog.cancel();
                        });
        AlertDialog alertDialogApprove = mDialogBuilderApprove.create();
        alertDialogApprove.show();
    }

    @Override
    public void onApproveTranslationClicked(QuizTranslation quizTranslation) {
        LayoutInflater inflaterApprove = LayoutInflater.from(getContext());
        @SuppressLint("InflateParams") View viewApprove = inflaterApprove.inflate(R.layout.dialog_approve_translation, null);
        AlertDialog.Builder mDialogBuilderApprove = new AlertDialog.Builder(Objects.requireNonNull(getContext()));
        mDialogBuilderApprove.setView(viewApprove);
        mDialogBuilderApprove
                .setCancelable(false)
                .setPositiveButton("APPROVE",
                        (dialog, id) -> {
//                            oneQuizPresenter.approveTranslationById(quizTranslation.id, true);TODO
                            dialog.cancel();
                        })
                .setNegativeButton("DISAPPROVE",
                        (dialog, id) -> {
//                            oneQuizPresenter.approveTranslationById(quizTranslation.id, false);
                            dialog.cancel();
                        });
        AlertDialog alertDialogApprove = mDialogBuilderApprove.create();
        alertDialogApprove.show();
    }

    @Override
    public void onDropDownClicked(QuizTranslation quizTranslation) {

    }

    @Override
    public void onApprovePhraseClicked(QuizTranslationPhrase quizTranslationPhrase) {
        LayoutInflater inflaterApprove = LayoutInflater.from(getContext());
        @SuppressLint("InflateParams") View viewApprove = inflaterApprove.inflate(R.layout.dialog_approve_phrase, null);
        AlertDialog.Builder mDialogBuilderApprove = new AlertDialog.Builder(Objects.requireNonNull(getContext()));
        mDialogBuilderApprove.setView(viewApprove);
        mDialogBuilderApprove
                .setCancelable(false)
                .setPositiveButton("APPROVE",
                        (dialog, id) -> {
//                            oneQuizPresenter.approvePhraseById(quizTranslationPhrase.id, true);
                            dialog.cancel();
                        })
                .setNegativeButton("DISAPPROVE",
                        (dialog, id) -> {
//                            oneQuizPresenter.approvePhraseById(quizTranslationPhrase.id, false);TODO
                            dialog.cancel();
                        });
        AlertDialog alertDialogApprove = mDialogBuilderApprove.create();
        alertDialogApprove.show();
    }
}
