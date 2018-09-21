package com.example.user.adminforquiz.ui.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.arellomobile.mvp.MvpAppCompatFragment;
import com.arellomobile.mvp.presenter.InjectPresenter;
import com.example.user.adminforquiz.R;
import com.example.user.adminforquiz.model.db.Quiz;
import com.example.user.adminforquiz.mvp.AllQuizPresenter;
import com.example.user.adminforquiz.mvp.AllQuizView;
import com.example.user.adminforquiz.ui.adapters.AllQuizRecyclerViewAdapter;

import java.util.List;

import timber.log.Timber;

public class AllQuizFragment extends MvpAppCompatFragment implements AllQuizView {
    @InjectPresenter
    AllQuizPresenter allQuizPresenter;
    RecyclerView recyclerView;
    AllQuizRecyclerViewAdapter allQuizRecyclerViewAdapter;
    ProgressBar progressBar;
    SwipeRefreshLayout swipeRefreshLayout;

    public static AllQuizFragment newInstance() {
        return new AllQuizFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_allquiz, container, false);
        recyclerView = view.findViewById(R.id.recyclerView);
        progressBar = view.findViewById(R.id.progressBar);
        swipeRefreshLayout = view.findViewById(R.id.swipeRefreshLayout);
        allQuizRecyclerViewAdapter = new AllQuizRecyclerViewAdapter(new AllQuizRecyclerViewAdapter.OnQuizClickListener() {
            @Override
            public void onQuizClick(Quiz quiz) {
                allQuizPresenter.goToQuiz(quiz);
            }
        });
        recyclerView.setAdapter(allQuizRecyclerViewAdapter);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        swipeRefreshLayout.setOnRefreshListener(() -> allQuizPresenter.loadDataFromApi());
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
    }

    @Override
    public void showProgressBar(boolean showProgressBar) {
        progressBar.setVisibility(showProgressBar ? View.VISIBLE : View.GONE);
    }

    @Override
    public void showQuizList(List<Quiz> quizList) {
        if (recyclerView.getAdapter() == null) {
            AllQuizRecyclerViewAdapter allQuizRecyclerViewAdapter = new AllQuizRecyclerViewAdapter(new AllQuizRecyclerViewAdapter.OnQuizClickListener() {
                @Override
                public void onQuizClick(Quiz quiz) {
                }
            });
            allQuizRecyclerViewAdapter.setQuizList(quizList);
            recyclerView.setAdapter(allQuizRecyclerViewAdapter);
        } else {
            AllQuizRecyclerViewAdapter adapter = (AllQuizRecyclerViewAdapter) recyclerView.getAdapter();
            adapter.setQuizList(quizList);
            recyclerView.getAdapter().notifyDataSetChanged();
        }
    }

    @Override
    public void showError(String errorMessage) {
        Timber.e(errorMessage);
        Toast.makeText(getActivity(), errorMessage, Toast.LENGTH_LONG).show();
    }

    @Override
    public void showSwipeRefresherBar(boolean showSwipeRefresherBar) {
        swipeRefreshLayout.setRefreshing(showSwipeRefresherBar);
    }

}
