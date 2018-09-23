package com.example.user.adminforquiz.ui.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.arellomobile.mvp.MvpAppCompatFragment;
import com.arellomobile.mvp.presenter.InjectPresenter;
import com.arellomobile.mvp.presenter.PresenterType;
import com.arellomobile.mvp.presenter.ProvidePresenter;
import com.arellomobile.mvp.presenter.ProvidePresenterTag;
import com.example.user.adminforquiz.R;
import com.example.user.adminforquiz.model.db.Quiz;
import com.example.user.adminforquiz.mvp.OneQuizPresenter;
import com.example.user.adminforquiz.mvp.OneQuizView;
import com.example.user.adminforquiz.ui.adapters.OneQuizRecyclerViewAdapter;

import timber.log.Timber;

public class OneQuizFragment extends MvpAppCompatFragment implements OneQuizView {
    @InjectPresenter
    OneQuizPresenter oneQuizPresenter;
    RecyclerView recyclerViewOneQuiz;
    OneQuizRecyclerViewAdapter oneQuizRecyclerViewAdapter;
    public final static String EXTRA_QUIZID = "EXTRA_QUIZID";


    public static OneQuizFragment newInstance(Long quizId) {
        OneQuizFragment fragment = new OneQuizFragment();
        Bundle bundle = new Bundle();
        bundle.putLong(EXTRA_QUIZID, quizId);
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
        recyclerViewOneQuiz = view.findViewById(R.id.recyclerViewOneQuiz);
        recyclerViewOneQuiz.setLayoutManager(new LinearLayoutManager(getContext()));
        oneQuizRecyclerViewAdapter = new OneQuizRecyclerViewAdapter();
        recyclerViewOneQuiz.setAdapter(oneQuizRecyclerViewAdapter);
    }


    @ProvidePresenterTag(presenterClass = OneQuizPresenter.class)
    String provideQuizPresenterTag() {
        Timber.d("provodePresenterTag");
        return String.valueOf(getArguments().getLong(EXTRA_QUIZID));
    }

    @ProvidePresenter
    OneQuizPresenter provideQuizPresenter() {
        Timber.d("providePresenter");
        OneQuizPresenter oneQuizPresenter = new OneQuizPresenter();
        oneQuizPresenter.setQuizId(getArguments().getLong(EXTRA_QUIZID));
        return oneQuizPresenter;
    }

    @Override
    public void showQuiz(Quiz quiz) {
        Timber.d("showQuiz from OneQuizFragment");
        oneQuizRecyclerViewAdapter.setQuiz(quiz);
    }

    @Override
    public void showError(String errorMessage) {
        Timber.e(errorMessage);
        Toast.makeText(getContext(), errorMessage, Toast.LENGTH_LONG).show();
    }
}
