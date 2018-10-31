package com.scp.adminforquiz.ui.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
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
import com.scp.adminforquiz.mvp.OneQuizPresenter;
import com.scp.adminforquiz.mvp.OneQuizView;
import com.scp.adminforquiz.ui.adapters.OneQuizRecyclerViewAdapter;

import java.util.Objects;

import timber.log.Timber;

public class OneQuizFragment extends MvpAppCompatFragment implements OneQuizView {
    @InjectPresenter
    OneQuizPresenter oneQuizPresenter;
    RecyclerView recyclerViewOneQuiz;
    OneQuizRecyclerViewAdapter oneQuizRecyclerViewAdapter;
    View progressBarEdit;
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
                case R.id.editQuiz:
                    oneQuizPresenter.goToEditQuiz();
                    break;
            }
            return super.onOptionsItemSelected(menuItem);
        });
        progressBarEdit = view.findViewById(R.id.flProgressBarEdit);
        recyclerViewOneQuiz = view.findViewById(R.id.recyclerViewOneQuiz);
        recyclerViewOneQuiz.setLayoutManager(new LinearLayoutManager(getContext()));
        oneQuizRecyclerViewAdapter = new OneQuizRecyclerViewAdapter();
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
        progressBarEdit.setVisibility(showProgressBar ? View.VISIBLE : View.GONE);
    }
}
